package binsic

import java.util.regex.Matcher
import java.util.regex.Pattern

/*
 * Preprocess BASIC script into something
 * Groovy will handle (eg deal with capitalisation etc)
 */
class BinsicPreprocessor {
	
	private BinsicProcessor() {}
	
	private static final currentPreproc = new BinsicPreprocessor()
	static getCurrentPreproc() {return currentPreproc}
	
	def basicIn
	def binsicMid
	def binsicOut
	def lineMap =[:]
	def lineNo = 0
	
	def commands = ["^PRINT", "^REM", "^LET ", "^FAST", "^SLOW",
		"^POKE", "^PEEK", "^USR", "^CLS", "^GOTO", "^NEXT(\\s)+[A-Z]"]
	def processedCommands = ["printIt", "//", "","//FAST","//SLOW",
		"//POKE", "//PEEK", "//USR", "cls()", "getTo", "}"]

	
	def partIf = "^IF\\s((.(?!THEN))+)\\sTHEN\\s((.(?!ELSE))+)"
	
	def complexCommands = ["${partIf}?!(\\sELSE(.+))", "${partIf}\\sELSE(.+)",
		"^FOR(\\s)+([A-Z])(\\s)*=((.(?!TO))+)\\sTO\\s((.(?!STEP))+)((\\s)+(STEP((.)+)))*",
		"^DIM\\s+([A-Z]\\\$?)\\s*\\((.+)\\)",
		"^([A-Z])\\\$(.+)", "^[A-Z](\\(.+\\))", "^[A-Z]_dollar(\\(.+\\))"]


	
	def matchedIf = {statementMatch, line ->
		def matcher = (line =~ statementMatch)
		def mainClause = (matcher[0][1]).trim()
		def actionClause = matcher[0][3]
		actionClause = processCaps(actionClause.trim())
		return "if (${mainClause}) ${actionClause}"
	}
	
	def matchedElse = {statementMatch, line ->
		def matcher = (line =~ statementMatch)
		def mainClause = (matcher[0][1]).trim()
		def actionClause = matcher[0][3]
		def elseClause = matcher[0][5]
		actionClause = processCaps(actionClause.trim())
		elseClause = processCaps(elseClause.trim())
		return "if (${mainClause}) ${actionClause} else { ${elseClause} }"
	}
	
	def matchedFor = {statementMatch, line ->
		def matcher = (line =~ statementMatch)
		def varCount = matcher[0][2]
		def startCount = matcher[0][4]
		def endCount = matcher[0][6]
		def lineBack = "for ($varCount in ($startCount..$endCount)"
		if (matcher[0][11]) 
			lineBack += ".step(${matcher[0][11].trim()})"
		lineBack += ") {"
		return lineBack
	}
	
	def matchedDim = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		def varName = (matcher[0][1]).getAt(0)
		if (matcher[0][1].size() > 1)
			varName += "_dollar"
		def getDimensions = {dimString, outString->
			def dimPattern = Pattern.compile("[^, ]+")
			def dimMatch = dimPattern.matcher(dimString)
			dimMatch.each{outString += "[$it]"}
			return outString
		}
		def dimLine = "$varName = new Object"
		dimLine += getDimensions(matcher[0][2], "")
		return dimLine
	}
	
	def matchedDollar = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		return "${matcher[0][1]}_dollar${matcher[0][2]}"
	}
	
	def matchedArray = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		matcher.each{println "Array match was $it"}
	}

	def dummyMatch = { statementMatch, line->
		println "DUMMY"
	}
		
	def complexCommandClosures = [matchedIf, matchedElse, matchedFor,
		matchedDim, matchedDollar, matchedArray, matchedArray, dummyMatch]
	
	def stripLines = {lineIn->
		def lineOut = new String(lineIn)
		def progLine = (lineOut =~/^[0-9]+/)
		if (progLine.size() > 0) {
			lineOut = lineOut - progLine[0]
			lineMap << ["${progLine[0]}":"$lineNo"]
		}
		lineNo++
		binsicMid.append lineOut.trim()
		binsicMid.append "\n"
	}
		
	def processCaps = {lineIn ->
		def lineOut = new String(lineIn)
		commands.eachWithIndex { com, index ->
			lineOut = lineOut.replaceAll(com, processedCommands[index])
		}
		complexCommands.eachWithIndex { complexCom, index ->
			if (lineOut =~ complexCom)
				lineOut = (complexCommandClosures[index]).call(
					complexCom, lineOut)
		}
		return lineOut
	}
	
	def processLines = { line->
		def outLine = processCaps(line)
		println outLine
		binsicOut.append outLine
		binsicOut.append "\n"
	}
	
	def startUp(def name)
	{
		setupTempFile(name)
		binsicMid = File.createTempFile("${System.nanoTime()}", null)
		basicIn = new File(name)
	
		basicIn.eachLine(stripLines)
		binsicMid.eachLine(processLines)
	}
	
	def reStart(def name, def line)
	{
		setupTempFile(name)
		def count = 0
		binsicMid.eachLine(){
			if (count++ >= line)
				processLines(it)
		}
	}
	
	def setupTempFile(def name)
	{
		binsicOut = File.createTempFile("${System.nanoTime()}", null)
		binsicOut.write "package binsic\n"
	}
}
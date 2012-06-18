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
	def shell
	def aClosure
	def inClosure = false
	
	def commands = ["^PRINT", "^REM", "^LET ", "^FAST", "^SLOW",
		"^POKE", "^PEEK", "^USR", "^CLS", "^NEXT(\\s)+[A-Z]",
		"^RETURN", "END"]
	def processedCommands = ["printIt", "//", "","//FAST","//SLOW",
		"//POKE", "//PEEK", "//USR", "cls()", "}", "return",
		"new BinsicDialog(); System.in.withReader {println (it.readLine())}"]

	def partIf = "^IF\\s((.(?!THEN))+)\\sTHEN\\s((.(?!ELSE))+)"
	
	def complexCommands = ["${partIf}(?!(.*ELSE.*))", "${partIf}\\sELSE(.+)",
		"^FOR(\\s)+([A-Z])(\\s)*=((.(?!TO))+)\\sTO\\s((.(?!STEP))+)((\\s)+(STEP((.)+)))*",
		"^DIM\\s+([A-Z]\\\$?)\\s*\\((.+)\\)", "(.*)([A-Z])\\\$(.*)",
		/* After here all $ have become _ */
		"^([A-Z])\\((.+)\\)(.*)", "^([A-Z]_)\\((.+)\\)(.*)",
		"(.*)GOSUB\\s+(.*)", "^GOTO(.+)", "^INPUT\\s((([A-Z0-9])(?!_))+)\$",
		"^INPUT\\s([A-Z0-9]+_)"]
	
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

	def getDimensions = {dimString, outString->
		def dimPattern = Pattern.compile("[^,]+")
		def dimMatch = dimPattern.matcher(dimString)
		dimMatch.each{outString += "[${it.trim()}]"}
		return outString
	}
		
	def matchedDim = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		def varName = (matcher[0][1]).getAt(0)
		if (matcher[0][1].size() > 1)
			varName += "_"
		def dimLine = "$varName = new Object"
		dimLine += getDimensions(matcher[0][2], "")
		/* array references look like functions to Groovy so trap them */
		BinsicInterpreter.metaClass."$varName" = {Object[] arg ->
			def answer = "package binsic; $varName"
			arg.each { 
				answer = answer + "[$it]"
			}
			def something = shell.evaluate(answer)
			return something
		}
		return dimLine
	}
	
	def matchedDollar = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		return "${matcher[0][1]}${matcher[0][2]}_${matcher[0][3]}"
	}
	
	def matchedArray = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		def arrayRef ="${matcher[0][1]}"
		arrayRef += getDimensions(matcher[0][2], "")
		arrayRef += "${matcher[0][3]}"
		return arrayRef
	}
	
	def matchedGosub = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		return "${matcher[0][1]} buildClosure(${matcher[0][2]})"
	}
	
	def matchedGoto = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		if (inClosure)
			return "runTo ${matcher[0][1]}"
		else return "getTo ${matcher[0][1]}"
	}
	
	def matchedInputNum = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		matcher.each {println "Num Input match is $it"}
		def retString = "${matcher[0][1]} ="
		retString += "(new BufferedReader(new InputStreamReader(System.in)))."
		retString += "readLine()"
	}
	
	def matchedInputStr = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		matcher.each {println "Str Input match is $it"}
		def retString = "String ${matcher[0][1]} ="
		retString += "new Scanner(System.in).nextLine()\n"
		return retString
	}

	def dummyMatch = { statementMatch, line->
		println "DUMMY"
	}
		
	def complexCommandClosures = [matchedIf, matchedElse, matchedFor,
		matchedDim, matchedDollar, matchedArray, matchedArray,
		matchedGosub, matchedGoto, matchedInputNum, matchedInputStr, 
		dummyMatch]
	
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
	
	def buildClosure(line) 
	{
		def outLine = processCaps(line)
		println "Closure line: $outLine"
		aClosure += "$outLine\n"
		if (outLine =~ "^return"){
			aClosure += "}\n"
			return false
		} else
			return true
	}
	
	def setShell(def sh)
	{
		shell = sh
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
	
	def hackClosure(def line)
	{
		def count = 0
		inClosure = true
		aClosure = "package binsic\n{->\n"
		def good = true
		binsicMid.eachLine() {
			if (count++ >= line && good)
				good = buildClosure(it)
		}
		if (good)
			aClosure += "}\n"
		inClosure = false
		return aClosure
	}
	
	def setupTempFile(def name)
	{
		binsicOut = File.createTempFile("${System.nanoTime()}", null)
		binsicOut.write "package binsic\n"
	}
}
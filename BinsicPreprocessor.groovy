package binsic

import java.util.regex.Matcher

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
	
	def commands = ["^PRINT", "^REM", "^LET", "^FAST", "^SLOW",
		"^POKE", "^PEEK", "^USR", "^CLS", "^GOTO"]
	def processedCommands = ["printIt", "//", "","//FAST","//SLOW",
		"//POKE", "//PEEK", "//USR", "cls()", "getTo" ]
	def complexCommands = ["^IF\\s((.(?!THEN))+)\\sTHEN\\s(.+)"]

	
	def matchedIf = {statementMatch, line ->
		def matcher = (line =~ statementMatch)
		def mainClause = matcher[0][1]
		def actionClause = matcher[0][3]
		actionClause = processCaps(actionClause.trim())
		return "if (${mainClause.trim()}) ${actionClause}"
	}
	
	def complexCommandClosures = [matchedIf]
	
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
		def retLine
		def lineOut = new String(lineIn)
		commands.eachWithIndex { com, index ->
			lineOut = lineOut.replaceAll(com, processedCommands[index])
		}
		
		complexCommands.eachWithIndex { complexCom, index ->
			retLine = new String(lineOut)
			if (lineOut =~ complexCom)
				retLine = (complexCommandClosures[index]).call(complexCom, lineOut)
		}
		return retLine
	}
	
	def processLines = { line->
		line = processCaps(line)
		binsicOut.append line
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

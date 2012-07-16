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
	def engine
	def textFieldIn
	def inClosure = false
	
	/* Order matters here so careful if editing or adding */
	def commands = ["PRINT\$", "^PRINT(\\s)+AT", "^PRINT", "^REM", "^LET ",
		"^FAST", "^SLOW", "^POKE", "^PEEK", "^USR", "^CLS", 
		"^RETURN", "^STOP", "^END", "^SCROLL", "<>", "TAB", "LEN"]
	def processedCommands = ["scroll()", "printIt", "printIt ", "//", " ",
		"//FAST","//SLOW","//POKE", "//PEEK", "//USR", "cls()", "return",
		"END",
		"{new BinsicDialog(); System.in.withReader {println (it.readLine())}}",
		"scroll()", "!=", "tab", "sizeStr"]

	def partIf = "^IF\\s((.(?!THEN))+)\\sTHEN\\s((.(?!ELSE))+)"
	
	def complexCommands = [
		"^DIM\\s+([A-Z]\\\$?)\\s*\\((.+)\\)",		//creating arrays
		"(.+)NEXT\\s+[A-Z](.*)", "^NEXT(\\s)+[A-Z]",//end of FOR .. NEXT
		"(.*)(\\s)([A-Z])\\(([^\\)]+)\\)(.*)",  	//array referencing
		"(.*)(\\s)([A-Z]\\\$)\\(([^\\)]+)\\)(.*)", 	//string array referencing
		/* After here IF ... THEN ... ELSE will be parsed as subclauses */
		"${partIf}(?!(.*ELSE.*))", "${partIf}\\sELSE(.+)",
		"^FOR(\\s)+([A-Z])(\\s)*=((.(?!TO))+)\\sTO\\s((.(?!STEP))+)((\\s)+(STEP((.)+)))*",
		 "(.*)([A-Z])\\\$(.*)", 					//handle dollar sign
		/* After here all $ have become _ */
		"(.*)GOSUB\\s+(.*)", "^GOTO(.+)", "^INPUT\\s((([A-Z0-9])(?!_))+)",
		"^INPUT\\s([A-Z0-9]+_)(.*)", "^PAUSE\\s(.+)", "^RAND(.*)",
		"^MID_\\((([^,]+),([^,]+),([^\\)]+))\\)\\s=\\s(.*)",
		"(.*)VAL\\s?\\(?([^)]+)\\)?(.*)", "printIt(.*(,|;).*)",
		 "printIt(.*),\$"						//handle semicolon in PRINT
		 ]
	

	def matchedIf = {statementMatch, line ->
		def matcher = (line =~ statementMatch)
		def mainClause = (matcher[0][1]).trim()
		mainClause = mainClause.replaceAll(" = ", " == ")
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

	def getDimensionsAccess = {dimString, outString->
		def dimPattern = Pattern.compile("[^,]+")
		def dimMatch = dimPattern.matcher(dimString)
		dimMatch.each{outString += "[${it.trim()} as Integer]"}
		return outString
	}
	
	def getDimensionsDim = {dimString, outString->
		def dimPattern = Pattern.compile("[^,]+")
		def dimMatch = dimPattern.matcher(dimString)
		dimMatch.each{outString += "[${it.trim()} + 1 as Integer]"}
		return outString
	}
		
	def matchedDim = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		def varName = (matcher[0][1]).getAt(0)
		if (matcher[0][1].size() > 1)
			varName += "_"
		def dimLine = "$varName = new Object"
		dimLine += getDimensionsDim(matcher[0][2], "")
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
		def outLine = line
		while (outLine =~ statementMatch)
		{
			def matcher = (outLine =~ statementMatch)
			outLine = "${matcher[0][1]}${matcher[0][2]}_${matcher[0][3]}"
		}
		return outLine
	}
	
	def matchedArray = {statementMatch, line->
		def outLine = line
		while (outLine =~ statementMatch) {
			def matcher = (outLine =~ statementMatch)
			outLine ="${matcher[0][1]}${matcher[0][2]}"
			outLine += "${matcher[0][3]}"
			outLine += getDimensionsAccess(matcher[0][4], "")
			outLine += "${matcher[0][5]}"
		}
		return outLine
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
		def retString =
			"${matcher[0][1]}${matcher[0][2]} = waitOnInput()"
		return retString
	}
	
	def matchedInputStr = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		def retString =
			"${matcher[0][1]}${matcher[0][2]} = waitOnInputString()"
		return retString
	}
	
	def matchedPause = { statementMatch, line->
		def matcher = (line =~ statementMatch)
		def delay
		delay = matcher[0][1] as Integer
		return "pause($delay)"
	}
	
	def matchedRand = { statementMatch, line->
		def matcher = (line =~ statementMatch)
		def seed = -1
		if (matcher[0][1])
			seed = matcher[0][1] as Integer
		return "randomize($seed)"
	}
	
	def matchedMid = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		def ans = "${matcher[0][2]} ="
		ans += " insertInStr(${matcher[0][1]}, ${matcher[0][5]})\n"
		return ans
	}
	
	def matchedVal = {statementMatch, line->
		def lineOut = line
		while (line =~ statementMatch) {
			def matcher = (line =~ statementMatch)
			line = "${matcher[0][1]} val(${matcher[0][2]}) ${matcher[0][3]}"
		}
		return line
	}
	
	def matchedAppend = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		return "appendIt( ${matcher[0][1]} )"
	}
	
	def matchedNext = {statementMatch, line->
		def matcher = (line =~ statementMatch)
		return "${matcher[0][1]} break ${matcher[0][2]}"
	}
	
	def matchedFinalNext = { statementMatch, line->
		return "}"
	}
	
	def matchedPrintCommas = {statementMatch, line->
		def simpleMatch = "printIt(\\s)*(.*)"
		def simpleMatcher = (line =~ simpleMatch)
		def processString = simpleMatcher[0][2]
		line = "printIt "
		def quoteOpen = false
		for (i in 0..processString.size() - 1) {
			def nextChar = processString[i]
			def addChars = new String(nextChar)
			if (nextChar == '\"')
				quoteOpen = !quoteOpen
			else if (quoteOpen == false && nextChar == ',')
				addChars = ",\"    \","
			else if (quoteOpen == false && nextChar == ';')
				addChars = ","
			line += addChars
		}
		line = line.replaceAll(",,", ",")
		return line
	}

	def dummyMatch = { statementMatch, line->
		println "DUMMY"
	}
		
	def complexCommandClosures = [matchedDim, matchedNext, matchedFinalNext,
		matchedArray, matchedArray,
		matchedIf, matchedElse, matchedFor, matchedDollar, 
		matchedGosub, matchedGoto, matchedInputNum, matchedInputStr, 
		matchedPause, matchedRand, matchedMid, matchedVal,
		matchedPrintCommas, matchedAppend, dummyMatch]
	
	def mathBuilder = ["ABS", "ACS", "ASN", "ATN", "COS", "EXP",
		"LN", "PI", "SIN", "SQR", "TAN", "RND", "SGN"]
	def mathReplace = ["abs", "acos", "asin", "atan", "cos", "exp",
		"log", "PI", "sin", "sqrt", "tan", "random()", "signum"]
	
	def oddments = ["AND", "CHR_", "INT", "NOT", "OR", "TO", "LEFT_",
		"MID_", "RIGHT_", "CODE", "INKEY_", "STR_", "PLOT",
		"UNPLOT"]
	def oddReplace = ["&&", "charIt", "intIt", "!", "||", "..", "getLeft",
		"getMid", "getRight", "code", "inkey()", "stringify", "plot",
		"unplot"]
	
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
		mathBuilder.eachWithIndex {mathsElement, index ->
			lineOut = lineOut.replaceAll(mathsElement, 
				"Math.${mathReplace[index]}")
		}
		oddments.eachWithIndex {oddBits, index->
			if (lineOut =~ oddBits)
				lineOut = lineOut.replaceAll(oddBits, oddReplace[index])
		}
		return lineOut
	}
	
	def processLines = { line->
		def outLine = processCaps(line)
		if (engine.debug)
			println outLine
		binsicOut.append outLine
		binsicOut.append "\n"
	}
	
	def buildClosure(line) 
	{
		def outLine = processCaps(line)
		if (engine.debug)
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
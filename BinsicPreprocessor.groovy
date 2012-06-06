package binsic

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
	
	def commands = ["PRINT", "REM", "LET", "FAST", "SLOW",
		"POKE", "PEEK", "USR", "CLS", "GOTO"]
	def processedCommands = ["printIt", "//", "","//FAST","//SLOW",
		"//POKE", "//PEEK", "//USR", "cls()", "getTo" ]
	
	def stripLines = {lineIn->
		def lineOut = new String(lineIn)
		def progLine = (lineOut =~/^[0-9]+/)
		if (progLine.size() > 0) {
			lineOut = lineOut - progLine[0]
			lineMap << ["${progLine[0]}":"$lineNo"]
		}
		lineNo++
		binsicMid.append lineOut
		binsicMid.append "\n"
	}
	
	def processCaps = {lineIn ->
		def lineOut = new String(lineIn)
		commands.eachWithIndex { com, index ->
			lineOut = lineOut.replace(com, processedCommands[index])
		}
		binsicOut.append lineOut
		binsicOut.append "\n"
	}
	
	def startUp(def name)
	{
		setupTempFile(name)
		binsicMid = File.createTempFile("${System.nanoTime()}", null)
		basicIn = new File(name)
	
		basicIn.eachLine(stripLines)
		binsicMid.eachLine(processCaps)
		
	}
	
	def reStart(def name, def line)
	{
		setupTempFile(name)
		def count = 0
		binsicMid.eachLine(){
			if (count++ >= line)
				processCaps(it)
		}
	}
	
	def setupTempFile(def name)
	{
		binsicOut = File.createTempFile("${System.nanoTime()}", null)
		binsicOut.write "package binsic\n"
	}
}

package Binsic

/*
 * Preprocess BASIC script into something
 * Groovy will handle (eg deal with capitalisation etc)
 */
class BinsicPreprocessor {
	
	def basicIn
	def binsicMid
	def binsicOut
	def lineMap =[:]
	def lineNo = 1 //start at 1 because of package line
	
	def commands = ["PRINT", "REM", "LET", "FAST", "SLOW",
		"POKE", "PEEK", "USR", "CLS"]
	def processedCommands = ["printIt", "//", "","//FAST","//SLOW",
		"//POKE", "//PEEK", "//USR", "cls()" ]
	
	BinsicPreprocessor(def name)
	{
		binsicOut = File.createTempFile("BINSIC", null)
		binsicMid = File.createTempFile("BINSICHOLDER", null)
		binsicOut.write "package Binsic\n"
		basicIn = new File(name)
		
		def stripLines = {lineIn->
			def lineOut = new String(lineIn)
			def progLine = (lineOut =~/^[0-9]+/)
			if (progLine.size() > 0) {
				lineOut = lineOut - progLine[0]
				lineMap << ["$lineNo":"${progLine[0]}"]
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
		
		basicIn.eachLine(stripLines)
		binsicMid.eachLine(processCaps)
		
	}
	
}

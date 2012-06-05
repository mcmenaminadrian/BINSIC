package Binsic

/*
 * Preprocess BASIC script into something
 * Groovy will handle (eg deal with capitalisation etc)
 */
class BinsicPreprocessor {
	
	def basicIn
	def binsicOut
	
	def commands = ["PRINT", "REM", "LET", "FAST", "SLOW",
		"POKE", "PEEK", "USR", "CLS"]
	def processedCommands = ["printIt", "//", "","//FAST","//SLOW",
		"//POKE", "//PEEK", "//USR", "cls()" ]
	
	BinsicPreprocessor(def name)
	{
		binsicOut = File.createTempFile("BINSIC", null)
		binsicOut.write "package Binsic\n"
		basicIn = new File(name)
		
		def processCaps = {lineIn ->
			def lineOut = new String(lineIn)
			commands.eachWithIndex { com, index ->
				lineOut = lineOut.replace(com, processedCommands[index])
			}
			binsicOut.append lineOut
			binsicOut.append "\n"
		}
		
		basicIn.eachLine(processCaps)
		
	}
	
}

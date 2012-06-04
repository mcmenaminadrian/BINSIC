package Binsic

abstract class BinsicInterpreter extends Script {
	
	static def textArea
	
	static def setTextArea(def window)
	{
		textArea = window
	}
	
	def printIt(Object [] param) {
		param.each {
			textArea.append "$it"
		}
	}
	

}

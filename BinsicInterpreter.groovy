package binsic

import java.lang.Math

abstract class BinsicInterpreter extends Script {
	
	static def textArea
	static def binsicEngine
	
	static def setTextArea(def window)
	{
		textArea = window
	}
	
	static def setEngine(def engine)
	{
		binsicEngine = engine
	}
	
	def printIt(Object [] param) {
		param.each {
			textArea.append "$it"
		}
		textArea.append "\n"
		def rect = textArea.modelToView(textArea.getDocument().getLength() - 1)
		textArea.scrollRectToVisible(rect)
	}
	
	def cls()
	{
		textArea.write("")
	}
	
	def getTo(def lineNo)
	{
		binsicEngine.getTo(lineNo)
		new BinsicDialog()
		System.in.withReader { println (it.readLine()) }
	}
	
	def runTo(def lineNo)
	{
		binsicEngine.getTo(lineNo)
	}
	
	def buildClosure(def lineNo)
	{
		def subString= binsicEngine.buildClosure(lineNo)
		def subroutine = binsicEngine.shell.evaluate("$subString")
		subroutine()
	}
}

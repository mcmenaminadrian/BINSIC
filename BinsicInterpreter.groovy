package binsic

import java.lang.Math
import java.util.concurrent.CountDownLatch
import javax.swing.*

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
	
	
	def clearInputs(def textIn)
	{
		textIn.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), null)
		textIn.setText("")
	}
	
	def waitOnInput()
	{
		def textIn = binsicEngine.binsicWindow.textIn
		clearInputs(textIn)
		def countDown = new CountDownLatch(1)
		textIn.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
			BinsicConstants.INPUT)
		def inputAction = new BinsicInputAction(textIn, binsicEngine.preProc,
			countDown)
		textIn.getActionMap().put(BinsicConstants.INPUT, inputAction)
		countDown.await()
		return inputAction.result
		
	}
	
	def waitOnInputString()
	{
		def textIn = binsicEngine.binsicWindow.textIn
		clearInputs(textIn)
		def countDown = new CountDownLatch(1)
		textIn.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
			BinsicConstants.INPUTSTRING)
		def inputStringAction = new BinsicInputStringAction(textIn,
			binsicEngine.preProc, countDown)
		textIn.getActionMap().put(BinsicConstants.INPUTSTRING,
			inputStringAction)
		countDown.await()
		return inputStringAction.result
		
	}
}

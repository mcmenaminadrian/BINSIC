package binsic

import java.lang.Math
import java.util.concurrent.CountDownLatch
import javax.swing.*

abstract class BinsicInterpreter extends Script {
	
	static def textArea
	static def binsicEngine
	def randomNumberGenerator
	
	static def setTextArea(def window)
	{
		textArea = window
	}
	
	static def setEngine(def engine)
	{
		binsicEngine = engine
	}
	
	def printIt()
	{
		textArea.append("\n")
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
	
	def pause(def interval)
	{
		if (interval > 32767){
			return waitOnInput()
		} else {
			Thread.sleep(interval * 20)
		}
	}
	
	def randomize(def seed)
	{
		if (seed < 0)
			randomNumberGenerator = new Random();
		else
			randomNumberGenerator = new Random(seed)
	}
}

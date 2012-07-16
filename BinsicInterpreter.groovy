package binsic

import java.awt.Point
import java.awt.geom.Point2D
import java.lang.Math
import java.util.concurrent.CountDownLatch
import javax.swing.*

abstract class BinsicInterpreter extends Script {
	
	static def textArea
	static def binsicEngine
	def randomNumberGenerator
	static def keyMonitor
	static def aFrame
	static def grafix
	
	static def setTextArea(def window)
	{
		textArea = window
		keyMonitor = new BinsicKeyMonitor()
		textArea.addKeyListener(keyMonitor)
		grafix = textArea.getGraphics()
	}
	
	static def setEngine(def engine)
	{
		binsicEngine = engine
	}
	
	def keepVisible()
	{
		def rect = textArea.modelToView(textArea.getDocument().getLength() - 1)
		textArea.scrollRectToVisible(rect)
	}
	
	def scroll()
	{
		textArea.append("\n")
		keepVisible()
	}
	
	def printIt(Object [] param) {
		appendIt(param)
		textArea.append "\n"
		keepVisible()
	}
	
	def appendIt(Object [] param)
	{
		param.each { textArea.append "$it" }
	}
	
	def cls()
	{
		textArea.setText("")
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
	
	def pause(def pInterval)
	{
		def interval = pInterval as Integer
		if (interval > 32767){
			return waitOnInputString()
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
	
	def charIt(def c)
	{
		return Character.toChars(c as Integer)
	}
	
	def intIt(def i)
	{
		return i as Integer
	}
	
	def getLeft(def aString, def pNumber)
	{
		def aNumber = pNumber as Integer
		if (aNumber == 0)
			return ""
		if (aNumber >= aString.size() - 1)
			return aString
		return aString[0..(aNumber - 1)]
	}
	
	def getMid(def aString, def pNumber)
	{
		def aNumber = pNumber as Integer
		if (aNumber == 0)
			return ""
		def len = aString.size()
		if (aNumber >= len - 1)
			return aString
		else
			return aString[(aNumber - 1) .. (len - 1)]
	}
	
	def getMid(def aString, def pNumber, def pLength)
	{
		if (pNumber > aString.size())
			return ""
		def aNumber = pNumber as Integer
		def aLength = pLength as Integer
		def endPoint
		if (aNumber - 1 + aLength > aString.size())
			endPoint = aString.size() - 1
		else
			endPoint = aNumber + aLength - 2
		return aString[(aNumber - 1) .. endPoint]
	}
	
	def getRight(def aString, def pNumber)
	{
		def aNumber = pNumber as Integer
		if (aNumber == 0)
			return ""
		def len = aString.size()
		if (aNumber > len)
			return aString
		else
			return aString[(len - aNumber) .. (len - 1)]
	}
	
	def insertInStr(def aString, def pStarting, def pLength, def replacement)
	{
		def starting = pStarting as Integer
		def length = pLength as Integer
		if (length == 0)
			return aString
		def len = aString.size()
		if (starting > len)
			return aString
		def answer = ""
		for (i in (0..len - 1)) {
			if (i < starting - 1 || i + 2 > (starting + length) ||
				i + 2 > replacement.size() + starting)
				answer += aString[i]
			else
				answer += replacement[i + 1 - starting]
		}
		return answer
	}
	
	def code(def aString)
	{
		if (aString.size() == 0)
			return 0
		else {
			char code = aString[0]
			return code as Integer
		}
	}
	
	def inkey()
	{
		return keyMonitor.getCurrentKey()
	}
	
	def val(String aString)
	{
		return aString.toBigDecimal()
	}
	
	def stringify(def aNumber)
	{
		BigDecimal aBigDecimal = new BigDecimal(aNumber)
		return aBigDecimal.toString()
	}
	
	def plot(def x, def y)
	{
		grafix.fillRect(x * 20 as Integer, y * 20 as Integer, 20, 20)
	}
	
	def unplot(def x, def y)
	{
		grafix.clearRect(x * 20 as Integer, y * 20 as Integer, 20, 20)
	}
	
	def tab(def pTabs)
	{
		def tabs = pTabs as Integer
		def retString = ""
		for (i in 0..tabs - 1)
			retString += "    "
		return retString
	}
	
	def sizeStr(def aString)
	{
		if (aString == null)
			return 0
		return aString.size()
	}
}

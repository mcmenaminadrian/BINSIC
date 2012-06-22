package binsic

import org.codehaus.groovy.control.*

/* 
 * BINSIC is not Sinclair Instruction Code 
 * BINSIC is a reimplementation of Sinclair ZX81 BASIC
 * - along with some of the bits Sinclair forgot to add
 * last time!
 */

class BinsicEngine{
	
	def binsicWindow
	def binsicInterpreter
	def preProc
	def conf
	def shell
	def basic
	def interpreter
	
	BinsicEngine()
	{
		binsicWindow = new BinsicWindow(this)
	}
	
	def getTo(def lineNo)
	{
		def fileLine = preProc.lineMap.get("$lineNo")
		preProc.reStart(basic, fileLine as Integer)
		shell.evaluate(preProc.binsicOut)
	}
	
	def buildClosure(def lineNo)
	{
		def fileLine = preProc.lineMap.get("$lineNo")
		return preProc.hackClosure(fileLine as Integer)
	}
	
	def process(def name)
	{
		basic = name
		BinsicInterpreter.setTextArea(binsicWindow.screenZX)
		BinsicInterpreter.setEngine(this)
		conf = new CompilerConfiguration()
		conf.setScriptBaseClass("BinsicInterpreter")
		shell = new GroovyShell(conf)
		preProc = BinsicPreprocessor.getCurrentPreproc()
		preProc.setShell(shell)
		preProc.startUp(basic)
		shell.evaluate(preProc.binsicOut)
		new BinsicDialog()
		System.in.withReader { println (it.readLine()) }
	}
}

if (args) {
	def engine = new BinsicEngine()
	engine.process(args[0])
}
else
	println "Usage: Binsic <BASIC script>"


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
	def debug = false
	
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
		preProc.engine = this
		preProc.setShell(shell)
		preProc.startUp(basic)
		shell.evaluate(preProc.binsicOut)
		new BinsicDialog()
		System.in.withReader { println (it.readLine()) }
	}
}
	
	def binsicCli = new CliBuilder
		(usage:'binsic [-d] -f <BASIC script>')
	
	binsicCli.d(longOpt:'debug',
		'output generated script')
	binsicCli.f(longOpt:'file',
		'BASIC script to run')
	
	
	def binsicParse = binsicCli.parse(args)
	
	if (binsicParse.u)
		binsicCli.usage()
	else {
		def engine = new BinsicEngine()
		if (binsicParse.d)
			engine.debug = true
		if (binsicParse.f)
			engine.process(binsicParse.f)
		else
			engine.process(args[args.size() - 1])
	}
	
	
	
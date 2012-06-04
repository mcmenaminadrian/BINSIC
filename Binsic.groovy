#!/usr/bin/env groovy
package Binsic
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
	
	BinsicEngine()
	{
		binsicWindow = new BinsicWindow(this)
	}
}

	
	def engine = new BinsicEngine()
	BinsicInterpreter.setTextArea(engine.binsicWindow.screenZX)
	def conf = new CompilerConfiguration()
	conf.setScriptBaseClass("BinsicInterpreter")
	def shell = new GroovyShell(conf)
	def preProc = new BinsicPreprocessor("./src/Binsic/test.bas")
	shell.evaluate(preProc.binsicOut)
/*}
else
	println "Usage: Binsic <BASIC script>"
*/

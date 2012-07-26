package binsic

import groovy.swing.SwingBuilder
import java.awt.Font
import javax.swing.*
//import java.awt.BorderLayout as BL
import java.awt.GridBagConstraints as BL

class BinsicWindow {
	
	def controlObject
	def swinger
	def mainFrame
	def screenZX
	def textIn
	def scroller
	
	BinsicWindow(def controller)
	{
		controlObject = controller
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "Binsic is not Sinclair Instruction Code",
			size:[640, 520],
			show:true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE){
			borderLayout()
			scroller = scrollPane(autoscrolls:true) {
			//	screenZX = textArea(rows:24, columns:32, editable:false,
				//	constraints:BL.NORTH) {visble:true}
			}
			screenZX = new BinsicTextArea(24, 32)
			scroller.add(screenZX, BL.PAGE_START)
			textIn = textField(editable:true, constraints:BL.SOUTH)
			screenZX.setFont(new Font("Monospaced", Font.PLAIN, 18))
		}
	}
}
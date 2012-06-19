package binsic

import groovy.swing.SwingBuilder
import java.awt.Font
import javax.swing.*
import java.awt.BorderLayout as BL

class BinsicWindow {
	
	def controlObject
	def swinger
	def mainFrame
	def screenZX
	def textIn
	
	BinsicWindow(def controller)
	{
		controlObject = controller
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "Binsic is not Sinclair Instruction Code",
			size:[640, 480],
			show:true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE){
			borderLayout()
			scrollPane(autoscrolls:true) {
				screenZX = textArea(rows:24, columns:32, editable:false,
					constraints:BL.NORTH) {visble:true}
			}
			textIn = textField(editable:true, constraints:BL.SOUTH)
			screenZX.setFont(new Font("Monospaced", Font.PLAIN, 18))
		}
	}
}
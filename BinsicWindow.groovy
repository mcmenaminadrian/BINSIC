package Binsic

import groovy.swing.SwingBuilder
import java.awt.Font
import javax.swing.*

class BinsicWindow {
	
	def controlObject
	def swinger
	def mainFrame
	def screenZX
	
	BinsicWindow(def controller)
	{
		controlObject = controller
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "BINSIC is not Sinclair Instruction Code",
			size:[640, 480],
			show:true,
			defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE){
			scrollPane() {
				screenZX = textArea(rows:24, columns:32) {visble:true}
			}
			screenZX.setFont(new Font("Monospaced", Font.PLAIN, 18))
		}
	}
}
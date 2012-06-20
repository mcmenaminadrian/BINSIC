package binsic

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class BinsicInputAction extends AbstractAction {

	def preProc
	def textField
	def countDown
	def result
	
	BinsicInputAction(def window, def preP, def latch)
	{
		preProc = preP
		textField = window
		countDown = latch
	}
	
	void actionPerformed(ActionEvent e)
	{
		result = textField.getText() as Double
		countDown.countDown()
	}
}

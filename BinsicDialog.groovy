package binsic

import groovy.swing.SwingBuilder


class BinsicDialog {

	BinsicDialog()
	{
	new SwingBuilder().dialog(title:"End of BASIC code", modal:false,
			show:true, pack:true) {
			panel {
				button(actionPerformed: this.&kill, "OK")
			}
		}
	}
	
	def kill(def event)
	{
		System.exit(1)
	}
}

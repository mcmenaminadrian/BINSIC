package binsic


import javax.swing.*
import java.awt.Graphics

class BinsicTextArea extends JTextArea {

	def listCommands = []
	
	BinsicTextArea(int rows, int cols)
	{
		super(rows, cols)
		setEditable(false)
		setVisible(true)
	}
	
	def addCommand(def command)
	{
		listCommands << command
	}

	void paintComponent(Graphics g)
	{
		super.paintComponent(g) 
		def index = 0
		while (listCommands[index]) {
			if (listCommands[index] == "plot") {
				g.fillRect(listCommands[index + 1] * 20 as Integer,
					listCommands[index + 2] * 20 as Integer, 20, 20)
				index += 2
				continue
			}
			else if (listCommands[index] == "unplot") {
				g.clearRect(listCommands[index + 1] * 20 as Integer,
					listCommands[index + 2] * 20 as Integer, 20, 20)
				index += 2
				continue
			}
			else if (listCommands[index] == "write") {
				g.drawString(
					listCommands[index + 1], listCommands[index + 2] * 20,
					listCommands[index + 3] * 20)
				index += 3
			}
			else
				break
		}
	}
}

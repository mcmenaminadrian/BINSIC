package binsic

import java.awt.Graphics
import javax.swing.JTextArea

class BinsicGraphicsArea extends JTextArea {

	def model
	
	BinsicGraphicsArea(def rows, def cols, def modelObject)
	{
		super(rows, cols)
		setVisible(true)
		model = modelObject
	}
	
	void paintComponent(Graphics g)
	{
		super.paintComponent(g)
		model.plotList.each {
			g.fillRect(it, 20, 20)
		}
		model.printMap.each {key, value->
			g.drawString(value, key)
		}
	}
}

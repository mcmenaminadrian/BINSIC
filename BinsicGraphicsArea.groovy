package binsic

import java.awt.Graphics
import javax.swing.JTextArea

class BinsicGraphicsArea extends JTextArea {

	def model
	
	BinsicGraphicsArea(def rows, def cols, def modelObject)
	{
		super(rows, cols)
		model = modelObject
	}
	
	void paintComponent(Graphics g)
	{
		super.paintComponent(g)
		model.repaintLock.acquire()
		model.plotList.each {
			g.fillRect(20 * it.x as Integer, 20 * it.y as Integer, 20, 20)
		}
		model.printMap.each {key, value->
			g.drawString(value, 20 * key.x as Integer, 20 * key.y as Integer)
		}
		model.repaintLock.release()
	}
}

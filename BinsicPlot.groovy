package binsic

import java.awt.Color
import javax.swing.JFrame

class BinsicPlot extends JFrame {
	
	def grafix
	def visi = false
	
	BinsicPlot()
	{
		super()
		setSize(640, 440)
		makeVisi()
		grafix = this.getGraphics()
		grafix.setPaint(Color.black)
	}
	
	def plot(def x, def y)
	{
		grafix.fillRect(x * 20 as Integer, y * 20 as Integer, 20, 20)
		update(grafix)
	}
	
	def unplot(def x, def y)
	{
		grafix.clearRect(x * 20 as Integer, y * 20 as Integer, 20, 20)
		update(grafix)
	}
	
	def makeVisi()
	{
		setVisible(true)
		visi = true
	}

}

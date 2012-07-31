package binsic

import groovy.swing.SwingBuilder
import java.awt.Point
import javax.swing.*

class BinsicGraphicsWindow {
	
	def swinger
	def mainFrame
	def graphicsZX
	def plotList = []
	def printMap = [:]
	def scroller
	
	BinsicGraphicsWindow()
	{
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "BINSIC graphics pane",
			size:[640, 520],
			show: true){
			borderLayout()
			scroller = scrollPane()
		}
		graphicsZX = new BinsicGraphicsArea(24, 32, this)
		scroller.add(graphicsZX)
		graphicsZX.setVisible(true)
	}
	
	def insertPlot(def x, def y)
	{
		def plotPoint = new Point(x, y)
		if (plotList.contains(plotPoint))
			return
		else
			plotList << plotPoint
	}
	
	def removePlot(def x, def y)
	{
		plotList.remove(new Point(x, y))
	}
	
	def insertPrint(def x, def y, def string)
	{
		def printPoint = new Point(x, y)
		printMap[printPoint] = string
	}

}

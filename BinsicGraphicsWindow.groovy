package binsic

import groovy.swing.SwingBuilder
import java.awt.Point
import javax.swing.*

class BinsicGraphicsWindow {
	
	def controlObject
	def swinger
	def mainFrame
	def graphicsZX
	def plotList = []
	def printMap = [:]
	
	BinsicGraphicsWindow(def controller)
	{
		controlObject = controller
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "BINSIC graphics pane",
			size:[640, 520],
			show: true)
			}
		graphicsZX = new BinsicGraphicsArea(24, 32, this)
		mainFrame.add(graphicsZX)
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

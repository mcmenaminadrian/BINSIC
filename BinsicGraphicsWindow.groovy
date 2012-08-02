package binsic

import groovy.swing.SwingBuilder
import java.awt.Point
import javax.swing.*
import java.awt.BorderLayout as BL

class BinsicGraphicsWindow {
	
	def swinger
	def mainFrame
	def graphicsZX
	def plotList = []
	def printMap = [:]
	def scroller
	def visible = false
	
	BinsicGraphicsWindow()
	{
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "BINSIC graphics pane",
			size:[640, 520],
			show: true){
			borderLayout()
			scroller = scrollPane() {
				widget(graphicsZX = new BinsicGraphicsArea(24, 32, this),
					constraints: BL.CENTER, visible:true)
			}
		}
	}
	
	def insertPlot(def x, def y)
	{
		if (!visible)
			showGraphicsWindow()
		def plotPoint = new Point(x, y)
		if (plotList.contains(plotPoint))
			return
		else
			plotList << plotPoint
		graphicsZX.repaint()
	}
	
	def removePlot(def x, def y)
	{
		plotList.remove(new Point(x, y))
		graphicsZX.repaint()
	}
	
	def insertPrint(def x, def y, def string)
	{
		if (!visible)
			showGraphicsWindow()
		def printPoint = new Point(x, y)
		printMap[printPoint] = string
		graphicsZX.repaint()
	}
	
	def showGraphicsWindow()
	{
		visible = true
		setVisible(true)
	}

}

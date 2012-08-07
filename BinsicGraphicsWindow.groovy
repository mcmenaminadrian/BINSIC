package binsic

import groovy.swing.SwingBuilder
import java.awt.Point
import javax.swing.*
import java.awt.BorderLayout as BL
import java.util.concurrent.Semaphore

class BinsicGraphicsWindow {
	
	def swinger
	def mainFrame
	def graphicsZX
	def plotList = []
	def printMap = [:]
	def scroller
	def visible = false
	def repaintLock
	
	BinsicGraphicsWindow()
	{
		repaintLock = new Semaphore(1)
		swinger = new SwingBuilder()
		mainFrame = swinger.frame(
			title: "BINSIC graphics pane",
			size:[640, 520],
			show: false){
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
		else {
			repaintLock.acquire()
			plotList << plotPoint
			repaintLock.release()
		}
		graphicsZX.repaint()
	}
	
	def removePlot(def x, def y)
	{
		repaintLock.acquire()
		plotList.remove(new Point(x, y))
		repaintLock.release()
		graphicsZX.repaint()
	}
	
	def insertPrint(def x, def y, def message)
	{
		if (!visible)
			showGraphicsWindow()
		def printPoint = new Point(x, y)
		repaintLock.acquire()
		printMap[printPoint] = message
		repaintLock.release()
		graphicsZX.repaint()
	}
	
	def showGraphicsWindow()
	{
		visible = true
		mainFrame.show()
	}

}

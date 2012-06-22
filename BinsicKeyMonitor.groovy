package binsic

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener

class BinsicKeyMonitor implements KeyListener{

	def currentKey = ""
	
	void keyTyped(KeyEvent e){}
	void keyReleased(KeyEvent e){
		currentKey = ""
	}
	void keyPressed(KeyEvent e){
		currentKey = Character.toChars(e.keyCode & 0xFF) 
	}
	
	def getCurrentKey()
	{
		return currentKey
	}
}

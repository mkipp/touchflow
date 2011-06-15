package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class KeyListenerImpl implements KeyListener{
//--------  Keylistener für Keyboard
	
	HashMap<Integer, Boolean> pressed=new HashMap<Integer,Boolean>();
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (!pressed.keySet().contains(arg0.getKeyCode())){
			pressed.put(arg0.getKeyCode(),true);
		}
		

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (pressed.keySet().contains(arg0.getKeyCode())) {
			pressed.remove((Integer)arg0.getKeyCode());
			pressed.put((Integer)arg0.getKeyCode(),false);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		
	}
	
	public boolean isKeyDown(int keyCode){
		if (pressed.keySet().contains(keyCode)){
			if (!pressed.get(keyCode)){
				pressed.remove(keyCode);
				
			}
			return true;
		}
		return false;
	}
}

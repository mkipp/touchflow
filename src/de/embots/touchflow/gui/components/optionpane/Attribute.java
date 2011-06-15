package de.embots.touchflow.gui.components.optionpane;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Attribute extends JPanel{
	
	Object content;
	String Caption="";
	
	JFrame mainFrame=null; //needed for pack() -> update sizes & layout
	
	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public abstract void setContent(Object in);
	
	public Object getContent(){
		
		return content;
	}
	
	protected abstract void updateContent();
}

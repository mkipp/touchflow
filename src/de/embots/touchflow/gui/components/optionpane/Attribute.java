package de.embots.touchflow.gui.components.optionpane;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Attribute extends JPanel{
	
	Object content;
	String Caption="";
	boolean newContent=false;
	
	OptionPane mainFrame=null; //needed for pack() -> update sizes & layout
	
	public void setMainFrame(OptionPane mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public abstract void setContent(Object in);
	
	public Object getContent(){
		
		return content;
	}
	
	public boolean isNewContent() {
		return newContent;
	}

	public void setNewContent(boolean newContent) {
		this.newContent = newContent;
	}

	protected void updateContent(){
		newContent=true;
		if (mainFrame!=null) mainFrame.newData();
	}
}

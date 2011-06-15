package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.KeyListenerImpl;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.gui.components.optionpane.StringAttribute;

import java.awt.event.KeyEvent;

import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.util.RAClass;


public class Keyboard extends InputModule  {

	@Override
	public void openOptions() {
		StringAttribute keyarg=new StringAttribute("Taste");
		keyarg.setContent(KeyEvent.getKeyText(KeyCode));
		
		OptionPane.showOptionPane(new Attribute[]{keyarg},this);

	}


	@Override
	public void reinit(Attribute[] args) {
		String key=(String) args[0].getContent();
		if (key.length()!=1){
			RAClass.msgbox("Sie muessen genau ein Zeichen eingeben!", "Fehler", "Warning");
			return;
		}
		char keyc=key.charAt(0);
		
		KeyCode=charToKeyEvent(keyc);
	}

	@Override
	public void init(String params) throws ModulException {
		try{
			KeyCode=Integer.parseInt(params);
		}
		catch(Exception nf){
			throw new ModulFactoryException("Keyboard:Konstruktorparam fehlt oder ist kein int");
		}
	}


	private int charToKeyEvent(char zeichen){
		zeichen=Character.toUpperCase(zeichen);
		String z=zeichen+"";
		String sym;
		for (int i=0;i<256;i++){
			sym=KeyEvent.getKeyText(i);
			if (z.equals(sym)) return i;
		}
		return -1;
	}
	int KeyCode;
	
	KeyListenerImpl attachedComponent;
	
		
	public void attachComponent(KeyListenerImpl listener){
		attachedComponent=listener;
	}
	
	public Keyboard() throws ModulException{
		this(65);
	}
	public Keyboard(int keyCode) throws ModulException {
		super();
		KeyCode=keyCode;
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.PRESSED,this);

		
		//PortMap
		addPortMapEntry(4, PinName.PRESSED);
	}

	@Override
	protected void processData() throws ModulException {
		if (attachedComponent!=null){
			if (attachedComponent.isKeyDown(KeyCode)){
				getOutputPin(PinName.PRESSED).writeData(1);
			}
			else{
				getOutputPin(PinName.PRESSED).writeData(0);
			}
		}
	}

	

	@Override
	public String getDescription() {
		return "<html>Outputs wheather a specific key is pressed or not.<br><br><i>PRESSED</i> is <br><b>1</b> if the button is pressed,<br><b>0</b> if not</html>";
	}


	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",KeyCode+"");
		
	}


	@Override
	public String getModuleName() {
		
		return "Keyboard";
	}



}

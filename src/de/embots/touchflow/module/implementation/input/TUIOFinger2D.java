package de.embots.touchflow.module.implementation.input;

import java.net.SocketException;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.implementation.input.stantum.MTPoint;
import de.embots.touchflow.module.implementation.input.stantum.SMTListeningSocket;
import de.embots.touchflow.module.implementation.input.stantum.StantumBuffer;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

import org.jdom.Element;

import TUIO.TuioClient;
import TUIO.TuioCursor;
import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;

public class TUIOFinger2D extends InputModule {
public static TUIOBuffer iface;
public static StantumBuffer stantumBuffer;
public static Thread socketThread;
	
	public TUIOFinger2D(){
		this(1);
	}

	public TUIOFinger2D(int fingerId) {
		super();
		
		//if interface not started yet
		
		if (Globals.useStantum){
			
			if (stantumBuffer==null){
				stantumBuffer=new StantumBuffer();

				
					SMTListeningSocket socket=SMTListeningSocket.getInstance();
					socket.addMultitouchListener(stantumBuffer);
					socketThread=new Thread(socket);
					socketThread.start();
				
			
				
			}
		}
		
		else{
			
		
			if (iface==null){
				iface=new TUIOBuffer();
				TuioClient client = new TuioClient(Globals.TUIOPort);
				 client.addTuioListener(iface);
				 client.connect();
			}
		}
		
		this.fingerId = fingerId;
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin2D(pinName.POSITION,this);
		

	}

	@Override
	public void init(String params) throws ModulException {
		try{
			fingerId=Integer.parseInt(params);
		}
		catch(Exception nf){
			throw new ModulFactoryException("Finger:Konstruktorparam fehlt oder ist kein int:" + params );
		}
	}

	@Override
	public void openOptions() {
		NumberAttribute kxarg=new NumberAttribute("Fingernummer");
		kxarg.setContent(fingerId);
	
		
		OptionPane.showOptionPane(new Attribute[]{kxarg},this);
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=((Double) args[0].getContent());
		fingerId=(int) d;
	}

	public int getFingerId() {
		return fingerId;
	}

	int fingerId;
	
	@Override
	public String getDescription() {
		return "<html>Outputs the position of the n-th finger on a multitouchscreen,<br>where n=<b>fingerID</b>";
	}

	@Override
	protected void processData() throws ModulException {
		
		if (Globals.useStantum){
			MTPoint cursor=stantumBuffer.getCursor(fingerId);
			
			//Cursor existiert nicht -> pins auf 0 setzen
			if (cursor==null){
				getOutputPin2D(pinName.POSITION).writeData(0);
				getOutputPin2D(pinName.POSITION).writeData2(0);
			}
			else{
				getOutputPin(pinName.POSITION).writeData((int)(cursor.x));
				getOutputPin2D(pinName.POSITION).writeData2((int)(cursor.y));
			}
		}
		
		else{
			
			TuioCursor cursor=TUIOBuffer.getCursorFromId(fingerId);
			
			//Cursor existiert nicht -> pins auf 0 setzen
			if (cursor==null){
				getOutputPin2D(pinName.POSITION).writeData(0);
				getOutputPin2D(pinName.POSITION).writeData2(0);
			}
			else{
				getOutputPin(pinName.POSITION).writeData((int)(cursor.getX()*Globals.ScreenWidth));
				getOutputPin2D(pinName.POSITION).writeData2((int)(cursor.getY()*Globals.ScreenHeight));
			}
		}
		
		
	}


	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",fingerId+"");
		
	}

	@Override
	public String getModuleName() {
		
		return "TUIOFinger";
	}

}

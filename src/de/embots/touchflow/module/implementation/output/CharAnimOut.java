package de.embots.touchflow.module.implementation.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import org.jdom.Element;


import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.gui.components.optionpane.StringAttribute;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;

/**
 * Writes attribute-value pairs as string on UDP socket - for Michael Neff's
 * correlation-map based character animation software.
 * 
 * The module should only write those parameters that have their input pin
 * wired up.
 * 
 * In the current implementation, the server has to be running under port 1234.
 */

public class CharAnimOut extends OutputModule {
	int port=1234;
	String host="localhost";
	java.net.Socket server;
	BufferedWriter out;
	
	public CharAnimOut(){
		inputPins=new InputPin[16];
		inputPins[0]=new InputPin(PinName.rightwristx,this);
		inputPins[1]=new InputPin(PinName.rightwristy,this);
		inputPins[2]=new InputPin(PinName.leftwristx,this);
		inputPins[3]=new InputPin(PinName.leftwristy,this);
		inputPins[4]=new InputPin(PinName.corcentre,this);
		inputPins[5]=new InputPin(PinName.colcentre,this);
		inputPins[6]=new InputPin(PinName.rknee,this);
		inputPins[7]=new InputPin(PinName.lknee,this);
		inputPins[8]=new InputPin(PinName.sagcentre,this);
		inputPins[9]=new InputPin(PinName.corcentre,this);
		inputPins[10]=new InputPin(PinName.colcentre,this);
		inputPins[11]=new InputPin(PinName.comx,this);
		inputPins[12]=new InputPin(PinName.headz,this);
		inputPins[13]=new InputPin(PinName.trancentre,this);
		inputPins[14]=new InputPin(PinName.colycentre,this);
		inputPins[15]=new InputPin(PinName.pelvisy,this);

		
	}
	private void initSocket() {
		
		/*Out-vals:
		 * 
		 * rightwristx
		 * rightwristy
		 * leftwristx
		 * leftwristy
		 * corcentre
		 * colcentre
		 * rknee
		 * lknee
		 * sagcentre
		 * corcentre
		 * colcentre
		 * comx
		 * headz
		 * trancentre
		 * colycentre
		 * pelvisy
		 */
		
		try {
			server=new java.net.Socket(host,port);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.err.println("ERROR establishing socket connection: Unknown host:" + host);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("ERROR establishing socket connection:" + e.getMessage() + " - is server online?");
			e.printStackTrace();
		}
	}
	@Override
	protected void processData() throws ModulException {
		double data;
		
		
		
		
		
		
		for (int i=0; i<inputPins.length;i++){
			
			InputPin actualPin=inputPins[i];
			
			if (actualPin.getConnectedPin()!=null){
				//init socket if not already done
				initSocket();
				
				try {
					out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				
				
				String msg=actualPin.getName().toString() + " " + actualPin.getData() + "\n";
		
				//send msg
		
		
		
				try {
				//System.err.println("written:" + msg);
					
					out.write(msg);
					out.flush();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("Socket: Error writing data:" + e.getMessage());
				}
				
			}
			try {
				server.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			
			
		
		
		
		

	}
	
	@Override
	public String getDescription() {
		return "<html>Sends <b>IN</b> over a Socket</html>";
	}
	
	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",port+" " + host);

	}
	
	@Override
	public void init(String params) throws ModulException {
		String[] par=params.split(" ");
		
		if (par.length!=2) throw new ModulFactoryException("Socket: Mehr oder weniger als 2 Konstruktorparameter");
		try{
			port=Integer.parseInt(par[0]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("Keyboard:Konstruktorparam fehlt oder ist kein int");
		}
		
		host=par[1];
	}
	
	@Override
	public void openOptions() {
		StringAttribute host=new StringAttribute("host");
		host.setContent(this.host);
		
		NumberAttribute port=new NumberAttribute("port");
		port.setContent(this.port);
		
		OptionPane.showOptionPane(new Attribute[]{host,port},this);

	}
	
	@Override
	public void reinit(Attribute[] args) {
		host=(String) args[0].getContent();
		double d=(Double)args[1].getContent();
		port =(int) d;
	}
	@Override
	public String getModuleName() {
		
		return "CharAnimOut";
	}

}

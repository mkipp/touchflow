package de.embots.touchflow.module.implementation.output;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import org.jdom.Element;

import de.embots.touchflow.util.RAClass;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.gui.components.optionpane.StringAttribute;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;

public class Socket2D extends OutputModule {
	int port=1234;
	String host="localhost";
	java.net.Socket server;
	BufferedWriter out;
	
	public Socket2D(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin2D(PinName.OUT,this);
		
		
	}
	private void initSocket() {
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
		double data,data2;
		
		//init socket if not already done
		initSocket();
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		data=getInputPin(PinName.OUT).getData();
		data2=getInputPin2D(PinName.OUT).getData2();
		String msg=data+";"+data2;
		
		//send msg
		
		
		
		try {
			//System.err.println("written:" + msg);
			out.write(msg);
			out.flush();
			server.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Socket: Error writing data:" + e.getMessage());
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
		
		return "Socket 2D";
	}

}

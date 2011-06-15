package module.implementation.output;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import org.jdom.Element;

import util.RAClass;

import exceptions.ModulException;
import exceptions.ModulFactoryException;
import gui.touchflow.components.OptionPane.Attribute;
import gui.touchflow.components.OptionPane.NumberAttribute;
import gui.touchflow.components.OptionPane.OptionPane;
import gui.touchflow.components.OptionPane.StringAttribute;
import module.core.OutputModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.InputPin2D;

public class Socket extends OutputModule {
	int port=1234;
	String host="localhost";
	java.net.Socket server;
	BufferedWriter out;
	
	public Socket(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(pinName.OUT,this);
		
		
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
		double data;
		
		//init socket if not already done
		initSocket();
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		data=getInputPin(pinName.OUT).getData();
		
		String msg=data+"";
		
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
		
		return "Socket";
	}

}

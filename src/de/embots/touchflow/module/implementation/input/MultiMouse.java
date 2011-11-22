package de.embots.touchflow.module.implementation.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

public class MultiMouse  extends InputModule implements Runnable{

	int port=1235;
	
	static int xpos=0;
	static int ypos=0;
	
	private static ServerSocket server;
	boolean running=false;
	static boolean isLMouseDown=false;
	static boolean isRMouseDown=false;
	private static BufferedReader in;
	private static Thread thisThread=null;
	
	
	@Override
	protected void processData() throws ModulException {
		OutputPin2D out=getOutputPin2D(PinName.POSITION);
		out.writeData(xpos);
		out.writeData2(ypos);
	
	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "MultiMouse";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		

	}
	
	public MultiMouse(){
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin2D(PinName.POSITION,this);
		startListenerThread();
	}

	@Override
	public void run() {
		try {
			startServer();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(running){	
			String msg="";
			try {
				msg = in.readLine();
				parseMSG(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			server.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}

	private void parseMSG(String msg) {
		msg.trim();
		String[] parts=msg.split(" ");
		if (parts.length!=3){
			System.err.println("WARNING: MultiMouse:could not parse/split msg:" + msg);
			return;
		}
		
		if (parts[0].equals("motion")){
			String[] xcor=parts[1].split("=");
			String[] ycor=parts[2].split("=");
			
			if (xcor.length!=2 || ycor.length!=2){
				System.err.println("WARNING: MultiMouse:could not parse/2split msg:" + msg);
				return;
			}
			
			try{
				xpos=Integer.parseInt(xcor[1]);
				ypos=Integer.parseInt(ycor[1]);
			}
			catch(NumberFormatException nf){
				System.err.println("WARNING: MultiMouse:could not parse/numberParse msg:" + msg);
			}
			return;
		}
		
		if (parts[0].equals("button")){
			if (parts[1].equals("press")){
				if (parts[2].equals("1")){
					this.isLMouseDown=true;
				}
				else if(parts[2].equals("2")){
					this.isRMouseDown=true;
				}
			}
			else{
				if (parts[2].equals("1")){
					this.isLMouseDown=false;
				}
				else if(parts[2].equals("2")){
					this.isRMouseDown=false;
				}
			}
			return;
		}
		
		System.err.println("WARNING: MultiMouse:could not parse/get type of msg:" + msg);
		
	}

	@Override
	public void stopRunning() {
		// TODO Auto-generated method stub
		super.stopRunning();
		running=false;
	}

	public void startServer() throws IOException{
		if (server==null){
			System.out.println("Try MM server on port " + port);
			server=new ServerSocket(port);
			System.out.println("Created MM server on port " + port);
			Socket s=server.accept();
			
			in = new BufferedReader(new InputStreamReader(
	                   s.getInputStream()));
			
		}

		
	}
	public void startListenerThread(){
		if (thisThread==null){
			Thread t=new Thread(this);
			running=true;
			t.start();
		}
	}
}

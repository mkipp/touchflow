package de.embots.touchflow.module.implementation.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.factory.LibraryManager;
import de.embots.touchflow.util.KinectPoint;

public class KinectServer implements Runnable{
	
	/**
	 * Lauscht auf einen bestimmten Port umKinect-Bewegungen abzufangen
	 */
	
	private ServerSocket server;
	static KinectPoint leftHandPos=new KinectPoint();
	static KinectPoint rightHandPos=new KinectPoint();
	
	private static KinectServer thisServer=new KinectServer();
	static{
		thisServer.init(1234);
	}
	
	private boolean running;
	private Thread thisThread;
	private ArrayList<Module> listeners=new ArrayList<Module>();
		
	public void stopListening(Module listener){
		listeners.remove(listener);
		
		//no more listeners-> stop running & reset server
		if (listeners.size()==0) {
			running=false;
			server=null;
		}
	}
	
	/*
	 * default port: 2020
	 */
	public void init(int port){
		running=true;
		
		try {
			server=new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		thisThread=new Thread(this);
		thisThread.start();
	}
	
	
	
	public static KinectPoint getLeftHandPos() {
		return leftHandPos;
	}

	public static KinectPoint getRightHandPos() {
		return rightHandPos;
	}

	public void addListener(Module listener){
		listeners.add(listener);
	}
	@Override
	public void run() {
		while(running){
			
			Socket client = null;
			try {
				client = server.accept();
		
			BufferedReader cliIn;
	
			cliIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String msg=cliIn.readLine();
			
			System.out.println("got kinect xml:" + msg);
			parseXML(msg);
			
			//System.out.println("recv:" + zahl + " by " + server.getLocalPort());
			cliIn.close();
			client.close();
				
			}
			catch (Exception e){
				//e.printStackTrace();
			}
		}
		
	}

	/*
	 * sample xml block
	 * 
	 * <Kinect_Data>
			<Right_foot X="0" Y="3.5" Z="4" Confidence="-123" />
			<Left_foot X="0" Y="3.5" Z="4" Confidence="-123" />
			<Right_hip X="0.2" Y="-0.1" Z="0.7" Confidence="1" />
			<Left_hip X="0.2" Y="-0.1" Z="0.9" Confidence="1" />
			<Right_hand X="0.2" Y="-0.3" Z="0.5" Confidence="1" />
			<Left_hand X="0.4" Y="-0.3" Z="0.6" Confidence="1" />
		</Kinect_Data>
	 */
	private void parseXML(String msg) throws ModulException {
			
		SAXBuilder sxbuild  = new SAXBuilder();
		InputSource is = new InputSource(new StringReader(msg));

		Document doci;
		Element rooti;

		//Versuchen den Block zu parsen

		try {
			doci = sxbuild.build(is);
		} catch (Exception e) {
			if (Globals.isDebug) e.printStackTrace();
			throw new ModulException("KinectServer: Exception parsing XML block:" + e.getMessage());
		} 

		rooti = doci.getRootElement();
		System.err.println(rooti.getName() + ":" + rooti.getContentSize());
		
		// Phase 1: Module an sich aufbauen, ohne Verbindungen

		for (Object o:rooti.getContent()){
			//Modul in factory herstellen und in den Graph einhängen
			if (! (o instanceof Element)){
				System.err.println("invalid xml structure. got" + o.getClass());
				System.err.println(o);
				return;
			}
			
			
			
			Element e=(Element) o;

			
			parsePos(e, rightHandPos, "Right_hand");
			parsePos(e, leftHandPos, "Left_hand");	
				
		}
		
	}

	private void parsePos(Element e, KinectPoint destination, String identString) throws ModulException {
		
		if (e.getName().equals(identString)){
			double x,y,z;
			try{
				x=Double.parseDouble(e.getAttributeValue("X"));
				y=Double.parseDouble(e.getAttributeValue("Y"));
				z=Double.parseDouble(e.getAttributeValue("Z"));
			}
			catch(NumberFormatException nf){
				throw new ModulException("KinectServer Could not parse position");
			}
			destination.x=x;
			destination.y=y;
			destination.z=z;
		}
	}

}

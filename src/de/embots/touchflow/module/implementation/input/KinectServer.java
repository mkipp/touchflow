package de.embots.touchflow.module.implementation.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.util.KinectPoint;

public class KinectServer implements Runnable{
	
	/**
	 * Lauscht auf einen bestimmten Port umKinect-Bewegungen abzufangen
	 */
	
	private ServerSocket server;
	static KinectPoint leftHandPos=new KinectPoint();
	static KinectPoint rightHandPos=new KinectPoint();
	
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

	private void parseXML(String msg) {
		// TODO Auto-generated method stub
		
	}

}

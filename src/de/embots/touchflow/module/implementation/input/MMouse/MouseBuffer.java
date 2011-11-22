package de.embots.touchflow.module.implementation.input.MMouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.embots.touchflow.module.implementation.input.MultiMouse;

public class MouseBuffer implements Runnable{
	
	
	
	ServerSocket server=null;
	int port=1235;
	private static ArrayList<MouseState> mouses=new  ArrayList<MouseState>();
	private boolean running=true;
	private Thread thisThread;
	
	/**
	 * waits for new clients to connect, and spawns a listener thread then
	 * @throws IOException
	 */
	public void spawnListeners() throws IOException{
		if (server==null){
			System.out.println("Try MM server on port " + port);
			server=new ServerSocket(port);
			System.out.println("Created MM server on port " + port);
		}
		Socket s=server.accept();
		mouses.add(new MouseState(s));
	}
	
	@Override
	public void run() {
		while(running){
			try {
				spawnListeners();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public MouseBuffer(){
		thisThread=new Thread(this);
		thisThread.start();
	}

	public static MouseState getMouseState(int id){
		if (id<0||id>=mouses.size()) return null;
		return mouses.get(id);
	}

}

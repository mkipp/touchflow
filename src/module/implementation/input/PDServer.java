package module.implementation.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import module.core.Module;

public class PDServer implements Runnable{
	
	/**
	 * Lauscht auf einen bestimmten Port der PD_Bridge 
	 */
	
	private ServerSocket server;
	private int lastNum;
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
	
	public int getLastNum() {
		return lastNum;
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
		
			InputStream cliIn;
	
			cliIn = client.getInputStream();
			
			int zahl=cliIn.read();
			lastNum=zahl;
			//System.out.println("recv:" + zahl + " by " + server.getLocalPort());
			cliIn.close();
			client.close();
				
			}
			catch (Exception e){
				//e.printStackTrace();
			}
		}
		
	}

}

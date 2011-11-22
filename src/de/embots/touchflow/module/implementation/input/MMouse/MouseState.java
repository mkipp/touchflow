package de.embots.touchflow.module.implementation.input.MMouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MouseState implements Runnable{
	int xpos,ypos,scrollpos;
	BufferedReader source;
	Thread thisThread;
	
	public void scrollForward(){
		scrollpos++;
	}
	public int getXpos() {
		return xpos;
	}
	public int getYpos() {
		return ypos;
	}
	public int getScrollpos() {
		return scrollpos;
	}
	public void scrollBackward(){
		scrollpos--;
	}
	
	public MouseState(Socket s){
		try {
			source=new BufferedReader(new InputStreamReader(
			        s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thisThread=new Thread(this);
		thisThread.start();
	}
	
	private boolean running=true;
	private boolean isLMouseDown;
	private boolean isRMouseDown;
	@Override
	public void run() {
		
		while(running){	
			String msg="";
			try {
				if (source!=null){
					msg = source.readLine();
					if (msg==null) {
						running=false; //stream closed->terminate
						break;
					}
					parseMSG(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		/*try {
			server.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
		
		
	}

	private void parseMSG(String msg) {
		msg.trim();
		String[] parts=msg.split(" ");
		if (parts.length<3){
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
		
		int partslength=parts.length-1;
		
		if (parts[0].equals("button")){
			if (parts[1].equals("press")){
				if (parts[partslength].equals("1")){
					this.isLMouseDown=true;
				}
				else if(parts[partslength].equals("2")){
					this.isRMouseDown=true;
				}
				else if(parts[partslength].equals("4")){
					scrollForward();
				}
				else if(parts[partslength].equals("5")){
					scrollBackward();
				}
			}
			else{
				if (parts[partslength].equals("1")){
					this.isLMouseDown=false;
				}
				else if(parts[partslength].equals("2")){
					this.isRMouseDown=false;
				}
				
			}
			return;
		}
		
		System.err.println("WARNING: MultiMouse:could not parse/get type of msg:" + msg);
		
	}
}

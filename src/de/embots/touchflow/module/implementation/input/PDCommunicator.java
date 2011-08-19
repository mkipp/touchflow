package de.embots.touchflow.module.implementation.input;

import java.util.HashMap;

import de.embots.touchflow.module.core.Module;

public class PDCommunicator {
	/**
	 * master class for PD-cummunication. creates PDServers & manages communication 
	 */

	private static HashMap<Integer,PDServer> servers=new HashMap<Integer, PDServer>();
	
	public static void init(Module listener, int port){
		PDServer server;
		if (servers.containsKey(port)){
			server=servers.get(port);
		}
		else{
			server=new PDServer();
			servers.put(port, server);
			server.init(port);
		}
		
		
		server.addListener(listener);
		
	}
	
	public static int getLastNum(int port){
		PDServer server;
		if (servers.containsKey(port)){
			server=servers.get(port);
			return server.getLastNum();
		}
		else{
			return 0;
		}
	}
	
	public static void stopListening(Module listener, int port){
		PDServer server;
		if (servers.containsKey(port)){
			server=servers.get(port);
			server.stopListening(listener);
		}
	}
}

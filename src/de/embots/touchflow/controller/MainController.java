package de.embots.touchflow.controller;

import de.embots.touchflow.exceptions.ModulException;

public class MainController {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length!=1){
			System.err.println("Keine Eingabedatei angegeben");
			return;
		}
		XMLParser parser=new XMLParser();
		try {
			parser.parseXMLFile(args[0]);
		} catch (ModulException e) {
			System.err.println("Fehler beim laden des XML-files: " + e.getMessage());
			return;
		}
		
		ModulesController controller=new ModulesController(parser.getGraph());
		

		controller.startSystem();
		Thread.sleep(10000);
		//controller.stopAll();
	}

}

package de.embots.touchflow.controller;

import java.io.FileOutputStream;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.factory.LibraryManager;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ParserException;


public class XMLParser {
	
private ModuleGraph graph;

public ModuleGraph getGraph() {
	return graph;
}

/**
 * speichert den Graph als XML-Datei zum späteren laden
 * @param fileName
 * @return
 * @throws ParserException 
 */
public boolean saveToXMLFile(String fileName) throws ParserException{
	
	
	
	//Datei öffnen, nacheinander Modulbeschreibungszeilen abholen und reinschreiben
	Element root = new Element("InModulix");
	
          //schauen ob vergessen wurde graph reinzumachen
        if (graph==null){
        	System.err.println("XMLParser: kein Graph");
        	return false;
        }
        
        //module einzeln abholen und reinschreiben
        
        for (Module m:graph.getAllModules()){
        	root.addContent(m.getSaveEntry());
        }
        
        
        Document doc = new Document(root);
        XMLOutputter outputter = new XMLOutputter();
        
        //huebsches Format mit einrueckung verwenden
        outputter.setFormat(Format.getRawFormat());
  	FileOutputStream output;
	try {
		output = new FileOutputStream(fileName);
		outputter.output(doc,output);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		throw new ParserException("Fehler beim speichern des XML:" + e.getMessage());
		
	}
  	

        
        
        
    //hat geklappt
	return true;
}


public XMLParser(ModuleGraph graph) {
	super();
	this.graph = graph;
}

/**
 * parst ein XML-file und speichert es im Attribut "graph"
 * @param fileName
 * @throws ModulException
 */

public void parseXMLFile(String fileName) throws ModulException{
	
	
SAXBuilder sxbuild  = new SAXBuilder();
InputSource is = new InputSource(fileName);

Document doci;
Element rooti;

graph=new ModuleGraph();

//Versuchen das file zu laden

try {
	doci = sxbuild.build(is);
} catch (Exception e) {
	if (Globals.isDebug) e.printStackTrace();
	throw new ModulException("Exception Loading XML File:" + e.getMessage());
} 

rooti = doci.getRootElement();

// Phase 1: Module an sich aufbauen, ohne Verbindungen

for (Object o:rooti.getContent()){
	//Modul in factory herstellen und in den Graph einhängen
	Element e=(Element) o;
	Module m=LibraryManager.manager.createModulFromEntry(e); //ModuleFactory.createModulFromEntry(e);
	graph.addModul(m, m instanceof OutputModule);
}

//MaxID richtig setzen, damit bei zur Laufzeit erstellten Modulen
// ids nicht doppelt vergeben werden
Module.setIdcounter(LibraryManager.manager.getMaxModulID()+1);



//Phase 2: Verbindungen herstellen

//wieder über alle Moduleinträge gehen
for (Object o:rooti.getContent()){
	
	Element e=(Element) o;
	Module m;
	
	//Modul raussuchen
	int id=Integer.parseInt(e.getAttribute("id").getValue());
	
	m=graph.findModul(id);
	
	//für jeden InputPin schauen, ob eine Verbindung vorliegt
	for (InputPin p:m.getInputPins()){
		if (e.getAttribute(p.getName() + "ModulID")!=null){
			
			//Verbindung liegt vor -> Attribute extrahieren
			
			//zuerst id des anderen Moduls
			int otherID;
			try{
				otherID=Integer.parseInt(e.getAttribute(p.getName() + "ModulID").getValue());
			}
			catch(NumberFormatException nf){
				throw new ParserException("OtherModuleID ist kein int bei Modul " + id);
			}
			
			//dann Name des anderen Pins
			String otherName;
			
			if (e.getAttribute(p.getName() + "PinName")==null)throw new ParserException("Name des Verbundenen Pins fehlt bei Modul " + id +  ", Pin " + p.getName());
			
			otherName=e.getAttribute(p.getName() + "PinName").getValue();
			
			
			//Pin aus anderemModul rausziehen
			Module OtherModul=graph.findModul(otherID);
			OutputPin otherPin=OtherModul.getOutputPin(strToName(otherName));
			
			
			//endlich verbinden
			
			p.connectTo(otherPin);
			
		}
	}
}

	
	
}


//public void parseXMLFile(String fileName) throws ModulException{
//
//	graph=new ModuleGraph();
//	
//	//beinhaltet die Inputpins mit verbindungen zur id eines Moduls für Phase 2
//	HashMap<Integer,ArrayList<String>> pinsPerModule=new HashMap<Integer, ArrayList<String>>();
//	
//	BufferedReader in;
//	
//	
//	//--------  1. Stufe: Module an sich aufbauen, ohne Verbindungen
//	
//	
//	try {
//		in = new BufferedReader(new FileReader(fileName));
//		String zeile = null;
//		//die Zeilen für den Moduleintrag
//		ArrayList<String> entry=new ArrayList<String>();
//		
//		//die Zeilen mit Pineinträgen für Phase 2
//		ArrayList<String> pins=new ArrayList<String>();
//		
//		
//		while ((zeile = in.readLine()) != null) {
//			//Kommentarzeilen ausblenden
//			if (!zeile.trim().substring(0, 1).equals("//")){
//				
//				//neue Moduldeklaration
//				if (zeile.trim().equals("<modul>")){
//					entry=new ArrayList<String>();
//					pins=new ArrayList<String>();
//					
//				}
//				
//				//Moduldeklaration zu Ende-> erstellen
//				if (zeile.trim().equals("</modul>")){
//					Modul toadd=ModulFactory.createModulFromEntry(entry);
//					graph.addModul(toadd, toadd instanceof OutputModul);
//					pinsPerModule.put(toadd.getId(),pins);
//				}
//				
//				//Pin-spezifikation-> für phase 2 aufheben
//				if (zeile.trim().startsWith("INPIN")){
//					pins.add(zeile.trim());
//				}
//				else{
//					entry.add(zeile.trim());
//				}
//				
//			}
//		}
//		in.close();
//		
//		
//	} catch (Exception e) {
//		throw new ParserException("Fehler beim Laden:" + e.getMessage());
//	}
//	
//	//idcounter richtig setzen, damit neue module richtige id bekommen
//	Modul.setIdcounter(ModulFactory.getMaxModulID()+1);
//	
//// -----------------  Phase 2: Pins verbinden
//	
//	
//	for (int modid:pinsPerModule.keySet()){
//		ArrayList<String> pins=pinsPerModule.get(modid);
//		
//		for (String pinLine:pins){
//			
//			//Wort "InPin " abschneiden
//			pinLine=pinLine.substring(6);
//			String[] specs=pinLine.split(" ");
//			
//			//prüfen ob genug Teile rauskamen
//			if (specs.length!= 3) throw new ParserException("Pin-Spezifikation hat zu viele Argumente: " + specs.length + " bei " + pinLine);
//			
//			//einzelne parameter aus dem array auslesen, evtl. casten
//			String pinName=specs[0];
//			int id;
//			
//			try{
//				id=Integer.parseInt(specs[1]);
//			}
//			catch (NumberFormatException nf){
//				throw new ParserException("id ist kein int: " + specs[1]);
//			}
//			
//			String andererName=specs[2];
//			
//			//passenden gegenpin raussuchen
//			Pin anderes=graph.findModul(id).getPin(strToName(andererName));
//			
//			//ist der andere auch ein outputpin?
//			if (!(anderes instanceof OutputPin)) throw new ParserException("Zielpin ist kein Output-Pin!");
//			
//			//pins verbinden
//			graph.findModul(modid).getInputPin(strToName(pinName)).connectTo((OutputPin)anderes);
//		}
//	
//	}
//	
//	
//	
//}




/**
 * Wandelt String in pinName um
 * @param typ
 * @return
 * @throws ParserException
 */
private pinName strToName(String typ) throws ParserException{
	for (pinName m:pinName.values()){
		if (m.toString().equals(typ)) return m;
	}
	throw new ParserException("Pinname " + typ + " existiert nicht");
}


public XMLParser(){
	
}



}

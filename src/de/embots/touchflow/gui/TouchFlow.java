package de.embots.touchflow.gui;

import de.embots.touchflow.gui.ToolBox;
import de.embots.touchflow.gui.NodeStorage;
import de.embots.touchflow.gui.IXMarqueeHandler;
import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ParserException;
import de.embots.touchflow.gui.components.GraphModul;
import de.embots.touchflow.gui.components.MyJGraph;
import de.embots.touchflow.gui.components.PinPort;
import de.embots.touchflow.gui.components.Toolbar;
import de.embots.touchflow.gui.serializer.GraphSerializer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputListener;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.Settings;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.implementation.input.stantum.SMTListeningSocket;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.Pin;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;

import de.embots.touchflow.util.RAClass;
import de.embots.touchflow.controller.XMLParser;


public class TouchFlow implements MouseInputListener{
	
	//private static ArrayList<GraphModul> nodes=new ArrayList<GraphModul>();
	//private static ArrayList<DefaultEdge> edges=new ArrayList<DefaultEdge>();
	
	private static PortView firstPort=null;
	private static MyJGraph graph;
	
	private static TouchFlow graphTool;
	private static String lastFile=null;
	private static double ScaleRate=1.0;
	private static NodeStorage storage;
	
	public static void clearGraph(){ 
		for (GraphModul m:getNodes()){
			graph.getModel().remove(m.getChildren().toArray());
		}
		
		graph.getModel().remove(getNodes().toArray());
		graph.getModel().remove(getEdges().toArray());
		storage.clear();
	}
	
	
	public static void decZoom(){
		if (ScaleRate > Globals.ZoomStep) ScaleRate-=Globals.ZoomStep;
		graph.setScale(ScaleRate);
	}

		public static DefaultEdge[] getConnectedEdges(GraphModul modul){
			ArrayList<DefaultEdge> edges=new ArrayList<DefaultEdge>();
			
			for (DefaultEdge e:storage.getKanten()){
				
				if (((PinPort)e.getSource()).getParent()==modul || ((PinPort)e.getTarget()).getParent()==modul) edges.add(e);
			}
			
			DefaultEdge[] ret=new DefaultEdge[edges.size()];
			
			for (int i=0; i< ret.length;i++){
				ret[i]=edges.get(i);
			}
			
			return ret;
		}

		public static ArrayList<DefaultEdge> getEdges() {
			return storage.getKanten();
		}
	
	public static PinPort getFirstPort() {
		return (PinPort)firstPort.getCell();
	}
	
	
	//TODO: groesse dynamisch machen

	public static PortView getFirstPortView() {
		return firstPort;
	}
	
	public static ArrayList<GraphModul> getNodes() {
		return storage.getKnoten();
	}

	public static double getScaleRate() {
		return ScaleRate;
	}

	public static ArrayList<GraphModul> getSelectedMods(){
		Object[] selected=graph.getSelectionCells();
		ArrayList<GraphModul> module=new ArrayList<GraphModul>();
		
		for (Object o:selected){
			if (o instanceof GraphModul){
				GraphModul mod=(GraphModul) o;
				module.add(mod);
			}
		}
		
		return module;
		
	}
	public static void incZoom(){
		ScaleRate+=Globals.ZoomStep;
		double percentscale=Globals.ZoomStep/ScaleRate;
		graph.setScale(ScaleRate);
		graph.setSize((int)(graph.getWidth() * percentscale) ,(int) (graph.getHeight()*percentscale));
	}
	public static void main(String[] args) throws InterruptedException  {
		
		
		if (alreadyRunning()){
			RAClass.msgbox("TouchFlow is already running. Please close the older instance.", "Touchflow", "Error");
			return;
		}

		
		graphTool=new TouchFlow();
		if (args.length==1){
			try {
				synchronized(Thread.currentThread()){
					Thread.currentThread().wait(500);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			graphTool.loadGraph(args[0]);
		}
		if (args.length >1){
			RAClass.msgbox("Obsolete parameter: only one (<path>) is allowed", "Touchflow", "Warning");
		}
		
		
	}


	private static boolean alreadyRunning() {
		
		//Check if running by checking wheather port is open
		
		try{
			DatagramSocket socket = new DatagramSocket(SMTListeningSocket.UDP_PORT);
			socket.close();
		}
		catch (BindException be){
			if (be.getMessage().equals("Address already in use")) return true;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static void removeKante(DefaultEdge e) {
		
		
		
		storage.removeKante(e);
	}
	public static void updateGraph() {
		graph.repaint();
	}


	private ToolBox box;

	
	private int id=0;
	
	private Point oldPoint=null;
	private static JFrame thisFrame;
	

		public TouchFlow(){
			 setupGraph();
		     box=new ToolBox(this);
		     		    
		    //create frame and attach graph
		    
		     try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("WARNING: failed to set look and feel:");
				e.printStackTrace();
			}

		    createFrame();
		    
		}
		private PinPort attachPin(GraphModul knoten, pinName pinname) throws ModulException {
			
			   Pin p=knoten.getPin(pinname);
			   PinPort port = new PinPort(p);
			   knoten.add(port);
			return port;
		}
		public DefaultEdge createEdge(DefaultGraphCell source, int sourceAnschluss, DefaultGraphCell target, int targetAnschluss){
			 DefaultEdge edge = new DefaultEdge();
             edge.setSource(source.getChildAt(sourceAnschluss));
             edge.setTarget(target.getChildAt(targetAnschluss));
             GraphConstants.setLineStyle(edge.getAttributes(), GraphConstants.STYLE_ORTHOGONAL);
             GraphConstants.setRouting(edge.getAttributes(), Globals.RoutingScheme);
            
             Map map = new Hashtable();
             // Add a Line End Attribute
             GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
             //GraphConstants.setLineBegin(map, GraphConstants.ARROW_TECHNICAL);
             // Add a label along edge attribute
             GraphConstants.setLabelAlongEdge(map, true);
             GraphConstants.setEditable(map, false);
            edge.setAttributes(new AttributeMap(map));
             
             storage.addKante(edge);
             graph.getGraphLayoutCache().insert(new DefaultGraphCell[]{edge});
             return edge;
		}
		private void createEdgePorts(PortView source,PortView target){
			 DefaultEdge kante = new DefaultEdge();
			 kante=new DefaultEdge();
			 PinPort p1=(PinPort) source.getCell();
			 PinPort p2=(PinPort) target.getCell();
			 if (!p1.isCompatibleWith(p2)){
				 System.err.println("incompatible ports");
				 RAClass.msgbox("Diese Ports sind nicht kompatibel!\n\nBitte prüfen Sie:\n- Sie müssen einen InputPin mit einem OutputPin verbinden\n- Entweder müssen beide Pins normale, oder beide 2D-Pins sein.", "Fehler", "Warning");
				 return;
			 }
			kante.setSource(p1);
            kante.setTarget(p2);

            GraphConstants.setLineStyle(kante.getAttributes(), GraphConstants.STYLE_ORTHOGONAL);
            GraphConstants.setRouting(kante.getAttributes(), Globals.RoutingScheme);
            storage.addKante(kante);
             graph.getGraphLayoutCache().insert(new DefaultGraphCell[]{kante});

		}

		
		private void createFrame() {
			thisFrame = new JFrame();
			
			JMenuBar menuBar = new JMenuBar();
	        
	        // Add the menubar to the frame
	        thisFrame.setJMenuBar(menuBar);
	        
	        // Define and add two drop down menu to the menubar
	        JMenu fileMenu = new JMenu("File");
	        //JMenu editMenu = new JMenu("Edit");
	        menuBar.add(fileMenu);
	        //menuBar.add(editMenu);
	        
	        // Create and add simple menu item to one of the drop down menu
	        JMenuItem openAction = new JMenuItem("Open ...");
	        JMenuItem saveAction = new JMenuItem("Save as ...");
	        JMenuItem quickSaveAction = new JMenuItem("Save");
	        JMenuItem exitAction = new JMenuItem("Exit");
	        
	        openAction.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                openClicked();
	            }
	        });
	        
	        saveAction.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                saveClicked();
	            }
	        });
	        
	        exitAction.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                exitClicked();
	            }
	            private void exitClicked() {
	    			System.exit(0);
	    		}
	        });
	        
	        quickSaveAction.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                quickSaveClicked();
	            }
	        });
	        
	       /* JMenuItem exitAction = new JMenuItem("Exit");
	        JMenuItem cutAction = new JMenuItem("Cut");
	        JMenuItem copyAction = new JMenuItem("Copy");
	        JMenuItem pasteAction = new JMenuItem("Paste");
	        */
	        
	        fileMenu.add(openAction);
	        fileMenu.add(quickSaveAction);
	        fileMenu.add(saveAction);
	        fileMenu.add(exitAction);
	        
	        thisFrame.getContentPane().add(new Toolbar(), BorderLayout.PAGE_START);
	        
			JScrollPane pane=new JScrollPane(graph);
			thisFrame.setTitle("TouchFlow - GraphDesigner");
			
		    thisFrame.getContentPane().add(pane);
		    
		    
		    Rectangle desktopBounds = GraphicsEnvironment.getLocalGraphicsEnvironment(). getMaximumWindowBounds();


		    
		    //toolbox beim beenden schließen     		              
		    thisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    thisFrame.pack();
		    thisFrame.setLocation(box.getWidth()+desktopBounds.x,desktopBounds.y);
		    box.setLocation(0, desktopBounds.y);
		    
		    thisFrame.setSize(new Dimension(1000,700));
		    thisFrame.setVisible(true);
		}

		
		
		public static Point getLocation() {
			return thisFrame.getLocation();
		}


		public GraphModul createNode(Module mod, int posX, int posY) throws ModulException {
			GraphModul knoten;
			
			try{
				knoten = new GraphModul(mod);
			}
			catch (Exception e){
				if (Globals.isDebug) {
					System.err.println("Exception creating GraphModul!");
					e.printStackTrace();
				}
				return null;
			}
	           return createNodePins(posX, posY, knoten);
		}
		public GraphModul createNode(String modName, int posX, int posY) throws ModulException {

			GraphModul knoten;
			
			try{
				knoten = new GraphModul(modName);
			}
			catch (Exception e){
				if (Globals.isDebug) {
					System.err.println("Exception creating GraphModul! :" + e.getMessage());
					//e.printStackTrace();
				}
				return null;
			}
			knoten.getCorrespondingModule().setGraphXPos(posX);
			knoten.getCorrespondingModule().setGraphYPos(posY);
	           return createNodePins(posX, posY, knoten);
		}

		private GraphModul createNodePins( int posX, int posY,
				GraphModul knoten) throws ModulException {
			GraphConstants.setBounds(knoten.getAttributes(), new Rectangle2D.Double(posX,posY,Globals.ModulPicWidth,getModulHeight(knoten.getCorrespondingModule())));
				GraphConstants.setAutoSize(knoten.getAttributes(), false);
				
				GraphConstants.setGradientColor(
						knoten.getAttributes(),getModulColor(knoten.getCorrespondingModule()));
				GraphConstants.setLineColor(
						knoten.getAttributes(),Color.black);
				
				GraphConstants.setOpaque(knoten.getAttributes(), true);
				
				
				
	           
	           
	           
	           Module mod=knoten.getCorrespondingModule();
	           
	           int i=0;
	           double abstand= GraphConstants.PERMILLE/(double)mod.getInputPins().length;
	           
	     	   for (InputPin p:mod.getInputPins()){
	     		 
	     		  PinPort port = attachPin(knoten,p.getName());
	        	  GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(0,abstand*(i+0.5)));
	        	  i++;
	     	   }
	     	   
	     	   i=0;
	     	  abstand= GraphConstants.PERMILLE/(double)mod.getOutputPins().length;
	     	   
	     	   for (OutputPin p:mod.getOutputPins()){
		     		 
		     		  PinPort port = attachPin(knoten,p.getName());
		        	  GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE,abstand*(i+0.5)));
		        	  i++;
		     	  }
	         
	           /*
	           if (knoten.isPortExisting(0)){
	        	   PinPort port = attachPin(knoten,0);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(0,Globals.PinOffset[0]));
	           }   

	           if (knoten.isPortExisting(1)){
	        	   PinPort port = attachPin(knoten,1);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(0,Globals.PinOffset[1]));
	           } 
	           if (knoten.isPortExisting(2)){
	        	   PinPort port = attachPin(knoten,2);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(0,Globals.PinOffset[2]));
	           } 
	           if (knoten.isPortExisting(3)){
	        	   PinPort port = attachPin(knoten,3);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(0,Globals.PinOffset[3]));
	           } 
	           
	           if (knoten.isPortExisting(4)){
	        	   PinPort port = attachPin(knoten,4);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE,Globals.PinOffset[0]));
	           }
	           
	           if (knoten.isPortExisting(5)){
	        	   PinPort port = attachPin(knoten,5);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE,Globals.PinOffset[1]));
	           } 
	           
	           if (knoten.isPortExisting(6)){
	        	   PinPort port = attachPin(knoten,6);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE,Globals.PinOffset[2]));
	           } 
	           
	           if (knoten.isPortExisting(7)){
	        	   PinPort port = attachPin(knoten,7);
	        	   GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE,Globals.PinOffset[3]));
	           } */

	           storage.addModul(knoten);
	           graph.getGraphLayoutCache().insert(new DefaultGraphCell[]{knoten});
	           return knoten;
		}
		private Color getModulColor(Module m){
			if (m instanceof InputModule){
				return Globals.InputModuleColor;
			}
			if (m instanceof ModifyModule){
				return Globals.ModifyModuleColor;
			}
			if (m instanceof OutputModule){
				return Globals.OutputModuleColor;
			}
			return Color.white;
		}

		private double getModulHeight(Module correspondingModule) {
			
			int ports=Math.max(correspondingModule.getInputPins().length, correspondingModule.getOutputPins().length);
			
			return (ports +1)*Globals.PixelsPerPort;
		}

		// Returns the Port at the specified Position
		public PortView getSourcePortAt(Point point) {
		  // Scale from Screen to Model
		  Point tmp = (Point) graph.fromScreen(new Point(point));
		  // Find a Port View in Model Coordinates and Remember
		  return graph.getPortViewAt(tmp.x, tmp.y);
		}

		private boolean isPortConnectable(PortView port) {
			return true;
//			GraphModul g=(GraphModul) port.getParentView().getCell();
//
//			int index=getPortIndex(port, g);
//			return g.isPortExisting(index);
//		}
//		private int getPortIndex(PortView port, GraphModul g) {
//			for (int i=0;i<g.getChildren().size();i++){
//				Port p=(Port) g.getChildren().get(i);
//				if (p.equals(port.getCell())) return i;
//			}
//			System.err.println("Port Index not found!");
//			return -1;
		}
		public void loadGraph(String lastFile) {
			ModuleGraph mgraph;
			
			this.lastFile=lastFile;
			XMLParser parser=new XMLParser();
			try{
			parser.parseXMLFile(lastFile);
			
			mgraph=parser.getGraph();
			clearGraph();
			GraphSerializer.loadGraph(mgraph, this);
			
			}
			catch(Exception e){
				RAClass.msgbox("Fehler beim laden der Datei!\n\n"+e.getMessage(), "Loading Error", "Error");
				if (Globals.isDebug) e.printStackTrace();
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
	
			
			switch (box.getLastClicked()){
	
			case Select:
				//standart-Bearbeitung
				break;
				
			
			default:
				//irgend ein Bauteil
				int xcor=arg0.getX()-(Globals.ModulPicWidth/2);
				int ycor=arg0.getY()-(Globals.ModulPicHeight/2);
				try {
					createNode(box.getSelectedModule(),xcor,ycor);
				} catch (ModulException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (Globals.resetEditMode) box.resetClicked();
				break;
				
			}
			graph.repaint();
			
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
					
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
				
				MyJGraph.markedPort=getSourcePortAt(arg0.getPoint());
			
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		private void openClicked(){
			final JFileChooser fc = new JFileChooser();
			if (Settings.getLastPath()!=null) fc.setCurrentDirectory(new File(Settings.getLastPath()));
			if (fc.showOpenDialog(graph)!=JFileChooser.APPROVE_OPTION) return;
			
			Settings.setLastPath(fc.getCurrentDirectory().toString());

			lastFile=fc.getSelectedFile().toString();
			
			loadGraph(lastFile);
		}
		
		private void quickSaveClicked(){
			
			if (lastFile==null){
				saveClicked();
				return;
			}
			
			Module[] mods=GraphSerializer.getModules(getNodes(), getEdges());
			ModuleGraph g=new ModuleGraph();
			
			for (Module m:mods){
				g.addModul(m, m instanceof OutputModule);
			}
			
			XMLParser parser=new XMLParser(g);
			try {
				parser.saveToXMLFile(lastFile);
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void repaint() {
			thisFrame.validate();
			graph.validate();
		}
		
		/**
		 * setzt eventuell gespeicherten ersten punkt bei Kantenerstellung zurueck
		 */
		public void resetConnectionMode(){
			firstPort=null;
		}
		private void saveClicked(){
			final JFileChooser fc = new JFileChooser();
		
			if (Settings.getLastPath()!=null) {
				fc.setCurrentDirectory(new File(Settings.getLastPath()));
			}
			if (fc.showSaveDialog(graph)!=JFileChooser.APPROVE_OPTION) return;
			
			Settings.setLastPath(fc.getCurrentDirectory().toString());
			lastFile=fc.getSelectedFile().toString();
			
			if (!lastFile.substring(lastFile.length()-4,lastFile.length()).equals(".xml")){
				lastFile=lastFile + ".xml";
			}
			
			Module[] mods=GraphSerializer.getModules(getNodes(), getEdges());
			ModuleGraph g=new ModuleGraph();
			
			for (Module m:mods){
				g.addModul(m, m instanceof OutputModule);
			}
			
			XMLParser parser=new XMLParser(g);
			try {
				parser.saveToXMLFile(lastFile);
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void setupGraph() {
			
			GraphModel model = new DefaultGraphModel();
		    GraphLayoutCache view = new GraphLayoutCache(model,new DefaultCellViewFactory());
		    graph = new MyJGraph(model, view);
		   
		    //Standart-Groesse fuer Graph
		    graph.setPreferredSize(new Dimension(1680,1050));
		    graph.addMouseListener(this);
		    graph.setConnectable(true);
		    graph.setDisconnectable(true);
		    graph.setPortsVisible(true);
		    storage=new NodeStorage();
		    graph.setMarqueeHandler(new IXMarqueeHandler(graph, storage));
		    
		    ToolTipManager.sharedInstance().registerComponent(graph);

		}
}

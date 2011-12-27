package de.embots.touchflow.module;

import java.awt.Color;

import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class Globals {
	
	public static boolean isDebug=true;
	public static boolean isJar=true;
	public static String settingsFile="settings.cnf";
	
	public static boolean useStantum=false; //stantum oder TUIO-protokoll f�r Finger nutzen?
	public static boolean useMSFramework=true;
	
	public static boolean sleepAfterWork=true; //sollen Module kurz schlafen nachdem sie die Daten verarbeitet haben, damit andere Module drankommen?
	public static int moduleDelay=5; //delay die ein Modul nach erfolgreicher Berechnung schläft in ms
	public static boolean onlyCalculateIfNewData=true; //sollen Module nur rechnen wenn auch wirklich neue Daten da sind?
	public static boolean showInspectorView=false; // soll beim starten der Module gleich der Inspector gestartet werden?
												   // unbedingt abschalten wenn JGraphTool verwendet wird!
	
	public static boolean useAngleRadiant=true;
	
	public static int TUIOPort=3333;

	public static int PDPitchPort=1712;
	public static int PDAmplitudePort=1713;
	
	//Handshake-protokoll bei modulen: erst ausgaenge neu berechnen, wenn die anliegenden Daten abgeholt wurden
	public static boolean useModuleHandshake=false;
	
	//MAC-Hacks
	
	public static boolean useMacFix=true;
	
	
	
	//InModulix
	//---------- GFX
	
	
	//Descriptor
	
	public static int DescriptorWidth=300;
	public static int DescriptorHeight=200;
	public static int DescriptorYOffset=200;
	public static boolean autoShowDescriptor=false;
	
	
	
	//Inspector-Konponente
	public static int preferredWidth=300;
	public static int preferredHeight=150;
	
	public static String numberFormat= "#####0.0##";
	public static int NameLetterSize=12; //Schriftengröße für Modulname
	public static int PinLetterSize=9; //Schriftengröße für Pin
	public static double letterScalerRef=300; //Scaler für die Schriftgröße, damit sich die beim vergrößern der Komponente mitvergrößert
	public static int rightBorder=10; //Abstand vom rechten rand
	public static int leftBorder=10; //Abstand vom linken rand
	public static int topBorder=20; //Abstand vom oberen rand
	public static int bottomBorder=10; //Abstand vom unteren rand 
	public static int borderWidth=2; //dicke des Rahmens
	public static int ActRectOffset=2; //Abstand des aktualisierungs-Hintergrunds (das rechteck) 
	public static int refreshTime=500; // Zeit in Millisekunden, nach der die Daten neu bezogen werden
	public static Color BGCOLOR=new Color(0,0,0);
	public static Color TEXTCOLOR=new Color(255,255,255);
	public static Color ACTRECTCOLOR=new Color(0,155,0);
	
	
	public static boolean drawActRect=true;
	public static boolean drawFPS=true;
	
	public static int actNameLetterSize=NameLetterSize; //aktuelle Schriftengröße für Modulname
	public static int actPinLetterSize=PinLetterSize; //aktuelle Schriftengröße für Pin
	
	
	//----------------  JGraph-Tool
	public static boolean resetEditMode=true;
	public static int[] PinOffset=new int[]{210,400,590,780};
	public static int ModulPicWidth=90;
	public static int ModulPicHeight=110;
	public static int PixelsPerPort=12;
	public static Edge.Routing RoutingScheme=GraphConstants.ROUTING_DEFAULT;
	public static int ConnectionMarkerSize=8;
	public static Color ConnectionMarkerColor=new Color(255,255,0);
	public static double ZoomStep=0.3;
	public static double ModuleCols=4.0;
	
	public static Color InputModuleColor=new Color(150,0,0);
	public static Color OutputModuleColor=new Color(0,0,255);
	public static Color ModifyModuleColor=new Color(0,122,0);
	public static Color selectedButtonColor=new Color(255,0,0);
	
	//----------------- MyJGraph 
	
	public static int PinBeschriftungBorder=4;
	public static int PinBeschriftungLetterSize=10;
	
	//Toolbox
	
	public static int RigidAreaSize=10;
	
	//Bildschirmgroesse, wird vom Finger-Modul gebraucht um die Prozentualen angaben auf Pixelkoordinaten umzurechnen
	//1504, 800
	public static int ScreenWidth=java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int ScreenHeight=java.awt.Toolkit.getDefaultToolkit().getScreenSize().height ;
	
	public static int CyclesCountAmount=100; //wieviele Zyklen werden abgewartet um zeit zu berechnen
	public static int timeDiffCountAmount=1000; //wieviel Zeit wird maximal abgewartet, falls die Zyklen nicht erreicht werden
	
	
	
	//--testapp
	
	public static int minSize=50;
	public static int maxDoubleClickTime=500;
	
}

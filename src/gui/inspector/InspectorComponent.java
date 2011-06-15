package gui.inspector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

import module.Globals;
import module.core.InputModule;
import module.core.ModifyModule;
import module.core.Module;
import module.core.OutputModule;
import module.implementation.modify.MapPosCircle2D;
import module.implementation.modify.MapPosSquare2D;
import module.pin.InputPin;
import module.pin.InputPin2D;
import module.pin.OutputPin;
import module.pin.OutputPin2D;
import module.pin.Pin;


public class InspectorComponent extends JComponent implements MouseWheelListener,KeyListener,ComponentListener{

	private Module modul;
	private final Timer timer = new Timer();
	 
	public InspectorComponent(Module modul) {
		super();
		this.modul = modul;
		
		this.setPreferredSize(new Dimension(300,150));
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.addComponentListener(this);
		
		//Timer anwerfen, damit sekuendlich die daten aktualisiert werden
		timer.schedule(new TimerTask(){
			 public void run() {
				 update();
			 }
		}, Globals.refreshTime,Globals.refreshTime);
	}

	
		@Override
	public void componentResized(ComponentEvent arg0) {
		Globals.actPinLetterSize=(int) ((((double)this.getWidth())/Globals.letterScalerRef)*Globals.PinLetterSize);
		Globals.actNameLetterSize=(int) ((((double)this.getWidth())/Globals.letterScalerRef)*Globals.NameLetterSize);
		repaint();
	}
	
		
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
		drawBG(arg0);
		if (Globals.useModuleHandshake)drawActiveDot(arg0);
		drawModName(arg0);
		drawFPS(arg0);
		drawOutputPins(arg0, modul.getOutputPins());
		drawInputPins(arg0, modul.getInputPins());
	}
	
	private void drawActiveDot(Graphics g){
		if (!modul.dataPushed()){
			g.setColor(new Color(255,0,0));
			g.fillOval(this.getWidth()-12, 5, 7, 7);
		}
	}
	
	private void drawBG(Graphics g) {
		//Background
		g.setColor(new Color(255,0,0));
		g.fillRect(0,0, this.getWidth(), this.getHeight());
		g.setColor(Globals.BGCOLOR);
		if (modul instanceof InputModule){
			g.setColor(Globals.InputModuleColor);
		}
		if (modul instanceof OutputModule){
			g.setColor(Globals.OutputModuleColor);
		}
		if (modul instanceof ModifyModule){
			g.setColor(Globals.ModifyModuleColor);
		}
		g.fillRect(Globals.borderWidth,Globals.borderWidth, this.getWidth()-Globals.borderWidth, this.getHeight()-Globals.borderWidth);
	}


	private void drawModName(Graphics g){
		g.setFont(new Font("Arial", 1, Globals.actNameLetterSize));
		g.setColor(Globals.TEXTCOLOR);
		int namexcor=(int) ((this.getWidth() - StringOP.getStringWidth(modul.getTyp().toString(), g))/2);
		int nameycor=(int) ((this.getHeight() - StringOP.getStringHeight(modul.getTyp().toString(), g))/2);
		g.drawString(modul.getTyp().toString(),namexcor,nameycor);
	}
	
	private void drawOutputPins(Graphics g, OutputPin[] pins ){
		for (int i=0;i< pins.length;i++){
			drawOutputPin(g, pins[i], i, pins.length);
		}
	}
	
	private void drawInputPins(Graphics g, InputPin[] pins ){
		for (int i=0;i< pins.length;i++){
			drawInputPin(g, pins[i], i, pins.length);
		}
	}
	/**
	 * malt einen OutputPin, der an Stelle <Index> in der OutputPin-Liste ist
	 * @param g
	 * @param Index
	 * @param numOfPins Gesamtzahl der Pins
	 */
	private void drawOutputPin(Graphics g, OutputPin pin,int Index, int NumOfPins){
		
		
		DecimalFormat df = new DecimalFormat(Globals.numberFormat );
		String text=pin.getName() + " " + df.format(pin.getData());
		
		//spezialfall morph-map: name aendern
		if (pin.getParentModul() instanceof MapPosCircle2D || pin.getParentModul() instanceof MapPosSquare2D){
			text="TARGET" + Index  + " " + df.format(pin.getData());
		}
		
		g.setFont(new Font("Arial", 0, Globals.actPinLetterSize));
		
		if (pin instanceof OutputPin2D){
			OutputPin2D pin2D=(OutputPin2D) pin;
			text=text + " " + df.format(pin2D.getData2());
		}
		
		int xcor=(int) (this.getWidth() - Globals.rightBorder - StringOP.getStringWidth(text, g));
		int oneheight=(this.getHeight() - Globals.topBorder - Globals.bottomBorder)/NumOfPins;
		int ycor=(int) (Globals.topBorder + (Index+0.5)*oneheight-StringOP.getStringHeight(text, g)/2);
		if (Globals.drawActRect)drawActRect(g, pin, text, xcor, ycor);
		g.drawString(text,xcor,ycor);
	}
	
	private void drawInputPin(Graphics g, InputPin pin,int Index, int NumOfPins){
		DecimalFormat df = new DecimalFormat( Globals.numberFormat);
		String text=df.format(pin.getData())+"";
		
		g.setFont(new Font("Arial", 0, Globals.actPinLetterSize));
		if (pin instanceof InputPin2D){
			InputPin2D pin2D=(InputPin2D) pin;
			text=text + " " + df.format(pin2D.getData2());
		}
		text=text + " " + pin.getName();
		
		int xcor=(int) (Globals.leftBorder);
		int oneheight=(this.getHeight() - Globals.topBorder - Globals.bottomBorder)/NumOfPins;
		int ycor=(int) (Globals.topBorder + (Index+0.5)*oneheight-StringOP.getStringHeight(text, g)/2);
		if (Globals.drawActRect)drawActRect(g, pin, text, xcor, ycor);
		g.drawString(text,xcor,ycor);
	}

	private void drawActRect(Graphics g, Pin pin, String text, int xcor,
			int ycor) {
		if (pin.isNewDrawData()){
			g.setColor(Globals.ACTRECTCOLOR);
			g.fillRect(xcor-Globals.ActRectOffset, (int)(ycor-Globals.ActRectOffset-StringOP.getStringHeight(text, g)/1.4), (int)(StringOP.getStringWidth(text, g)+Globals.ActRectOffset),(int) (StringOP.getStringHeight(text, g)+Globals.ActRectOffset));
			g.setColor(Globals.TEXTCOLOR);
		}
	}
	private void drawFPS(Graphics g){
		if (!Globals.drawFPS) return;
		
		int fps=modul.getFps();
		
		String text="FPS: " + fps;
		int xcor=(int) ((getWidth()-StringOP.getStringWidth(text, g))/2);
		g.drawString(text, xcor, Globals.topBorder);
	}
	
	public void update() {
		this.repaint();
	}

	
	//-----------für Mouse-InputModul
	
	private int ScrollNumber=0;
	
	public int getScrollNumber() {
		return ScrollNumber;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		ScrollNumber+=arg0.getWheelRotation();
	}

	//--------  Keylistener für Keyboard
	
	ArrayList<Integer> pressed=new ArrayList<Integer>();
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (!pressed.contains(arg0.getKeyCode())) pressed.add(arg0.getKeyCode());
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (pressed.contains(arg0.getKeyCode())) pressed.remove(arg0.getKeyCode());
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
	
	public boolean isKeyDown(int keyCode){
		return (pressed.contains(keyCode));
	}
}

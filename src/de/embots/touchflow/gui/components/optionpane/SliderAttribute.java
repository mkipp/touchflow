package de.embots.touchflow.gui.components.optionpane;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.embots.touchflow.module.Globals;

public class SliderAttribute extends Attribute{
	JTextField textFeld;
	JSlider slider;
	
	int slidermult=1;

	@Override
	public Object getContent() {
		// TODO Auto-generated method stub
		Object ret= super.getContent();
		
		//on applyClick
		updateInterval();
		System.err.println(slider.getMaximum() + "-" + slider.getValue() + "-" + slidermult);
		
		return ret;
	}
	private int getMult(double val){
		String zahl="";

			zahl = new DecimalFormat("############################.#############################").format(val); 
		int pos=zahl.length() - zahl.indexOf(',')-1;
		return pos;
	}
	private void updateInterval() {
		
		double actualval=(Double) content;
		
		slidermult=(int) Math.pow(10, getMult(actualval));
		
		//if (actualval*slidermult<slider.getMinimum()|| actualval*slidermult >slider.getMaximum()){
			//reassign slider vals
			if (actualval<0){
				slider.setMinimum((int) (actualval*slidermult*2));
				slider.setMaximum(0);
			}
			else{
				slider.setMaximum((int) (actualval*slidermult*2));
				slider.setMinimum(0);
			}
			setSliderVal(actualval);
		//}
	}
	public SliderAttribute(String Caption, int minAmount, int maxAmount){
		this.Caption=Caption;
		JLabel bezeichnung=new JLabel(Caption);
		
		JPanel hBox=new JPanel();
		Box horBox=Box.createHorizontalBox();
		horBox.add(bezeichnung);
		this.add(hBox);
		textFeld=new JTextField("0");
		textFeld.setPreferredSize(new Dimension(50,textFeld.getPreferredSize().height));
		//textFeld.setAlignmentX(0);
		//this.setAlignmentX(Component.LEFT_ALIGNMENT);
		hBox.setLayout(new BoxLayout(hBox, BoxLayout.X_AXIS));
		//hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		//horBox.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		
		content=0;
		
		slider = new JSlider(JSlider.HORIZONTAL,
                minAmount, maxAmount, (maxAmount-minAmount)/2);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				textFeld.setText(getSliderVal()+"");
				updateContent();
				
			}
		});

		
		KeyListener keyListener = new KeyListener() {
		      public void keyPressed(KeyEvent keyEvent) {
		    	  
		      }

			@Override
			public void keyReleased(KeyEvent arg0) {
				updateContent();
			
			}

			

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		    };
		
		    textFeld.addKeyListener(keyListener);
		    horBox.add(Box.createRigidArea(new Dimension(10,10)));
		    //bezeichnung.setAlignmentX(0);
		    horBox.add(slider);
		horBox.add(textFeld);
		//hBox.add(Box.createHorizontalGlue());
		hBox.add(horBox);
		
	}
	
	public void setContent(Object in) {
		content=in;
		textFeld.setText(in.toString());
		double d=(Double) in;
		setSliderVal(d);
		updateContent();
	}
	
	protected void updateContent(){
		try{
			double d=Double.parseDouble(textFeld.getText());
			setSliderVal(d);
			slider.repaint();
			content=d;
			textFeld.repaint();
			if (mainFrame!=null){
				mainFrame.pack();
				mainFrame.repaint();
			}
			
		}
		catch(NumberFormatException nf){
			if (Globals.isDebug)
				System.out.println("WARNING: numberattribute: number not parsed");
		}
	}

	private double getSliderVal(){
		return slider.getValue()/slidermult;
	}
	private void setSliderVal(double val){
		System.err.println("slider set to:" + val*slidermult);
		slider.setValue((int) (val*slidermult));
	}

}

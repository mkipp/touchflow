package de.embots.touchflow.module.implementation.output;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.gui.components.optionpane.StringAttribute;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;

import org.jdom.Element;


public class FormattedOut extends OutputModule {
	@Override
	public void openOptions() {
		StringAttribute formatarg=new StringAttribute("Formatstring");
		formatarg.setContent(formatString);
		
		OptionPane.showOptionPane(new Attribute[]{formatarg},this);

	}

	@Override
	public void reinit(Attribute[] args) {
		formatString=(String) args[0].getContent();
	}

	double[] oldVals;
	boolean printOnlyDifferent=false;
	private String formatString;
	
	public FormattedOut(){
		this("Pin1: %1 - Pin2: %2 - Pin3: %3 - Pin4: %4");
	}
	
	public FormattedOut(String formatString) {
		this.formatString=formatString;
		
		inputPins=new InputPin[4];
		inputPins[0]=new InputPin(PinName.DATA1,this);
		inputPins[1]=new InputPin(PinName.DATA2,this);
		inputPins[2]=new InputPin(PinName.DATA3,this);
		inputPins[3]=new InputPin(PinName.DATA4,this);
		oldVals=new double[4];

	}

	public String getFormatString() {
		return formatString;
	}

	@Override
	protected void processData() throws ModulException {
		String tmp=formatString;
		
		tmp=tmp.replaceAll("%1", getInputPin(PinName.DATA1).getData()+"");
		tmp=tmp.replaceAll("%2", getInputPin(PinName.DATA2).getData()+"");
		tmp=tmp.replaceAll("%3", getInputPin(PinName.DATA3).getData()+"");
		tmp=tmp.replaceAll("%4", getInputPin(PinName.DATA4).getData()+"");
		
		boolean changed=false;
		
		for (int i=0;i<4;i++){
			if (inputPins[i].getData()!=oldVals[i]) {
				oldVals[i]=inputPins[i].getData();
				changed=true;
			}
		}
		
		if (!printOnlyDifferent||changed) System.out.println(tmp);

	}



	@Override
	public String getDescription() {
		return "<html>Outputs the values of <b>DATA1-4</b> on stdout using a format string</html>";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor", formatString);
		
	}

	@Override
	public String getModuleName() {
		
		return "Formatted out";
	}

}

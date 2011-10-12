package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.AffineMapAttribute;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

public class MultiAffineMap extends ModifyModule {

	private static int numOfMaps = 12;
	double[] slopes, intercept;

	public MultiAffineMap() {
		slopes = new double[numOfMaps];
		intercept = new double[numOfMaps];
		// init array
		for (int i = 0; i < slopes.length; i++) {
			slopes[i] = 1;
		}

		int inBegin = PinName.leftwristxIN.ordinal();

		inputPins = new InputPin[numOfMaps + 1];

		// init in pins
		for (int i = 0; i < numOfMaps; i++) {
			inputPins[i] = new InputPin(PinName.values()[inBegin + i], this);
		}

		// init overall gain
		inputPins[numOfMaps] = new InputPin(PinName.OVERALL_GAIN, this);
		inputPins[numOfMaps].setDefaultData(1);

		outputPins = new OutputPin[numOfMaps];

		int outBegin = PinName.leftwristxOUT.ordinal();
		for (int i = 0; i < numOfMaps; i++) {
			outputPins[i] = new OutputPin(PinName.values()[outBegin + i], this);
		}

	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "MultiAffineMap";
	}

	@Override
	protected void processData() throws ModulException {
		double overallGain = getInputPin(PinName.OVERALL_GAIN).getData();

		for (int i = 0; i < numOfMaps; i++) {
			double out = (inputPins[i].getData() * slopes[i] + intercept[i])
					* overallGain;

			outputPins[i].writeData(out);
		}

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		String cstring = "";
		for (int i = 0; i < numOfMaps; i++) {
			cstring = cstring + slopes[i] + " " + intercept[i] + " ";
		}
		e.setAttribute("Constructor", cstring);

	}

	@Override
	public void init(String params) throws ModulException {
		String[] paramsAffineMap;
		paramsAffineMap = params.split(" ");
		if (paramsAffineMap.length != 2 * numOfMaps)
			throw new ModulFactoryException(
					"MultiAffineMap: split ergab mehr oder weniger Konstruktorargumente als benötigt (ist:"
							+ paramsAffineMap.length
							+ " - soll:"
							+ (2 * numOfMaps) + ")");

		try {
			for (int i = 0; i < numOfMaps; i++) {
				slopes[i] = Double.parseDouble(paramsAffineMap[2 * i]);
				intercept[i] = Double.parseDouble(paramsAffineMap[2 * i + 1]);
			}
		} catch (Exception nf) {
			throw new ModulFactoryException(
					"MultiAffineMap: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	public void reinit(Attribute[] args) {
		for (int i = 0; i < args.length; i++) {
			String cont = (String) args[i].getContent();
			String[] params = cont.split(" ");
			if (params.length != 2) {
				System.err
						.println("MultiAffineMap: Reinit: more than 2 params for Map");
				continue;
			}
			try {
				slopes[i] = Double.parseDouble(params[0]);
				intercept[i] = Double.parseDouble(params[1]);
			} catch (NumberFormatException nf) {
				System.err
						.println("MultiAffineMap: Reinit: could not parse Map attributes: "
								+ cont);
				continue;
			}
		}

	}

	@Override
	public void openOptions() {

		int inBegin = PinName.leftwristxIN.ordinal();
		Attribute[] attr = new Attribute[numOfMaps];

		for (int i = 0; i < numOfMaps; i++) {
			attr[i] = new AffineMapAttribute(
					PinName.values()[inBegin + i].toString());
			attr[i].setContent(slopes[i] + " " + intercept[i]);
		}

		OptionPane.showOptionPane(attr, this);

	}

}

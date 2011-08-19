package de.embots.touchflow.module.implementation.output;

import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;

public class Print extends OutputModule
{

    public boolean printOnlyDifferent = true;
    private double oldVal;

    @Override
    protected void processData() throws ModulException
    {
        double newVal = getInputPin(PinName.IN).getData();



        if (!printOnlyDifferent || oldVal != newVal) {
            System.out.println(newVal);
        }

        oldVal = newVal;

    }

    public Print()
    {
        inputPins = new InputPin[1];
        inputPins[0] = new InputPin(PinName.IN, this);

    }

    @Override
    public String getDescription()
    {
        return "<html>Outputs the values of <b>IN</b> on stdout, using ';' as a seperator</html>";
    }

    @Override
    protected void additionalSaveAttribute(Element e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public String getModuleName()
    {

        return "Print";
    }
}

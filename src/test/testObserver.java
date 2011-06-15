package test;

import java.util.Observable;
import java.util.Observer;

import module.core.Module;


public class testObserver implements Observer{

	@Override
	public void update(Observable o, Object arg) {

		Module m=(Module) o;
		System.out.println("changed! " + m.getTyp());
	}

}

package de.embots.touchflow.module.implementation.input;

import java.util.ArrayList;

import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

public class TUIOBuffer implements TuioListener {

	private static ArrayList<TuioCursor> cursors=new ArrayList<TuioCursor>();
	
	@Override
	public void addTuioCursor(TuioCursor arg0) {
		cursors.add(arg0);
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
	
	}

	@Override
	public void refresh(TuioTime arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTuioCursor(TuioCursor arg0) {
		//System.out.println("remove cursor " + arg0.getCursorID());
		cursors.remove(arg0);

	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
		System.out.println("remove object " + arg0.getSymbolID());

	}

	@Override
	public void updateTuioCursor(TuioCursor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * gibt den Cursor mit id <id> zurueck
	 * @param id
	 * @return
	 */
	public static TuioCursor getCursorFromId(int id){
		for (TuioCursor c:cursors){
			if (c.getCursorID()==id-1) return c;
		}
		return null;
	}
}

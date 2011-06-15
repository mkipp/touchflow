package module.implementation.input.stantum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StantumBuffer implements MTListener {
	ArrayList<MTPoint> cursors=new ArrayList<MTPoint>();
	
	Lock lock=new ReentrantLock();
	
	@Override
	public void cursorAction(MTController controller, int id, int actionType,
			MTPoint point, int clickCount) {
		//purge old pos if there
		
		point.setId(id);
		lock.lock();

		//don't do anything when not created first
		MTPoint old=findCursorById(id);
		if (old !=null) {
			cursors.remove(old);
		}
		
		if (actionType !=0) insertCursor(point);
		
		
		
		lock.unlock();

	}

	@Override
	public void cursorCreated(int id) {
		//init with null
		/*MTPoint dummy=new MTPoint(1, 1);
		
	dummy.setId(id);
		cursors.put(id,null);
*/
	}

	@Override
	public void cursorDestroyed(int id) {
		lock.lock();
		
		MTPoint old=findCursorById(id);
		if (old!=null) cursors.remove(old);
		lock.unlock();
	}

	/**
	 * return nth finger on the screen, NOT cursor with ID <id>
	 * @param id number of finger
	 * @return
	 */
	public MTPoint getCursor(int id){
		lock.lock();
		if (id > cursors.size()|| id <=0) {
			lock.unlock();
			return null;
		}
		
		MTPoint ret;
		ret= (MTPoint) cursors.get(id-1);
		
		
		lock.unlock();
		return ret;
	}
	
	/**
	 * return pointer with ID <id>
	 * @param id
	 * @return pointer, or null if not found
	 */
	private MTPoint findCursorById(int id){
		for (MTPoint p:cursors){
			if (p.getId()==id) return p;
		}
		return null;
	}
	
	private void insertCursor(MTPoint p){
		if (p.getId()==-1){
			System.err.println("Warning: Insertion of MTPoint with id -1");
		}
		
		if (cursors.size()==0){
			cursors.add(p);
			return;
		}
		
		for (int i=0; i<cursors.size();i++){
			if (cursors.get(i).getId()>p.getId()){
				cursors.add(i, p);
				return;
			}
		}
		//add at end
		cursors.add(p);
	}
}

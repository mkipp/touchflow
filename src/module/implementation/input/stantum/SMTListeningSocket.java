package module.implementation.input.stantum;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listens to port 1000 for UDP broadcast of Stantum multitouch (SMT).
 * 
 * @author Michael Kipp
 */
public class SMTListeningSocket extends Thread implements MTController
{

	public final static int UDP_PORT = 7000;

	private final static int BUFFER_LENGTH = 1024;

	private DatagramSocket _socket;

	private int _watchId = -1; // id to watch for FPS computation

	private long _lastTime;

	private int _signalCount = 0;

	private float _fps = -1;

	private DatagramPacket _packet;

	private List<MTListener> _listeners = new ArrayList<MTListener>();

	private NumberFormat _format = NumberFormat.getInstance();

	private boolean _debug = false;

	private List<Integer> _fingerIDs = new ArrayList<Integer>();

	private HashMap<Integer, MTPoint> _fingerTable = new HashMap<Integer, MTPoint>();
	
	private static SMTListeningSocket thisSocket;

	static{
		try {
			thisSocket=new SMTListeningSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Stores current and previous coordinates of a point
	 * @throws StartedMultipleTimesException 
	 */
	private SMTListeningSocket() throws SocketException
	{


			System.out.println("SMTListeningSocket started");
				_socket = new DatagramSocket(UDP_PORT);
			_packet = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);
			System.out.println("Created datagram socket");
			_lastTime = System.currentTimeMillis();

		
	}

	public static SMTListeningSocket getInstance(){
		return thisSocket;
	}
	public void addMultitouchListener(MTListener li)
	{
		_listeners.add(li);
	}

	public void removeMultitouchListener(MTListener li)
	{
		_listeners.remove(li);
	}

	public void setDebug(boolean val)
	{
		_debug = val;
	}

	public float getFps()
	{
		return _fps;
	}

	private void createAction(int id)
	{
		_fingerIDs.add(id);
		if (_watchId < 0) {
			_watchId = id;
			_signalCount = 0;
			_lastTime = System.currentTimeMillis();
		}
		for (MTListener li : _listeners) {
			li.cursorCreated(id);
		}
	}

	private void removeAction(int id)
	{
		_fingerTable.remove(id);
		_fingerIDs.remove(new Integer(id));
		if (_watchId == id) {
			_watchId = -1;
		}
		for (MTListener li : _listeners) {
			li.cursorDestroyed(id);
		}
	}

	private void cursorAction(int id, int action, int x, int y)
	{
		MTPoint tp = _fingerTable.get(id);
		if (tp == null) {
			tp = new MTPoint(x, y);
			_fingerTable.put(id, tp);
		} else {
			tp.push(x, y);
		}
		if (tp != null) {
			//        	System.out.println("ACTION " + id + " type=" + action + " (" + tp + ") ");
			for (MTListener li : _listeners) {
				li.cursorAction(this, id, action, tp, -1);
			}
		}
		tp= null;
	}

	public void run()
	{
		System.out.println("Listening...");
		while (true) {
			try {
				_socket.receive(_packet);
				byte[] dat = _packet.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(dat);
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(in));
				String line = rd.readLine();
				if (_debug) {
					System.out.println("SMT input: " + line);
				}
				String[] d = line.split(" ");
				int id = Integer.parseInt(d[1]);
				if (d[0].equals("C")) {
					createAction(id);
				} else if (d[0].equals("R")) {
					removeAction(id);
				} else if (d[0].equals("U")) {

					int x = (Integer.parseInt(d[2]));
					int y = (Integer.parseInt(d[3]));
					//                        	float x = ((float) Integer.parseInt(d[2])) / Integer.parseInt(d[4]);
					//                        	float y = ((float) Integer.parseInt(d[3])) / Integer.parseInt(d[5]);
					cursorAction(id, MTListener.UP, x, y);
				} else if (d[0].equals("D")) {
					int x = (Integer.parseInt(d[2]));
					int y = (Integer.parseInt(d[3]));

					//                                float x = ((float) Integer.parseInt(d[2])) / Integer.parseInt(d[4]); 
					//                                float y = ((float) Integer.parseInt(d[3])) / Integer.parseInt(d[5]);
					cursorAction(id, MTListener.DOWN, x, y);
				} else if (d[0].equals("M")) {
					int x = (Integer.parseInt(d[2]));
					int y = (Integer.parseInt(d[3]));

					//                                	int x = ((float) Integer.parseInt(d[2])) / Integer.parseInt(d[4]);
					//                                	int y = ((float) Integer.parseInt(d[3])) / Integer.parseInt(d[5]);
					if (id == _watchId) {
						_signalCount++;
						long elapsed = System.currentTimeMillis() - _lastTime;
						if (elapsed > 1000) {
							_fps = _signalCount * 1000f / elapsed;
						}
					}
					cursorAction(id, MTListener.MOVED, x, y);
				}
				in = null;
				rd = null;
				dat = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public MTPoint getPoint(int id)
	{
		return _fingerTable.get(id);
	}

	public MTPoint getNeighbor(int id)
	{
		if (_fingerTable.size() < 2) {
			return null;
		}
		int pos = _fingerIDs.indexOf(id);
		int neighbor = pos == 0 ? pos + 1 : pos - 1; // find second finger

		int neighborID = _fingerIDs.get(neighbor);
		return _fingerTable.get(neighborID);
	}

	public int getNumPoints()
	{
		return _fingerIDs.size();
	}

	public static void main(String[] args)
	{
		try {
			SMTListeningSocket s = new SMTListeningSocket();
			s.addMultitouchListener(new MTListener() {

				public void cursorCreated(int id)
				{
					System.out.println("CREATE " + id);
				}

				public void cursorDestroyed(int id)
				{
					System.out.println("DESTROY " + id);
				}

				public void cursorAction(MTController c, int id,
						int actionType, MTPoint p, int clickCount)
				{
					System.out.println("ACTION " + id + " type=" + actionType
							+ " (" + p.x + ", " + p.y + ") " + clickCount);
				}
			});
			s.run();
		} catch (SocketException ex) {
			Logger.getLogger(SMTListeningSocket.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
}

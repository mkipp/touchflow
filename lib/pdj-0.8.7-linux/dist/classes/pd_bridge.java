import com.cycling74.max.*;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class pd_bridge extends MaxObject {
	int attr1;
	float port;
	
	public pd_bridge(float Port) {
		declareOutlets(new int[] {});
		port=Port;
	}
	
	public void inlet(float x) {
		
		try{
			Socket server=new Socket("localhost", ((int)port));
			
			OutputStream cliOut=server.getOutputStream();
			cliOut.write((int)x);
			cliOut.flush();
			cliOut.close();
			server.close();
			
		}
		catch(ConnectException ex){
			//post ("Could not connect to port " + port + "... is server listening?");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		//post("received " + x);
	}
	
	public void bang() {
		post ("BANG!");
	}
	
	public void callme(Atom args[]) {
		post( "callme has called with arg1:" + args[0].toString());
	}

	public void dynamic_method() {
		post("dynamic_method has been called");
	}

}

package de.embots.touchflow.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tests the output from the CharAnimOut module by creating a TCP
 * server socket which accepts output from this module...
 * 
 * @author Michael Kipp
 */
public class SocketTest extends Thread
{

    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private ServerSocket _socket;
    private Socket _client;

    public SocketTest()
    {
        try {
            _socket = new ServerSocket(PORT);
            //        _address = new InetSocketAddress(HOST, PORT);
        } catch (IOException ex) {
            Logger.getLogger(SocketTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run()
    {
        while (true) {
            try {
                _client = _socket.accept();
                System.out.println("## connected!");

                // listen
                BufferedReader inFromClient = null;
                inFromClient = new BufferedReader(new InputStreamReader(_client.getInputStream()));
                String input;
                while ((input = inFromClient.readLine()) != null) {
                    System.out.println("Received: " + input);
                }
            } catch (IOException ex) {
                System.out.println("## IO exception: " + ex.getMessage());
            }
        }
    }
    
    public static void main(String[] a) {
        System.out.println("## run socket test");
        SocketTest s = new SocketTest();
        s.start();
    }

}

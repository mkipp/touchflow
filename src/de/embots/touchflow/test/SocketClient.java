/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Michael Kipp
 */
public class SocketClient extends Thread
{

    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private ServerSocket _socket;
    private Socket _client;

    public SocketClient()
    {
        try {
            _socket = new ServerSocket(PORT);
            //        _address = new InetSocketAddress(HOST, PORT);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
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
}

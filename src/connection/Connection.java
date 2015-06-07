/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruchiranga
 */
public class Connection extends Observable{

    private static Connection connection;

    //public final static String LOCAL_HOST = "127.0.0.1";
    private static String SERVER_IP;
    private static int PORT_IN;  //7000
    private static int PORT_OUT; //6000

    ServerSocket serverSocket = null;

    /**
     * Instantiates a Connection with 120.0.0.1 unless specifically set by
     * setServerIP()
     *
     * @return
     */
    public static Connection getInstance(String serverIP, int portIn, int portOut) { //
        if (connection == null) {
            return new Connection(serverIP, portIn, portOut);
        }

        return connection;
    }

    public Connection(String SERVER_IP, int portIn, int portOut) {
        this.SERVER_IP = SERVER_IP;
        this.PORT_IN = portIn;
        this.PORT_OUT = portOut;
    }

    /**
     * Sends the passed message to the server specified by the SERVER_IP and PORT_OUT  
     * @param message
     * @throws IOException 
     */
    public void sendMessage(String message) throws IOException{
        Socket clientSocket;
            clientSocket = new Socket(SERVER_IP, PORT_OUT); //6000
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeBytes(message);
            clientSocket.close();
            out.close();
    }

    /**
     * Reads an incoming message from the server and returns it
     * @return
     * @throws java.net.SocketException 
     */
    public String receiveMessage() throws java.net.SocketException{

        String message = null;

        Socket clientSocket = null;
        InputStreamReader socketReader = null;
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = new ServerSocket(PORT_IN);  //7000
            } else {
                serverSocket = new ServerSocket(PORT_IN);  //7000
            }
            while (message == null) {

                clientSocket = serverSocket.accept();
                socketReader = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader input = new BufferedReader(socketReader);
                message = input.readLine();

            }

        } catch (Exception ex) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Connection reset $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (socketReader != null) {
                    socketReader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return message;
    }

}

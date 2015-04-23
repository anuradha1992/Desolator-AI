/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruchiranga
 */
public class OutputMessageBuffer implements Runnable {

    ConcurrentLinkedQueue<String> buffer;
    private static Connection conn;
    private static OutputMessageBuffer bufferOb;
    private static String SERVER_IP;
    private static int PORT_IN;
    private static int PORT_OUT;

    private OutputMessageBuffer() {
        conn = Connection.getInstance(SERVER_IP, PORT_IN, PORT_OUT);
        //conn.setServerIP(SERVER_IP);
        buffer = new ConcurrentLinkedQueue<>();
    }

    public static OutputMessageBuffer getInstance(String serverIP, int portIn, int portOut) throws Exception {
        SERVER_IP = serverIP;
        PORT_IN = portIn;
        PORT_OUT = portOut;
        conn = Connection.getInstance(SERVER_IP, portIn, portOut);
        if (conn != null) {
            bufferOb = new OutputMessageBuffer();
        } else {
            throw new Exception("No connection available");
        }
        return bufferOb;
    }

    public void sendMessage(String message) {
        buffer.add(message);
    }

    @Override
    public void run() {
        while (true) {
            while (buffer.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OutputMessageBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String message = buffer.remove();

            try {
                conn.sendMessage(message);
            } catch (IOException ex) {
                Logger.getLogger(OutputMessageBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1050);
            } catch (InterruptedException ex) {
                Logger.getLogger(OutputMessageBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Connection getConnection() {
        return conn;
    }
}

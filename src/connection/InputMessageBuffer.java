/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ruchiranga
 */
public class InputMessageBuffer implements Runnable {

    ConcurrentLinkedQueue<String> buffer;
    private static Connection conn;
    private static InputMessageBuffer bufferOb;
    private static String SERVER_IP;
    private static int PORT_IN;
    private static int PORT_OUT;

    private InputMessageBuffer() {
        conn = Connection.getInstance(SERVER_IP, PORT_IN, PORT_OUT);
        //conn.setServerIP(serverIP);
        buffer = new ConcurrentLinkedQueue<>();
    }

    public static InputMessageBuffer getInstance(String serverIP, int portIn, int portOut) throws Exception {
        SERVER_IP = serverIP;
        PORT_IN = portIn;
        PORT_OUT = portOut;
        conn = Connection.getInstance(SERVER_IP, portIn, portOut);
        if (conn != null) {
            bufferOb = new InputMessageBuffer();
        } else {
            throw new Exception("No connection available");
        }
        return bufferOb;
    }

    public String getNextMessage() {

        while (buffer.isEmpty()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(InputMessageBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String message = buffer.remove();
        return message;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = conn.receiveMessage();
                buffer.add(message);
//            System.out.println(buffer.size());
            } catch (Exception ex) {
                System.out.println("!@#$%^&*))(*&^%!@$%^&*% : SOCKET EXCEPTION");
            }
        }

    }
}

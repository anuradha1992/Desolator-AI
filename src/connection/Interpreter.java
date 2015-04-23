/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sprites.Brick;
import sprites.CoinPile;
import sprites.GameObject;
import sprites.LifePack;
import sprites.Tank;
import view.Map;

/**
 *
 * @author ruchiranga
 */
public class Interpreter extends Observable {

    InputMessageBuffer inBuffer;
    OutputMessageBuffer outBuffer;
    private Map map;
    private Tank[] tanks;
    private String playerName;
    private String message;
    private String communicator;
    private String[] joinErrors;
    private String[] inGameErrors;

    private final int PLAYERS_FULL_ERROR = 0;
    private final int ALREADY_ADDED_ERROR = 1;
    private final int GAME_ALREADY_STARTED_ERROR = 2;
    private final int ME_ALREADY_STARTED_ERROR = 3;

    private final int OBSTACLE = 0;
    private final int CELL_OCCUPIED = 1;
    private final int DEAD = 2;
    private final int TOO_QUICK = 3;
    private final int INVALID_CELL = 4;
    private final int GAME_HAS_FINISHED = 5;
    private final int GAME_NOT_STARTED_YET = 6;
    private final int NOT_A_VALID_CONTESTANT = 7;
    private final int PIT_FALL = 8;

    private int errorCode = -1;

    private final int MAX_PLAYER_COUNT = 5;

    Thread inputThread;
    Thread outputThread;

    public Interpreter(String serverIP, int portIn, int portOut) {
        joinErrors = new String[]{"PLAYERS_FULL#", "ALREADY_ADDED#", "GAME_ALREADY_STARTED#", "ME_ALREADY_STARTED#"};
        inGameErrors = new String[]{"OBSTACLE#", "CELL_OCCUPIED#", "DEAD#", "TOO_QUICK#", "INVALID_CELL#", "GAME_HAS_FINISHED#", "GAME_NOT_STARTED_YET#", "NOT_A_VALID_CONTESTANT#","PITFALL#"};
        try {
            inBuffer = InputMessageBuffer.getInstance(serverIP, portIn, portOut);
            outBuffer = OutputMessageBuffer.getInstance(serverIP, portIn, portOut);
        } catch (Exception ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
        inputThread = new Thread(inBuffer);
        outputThread = new Thread(outBuffer);
        inputThread.start();
        outputThread.start();

    }

    public void requestJoin() {

        this.message = "JOIN#";
        this.communicator = "player";

        /*
         Join request needs to have a direct connection
         since incase of an error of being unable to reach the server
         the error message wont show up if the message was sent via the outBuffer.
         */
        Connection conn = outBuffer.getConnection();

        try {
            conn.sendMessage(message);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not connect to the server. Please make sure the server is up and running.", "Error in connecting to the server", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(0);
        }
//        outBuffer.sendMessage(this.message);
        setChanged();
        notifyObservers(this);
    }

    public String getPlayerName() {
        return playerName;
    }

    Map requestMap() {

        String message = inBuffer.getNextMessage();
        if (isJoinError(message)) {
            return null;
        }
        this.message = message;
        this.communicator = "server";
        message = message.substring(2, message.indexOf("#"));

        String[] parts = message.split(":");
        String pname = parts[0];
        this.playerName = pname;
        String bricks = parts[1];
        String stones = parts[2];
        String water = parts[3];
        map = new Map(bricks, stones, water);
        return map;
    }

    Tank[] requestPlayers() {

        tanks = new Tank[MAX_PLAYER_COUNT];
        String message = "";
        message = inBuffer.getNextMessage();

        if (isJoinError(message)) {
            //handleError(message);
            return null;
        } else {

            this.message = message;
            this.communicator = "server";
            message = message.substring(2, message.indexOf("#"));
            String[] parts = message.split(":");

            for (int i = 0; i < parts.length; i++) {
                String[] tankInfo = parts[i].split(";");
                String pname = tankInfo[0];
                String[] coords = tankInfo[1].split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int direction = Integer.parseInt(tankInfo[2]);
                tanks[i] = new Tank(pname, x, y, direction);
            }

            return tanks;
        }
    }

    public String getMessage() {
        return message;
    }

    public String getCommunicator() {
        return communicator;
    }

    public GameObject[] getUpdate() {
        GameObject[] objects;
        String message = inBuffer.getNextMessage();
        this.communicator = "server";
        this.message = message;
        if (message.startsWith("C:")) {
            setChanged();
            notifyObservers(this);

            message = message.substring(2, message.indexOf("#"));
            String[] parts = message.split(":");
            String[] coordinates = parts[0].split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            int lt = Integer.parseInt(parts[1]);
            int val = Integer.parseInt(parts[2]);

            CoinPile coins = new CoinPile(x, y, val, lt);
//            System.out.println("coin created with a lt of " + lt);
            objects = new GameObject[]{coins};
            return objects;
        } else if (message.startsWith("L:")) {
            setChanged();
            notifyObservers(this);

            message = message.substring(2, message.indexOf("#"));
            String[] parts = message.split(":");
            String[] coordinates = parts[0].split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int lt = Integer.parseInt(parts[1]);

            LifePack life = new LifePack(x, y, lt);
            objects = new GameObject[]{life};
            return objects;
        } else if (message.startsWith("G:")) {
            setChanged();
            notifyObservers(this);

            message = message.substring(2, message.indexOf("#"));
            String[] parts = message.split(":");

            String brickInfo = parts[parts.length - 1];
            String[] bricks = brickInfo.split(";");

            objects = new GameObject[bricks.length + (parts.length - 1)];
            int objArrayPtr = 0;

            for (int i = 0; i < bricks.length; i++) {
                String[] abrick = bricks[i].split(",");
                int x = Integer.parseInt(abrick[0]);
                int y = Integer.parseInt(abrick[1]);
                int damage = Integer.parseInt(abrick[2]);
                Brick brick = new Brick(x, y);
                brick.setPercentage(damage);
                objects[objArrayPtr] = brick;
                objArrayPtr++;
            }

            for (int i = 0; i < parts.length - 1; i++) {
                String[] playerinfo = parts[i].split(";");
                String pname = playerinfo[0];
                String[] coordinates = playerinfo[1].split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int direction = Integer.parseInt(playerinfo[2]);
                int isShot = Integer.parseInt(playerinfo[3]);
                int health = Integer.parseInt(playerinfo[4]);
                int coins = Integer.parseInt(playerinfo[5]);
                int points = Integer.parseInt(playerinfo[6]);
                Tank tank = new Tank(pname, x, y, direction, isShot, health, coins, points);
                objects[objArrayPtr] = tank;
                objArrayPtr++;
            }

            return objects;
        } else if (isInGameError(message)) {
//            System.out.println("ingame erorrrrrrrrrrr");
            handleInGameError();
            return null;
        } else {

            setChanged();
            notifyObservers(this);
            return null;
        }

    }

    private boolean isJoinError(String message) {
        for (int i = 0; i < joinErrors.length; i++) {
            if (joinErrors[i].equals(message)) {
                errorCode = i;
                return true;
            }

        }

        return false;
    }

    public void handleInGameError() {

        if (errorCode >= 0) {
            Tank player = null;
            for (Tank tank : tanks) {
                if (tank.getName().equals(playerName)) {
                    player = tank;
                    break;
                }
            }
            switch (errorCode) {

                case OBSTACLE:
                    player.setHeadingObstacle(true);
                    break;
                case CELL_OCCUPIED:
                    player.setHeadingOccupiedCell(true);
                    break;
                case DEAD:
//                    System.out.println("DEADDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                    player.setIsDead(true);
                    break;
                case PIT_FALL:
//                    System.out.println("PIT FALLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    player.setIsDead(true);
                    break;
                case TOO_QUICK:
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case INVALID_CELL:
                    player.setInvalidMove(true);
                    break;
                case GAME_HAS_FINISHED:
//                    System.out.println("GAME IS FINISHED!");
//                    try {
//                        inputThread.wait();
//                        outputThread.wait();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
//                    }

                    break;
                case GAME_NOT_STARTED_YET:
                    JOptionPane.showMessageDialog(null, "Game has not yet been started. Please be patient.", "Game not started yet", JOptionPane.ERROR_MESSAGE);
                    break;
                case NOT_A_VALID_CONTESTANT:
                    JOptionPane.showMessageDialog(null, "Server did not accept you as a valid contestant. The client application will now close.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;

            }
        }
    }

    public void handleJoinError() {
        if (errorCode >= 0) {
            switch (errorCode) {
                case PLAYERS_FULL_ERROR:
                    JOptionPane.showMessageDialog(null, "Could not join the game. Server is already connected with the maximum number of players. The client application will now halt.", "Error in joining", JOptionPane.ERROR_MESSAGE);
                    break;
                case ALREADY_ADDED_ERROR:
                    JOptionPane.showMessageDialog(null, "The player is already added. The client application will now halt.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case ME_ALREADY_STARTED_ERROR:
                case GAME_ALREADY_STARTED_ERROR:
                    JOptionPane.showMessageDialog(null, "Game has already been started. Please wait until the next game starts. The client application will now halt.", "Error in joining", JOptionPane.ERROR_MESSAGE);
                    break;
            }
            System.exit(0);
        }

    }

    public void shoot() {
        outBuffer.sendMessage("SHOOT#");
    }

    public void moveUp() {
        outBuffer.sendMessage("UP#");
    }

    public void moveDown() {
        outBuffer.sendMessage("DOWN#");
    }

    public void moveLeft() {
        outBuffer.sendMessage("LEFT#");
    }

    public void moveRight() {
        outBuffer.sendMessage("RIGHT#");
    }

    private boolean isInGameError(String message) {
        for (int i = 0; i < inGameErrors.length; i++) {
            if (inGameErrors[i].equals(message)) {
                errorCode = i;
                return true;
            }

        }
        return false;
    }

}

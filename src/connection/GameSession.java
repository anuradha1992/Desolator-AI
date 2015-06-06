/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import game_maintenance.StatusUpdater;
import java.util.Observable;
import java.util.Observer;
import sprites.Tank;
import view.GamePanel;
import view.GameSetup;
import view.GameView;
import view.Map;

/**
 *
 * @author ruchiranga
 */
public class GameSession extends Observable implements Observer{

    Map map;
    Interpreter i;
    GamePanel gamePanel;

    private Tank[] players;
    StatusUpdater updater;
    private String playerName;

    private boolean noErrors = true;

    public GameSession(String serverIP, int portIn, int portOut) {
        i = new Interpreter(serverIP, portIn, portOut);
        map = null;
    }

    /**
     * Initialize the game 
     * Call send request message
     * Call generate map from the incoming message
     * Set player name
     */
    public void initGame() {
        addObserver(GameSetup.getInstance());
        requestJoin();
        generateMap();
        playerName = i.getPlayerName();
        if (noErrors) {
            placePlayers();
        }

    }
    
    private void requestJoin(){
        setChanged();
        notifyObservers("Requesting to Join....");
        i.requestJoin();
    }

    public void startGame() {
        if (noErrors) {
            map.addObserver(gamePanel);
            startUpdateThread();
        } else {
            i.handleJoinError();
        }
    }

    public void setObserver(GameView view) {
        i.addObserver(view);
        updater.addObserver(view);
    }

    public String getPlayerName() {
        return playerName;
    }

    private void generateMap() {
        setChanged();
        notifyObservers("Genrating Initial Map...");
        map = i.requestMap();

        if (map == null) {
            noErrors = false;
        } else {
            setChanged();
            notifyObservers("Map Generation Successful");
        }
    }

    private void placePlayers() {
        setChanged();
        notifyObservers("Requesting Initial Player Positions...");
        players = i.requestPlayers();

        if (players == null) {
            noErrors = false;
        } else {
            noErrors = true;
            setChanged();
            notifyObservers("Players Placement Successful");
        }
    }

    public Map getMap() {
        return map;
    }

    private void startUpdateThread() {
        for (Tank tank : players) {
            if(tank.getName().equals(playerName)){
                tank.setObserver(this);
                break;
            }
        }
        
        
        updater = new StatusUpdater(this);
        Thread updateThread = new Thread(updater);
        updateThread.setName("UpdateThread");
        updateThread.start();
    }

    public StatusUpdater getStatusUpdater() {
        return updater;
    }

    public Interpreter getInterpreter() {
        return i;
    }

    /**
     * @return the players
     */
    public Tank[] getTanks() {
        return players;
    }

    public void setTanks(Tank[] tanks) {
        this.players = tanks;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String)arg;
        switch(message){
            case "SHOOT":
                i.shoot();
                break;
            case "UP":
                i.moveUp();
                break;
            case "DOWN":
                i.moveDown();
                break;
            case "RIGHT":
                i.moveRight();
                break;
            case "LEFT":
                i.moveLeft();
                break;

        }
    }

}

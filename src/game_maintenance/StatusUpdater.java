/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_maintenance;

import ai.NextMove;
import connection.GameSession;
import connection.Interpreter;
import java.util.ArrayList;
import java.util.Observable;
import sprites.CoinPile;
import sprites.GameObject;
import sprites.Tank;
import sprites.Water;
import view.Map;

/**
 *
 * @author ruchiranga
 */
public class StatusUpdater extends Observable implements Runnable {

    GameSession session;
    Interpreter interpreter;
    Map map;
    Tank[] tanks;
    boolean[] dead;

    private Tank tank;
    private Tank[] opponents;
    long prevtime;

    public StatusUpdater(GameSession session) {
        /* set session, interpreter, map, tank objects */
        this.session = session;
        interpreter = session.getInterpreter();
        map = session.getMap();
        tanks = session.getTanks();
        prevtime = 0;
        dead = new boolean[5];
        setTanks();
    }

    @Override
    public void run() {
        while (true) {  // always check if interpreter has recieved an update, get that update and update all the objects related to the game
            GameObject[] obj = interpreter.getUpdate();
            String msgType = interpreter.getLastMsgType();
            if (obj != null) {
                ArrayList<Tank> tempTank = new ArrayList();
                int k = 0;
                for (GameObject gameObject : obj) {
                    if (gameObject != null) {
                        String type = gameObject.getClass().getName().substring(8);
                        if (type.equals("Tank")) {
                            Tank tankUpdate = (Tank) gameObject;
                            if (!tempTank.contains(tankUpdate)) {
                                tempTank.add(tankUpdate);
                            }
                            String name = tankUpdate.getName();
                            for (Tank tank : tanks) {  //This update can be done more efficiently by taking the digit in thee name as the index to the array.
                                if (tank != null && tank.getName().equals(tankUpdate.getName())) {
                                    tank.setX(tankUpdate.getX());
                                    tank.setY(tankUpdate.getY());
                                    tank.setDirection(tankUpdate.getDirection());
                                    tank.setCoins(tankUpdate.getCoins());
                                    tank.setHealth(tankUpdate.getHealth());
                                    tank.setIsShot(tankUpdate.getIsShot());
                                    if (tank.getIsShot() == 1) {
                                        tank.fire();
                                    }
                                    if (!dead[k]) {
                                        if (tank.getHealth() == 0) {
                                            dead[k] = true;
                                            GameObject[][] mapAr = map.getMap();
                                            if (mapAr[tank.getX()][tank.getY()] != null && mapAr[tank.getX()][tank.getY()].toString().equals("Water")) {
                                                Water water = new Water(tank.getX(), tank.getY());  // when our tank falls to water
                                                map.updateMap(water, "Water");
                                            } else {
                                                if (tank.getCoins() > 0) {
                                                    CoinPile coins = new CoinPile(tank.getX(), tank.getY(), tank.getCoins(), 5000 * 4);
                                                    map.updateMap(coins, "CoinPile");
                                                }
                                            }
                                        }
                                    }
                                    k++;
                                    tank.setScore(tankUpdate.getScore());
                                    break;
                                }
                            }
                            calculateRank(tanks);
                            setChanged();
                            notifyObservers(tanks);
                        } else if (type.equals("CoinPile") || type.equals("LifePack") || type.equals("Brick")) {
                            map.updateMap(gameObject, type);
                        } else {
                            System.out.println("ERROR IN RECEIVED GAME OBJECT");  // this should not happen
                        }
                    }
                }
                int actual_no_of_tanks = tempTank.size();
                int current_no_of_tanks = 0;
                while (tanks[current_no_of_tanks] != null) {
                    current_no_of_tanks++;
                    if (current_no_of_tanks >= 5) {
                        break;
                    }
                }
                if (actual_no_of_tanks != current_no_of_tanks) {
                    for (int i = current_no_of_tanks; i < actual_no_of_tanks; i++) {
                        tanks[i] = tempTank.get(i);
                    }
                    session.setTanks(tanks);
                }
            }
            if (msgType.equals("G")) {  // if the update type is a global update then call the NextMove method to determine the next move of the tank
                NextMove move = new NextMove(tank, opponents, map);
                tank.execute(move.getNextMove());
            }
        }
    }

    /* initialize our tank and opponent */
    private void setTanks() {
        String tankName = session.getPlayerName();
        Tank[] tanks = session.getTanks();
        if (tanks != null) {
            opponents = new Tank[tanks.length - 1];
            int i = 0;
            for (Tank t : tanks) {
                if (t != null) {
                    if (t.getName().equals(tankName)) {
                        tank = t;
                    } else {
                        opponents[i] = t;
                        i++;
                    }
                }
            }
        }
    }

    /* Calculate the current ranks of the tanks */
    private void calculateRank(Tank[] tanks) {
        int no_of_tanks = 0;
        while (tanks[no_of_tanks] != null) {
            no_of_tanks++;
            if (no_of_tanks >= 5) {
                break;
            }
        }
        int[] score = new int[no_of_tanks];
        int[] position = new int[no_of_tanks];
        int i = 0;
        for (Tank t : tanks) {
            if (t != null) {
                score[i] = t.getScore();
                position[i] = i;
                i++;
            }
        }
        for (i = 0; i < no_of_tanks - 1; i++) {
            for (int j = 0; j < no_of_tanks - i - 1; j++) {
                if (score[j] < score[j + 1]) {
                    int swap = score[j];
                    score[j] = score[j + 1];
                    score[j + 1] = swap;

                    swap = position[j];
                    position[j] = position[j + 1];
                    position[j + 1] = swap;
                }

            }
        }
        int rank = 1;
        for (int j = 0; j < position.length; j++) {
            tanks[position[j]].setRank(rank);
            rank++;
        }

    }

}

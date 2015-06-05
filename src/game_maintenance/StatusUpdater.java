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
        while (true) {
            GameObject[] obj = interpreter.getUpdate();

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
                            
                            for (Tank tank : tanks) {//This update can be done more efficiently by taking the digit in thee name as the index to the array.
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
//                                    System.out.println("I Is equal to : "+k+" Boolrsn value = "+dead[k]);
                                    if (!dead[k]) {
//                                        System.out.println("NOT DEAD : " + tank.getName()+" And health is : "+tank.getHealth());
                                        if (tank.getHealth() == 0) {
//                                            System.out.println("HEALTH 0 : " + tank.getName());
                                            dead[k] = true;
                                            GameObject[][] mapAr = map.getMap();
                                            if (mapAr[tank.getX()][tank.getY()] != null && mapAr[tank.getX()][tank.getY()].toString().equals("Water")) {
                                                //Fallen to water
                                                System.out.println("FALLEN TO WATER : " + tank.getName());
                                                Water water = new Water(tank.getX(), tank.getY());
                                                map.updateMap(water, "Water");
                                                
                                            } else {
                                                //Not fallen to water
                                                //Make a coin pile
//                                                System.out.println("DEAD BY SHOT : " + tank.getName());
                                                if(tank.getCoins() > 0){
                                                    CoinPile coins = new CoinPile(tank.getX(), tank.getY(), tank.getCoins(), 5000*4);
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
                            //This should not happen
                            System.out.println("ERROR IN RECEIVED GAME OBJECT");
                        }
                    }
                }

//            for (int i = 0; i < map.getObjectMap().length; i++) {
//                for (int j = 0; j < map.getObjectMap()[i].length; j++) {
//                    GameObject mapElement = map.getObjectMap()[i][j];
//                    if (mapElement != null) {
//
//                        String type = mapElement.getClass().getName().substring(8);
//                        if (type.equals("CoinPile")) {
//                            ((CoinPile) mapElement).decreaseRemainingLifeTime();
//                            if (((CoinPile) mapElement).getRemainingLifeTime() <= 0) {
//                                ((CoinPile) mapElement).setAcquired(true);
//                                map.getObjectMap()[i][j] = null;
//                                System.out.println("Coin Pile acquired or expired");
//                            }
//                        } else if (type.equals("LifePack")) {
//                            ((LifePack) mapElement).decreaseRemainingLifeTime();
//                            if (((LifePack) mapElement).getRemainingLifeTime() <= 0) {
//                                ((LifePack) mapElement).setAcquired(true);
//                                map.getObjectMap()[i][j] = null;
//                                System.out.println("Life Pack acquired or expired");
//                            }
//                        }
//                    }
//
//                }
//
//            }
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

//                System.out.println("Map and Tanks Updated!");
            }
            
            long curtime = java.lang.System.currentTimeMillis();
                if(curtime - prevtime >= 1000){
                    NextMove move = new NextMove(tank, opponents, map);
                    tank.execute(move.getNextMove());
                    prevtime = curtime;
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                }
                
            
        }
        
    }
    
    
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
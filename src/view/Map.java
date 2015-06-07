/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.Observable;
import java.util.Observer;
import sprites.Brick;
import sprites.CoinPile;
import sprites.GameObject;
import sprites.LifePack;
import sprites.Stone;
import sprites.Water;

/**
 *
 * @author ruchiranga
 */
public class Map extends Observable implements Observer {

    final int ARENA_SIZE = 20;
    final int BLOCK_SIZE = 30;

    private GameObject[][] map = new GameObject[ARENA_SIZE][ARENA_SIZE];

    public Map(String bricks, String stones, String water) {
        placeObjectsOnMap(bricks, stones, water);
    }
    
    public GameObject[][] getMap(){
        return map;
    }

    /* get the coordinates of bricks and set to where in the map array the bricks should be placed */
    private void evalBricks(String bricks) {
        String[] pairs = bricks.split(";");
        for (String pair : pairs) {
            String[] coordinates = pair.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            map[x][y] = new Brick(x, y);
            map[x][y].addObserver(this);
        }
    }

    /* get the coordinates of stones and set to where in the map array the stones should be placed */
    private void evalStones(String stones) {
        String[] pairs = stones.split(";");
        for (String pair : pairs) {
            String[] coordinates = pair.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            map[x][y] = new Stone(x, y);
        }
    }

    /* get the coordinates of water and set to where in the map array water should be placed */
    private void evalWater(String water) {
        String[] pairs = water.split(";");
        for (String pair : pairs) {
            String[] coordinates = pair.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            map[x][y] = new Water(x, y);
        }
    }

    /* Call methods which places each of places that blocks of bricks, stones and water are in */
    private void placeObjectsOnMap(String bricks, String stones, String water) {
        evalBricks(bricks);
        evalStones(stones);
        evalWater(water);
    }

    public void removeGameObject(int i, int j) {
        map[i][j] = null;
    }

    /* Identify the type of object and updates the map accordingly */
    public void updateMap(GameObject ob, String type) {
        int idxX;
        int idxY;
        switch (type) {
            case "CoinPile":
                idxX = ob.getX();
                idxY = ob.getY();
                map[idxX][idxY] = (CoinPile) ob;
                map[idxX][idxY].addObserver(this);
                break;
            case "LifePack":
                idxX = ob.getX();
                idxY = ob.getY();
                map[idxX][idxY] = (LifePack) ob;
                map[idxX][idxY].addObserver(this);
                break;
            case "Brick":
                Brick updatedBrick = (Brick) ob; // can get the value in the updatedBrick and use the reducePercentage with a for loop too
                if (map[ob.getX()][ob.getY()] != null) {
                    if (map[ob.getX()][ob.getY()].getClass().getName().substring(8).equals("Brick")) {
                        ((Brick) map[ob.getX()][ob.getY()]).setPercentage(updatedBrick.getPercentage());
                    } else {
                        System.out.println("ERROR IN MAP!!!");  // this should not happen
                    }
                }
                break;
            case "Water":
                Water updatedWater = (Water) ob;   // can get the value in the updatedBrick and use the reducePercentage witha  for loop too
                map[ob.getX()][ob.getY()] = updatedWater;
                break;
        }
    }

    /**
     * @return the map
     */
    public GameObject[][] getObjectMap() {
        return map;
    }

    @Override
    public void update(Observable o, Object arg) {  // get an update when a coin pile or life pack is taken by another tank or it gets expired
        String[] xy = ((String) arg).split(",");
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);
        map[x][y] = null;
        setChanged();
        notifyObservers();
    }

}

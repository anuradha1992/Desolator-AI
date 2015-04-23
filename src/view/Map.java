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

    private void evalStones(String stones) {
        String[] pairs = stones.split(";");
        for (String pair : pairs) {
            String[] coordinates = pair.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            map[x][y] = new Stone(x, y);
        }
    }

    private void evalWater(String water) {

        String[] pairs = water.split(";");
        for (String pair : pairs) {
            String[] coordinates = pair.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            map[x][y] = new Water(x, y);
        }
    }

    private void placeObjectsOnMap(String bricks, String stones, String water) {
        evalBricks(bricks);
        evalStones(stones);
        evalWater(water);
    }
    /*
     TODO:
     Check whether the coin pile object delete itself when it times out.
     */

    public void removeGameObject(int i, int j) {
        map[i][j] = null;
    }

    public void updateMap(GameObject ob, String type) {
        int idxX;
        int idxY;
        switch (type) {
            case "CoinPile":
                idxX = ob.getX();
                idxY = ob.getY();
                //ob.setX(ob.getX()*BLOCK_SIZE);
                //ob.setY(ob.getY()*BLOCK_SIZE);
                map[idxX][idxY] = (CoinPile) ob;
                map[idxX][idxY].addObserver(this);
//                System.out.println("Coin Pile placed at (" + map[idxX][idxY].getX() + "," + map[idxX][idxY].getY() + ") on map.");
                break;
            case "LifePack":
                idxX = ob.getX();
                idxY = ob.getY();
                //ob.setX(ob.getX()*BLOCK_SIZE);
                //ob.setY(ob.getY()*BLOCK_SIZE);
                map[idxX][idxY] = (LifePack) ob;
                map[idxX][idxY].addObserver(this);
//                System.out.println("Life pack placed at (" + ob.getX() + "," + ob.getY() + ") on map");
                break;
            case "Brick":
                Brick updatedBrick = (Brick) ob;//Can get the value in the updatedBrick and use the reducePercentage witha  for loop too.
                if (map[ob.getX()][ob.getY()] != null) {
                    if (map[ob.getX()][ob.getY()].getClass().getName().substring(8).equals("Brick")) {
                        ((Brick) map[ob.getX()][ob.getY()]).setPercentage(updatedBrick.getPercentage());
                    } else {
                        //this should not happen
                        System.out.println("ERROR IN MAP!!!");
                    }
                }
                break;
            case "Water":
                Water updatedWater = (Water) ob;//Can get the value in the updatedBrick and use the reducePercentage witha  for loop too.
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
    public void update(Observable o, Object arg) {
        String[] xy = ((String) arg).split(",");
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);
        map[x][y] = null;
        setChanged();
        notifyObservers();
    }

}

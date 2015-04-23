package ai;

import java.util.ArrayList;
import java.util.Collections;
import sprites.CoinPile;
import sprites.GameObject;
import sprites.LifePack;
import sprites.Tank;
import view.Map;

public class NextMove {

    private int MAP_SIZE = 20;

    private float COIN;
    private float LIFE;
//    private float SHOOT;
//    private float DEFEND;

    private Tank tank;
    private Tank[] opponents;

    ArrayList<GameObject> validGameObArr;
    ArrayList<Float> timeCosts;
    ArrayList<ArrayList<Cell>> aStarPaths;

    Map map;

    public NextMove(Tank tank, Tank[] opponents, Map map) {

        //to read from file and process coefficients
        COIN = 1000;
        LIFE = 1000;
//        SHOOT = 1000;
//        DEFEND = 0;

        this.tank = tank;
        this.opponents = opponents;
        this.map = map;
        this.validGameObArr = new ArrayList<>();
        this.timeCosts = new ArrayList<>();
        this.aStarPaths = new ArrayList<>();

        addActionObjects();

    }

    public ArrayList<Cell> getActualPath(ArrayList<Cell> path) {     //use this function to get the actual path after taking the decision to go to which cell
        ArrayList<Cell> actualPath = new ArrayList<>();

        Cell dest = path.get(path.size() - 1);
        Cell source = path.get(0);

        int i = 0;
        actualPath.add(dest);
        while (actualPath.get(i).getX() != source.getX() || actualPath.get(i).getY() != source.getY()) {
            Cell child = actualPath.get(i);
            actualPath.add(child.getParent());
            i++;
        }

        System.out.println("------------------------------------------------------------------------------------------------");

        for (int k = actualPath.size() - 1; k >= 0; k--) {        //the actual path needs to be read from largest cell index to smallest cell index to take the path
            System.out.print(" " + actualPath.get(k).getX() + " " + actualPath.get(k).getY() + " | ");
        }

        System.out.println("");
        System.out.println("");

        Collections.reverse(actualPath);

        return actualPath;

    }

    public void addActionObjects() {

        if (tank != null && !tank.isToBeRemoved()) {

            GameObject[][] gameObArr = map.getMap();

            for (int i = 0; i < MAP_SIZE; i++) {
                for (int j = 0; j < MAP_SIZE; j++) {
                    if (gameObArr[i][j] != null) {
                        if (gameObArr[i][j].toString().equalsIgnoreCase("CoinPile") || gameObArr[i][j].toString().equalsIgnoreCase("LifePack")) {
                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + tank.getDirection());
                            PathFinder pf = new PathFinder(tank.getX(), tank.getY(), tank.getDirection(), gameObArr[i][j].getX(), gameObArr[i][j].getY(), map);
                            ArrayList<Cell> path = pf.findPath();
                            if (path != null) {
                                Cell lastCell = (Cell) path.get(path.size() - 1);
                                System.out.println("At the first place");
                                getActualPath(path);
                                int timeCost = lastCell.getG_cost();

                                System.out.println("(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
                                System.out.println(gameObArr[i][j]);
                                System.out.println(tank);
                                System.out.println(gameObArr[i][j].toString() + " " + tank.getX() + " " + tank.getY() + " " + tank.getDirection() + " " + gameObArr[i][j].getX() + " " + gameObArr[i][j].getY() + " " + timeCost);
                                System.out.println("(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
                                float remainingLifeTime = 0;

                                if (gameObArr[i][j].toString().equalsIgnoreCase("CoinPile")) {
                                    CoinPile cp = (CoinPile) gameObArr[i][j];
                                    remainingLifeTime = cp.getRemainingLifeTime() / 1000;
                                } else if (gameObArr[i][j].toString().equalsIgnoreCase("LifePack")) {
                                    LifePack lp = (LifePack) gameObArr[i][j];
                                    remainingLifeTime = lp.getRemainingLifeTime() / 1000;
                                }
                                if (Math.floor(remainingLifeTime) >= timeCost) {
                                    validGameObArr.add(gameObArr[i][j]);
                                    timeCosts.add((float) timeCost);
                                    aStarPaths.add(path);
//                                    System.out.println("Place of added actionObjects to array");
//                                    getActualPath(path);
                                }

                            }

                        }
                    }
                }
            }

//            if (opponents != null) {
//                for (int k = 0; k < opponents.length; k++) {
//                    if (opponents[k] != null) {
//                        if (opponents[k].getHealth() > 0) {
//                            addViableOpponents(opponents[k]);
//                        }
//                    }
//                }
//            }
        }

//        System.out.println("___________________________________________________________________________________________");
//        System.out.println("size " + validGameObArr.size());
//        System.out.println("___________________________________________________________________________________________");
    }

    public void addViableOpponents(Tank opponent) {

        boolean add = true;
        float cost = 0;
        float directionCost = 0;
        GameObject[][] gameObArr = map.getMap();

        if (tank.getX() == opponent.getX()) {
            if (tank.getY() <= opponent.getY()) {

                for (int i = tank.getY() + 1; i < opponent.getY(); i++) {
                    if (gameObArr[tank.getX()][i] != null) {
                        if (gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Brick") || gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Stone")) {
                            add = false;
                            break;
                        }
                    }
                }
                if (add == true) {
                    //direction cost
                    if (tank.getDirection() == 0) {
                        directionCost = 0;
                    } else {
                        directionCost = 1;
                    }
                }

            } else {
                for (int i = opponent.getY() + 1; i < tank.getY(); i++) {
                    if (gameObArr[tank.getX()][i] != null) {
                        if (gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Brick") || gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Stone")) {
                            add = false;
                            break;
                        }
                    }
                }
                if (add == true) {
                    //direction cost
                    if (tank.getDirection() == 2) {
                        directionCost = 0;
                    } else {
                        directionCost = 1;
                    }
                }
            }
            if (add == true) {
                cost = directionCost + (Math.abs(opponent.getY() - tank.getY()) / 4f);   ///add direction cost too
                validGameObArr.add(opponent);
                timeCosts.add((float) cost);
//                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+opponent.getName()+" "+cost);
                return;
            }
        } else if (tank.getY() == opponent.getY()) {
            if (tank.getX() <= opponent.getX()) {
                for (int i = tank.getX() + 1; i < opponent.getX(); i++) {
                    if (gameObArr[i][tank.getY()] != null) {
                        if (gameObArr[i][tank.getY()].toString().equalsIgnoreCase("Brick") || gameObArr[i][tank.getY()].toString().equalsIgnoreCase("Stone")) {
                            add = false;
                            break;
                        }
                    }
                }
                if (add == true) {
                    //direction cost
                    if (tank.getDirection() == 1) {
                        directionCost = 0;
                    } else {
                        directionCost = 1;
                    }
                }
            } else {
                for (int i = opponent.getX() + 1; i < tank.getX(); i++) {
                    if (gameObArr[i][tank.getY()] != null) {
                        if (gameObArr[i][tank.getY()].toString().equalsIgnoreCase("Brick") || gameObArr[i][tank.getY()].toString().equalsIgnoreCase("Stone")) {
                            add = false;
                            break;
                        }
                    }
                }
                if (add == true) {
                    //direction cost
                    if (tank.getDirection() == 3) {
                        directionCost = 0;
                    } else {
                        directionCost = 1;
                    }
                }
            }
            if (add == true) {
                cost = directionCost + (Math.abs(opponent.getX() - tank.getX()) / 4f);   ///add direction cost too
                validGameObArr.add(opponent);
                timeCosts.add(cost);
//                System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb "+opponent.getName()+" "+cost);
                return;
            }
        }
    }

    public String getNextMove() {
        if (validGameObArr != null && validGameObArr.size() != 0 /*&& validGameObArr.size() == aStarPaths.size()*/) {
            float gameObScores[] = new float[validGameObArr.size()];

            for (int i = 0; i < validGameObArr.size(); i++) {
                GameObject ob = validGameObArr.get(i);
                String type = ob.toString();
                float coeff;
                switch (type) {
                    case "CoinPile":
                        coeff = (((CoinPile) ob).getValue() / timeCosts.get(i));
//                        coeff = (1 / timeCosts.get(i));
                        gameObScores[i] = coeff * COIN;
                        break;
                    case "LifePack":
                        coeff = 1 / timeCosts.get(i);
                        gameObScores[i] = coeff * LIFE;
                        break;
//                    case "LiveGameObject":
//                        coeff = 1/ timeCosts.get(i);
//                        gameObScores[i] = coeff * SHOOT;
//                        break;
                }

            }

            float max = 0;
            int maxIdx = 0;
            for (int i = 0; i < gameObScores.length; i++) {
                float f = gameObScores[i];
                if (f >= max) {
                    max = f;
                    maxIdx = i;
                }
            }
//            System.out.println(validGameObArr.size());
//            System.out.println(timeCosts.size());
//            System.out.println(aStarPaths.size());
//            System.out.println("Getting the actualest path : " + maxIdx);

            if (aStarPaths.size() > 0 && aStarPaths.size() - 1 >= maxIdx) {  // because an error is coming and aStarpaths only contain paths to coinpiles $ lifepacks no paths to opponents
                ArrayList<Cell> actualPath = getActualPath(aStarPaths.get(maxIdx));
                if (actualPath.size() > 1) {
                    Cell nextCell = actualPath.get(1);

                    int targetdir = -1;

                    //Determine target direction
                    int curx = tank.getX();
                    int cury = tank.getY();

                    if (nextCell.getX() == curx) {
                        if (nextCell.getY() > cury) {
                            targetdir = 2;
                        } else if (nextCell.getY() < cury) {
                            targetdir = 0;
                        } else {
                            System.out.println("ERRRORRR:this next move is the current cell itself!!!!!!!!!!!!!!!!!!!!");
                        }
                    } else {
                        if (nextCell.getX() > curx) {
                            targetdir = 3;
                        } else {
                            targetdir = 1;
                        }
                    }

                    if (targetdir >= 0) {
                        switch (targetdir) {
                            case 0:
                                return "UP";
                            case 1:
                                return "LEFT";
                            case 2:
                                return "DOWN";
                            case 3:
                                return "RIGHT";
                        }
                    } else {
                        System.out.println("ERRRORRRR:target direction is -1!!!!!!!!!!");

                    }
                }
            }

        } else {
            System.out.println("validgameobarr is nulllllllll");

        }

        return "ERROR";
    }

}

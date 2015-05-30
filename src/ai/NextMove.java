package ai;

import java.util.ArrayList;
import java.util.Collections;
import sprites.CoinPile;
import sprites.GameObject;
import sprites.LifePack;
import sprites.Tank;
import view.Map;

public class NextMove {

    private static boolean dodgingBullet = false;

    private int MAP_SIZE = 20;

    private float COIN;
    private float LIFE;
    private float SHOOT;
//    private float DEFEND;

    private Tank tank;
    private Tank[] opponents;

    ArrayList<GameObject> validGameObArr;
    ArrayList<Float> timeCosts;
    ArrayList<ArrayList<Cell>> aStarPaths;

    Map map;

    public NextMove(Tank tank, Tank[] opponents, Map map) {

        //to read from file and process coefficients
        COIN = 100;
        LIFE = 1000;
        SHOOT = 1000 * 100;
//        DEFEND = 0;

        this.tank = tank;

        this.opponents = opponents;
        this.map = map;
        this.validGameObArr = new ArrayList<>();
        this.timeCosts = new ArrayList<>();
        this.aStarPaths = new ArrayList<>();

        addActionObjects();
        addShootableOpponents(); // Shootable opponents will have time costs 0.0

    }

    //get path from A* implementation
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
        try {
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
//                                    getActualPath(path);  
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
//                            addShootableOpponents(opponents[k]);
//                        }
//                    }
//                }
//            }
            }

//        System.out.println("___________________________________________________________________________________________");
//        System.out.println("size " + validGameObArr.size());
//        System.out.println("___________________________________________________________________________________________");
        } catch (Exception e) {
            System.out.println("Game obs null exception occured");
        }
    }

    /*
     * returns true if the passed opponent is in clear range(without any obstacles in between our tank and it) along the direction our tank is headed
     * counts those opponents infront of us as well as behind us
     */
    private boolean isClearlyAlignedOpponent(Tank opponent) {
        GameObject[][] gameObArr = map.getMap();
        boolean clearlyAligned = true;
        if (tank.getDirection() == 1 || tank.getDirection() == 3) {
            System.out.println("in if");
            if (tank.getY() == opponent.getY() && opponent.getHealth() > 0) {
                if (tank.getX() <= opponent.getX()) {

                    for (int i = tank.getX() + 1; i < opponent.getX(); i++) {
                        if (gameObArr[tank.getY()][i] != null) {
                            if (gameObArr[tank.getY()][i].toString().equalsIgnoreCase("Brick") || gameObArr[tank.getY()][i].toString().equalsIgnoreCase("Stone")) {
                                clearlyAligned = false;
                                break;
                            }
                        }
                    }
                } else {
                    for (int i = opponent.getX() + 1; i < tank.getX(); i++) {
                        if (gameObArr[tank.getY()][i] != null) {
                            if (gameObArr[tank.getY()][i].toString().equalsIgnoreCase("Brick") || gameObArr[tank.getY()][i].toString().equalsIgnoreCase("Stone")) {
                                clearlyAligned = false;
                                break;
                            }
                        }
                    }

                }
                 return clearlyAligned;
            }else{
                return false;
            }
           
        } else {
            System.out.println("in else "+tank.getX()+"  "+opponent.getX());
            if (tank.getX() == opponent.getX() && opponent.getHealth() > 0) {
                if (tank.getY() <= opponent.getY()) {

                    for (int i = tank.getY() + 1; i < opponent.getY(); i++) {
                        if (gameObArr[tank.getX()][i] != null) {
                            if (gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Brick") || gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Stone")) {
                                clearlyAligned = false;
                                break;
                            }
                        }
                    }
                } else {
                    for (int i = opponent.getY() + 1; i < tank.getY(); i++) {
                        if (gameObArr[tank.getX()][i] != null) {
                            if (gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Brick") || gameObArr[tank.getX()][i].toString().equalsIgnoreCase("Stone")) {
                                clearlyAligned = false;
                                break;
                            }
                        }
                    }

                }
                return clearlyAligned;
            }else{
                return false;
            }
            

        }
    }

    public void addShootableOpponents() {
        for (Tank opponent : opponents) {
            if (opponent != null) {
                float cost = 0;

                boolean aligned = isClearlyAlignedOpponent(opponent);
                
                if (aligned) {
                    System.out.println("opponent "+ opponent.getName()+" is clearly aligned!!!!!!!!!!!");
                    if (tank.getDirection() == 0 && opponent.getY() < tank.getY()) {
                        cost = tank.getY() - opponent.getY();
                    } else if (tank.getDirection() == 2 && opponent.getY() > tank.getY()) {
                        cost = opponent.getY() - tank.getY();
                    } else if (tank.getDirection() == 1 && opponent.getX() > tank.getX()) {
                        cost = opponent.getX() - tank.getX();
                    } else if (tank.getDirection() == 3 && opponent.getX() < tank.getX()) {
                        cost = tank.getX() - opponent.getX();
                    }else{
                        continue;
                    }
                    if (cost <= 8) { //add as shootable only if it is in very close range coz if killed at distance someone else might get the huge coinpile  :P
                        validGameObArr.add(opponent);
                        timeCosts.add(0.0f);
                        System.out.println("opponent addedd!!!!!!!!!!!");
//                        aStarPaths.add(null);
                    }
                }
            }

        }
    }

    private boolean isAimedAtMe(Tank opponent) {
        if (opponent.getDirection() == 0 && tank.getY() < opponent.getY()) {
            return true;
        } else if (opponent.getDirection() == 2 && tank.getY() > opponent.getY()) {
            return true;
        } else if (opponent.getDirection() == 1 && tank.getX() > opponent.getX()) {
            return true;
        } else if (opponent.getDirection() == 3 && tank.getX() < opponent.getX()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean bulletIncoming() {
        boolean incoming = false;
        for (Tank opponent : opponents) {
            if (opponent!=null && isClearlyAlignedOpponent(opponent) && isAimedAtMe(opponent) && opponent.getIsShot() == 1) {
                incoming = true;
            }
        }
        return incoming;
    }

    public String getNextMove() {
        if (bulletIncoming()) {
            dodgingBullet = true;
        }
        if (validGameObArr != null && validGameObArr.size() != 0) {
            if (dodgingBullet) {
                int targetX = 0;
                int targetY = 0;

                if (tank.getDirection() == 0 || tank.getDirection() == 2) {
                    int posX = tank.getX();
                    int posY = tank.getY();
                    int l = posX - 1;
                    int r = posX + 1;
                    GameObject[][] gameObArr = map.getMap();
                    while (l >= 0 && !(gameObArr[posY][l] == null || gameObArr[posY][l].toString().equalsIgnoreCase("CoinPile") || gameObArr[posY][l].toString().equalsIgnoreCase("LifePack") )) {
                        l--;
                    }
                    targetY = posY;
                    if (l < 0) { // We dont have any posible cell to move to in our left side
                        while (r < MAP_SIZE && !(gameObArr[posY][r] == null || gameObArr[posY][r].toString().equalsIgnoreCase("CoinPile") || gameObArr[posY][r].toString().equalsIgnoreCase("LifePack"))) {
                            r++;
                        }
                        targetX = r;
                    } else {
                        targetX = l;
                    }

                } else {
                    int posX = tank.getX();
                    int posY = tank.getY();
                    int l = posY + 1;
                    int r = posY - 1;
                    GameObject[][] gameObArr = map.getMap();
                    while (r >= 0 && !( gameObArr[r][posX] == null || gameObArr[r][posX].toString().equalsIgnoreCase("CoinPile") || gameObArr[r][posX].toString().equalsIgnoreCase("LifePack"))) {
                        r--;
                    }
                    targetX = posX;
                    if (r < 0) {
                        while (l < MAP_SIZE && !( gameObArr[l][posX] == null || gameObArr[l][posX].toString().equalsIgnoreCase("CoinPile") || gameObArr[l][posX].toString().equalsIgnoreCase("LifePack"))) {
                            l++;
                        }
                        targetY = l;
                    } else {
                        targetY = r;
                    }
                }
                PathFinder pf = new PathFinder(tank.getX(), tank.getY(), tank.getDirection(), targetX, targetY, map);
                ArrayList<Cell> path = pf.findPath();

                //This code snippet is a duplicate and need to be implemented as a function
                ArrayList<Cell> actualPath = getActualPath(path);
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

                } else if (actualPath.size() <= 1) { //dodging is complete
                    System.out.println("dogingg completed!!!!!!!!!!!");
                    dodgingBullet = false;
                }

            } else {
                
                //this loop checks if any tank is obstructing our path and shoots it untill it leaves our path
                for(GameObject ob:validGameObArr){
                    if(validGameObArr.toString().startsWith("P")){
                        switch(tank.getDirection()){
                            case 0:
                                if(ob.getY() == (tank.getY()-1))
                                    return "SHOOT";
                                break;
                            case 1:
                                if(ob.getX() == (tank.getX()+1))
                                    return "SHOOT";
                                break;
                            case 2:
                                if(ob.getY() == (tank.getY()+1))
                                    return "SHOOT";
                                break;
                            case 3:
                                if(ob.getX() == (tank.getX()-1))
                                    return "SHOOT";
                                break;
                                
                        }
                    }
                }
                
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
                            if (tank.getHealth() <= 50) {
                                coeff = 1 / (tank.getHealth() * timeCosts.get(i));
                                gameObScores[i] = coeff * LIFE * 1000000;
                                break;
                            } else {
                                coeff = 1 / (tank.getHealth() * timeCosts.get(i));
                                gameObScores[i] = coeff * LIFE;
                                break;
                            }
                        default:
//                        case "LiveGameObject":
////                            coeff = 1 / timeCosts.get(i);
////                            gameObScores[i] = coeff * SHOOT;
                            gameObScores[i] = SHOOT;
                            break;
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
                
                
                
                if (validGameObArr.get(maxIdx).toString().startsWith("P")) {
                    System.out.println("SHooooooooooooooootinggggg!!!");
                    return "SHOOT";
                } else if (aStarPaths.size() > 0 && aStarPaths.size() - 1 >= maxIdx) {  // because an error is coming and aStarpaths only contain paths to coinpiles $ lifepacks no paths to opponents
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
            }

        } else {
            System.out.println("validgameobarr is nulllllllll");

        }

        return "ERROR";
    }

}

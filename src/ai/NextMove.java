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
    private static boolean dodgeHorizontal = false;
    private static boolean dodgeVertical = false;

    private static int targetX = 0;
    private static int targetY = 0;

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
        COIN = 500;
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

        System.out.println("------------------------------------------------------------------------------------------------");

        Collections.reverse(actualPath);

        return actualPath;

    }

    /*
    *Add the objects coresponding to the actions that the tank can possibly take at a given moment
    */
    public void addActionObjects() {
        try {
            if (tank != null && !tank.isToBeRemoved()) {

                GameObject[][] gameObArr = map.getMap();

                for (int i = 0; i < MAP_SIZE; i++) {
                    for (int j = 0; j < MAP_SIZE; j++) {
                        if (gameObArr[i][j] != null) {
                            if (gameObArr[i][j].toString().equalsIgnoreCase("CoinPile") || gameObArr[i][j].toString().equalsIgnoreCase("LifePack")) {

                                PathFinder pf = new PathFinder(tank.getX(), tank.getY(), tank.getDirection(), gameObArr[i][j].getX(), gameObArr[i][j].getY(), map);
                                ArrayList<Cell> path = pf.findPath();
                                if (path != null) {
                                    Cell lastCell = (Cell) path.get(path.size() - 1);

                                    int timeCost = lastCell.getG_cost();

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
                                    }

                                }

                            }
                        }
                    }
                }
            }
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
            } else {
                return false;
            }

        } else {
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
            } else {
                return false;
            }

        }
    }

    /*
    * Find the set of tanks that are in our range of shooting visinity
    * Only opponents that are less than or equal to 8 positions away from us are considered to be shootable
    */
    public void addShootableOpponents() {
        for (Tank opponent : opponents) {
            if (opponent != null) {
                float cost = 0;

                boolean aligned = isClearlyAlignedOpponent(opponent);

                if (aligned) {
                    if (tank.getDirection() == 0 && opponent.getY() < tank.getY()) {
                        cost = tank.getY() - opponent.getY();
                    } else if (tank.getDirection() == 2 && opponent.getY() > tank.getY()) {
                        cost = opponent.getY() - tank.getY();
                    } else if (tank.getDirection() == 1 && opponent.getX() > tank.getX()) {
                        cost = opponent.getX() - tank.getX();
                    } else if (tank.getDirection() == 3 && opponent.getX() < tank.getX()) {
                        cost = tank.getX() - opponent.getX();
                    } else {
                        continue;
                    }
                    if (cost <= 8) { //add as shootable only if it is in very close range coz if killed at distance someone else might get the huge coinpile  :P
                        validGameObArr.add(opponent);
                        timeCosts.add(cost);
                        aStarPaths.add(null);
                    }
                } else {  //else just add the other opponents even if they are far away

                    if (opponent.getHealth() > 0) {
                        PathFinder pf = new PathFinder(tank.getX(), tank.getY(), tank.getDirection(), opponent.getX(), opponent.getY(), map);
                        ArrayList<Cell> path = pf.findPath();
                        if (path != null) {
                            Cell lastCell = (Cell) path.get(path.size() - 1);
                            //System.out.println("Just other opponents added");
                            int timeCost = lastCell.getG_cost();
                            validGameObArr.add(opponent);
                            timeCosts.add((float) timeCost);
                            aStarPaths.add(path);
                        }
                    }

                }
            }
        }

    }

    /*
    * Returns true if the passed opponent direction is headed towards our tank
    */
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

    /*
    * Checks and returns true if any tank has shot while aimed at us
    */
    private boolean bulletIncoming() {
        boolean incoming = false;
        for (Tank opponent : opponents) {
            if (opponent != null && isClearlyAlignedOpponent(opponent) && isAimedAtMe(opponent) && opponent.getIsShot() == 1) {
                incoming = true;
            }
        }
        return incoming;
    }

    /*
    * The min interface of the AI to the game client
    * Processes the current game state and calculates and returns the best possible move that should be taken next
    */
    public String getNextMove() {
        if (bulletIncoming()) { //If any bullet is heading our way, change state to the dodging state
            dodgingBullet = true;
            System.out.println("bullet incoming");
        }
        if (validGameObArr != null && validGameObArr.size() != 0) {
            if (dodgingBullet) { //If tank is in dodging state
                System.out.println("in dodging state");
                
                if(tank.getCellOccupiedCount() > 0){ //If the tank is trapped and cannot dodge, just shoot
                    return "SHOOT";
                }
                
                if (!dodgeHorizontal && !dodgeVertical) { //Execute this if-else code block only the first time after changing state to Dodging
                    if (tank.getDirection() == 0 || tank.getDirection() == 2) { //If we were headed North or South
                        dodgeVertical = true;
                        dodgeHorizontal = false;

                        int posX = tank.getX();
                        int posY = tank.getY();
                        int l = posX - 1;
                        int r = posX + 1;
                        GameObject[][] gameObArr = map.getMap();

                        boolean decL = true;

                        //Find the closest free cell that we can move to avoid being hit
                        while (l > 0 || r < (MAP_SIZE - 1)) {
                            if (decL) {
                                if (l > 0 && !(gameObArr[l][posY] == null || gameObArr[l][posY].toString().equalsIgnoreCase("CoinPile") || gameObArr[l][posY].toString().equalsIgnoreCase("LifePack"))) {
                                    l--;
                                } else {
                                    if (l <= 0) {
                                        decL = !decL;
                                        continue;
                                    } else {
                                        r = MAP_SIZE;
                                        break;
                                    }
                                }
                            } else {
                                if (r < (MAP_SIZE - 1) && !(gameObArr[r][posY] == null || gameObArr[r][posY].toString().equalsIgnoreCase("CoinPile") || gameObArr[r][posY].toString().equalsIgnoreCase("LifePack"))) {
                                    r++;
                                } else {
                                    if (r >= (MAP_SIZE - 1)) {
                                        decL = !decL;
                                        continue;
                                    } else {
                                        l = -1;
                                        break;
                                    }
                                }
                            }
                            decL = !decL;

                        }
                        targetY = posY;

                        if (l < 0) {
                            targetX = r;
                        } else if (r >= MAP_SIZE) {
                            targetX = l;
                        } else {
                            if (posX - l <= r - posX) {
                                targetX = l;
                            } else {
                                targetX = r;
                            }
                        }

                    } else {
                        dodgeVertical = false;
                        dodgeHorizontal = true;
                        int posX = tank.getX();
                        int posY = tank.getY();
                        int l = posY + 1;
                        int r = posY - 1;
                        GameObject[][] gameObArr = map.getMap();
                        
                        
                        boolean decR = true;

                        while (r > 0 || l < (MAP_SIZE - 1)) {
                            if (decR) {
                                if (r > 0 && !(gameObArr[posX][r] == null || gameObArr[posX][r].toString().equalsIgnoreCase("CoinPile") || gameObArr[posX][r].toString().equalsIgnoreCase("LifePack"))) {
                                    r--;
                                } else {
                                    if (r <= 0) {
                                        decR = !decR;
                                        continue;
                                    } else {
                                        l = MAP_SIZE;
                                        break;
                                    }
                                }
                            } else {
                                if (l < (MAP_SIZE - 1) && !(gameObArr[posX][l] == null || gameObArr[posX][l].toString().equalsIgnoreCase("CoinPile") || gameObArr[posX][l].toString().equalsIgnoreCase("LifePack"))) {
                                    l++;
                                } else {
                                    if (l >= (MAP_SIZE - 1)) {
                                        decR = !decR;
                                        continue;
                                    } else {
                                        r = -1;
                                        break;
                                    }
                                }
                            }
                            decR = !decR;

                        }
                        targetX = posX;

                        if (r < 0) {
                            targetY = l;
                        } else if (l >= MAP_SIZE) {
                            targetY = r;
                        } else {
                            if (l - posY <= posY - r) {
                                targetY = l;
                            } else {
                                targetY = r;
                            }
                        }
                        
                    }
                }

                //Find the path to the predetermined cell
                PathFinder pf = new PathFinder(tank.getX(), tank.getY(), tank.getDirection(), targetX, targetY, map);
                ArrayList<Cell> path = pf.findPath();
                ArrayList<Cell> actualPath = getActualPath(path);
                
                if (tank.getX() != targetX || tank.getY() != targetY) {
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
                            System.out.println("ERROR:This next move cell is the current cell itself!");
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
                        System.out.println("ERROR:Target direction is -1");

                    }
                }
                
                if (tank.getX() == targetX && tank.getY() == targetY) { //dodging is complete
                    System.out.println("dodging completed successfully");
                    dodgingBullet = false;
                    dodgeHorizontal = false;
                    dodgeVertical = false;
                }

            } else {
                System.out.println("Not in dodging state!");

                //this loop checks if any tank is obstructing our path and shoots it untill it leaves our path or get destroyed
                for (GameObject ob : validGameObArr) {
                    if (ob.toString().startsWith("P")) {
                        switch (tank.getDirection()) {
                            case 0:
                                if (ob.getY() == (tank.getY() - 1) && ob.getX() == tank.getX()) {
                                    return "SHOOT";
                                }
                                break;
                            case 1:
                                if (ob.getX() == (tank.getX() + 1) && ob.getY() == tank.getY()) {
                                    return "SHOOT";
                                }
                                break;
                            case 2:
                                if (ob.getY() == (tank.getY() + 1) && ob.getX() == tank.getX()) {
                                    return "SHOOT";
                                }
                                break;
                            case 3:
                                if (ob.getX() == (tank.getX() - 1) && ob.getY() == tank.getY()) {
                                    return "SHOOT";
                                }
                                break;

                        }
                    }
                }

                //Array to store the scores evaluated for each possible option
                float gameObScores[] = new float[validGameObArr.size()];

                //calculate the score values for each possible option taking in to account the preset weights for each type of option
                for (int i = 0; i < validGameObArr.size(); i++) {
                    GameObject ob = validGameObArr.get(i);
                    String type = ob.toString();
                    float coeff;
                    switch (type) {
                        case "CoinPile":
                            coeff = (((CoinPile) ob).getValue() / timeCosts.get(i));
                            gameObScores[i] = coeff * COIN;
                            break;
                        case "LifePack":
                            if (tank.getHealth() <= 80) { //If the tank health is less than 80, it should go for lifepacks, hence multiplied by a large number(1000000)
                                coeff = 1 / (tank.getHealth() * timeCosts.get(i));
                                gameObScores[i] = coeff * LIFE * 1000000;
                                break;
                            } else {
                                coeff = 1 / (tank.getHealth() * timeCosts.get(i));
                                gameObScores[i] = coeff * LIFE * 0.000001f;
                                break;
                            }
                        default: //Scores to go after and shoot the opponent that would benefit us the most
                            if (aStarPaths.get(i) != null) {
                                gameObScores[i] = ((SHOOT / 100000) * (((Tank) ob).getScore()) + 1) / (((Tank) ob).getHealth() * timeCosts.get(i));
                            } else {
                                gameObScores[i] = (SHOOT * (((Tank) ob).getScore()) + 1) / (((Tank) ob).getHealth() * timeCosts.get(i));
                            }
                            break;
                    }
                }

                //Finding the best possible next move from the highest score
                float max = 0;
                int maxIdx = 0;
                for (int i = 0; i < gameObScores.length; i++) {
                    float f = gameObScores[i];
                    if (f >= max) {
                        max = f;
                        maxIdx = i;
                    }
                }

                if (validGameObArr.get(maxIdx).toString().startsWith("P")) { //If a tank

                    if (aStarPaths.get(maxIdx) != null) {
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
                                    System.out.println("ERROR:this next move is the current cell itself!");
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
                                System.out.println("ERROR:target direction is -1!");
                            }
                        }
                    } else {
                        return "SHOOT";
                    }

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
                                System.out.println("ERROR:this next move is the current cell itself!");
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
                            System.out.println("ERROR:target direction is -1!");

                        }
                    }
                }
            }

        } else {
            System.out.println("validGameObArr is null");
        }

        return "ERROR";
    }

}

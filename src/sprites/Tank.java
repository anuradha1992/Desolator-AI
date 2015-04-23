/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observer;

/**
 *
 * @author Anuradha
 */
public class Tank extends LiveGameObject {

    private int score = 0;
    private int coins = 0;
    private int health = 100;
    private int rank = 0;
    private int isShot = 0;

    private static int no_of_tanks = 1;

    private int dx;
    private int dy;

    private ArrayList bullets;

    private String name;

    private static String[] imageURL;

    private boolean headingObstacle;
    private boolean headingOccupiedCell;
    private boolean dead;
    private boolean isInvalidMove;

    public Tank(String name, int x, int y, int direction) {
        super(x, y, direction, new String[]{"../Desolator/src/images/tankRedSmallResized.png", "../Desolator/src/images/tankYellowSmallResized.png", "../Desolator/src/images/tankBlueSmallResized.png", "../Desolator/src/images/tankPinkSmallResized.png", "../Desolator/src/images/tankOrangeSmallResized.png"}, no_of_tanks);
        no_of_tanks++;
        String url = "../Desolator/src/images/tankRedSmallResized.png,../Desolator/src/images/tankYellowSmallResized.png,../Desolator/src/images/tankBlueSmallResized.png,../Desolator/src/images/tankPinkSmallResized.png,../Desolator/src/images/tankOrangeSmallResized.png";
        imageURL = url.split(",");
        this.name = name;
        bullets = new ArrayList();
    }

    public static String[] getImageURL() {
        return imageURL;
    }

    public Tank(String name, int x, int y, int direction, int isShot, int health, int coins, int points) {
        super(x, y, direction, null);
        this.name = name;
        this.isShot = isShot;
        this.health = health;
        this.coins = coins;
        this.score = points;
    }

    public void setObserver(Observer o) {
        addObserver(o);
    }

    public String getName() {
        return name;
    }

    public void move() {
        x += dx;
        y += dy;

        if (x < 0) {
            x = 0;
        }

        if (x > (width_of_gamePanel - width)) {
            x = width_of_gamePanel - width;
        }

        if (y < 0) {
            y = 0;
        }

        if (y > (height_of_gamePanel - height)) {
            y = height_of_gamePanel - height;
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void execute(String action) {
        System.out.println("AI decided to go " + action);
        if (action.equals("ERROR")) {
            System.out.println("No direction Recieved ERROR");
        } else {
            setChanged();
            notifyObservers(action);
        }
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
//            fire();
            setChanged();
            notifyObservers("SHOOT");

        }

        if (key == KeyEvent.VK_LEFT) {
            System.out.println("   leeeeeeeeeeefffffffffffffffttttttttt");
            setChanged();
            notifyObservers("LEFT");
//            if (direction == 3) {
//                dx = -1;
//            } else {
//                int times = calculateRotation(direction, 3);
//                turnImage(times);
//                direction = 3;
//                //Rotate the tank image 
//            }
        }

        if (key == KeyEvent.VK_RIGHT) {
            setChanged();
            System.out.println("rightttttttttttttt in tank");
            notifyObservers("RIGHT");

//            if (direction == 1) {
//                dx = 1;
//            } else {
//                int times = calculateRotation(direction, 1);
//                turnImage(times);
//                direction = 1;
//                //Rotate the tank image 
//            }
        }

        if (key == KeyEvent.VK_UP) {
            setChanged();
            notifyObservers("UP");
//            if (direction == 0) {
//                dy = -1;
//            } else {
//                int times = calculateRotation(direction, 0);
//                turnImage(times);
//                direction = 0;
//                //Rotate the tank image 
//            }
        }

        if (key == KeyEvent.VK_DOWN) {
            setChanged();
            notifyObservers("DOWN");
//            if (direction == 2) {
////                System.out.println("asdhasbjdb");
//                dy = 1;
//            } else {
//                int times = calculateRotation(direction, 2);
//                turnImage(times);
//                direction = 2;
//                //Rotate the tank image 
//            }
        }
    }

    public void fire() {

        int x_offset = 0;
        int y_offset = 0;

        if (direction == 0) {
            x_offset = 0;
            y_offset = -1;
        } else if (direction == 1) {
            x_offset = 1;
            y_offset = 0;
        } else if (direction == 2) {
            x_offset = 0;
            y_offset = 1;
        } else if (direction == 3) {
            x_offset = -1;
            y_offset = 0;
        }

        bullets.add(new Bullet(x + x_offset, y + y_offset, direction));

    }

    public void stop() {
        dx = 0;
        dy = 0;
    }

//    public void keyReleased(KeyEvent e) {
//        int key = e.getKeyCode();
//
//        if (key == KeyEvent.VK_LEFT) {
//            dx = 0;
//        }
//
//        if (key == KeyEvent.VK_RIGHT) {
//            dx = 0;
//        }
//
//        if (key == KeyEvent.VK_UP) {
//            dy = 0;
//        }
//
//        if (key == KeyEvent.VK_DOWN) {
//            dy = 0;
//        }
//    }
    protected int calculateRotation(int direction, int dest) {
        switch (direction) {
            case 0:
                switch (dest) {
                    case 1:
                        return 3;
                    case 2:
                        return 2;
                    case 3:
                        return 1;
                }
            case 1:
                switch (dest) {
                    case 2:
                        return 3;
                    case 3:
                        return 2;
                    case 0:
                        return 1;
                }
            case 2:
                switch (dest) {
                    case 3:
                        return 3;
                    case 0:
                        return 2;
                    case 1:
                        return 1;
                }
            case 3:
                switch (dest) {
                    case 0:
                        return 3;
                    case 1:
                        return 2;
                    case 2:
                        return 1;
                }
        }
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * @param coins the coins to set
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the isShot
     */
    public int getIsShot() {
        return isShot;
    }

    /**
     * @param isShot the isShot to set
     */
    public void setIsShot(int isShot) {
        this.isShot = isShot;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setHeadingObstacle(boolean b) {
        headingObstacle = b;
    }

    public void setHeadingOccupiedCell(boolean b) {
        headingOccupiedCell = b;
    }

    public void setIsDead(boolean b) {
        dead = b;
    }

    public void setInvalidMove(boolean b) {
        isInvalidMove = b;
    }
}
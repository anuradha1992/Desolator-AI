/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import support.ImageRotate;

/**
 *
 * @author Anuradha
 */
public abstract class LiveGameObject extends GameObject {

    protected int previousDirection;
    protected int direction;
    boolean visible;
    boolean toBeRemoved;
    

    public LiveGameObject(int x, int y, int direction, String imageURL) {   //For tank
        super(x, y, imageURL);
        visible = true;
        toBeRemoved = false;
        this.direction = direction;
        previousDirection = 2;
        int times = calculateRotation(previousDirection, direction);
        if (image != null) {
            turnImage(times);
        }
    }
    
    public LiveGameObject(int x, int y, int direction, String imageURL, boolean forBullet) {   //For Bullet
        super(x, y, imageURL);
        visible = true;
        toBeRemoved = false;
        this.direction = direction;
        previousDirection = 0;
        int times = calculateRotation(previousDirection, direction);
        if (image != null) {
            turnImage(times);
        }
    }
    
    public LiveGameObject(int x, int y, int direction, String[] imageURL, int no) {
        super(x, y, imageURL[no-1]);
        visible = true;
        toBeRemoved = false;
        this.direction = direction;
        previousDirection = 2;
        int times = calculateRotation(previousDirection, direction);
        if (image != null) {
            turnImage(times);
        }
    }

//    public LiveGameObject(int x, int y, int direction) {
//        super(x, y);
//        visible = true;
//        this.direction = direction;
//        
//        int times = calculateRotation(direction, 0);
//        turnImage(times);
//    }
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public boolean isToBeRemoved() {
        return toBeRemoved;
    }

    public void setToBeRemoved(Boolean toBeRemoved) {
        this.toBeRemoved = toBeRemoved;
    }
    
    public void setImageURL(String url){
        this.imageURL = url;
        loadImage();
        previousDirection = 2;
        int times = calculateRotation(previousDirection, direction);
        if (image != null) {
            turnImage(times);
        }
    }

    protected abstract int calculateRotation(int direction, int dest);

    void turnImage(int times) {
        for (int i = 0; i < times; i++) {
            image = new ImageRotate().rotate(image);
        }
    }

    public void setDirection(int newDirection) {
        this.previousDirection = this.direction;
        this.direction = newDirection;
        if (previousDirection != newDirection) {
            int times = calculateRotation(previousDirection, newDirection);
            if (image != null) {
                turnImage(times);
            }
        }

    }

    public int getDirection() {
        return direction;
    }

    public abstract void move();

    @Override
    public String toString() {
        return "LiveGameObject";
    }

}

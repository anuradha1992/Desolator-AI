/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Anuradha
 */
public class CoinPile extends GameObject implements ActionListener {

    private int value;
    private int lifeTime;
    private int remainingLifeTime;

    private boolean acquired;
    private boolean isVisible;

    Timer timer;

    public CoinPile(int x, int y, int value, int lifeTime) {
        super(x, y, "../Desolator/src/images/Coins35small.png");
        this.value = value;
        this.lifeTime = lifeTime;
        remainingLifeTime = lifeTime;
        acquired = false;
        isVisible = true;
//        System.out.println("############################################### Coin Pile of life time : "+lifeTime+" GENERATED "+getX()+" , "+getY());
        startTimer();
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the lifeTime
     */
    public int getLifeTime() {
        return lifeTime;
    }

    /**
     * @return the remainingLifeTime
     */
    public int getRemainingLifeTime() {
        return remainingLifeTime;
    }

    /**
     * @param remainingLifeTime the remainingLifeTime to set
     */
//    public void setRemainingLifeTime(int remainingLifeTime) {
//        this.remainingLifeTime = remainingLifeTime;
//    }
//    public void decreaseRemainingLifeTime() {
//        remainingLifeTime -= 1000;
//    }
    /**
     * @return the acquired
     */
    public boolean isAcquired() {
        return acquired;
    }

    /**
     * @param acquired the acquired to set
     */
    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }

    @Override
    public String toString() {
        return "CoinPile";
    }

    private void startTimer() {
        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        remainingLifeTime -= 100;
//        if (getLifeTime() == Integer.MAX_VALUE) {
//                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ coinpile at " + getX() + getY() + " ACTION PERFORMED! "+lifeTime + " Remaining : "+remainingLifeTime);
//        }
//        System.out.println("coinpile at "+getX()+getY()+"has "+remainingLifeTime+" ms remaining to vanish");
        if (remainingLifeTime <= 0) {
            if (getLifeTime() == Integer.MAX_VALUE) {
//                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ coinpile at " + getX() + getY() + " REMOVED!");
            }
//            System.out.println("coinpile at " + getX() +" , " + getY() + " has " + remainingLifeTime + " ms remaining to vanish and should not be there now " + getLifeTime());
            setVisible(false);
            setChanged();
//            String coordinates = "CoinPile:"+this.getX() + "," + this.getY();
            String coordinates = this.getX() + "," + this.getY();
            notifyObservers(coordinates);
            timer.stop();
        }

    }

    /**
     * @return the isVisible
     */
    public boolean isIsVisible() {
        return isVisible;
    }

    /**
     * @param isVisible the isVisible to set
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

}

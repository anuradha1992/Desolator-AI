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
public class LifePack extends GameObject implements ActionListener {

    private int lifeTime;
    private int remainingLifeTime;
    private boolean visible;

    private boolean acquired;
    
    Timer timer;

    public LifePack(int x, int y, int lifeTime) {
        super(x, y, "../Desolator/src/images/LifePack35small.png");
        this.lifeTime = lifeTime;
        remainingLifeTime = lifeTime;
        acquired = false;
        visible = true;
        startTimer();
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
    public void setRemainingLifeTime(int remainingLifeTime) {
        this.remainingLifeTime = remainingLifeTime;
    }

    public void decreaseRemainingLifeTime() {
        remainingLifeTime -= 1000;
    }

    private void startTimer() {
        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        remainingLifeTime-=100;
        if (remainingLifeTime <= 0) {
            setVisible(false);
            setChanged();
//            String coordinates = "LifePack"+this.getX()+","+this.getY();
            String coordinates = this.getX()+","+this.getY();
            notifyObservers(coordinates);
            timer.stop();
        }

    }

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
        return "LifePack";
    }

    /**
     * @return the visible
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    private void setVisible(boolean visible) {
        this.visible = visible;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import sprites.Tank;

/**
 *
 * @author ruchiranga
 */
public class KeyInputAdapter extends KeyAdapter {
    Tank tank;
    
    public KeyInputAdapter(Tank tank) {
        this.tank = tank;
    }

    
    @Override
    public void keyReleased(KeyEvent e) {
        //tank.keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("sdfsdfsdf");
        tank.keyPressed(e);
    }
}

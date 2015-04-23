/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sprites;

/**
 *
 * @author Anuradha
 */
public class Water extends GameObject{
    
    public Water(int x, int y) {
        super(x, y, "../Desolator/src/images/watersmall.jpg");
    }
    
    @Override
    public String toString(){
        return "Water";
    }
    
}

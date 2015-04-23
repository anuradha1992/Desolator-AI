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
public class Stone extends GameObject{
    
    public Stone(int x, int y) {
        super(x, y, "../Desolator/src/images/stonesmall.png");
    }
    
    @Override
    public String toString(){
        return "Stone";
    }
    
}

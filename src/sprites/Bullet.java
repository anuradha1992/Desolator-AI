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
public class Bullet extends LiveGameObject {

//    private int MISSILE_SPEED = 0;

    int move = 0;

    public Bullet(int x, int y, int direction) {          
        super(x, y, direction, "../Desolator/src/images/Bullet35small.png",true);
    }

    public void move() {

        if (move == 0) {
            x += 0;
            y += 0;
//            MISSILE_SPEED = 1;
            move = 1;
        } else {
            if (direction == 0) {
                y -= 1;
            } else if (direction == 1) {
                x += 1;
            } else if (direction == 2) {
                y += 1;
            } else if (direction == 3) {
                x -= 1;
            }
        }

        if (x > width_of_gamePanel || y > height_of_gamePanel || x < 0 || y < 0) {
            visible = false;
        }
    }

    protected int calculateRotation(int direction, int dest) {
        switch (dest) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 1;
        }
        return 0;
    }
    

    @Override
    public String toString() {
        return "Bullet";
    }

}

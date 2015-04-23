/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Observable;
import javax.swing.ImageIcon;
import view.GamePanel;

/**
 *
 * @author Anuradha
 */
public abstract class GameObject extends Observable{

    protected String imageURL;

    protected int x, y;
    protected Image image;

    protected int width = GamePanel.getWidthOfGameBlock();
    protected int height = GamePanel.getHeightOfGameBlock();

    protected int width_of_gamePanel = GamePanel.getHeightOfGamePanel();
    protected int height_of_gamePanel = GamePanel.getWidthOfGamePanel();

    public GameObject(int x, int y, String imageURL) {
        this.imageURL = imageURL;
        if (imageURL != null) {
            loadImage();
        }
        this.x = x;
        this.y = y;
    }
//    public GameObject(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }

    protected void loadImage() {
        ImageIcon ii = new ImageIcon(imageURL);
        image = ii.getImage();
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x*width, y*height, width, height);
    }

    @Override
    public String toString() {
        return "GameObject";
    }

}

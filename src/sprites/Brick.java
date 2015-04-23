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
public class Brick extends GameObject{
    
    private int percentage;
    private boolean visible;

    
    public Brick(int x, int y) {
        super(x, y, "../Desolator/src/images/Bricksmall.png");
        this.percentage = 100;
        visible = true;
    }
    
    public int getPercentage(){
        return percentage;
    }
    
    public void setPercentage(int percentage){
        this.percentage = percentage;
        if(percentage == 4){
            setVisible(false);
            setChanged();
//            String coordinates = "Brick:"+this.getX()+","+this.getY();
            String coordinates = this.getX()+","+this.getY();
            notifyObservers(coordinates);
        }
    }
    
    private boolean reducePercentage(){
        if(percentage > 0){
            percentage -= 25;
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public String toString(){
        return "Brick";
    }

    /**
     * @return the isVisible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param isVisible the isVisible to set
     */
    private void setVisible(boolean isVisible) {
        this.visible = isVisible;
    }
    
}

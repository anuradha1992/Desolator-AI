/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

/**
 *
 * @author Anuradha
 */
public class Cell {
    private int x;
    private int y;
    private int direction;
    private Cell parent;
    private int g_cost;
    private int h_cost;
    private boolean InOpenList;
    private boolean InClosedList;
    private boolean invalid;

    public Cell(int x, int y, int direction, Cell parent, int g_cost, int h_cost) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.parent = parent;
        this.g_cost = g_cost;
        this.h_cost = h_cost;
        InOpenList = false;
        InClosedList = false;
        invalid = false;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the parent
     */
    public Cell getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Cell parent) {
        this.parent = parent;
    }

    /**
     * @return the g_cost
     */
    public int getG_cost() {
        return g_cost;
    }

    /**
     * @param g_cost the g_cost to set
     */
    public void setG_cost(int g_cost) {
        this.g_cost = g_cost;
    }

    /**
     * @return the h_cost
     */
    public int getH_cost() {
        return h_cost;
    }

    /**
     * @param h_cost the h_cost to set
     */
    public void setH_cost(int h_cost) {
        this.h_cost = h_cost;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @return the InOpenList
     */
    public boolean isInOpenList() {
        return InOpenList;
    }

    /**
     * @param InOpenList the InOpenList to set
     */
    public void setInOpenList(boolean InOpenList) {
        this.InOpenList = InOpenList;
    }

    /**
     * @return the InClosedList
     */
    public boolean isInClosedList() {
        return InClosedList;
    }

    /**
     * @param InClosedList the InClosedList to set
     */
    public void setInClosedList(boolean InClosedList) {
        this.InClosedList = InClosedList;
    }

    /**
     * @return the invalid
     */
    public boolean isInvalid() {
        return invalid;
    }

    /**
     * @param invalid the invalid to set
     */
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
    
    
}

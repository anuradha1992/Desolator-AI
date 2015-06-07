/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.ArrayList;
import sprites.GameObject;
import view.Map;

/**
 *
 * @author Anuradha
 */
public class PathFinder {

    int x;
    int y;
    int direction;
    int dest_x;
    int dest_y;
    Map map;
    GameObject[][] mapArray;
    Cell[][] cellMapArray;
    final int GRID_SIZE = 20;
    ArrayList<Cell> openList;
    ArrayList<Cell> closedList;
    private boolean pathCannotBeFound;

    public PathFinder(int x, int y, int direction, int dest_x, int dest_y, Map map) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.dest_x = dest_x;
        this.dest_y = dest_y;
        this.map = map;
        mapArray = map.getMap();
        initCellMap();
        closedList = new ArrayList<>();
        openList = new ArrayList<>();
        pathCannotBeFound = false;
    }

    private void initCellMap() {
        cellMapArray = new Cell[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cellMapArray[i][j] = new Cell(j, i, -1, null, -1, -1);
            }
        }
    }

    public ArrayList<Cell> findPath() {     
        Cell parent = cellMapArray[y][x];   // initialize the first parent to source cell getting x, y coordinates from input into the constructor
        parent.setDirection(direction); // set direction of the tank at the moment tank calls the A* algorithm to find path
        parent.setG_cost(0);
        parent.setH_cost(0);
        openList.add(parent);   // add initial cell to the open list
        parent.setInOpenList(true);
        addAdjacentCellsToOpenList(parent); // adding adjacent cells of the parent to the open list
        closedList.add(parent);     // add source cell into the closed list
        parent.setInClosedList(true);   
        openList.remove(parent);    // remove parent cell from the open list
        parent.setInOpenList(false);
        while (closedList.get(closedList.size() - 1) != cellMapArray[dest_y][dest_x]) {
            addAdjacentCellsToOpenList(closedList.get(closedList.size() - 1));
            addMinCostCellToClosedList();
            if(pathCannotBeFound){
                return null;    // when path cannot be found return null
            }
        }
        return closedList;  // if a valid path is there to get to destination, return the closed list
    }

    /* Explore the minimum cost cell to go next from the open list and add it to the closed list */
    private void addMinCostCellToClosedList() {
        Cell minCell = null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < openList.size(); i++) {
            Cell c = openList.get(i);
            if ((c.getG_cost() + c.getH_cost()) <= min) {
                min = c.getG_cost() + c.getH_cost();
                minCell = c;
            }
        }
        if (minCell != null) {
            openList.remove(minCell);
            minCell.setInOpenList(false);
            closedList.add(minCell);
            minCell.setInClosedList(true);
        } else {
            pathCannotBeFound = true;
        }
    }

    /* Look for free adjacent cells around the parent cell and add them to the open list */
    private void addAdjacentCellsToOpenList(Cell parent) {
        int parent_x = parent.getX();
        int parent_y = parent.getY();
        if (parent_x - 1 >= 0) {
            int i = parent_y;
            int j = parent_x - 1;
            if ( (j == dest_x && i == dest_y) || ((mapArray[j][i] == null || mapArray[j][i].toString().equalsIgnoreCase("CoinPile") || mapArray[j][i].toString().equalsIgnoreCase("LifePack")) && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);
                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
                } else {
                    if (!cellMapArray[i][j].isInvalid()) {
                        compareG_score(cellMapArray[i][j], parent);
                    }
                }
            }
        }
        if (parent_y - 1 >= 0) {
            int i = parent_y - 1;
            int j = parent_x;
            if ((j == dest_x && i == dest_y) || ((mapArray[j][i] == null || mapArray[j][i].toString().equalsIgnoreCase("CoinPile") || mapArray[j][i].toString().equalsIgnoreCase("LifePack")) && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);
                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
                } else {
                    if (!cellMapArray[i][j].isInvalid()) {
                        compareG_score(cellMapArray[i][j], parent);
                    }
                }
            }
        }
        if (parent_x + 1 < GRID_SIZE) {
            int i = parent_y;
            int j = parent_x + 1;

            if ((j == dest_x && i == dest_y) || ((mapArray[j][i] == null || mapArray[j][i].toString().equalsIgnoreCase("CoinPile") || mapArray[j][i].toString().equalsIgnoreCase("LifePack")) && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);

                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
                } else {
                    if (!cellMapArray[i][j].isInvalid()) {
                        compareG_score(cellMapArray[i][j], parent);
                    }
                }
            }
        }
        if (parent_y + 1 < GRID_SIZE) {
            int i = parent_y + 1;
            int j = parent_x;
            if ((j == dest_x && i == dest_y) || ((mapArray[j][i] == null || mapArray[j][i].toString().equalsIgnoreCase("CoinPile") || mapArray[j][i].toString().equalsIgnoreCase("LifePack")) && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);

                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
                } else {
                    if (!cellMapArray[i][j].isInvalid()) {
                        compareG_score(cellMapArray[i][j], parent);
                    }
                }
            }
        }
    }

    /* When the same cell is met in two paths explored recalculate the G cost, compare it with former G cost and assign the lower one */
    private void compareG_score(Cell cell, Cell parent) {
        int parent_x = parent.getX();
        int parent_y = parent.getY();
        int newG_cost = 0;
        int newDirection = -1;
        if (cell.getY() > parent_y) {
            if (parent.getDirection() == 2) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 1;
        } else if (cell.getY() < parent_y) {
            if (parent.getDirection() == 0) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 3;
        } else if (cell.getX() > parent_x) {
            if (parent.getDirection() == 1) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 2;
        } else if (cell.getX() < parent_x) {
            if (parent.getDirection() == 3) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 0;
        }

        if (newG_cost < cell.getG_cost()) {
            cell.setParent(parent);
            cell.setDirection(newDirection);
            cell.setG_cost(newG_cost);
        }
    }

    /* call methods to calculate G cost and H cost */
    private void checkAdjacentCell(Cell cell, Cell parent) {
        calculateG_cost_and_direction(cell, parent);
        calculateH_cost(cell);
    }

    /* calculate G cost of a cell */
    private void calculateG_cost_and_direction(Cell cell, Cell parent) {
        int parent_x = parent.getX();
        int parent_y = parent.getY();
        if (cell.getY() > parent_y) {
            if (cell.getParent().getDirection() == 1) {
                cell.setG_cost(cell.getParent().getG_cost() + 1);
            } else {
                cell.setG_cost(cell.getParent().getG_cost() + 2);
            }
            cell.setDirection(1);
        } else if (cell.getY() < parent_y) {
            if (cell.getParent().getDirection() == 3) {
                cell.setG_cost(cell.getParent().getG_cost() + 1);
            } else {
                cell.setG_cost(cell.getParent().getG_cost() + 2);
            }
            cell.setDirection(3);
        } else if (cell.getX() > parent_x) {
            if (cell.getParent().getDirection() == 2) {
                cell.setG_cost(cell.getParent().getG_cost() + 1);
            } else {
                cell.setG_cost(cell.getParent().getG_cost() + 2);
            }
            cell.setDirection(2);
        } else if (cell.getX() < parent_x) {
            if (cell.getParent().getDirection() == 0) {
                cell.setG_cost(cell.getParent().getG_cost() + 1);
            } else {
                cell.setG_cost(cell.getParent().getG_cost() + 2);
            }
            cell.setDirection(0);
        }
    }

    /* Calculate H cost of a cell */
    private void calculateH_cost(Cell cell) {
        int cost = Math.abs(cell.getX() - dest_x) + Math.abs(cell.getY() - dest_y);
        cell.setH_cost(cost);
    }

}

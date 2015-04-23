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

        Cell parent = cellMapArray[y][x];
        parent.setDirection(direction);
        parent.setG_cost(0);
        parent.setH_cost(0);

        openList.add(parent);
        parent.setInOpenList(true);

        addAdjacentCellsToOpenList(parent);

        closedList.add(parent);
        parent.setInClosedList(true);
        openList.remove(parent);
        parent.setInOpenList(false);

        while (closedList.get(closedList.size() - 1) != cellMapArray[dest_y][dest_x]) {
            addAdjacentCellsToOpenList(closedList.get(closedList.size() - 1));
            addMinCostCellToClosedList();
//            System.out.println(closedList.size());
            if(pathCannotBeFound){
                return null;
            }
        }

        return closedList;

    }

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
            minCell.setInOpenList(false);   //////////////
//            System.out.println("Removed min Cell from open list : " + minCell.getX() + " " + minCell.getY() + " " + minCell.getG_cost() + " " + minCell.getH_cost());

            closedList.add(minCell);
            minCell.setInClosedList(true);
//            System.out.println("Added min Cell to closed list : " + minCell.getX() + " " + minCell.getY() + " " + minCell.getG_cost() + " " + minCell.getH_cost());

        } else {
            pathCannotBeFound = true;
//            System.out.println("*************PATH CANT BE FOUND*************");
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addAdjacentCellsToOpenList(Cell parent) {
        int parent_x = parent.getX();
        int parent_y = parent.getY();
        if (parent_x - 1 >= 0) {
            int i = parent_y;
            int j = parent_x - 1;
            if ( (j == dest_x && i == dest_y) || (mapArray[j][i] == null && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);

                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
//                    System.out.println("Added to open list : " + j + " " + i + " " + cellMapArray[i][j].getG_cost() + " " + cellMapArray[i][j].getH_cost());
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
            if ((j == dest_x && i == dest_y) || (mapArray[j][i] == null && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);

                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
//                    System.out.println("Added to open list : " + j + " " + i + " " + cellMapArray[i][j].getG_cost() + " " + cellMapArray[i][j].getH_cost());
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

            if ((j == dest_x && i == dest_y) || (mapArray[j][i] == null && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);

                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
//                    System.out.println("Added to open list : " + j + " " + i + " " + cellMapArray[i][j].getG_cost() + " " + cellMapArray[i][j].getH_cost());
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
            if ((j == dest_x && i == dest_y) || (mapArray[j][i] == null && !cellMapArray[i][j].isInClosedList())) {
                if (!cellMapArray[i][j].isInOpenList()) {
                    openList.add(cellMapArray[i][j]);

                    cellMapArray[i][j].setParent(parent);
                    cellMapArray[i][j].setInOpenList(true);
                    checkAdjacentCell(cellMapArray[i][j], parent);
//                    System.out.println("Added to open list : " + j + " " + i + " " + cellMapArray[i][j].getG_cost() + " " + cellMapArray[i][j].getH_cost());
                } else {
                    if (!cellMapArray[i][j].isInvalid()) {
                        compareG_score(cellMapArray[i][j], parent);
                    }
                }
            }
        }
    }

    private void compareG_score(Cell cell, Cell parent) {
        int parent_x = parent.getX();
        int parent_y = parent.getY();
        int newG_cost = 0;
        int newDirection = -1;
        if (cell.getY() > parent_y) {
            if (parent.getDirection() == 1) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 1;
        } else if (cell.getY() < parent_y) {
            if (parent.getDirection() == 3) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 3;
        } else if (cell.getX() > parent_x) {
            if (parent.getDirection() == 2) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 2;
        } else if (cell.getX() < parent_x) {
            if (parent.getDirection() == 0) {
                newG_cost = parent.getG_cost() + 1;
            } else {
                newG_cost = parent.getG_cost() + 2;
            }
            newDirection = 0;
        }

//        System.out.println("Compare G_score of : " + cell.getX() + " " + cell.getY() + " " + cell.getG_cost() + " " + cell.getH_cost() + " New G cost = " + newG_cost);

        if (newG_cost < cell.getG_cost()) {
            cell.setParent(parent);
            cell.setDirection(newDirection);
            cell.setG_cost(newG_cost);
        }
    }

    private void checkAdjacentCell(Cell cell, Cell parent) {
        calculateG_cost_and_direction(cell, parent);
        calculateH_cost(cell);
    }

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

    private void calculateH_cost(Cell cell) {
        int cost = Math.abs(cell.getX() - dest_x) + Math.abs(cell.getY() - dest_y);
        cell.setH_cost(cost);
    }

}

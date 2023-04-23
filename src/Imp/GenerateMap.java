package Imp;

import Cells.*;
import Imp.Support.Node;
import Imp.Support.Support;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Map;

/**
 * This class is used to generate the map.
 * It can generate random mazes or files full of specified cell.
 */
public class GenerateMap {

    private final GridBagConstraints g = new GridBagConstraints();
    private final JPanel jPanel;
    private Map<Map<Integer, Integer>, Cell> map, backup;
    private Map<Integer, Integer> start, finish;
    private Node[][] nodes;
    private int x,y;
    private final Support support = new Support();

    /**
     * Constructor
     * @param map The map.
     * @param x size x.
     * @param y size y.
     * @param start start location.
     * @param finish finish location.
     * @param jPanel the map panel to which the map will be added.
     */
    public GenerateMap(Map<Map<Integer, Integer>, Cell> map, int x, int y,
                       Map<Integer, Integer> start, Map<Integer, Integer> finish, JPanel jPanel){
        this.map = map;
        this.x = x;
        this.y = y;
        this.start = start;
        this.finish = finish;
        this.jPanel = jPanel;
        this.nodes = new Node[x][y];
        this.backup = this.support.deepCopyMap(this.map);
    }

    /**
     * This function takes in the map and constructs the field onto the panel.
     * If start/stop not provided then start will always be at the top and finish will always be at the bottom.
     */
    public void constructField() {
        removeOldMap();
        Map<Map<Integer, Integer>, Cell> cellMap = this.support.deepCopyMap(this.backup);

        for (Map.Entry<Map<Integer, Integer>, Cell> e : cellMap.entrySet()) {
            Map<Integer, Integer> m = e.getKey();
            Cell c = e.getValue();
            int i = m.keySet().iterator().next();
            this.g.fill = GridBagConstraints.HORIZONTAL;
            this.g.gridx = i;
            this.g.gridy = m.get(i);

            constructFieldDecisionMaker(m, cellMap, c, i);
        }

        this.map = cellMap;
        this.jPanel.revalidate();
    }

    /**
     * This function generates a map.
     * If Cell was not provided it will generate a random maze.
     * If Cell was provided it will generate a map filled with a copy of the Cell's class.
     * @param cell the cell to construct the map out of.
     */
    public void generateRandomMaze(Cell... cell){
        this.start = null;
        this.finish = null;

        if(cell.length == 0){
            generateMapOfCell(new Wall(0,0));
            depthFirstSearch();
        } else {
            generateMapOfCell(cell[0]);
        }

        this.backup = this.support.deepCopyMap(this.map);
    }

    /**
     * This function if cell should be start or finish.
     * If start/stop not provided then start will always be at the top and finish will always be at the bottom.
     * Otherwise, they will be at their provided coords.
     */
    private void constructFieldDecisionMaker(Map<Integer, Integer> m, Map<Map<Integer, Integer>, Cell> cellMap, Cell c,int i){
        if(m.get(i) == 0 && this.start == null ){
            cellMap.replace(Collections.singletonMap(i,m.get(i)),new Start(i, m.get(i)));
            this.jPanel.add(new Start(i, m.get(i)), this.g);
        } else if(m.get(i) == this.y-1 && this.finish == null){
            cellMap.replace(Collections.singletonMap(i,m.get(i)),new Finish(i, m.get(i)));
            this.jPanel.add(new Finish(i, m.get(i)), this.g);
        } else {
            this.jPanel.add(c, this.g);
        }

        if(this.start != null){
            cellMap.put(new Start(this.start).gridLocation(), new Start(this.start));
            this.jPanel.add(new Start(i, m.get(i)), this.g);
        }
        if(this.finish != null){
            cellMap.put(new Finish(this.finish).gridLocation(), new Finish(this.finish));
            this.jPanel.add(new Finish(i, m.get(i)), this.g);
        }
    }

    /**
     * Clears the panel.
     */
    private void removeOldMap(){
        for(Component c : this.jPanel.getComponents()){
            if(c instanceof Cell){
                this.jPanel.remove(c);
                this.jPanel.revalidate();
            }
        }
    }

    /**
     * Constructs a map full of the provided cell.
     * @param cell Cell to make a map out of.
     */
    private void generateMapOfCell(Cell cell){
        this.map.clear();
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                Cell c = this.support.copyCellClassIntoNewCell(cell, i, j);
                this.map.put(c.gridLocation(), c);
                this.nodes[i][j] = new Node(i,j);
            }
        }
        addNodeNeighbors();
    }

    /**
     * Finds and adds neighbours to each node.
     * Used for random maze generation.
     */
    private void addNodeNeighbors(){
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                if(i > 0){
                    this.nodes[i][j].addNeighbor(this.nodes[i-1][j]);
                }
                if(j < this.y-1){
                    this.nodes[i][j].addNeighbor(this.nodes[i][j+1]);
                }
                if(i < this.x-1){
                    this.nodes[i][j].addNeighbor(this.nodes[i+1][j]);
                }
                if(j > 0){
                    this.nodes[i][j].addNeighbor(this.nodes[i][j-1]);
                }
            }
        }
    }

    /**
     * Depth first search algorithm used for random maze generation.
     */
    private void depthFirstSearch(){
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                Node c = this.nodes[i][j];
                if(!c.isVisited()){ search(c); }
            }
        }
    }

    /**
     * Picks a random neighbor, removes a random wall and repeats.
     * @param node
     */
    private void search(Node node){
        node.setVisited();

        while(node.hasUnvisitedNeighbors()) {
            Node randNeighbor = node.pickRandomNeighbor();

            remoteWall(node, randNeighbor);

            search(randNeighbor);
        }
    }

    /**
     * Removes a wall to a neighbor.
     * @param current
     * @param next
     */
    private void remoteWall(Node current, Node next){
        int xDiff = current.getX() - next.getX();
        int yDiff = current.getY() - next.getY();

        if(xDiff == -1){
            Floor f = new Floor(current.getX()-1, current.getY());
            this.map.replace(Collections.singletonMap(current.getX()-1, current.getY()), f);
        }else if (xDiff == 1){
            Floor f = new Floor(current.getX()+1, current.getY());
            this.map.replace(Collections.singletonMap(current.getX()+1, current.getY()), f);
        }

        if(yDiff == -1){
            Floor f = new Floor(current.getX(), current.getY()-1);
            this.map.replace(Collections.singletonMap(current.getX(), current.getY()-1), f);
        }else if (yDiff == 1){
            Floor f = new Floor(current.getX()+1, current.getY()+1);
            this.map.replace(Collections.singletonMap(current.getX(), current.getY()+1), f);
        }
    }

    /**
     * Sets the back up map.
     */
    public void configBackUp(){
        this.backup = this.support.deepCopyMap(this.map);
    }

    public void setStart(Map<Integer, Integer> map){
        this.start = map;
    }

    public void setFinish(Map<Integer, Integer> map){
        this.finish = map;
    }

    public void setX(int x){
        this.x = x;
        this.nodes = new Node[x][y];
    }

    public void setY(int y){
        this.y = y;
        this.nodes = new Node[x][y];
    }

    public Map<Map<Integer, Integer>, Cell> getMap(){
        return this.map;
    }

    public void setMap(Map<Map<Integer, Integer>, Cell> map){
        this.map = map;
    }
}
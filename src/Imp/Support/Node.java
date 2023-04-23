package Imp.Support;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is a Node class. Nodes are used to traverse the map, constructing mazes and finding paths.
 */
public class Node {

    private final ArrayList<Node> neighbors = new ArrayList<>();
    private final Random random = new Random();
    private boolean visited;
    private Node parent;
    private double g, h;
    private int x,y;
    /////
    private double cost;

    /**
     * Constructs a Node
     * @param parent the previous Node
     * @param x coordinate x
     * @param y coordinate y
     * @param g  the movement cost to the next cell.
     * @param h estimated movement cost to move from the start to finish.
     */
    public Node(Node parent, int x, int y, double g, double h) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
    }

    /**
     *  Constructs a Node
     * @param x coordinate x
     * @param y coordinate y
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *  Constructs a Node
     * @param parent the previous Support.Node
     * @param x coordinate x
     * @param y coordinate y
     */
    public Node(Node parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    public boolean hasUnvisitedNeighbors(){
        for(Node n : this.neighbors){
            if(!n.isVisited()){ return true;}
        }

        return false;
    }

    public Node pickRandomNeighbor(){
        Node n = this.neighbors.get(this.random.nextInt(this.neighbors.size()));
        while (n.isVisited()) {
            this.neighbors.remove(n);
            n = neighbors.get(this.random.nextInt(this.neighbors.size()));
        }
        n.setVisited();
        this.neighbors.remove(n);
        return n;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get g value, the movement cost to the next cell.
     * @return g value
     */
    public double getG() {
        return g;
    }

    /**
     * Get h value, the estimated movement cost to move from the start to finish.
     * @return h value
     */
    public double getH() {
        return h;
    }

    /**
     * Set g value, the movement cost to the next cell.
     * @param g cost
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * Set h value, the estimated movement cost to move from the start to finish.
     * @param h cost
     */
    public void setH(double h) {
        this.h = h;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setVisited(){
        this.visited = true;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public boolean isVisited() {
        return visited;
    }

}

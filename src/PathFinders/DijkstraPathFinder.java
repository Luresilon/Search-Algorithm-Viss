package PathFinders;

import Imp.Support.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Constructs a pathfinder using Dijkstra's Algorithm.
 */
public class DijkstraPathFinder extends PathFinder {
    private static final String NAME = "Dijkstra's Algorithm";

    public DijkstraPathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
                              Map<String, Component> componentMap, int sizeX, int sizeY) {
        super(jPanel, start, finish, componentMap, sizeX, sizeY);
    }

    /**
     * Expands upon each active node until a path is found.
     * Finish if no path found.
     *
     * @throws InterruptedException this stops the thread.
     */
    @Override
    protected void search() throws InterruptedException {
        while (!checkIfFinished()) {
            if (this.noPath) {
                break;
            } else if (this.activeNodes.isEmpty() && this.start == null) {
                setCurrent();
            } else if (this.activeNodes.isEmpty()) {
                break;
            }

            // Checks and expands into all active nodes.
            Thread.sleep(10);
            this.iterator = this.activeNodes.listIterator();
//            Node nodeWithLowestCost = null;
            double lowestCost = Double.MAX_VALUE;
            while (iterator.hasNext()) {
                Node n = iterator.next();
                this.current = n;
                iterator.remove();
                this.visitedNodes.add(n);
                addNeighborsToFreeCellList(iterator);

                // Check if a better path has been found to any neighbor
                for (Node neighbor : this.visitedNodes) {
                    double tentativeCost = this.current.getCost() + getDistanceBetweenNodes(this.current, neighbor);
                    if (tentativeCost < neighbor.getCost()) {
                        neighbor.setCost(tentativeCost);
                        neighbor.setParent(this.current);
                    }
                }

                // Select the node with the lowest cost to expand next
                if (n.getCost() < lowestCost) {
//                    nodeWithLowestCost = n;
                    lowestCost = n.getCost();
                }
            }

            // Finish if there is no path from the start to the end
//            if (nodeWithLowestCost == null) {
//                this.noPath = true;
//                break;
//            }

//            this.current = nodeWithLowestCost;
        }
    }

    /**
     * Calculates the distance between two nodes.
     */
    private double getDistanceBetweenNodes(Node a, Node b) {
        double dx = Math.abs(a.getX() - b.getX());
        double dy = Math.abs(a.getY() - b.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
}

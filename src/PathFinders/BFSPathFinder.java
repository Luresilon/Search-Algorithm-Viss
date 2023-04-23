package PathFinders;

import Cells.Cell;
import Imp.Support.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BFSPathFinder extends PathFinder {
    private static final String NAME = "Breadth-First Search";

    public BFSPathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
                         Map<String, Component> componentMap, int sizeX, int sizeY) {
        super(jPanel, start, finish, componentMap, sizeX, sizeY);
    }
    @Override
    protected void search() throws InterruptedException {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visitedNodesSet = new HashSet<>(visitedNodes);
        queue.add(current);

        while (!queue.isEmpty() && !checkIfFinished()) {
            current = queue.poll();
            visitedNodesSet.add(current);

            // Expand neighbors
            addNeighborsToFreeCellList(iterator);

            // Iterate through active nodes, add unvisited nodes to the queue
            iterator = activeNodes.listIterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                if (!visitedNodesSet.contains(n)) {
                    queue.add(n);
                    visitedNodesSet.add(n);
                }
            }

            // Introduce a small delay for visualization
            Thread.sleep(10);
        }
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
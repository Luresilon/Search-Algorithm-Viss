package PathFinders;

import Imp.Support.Node;
import Cells.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Constructs a basic pathfinder which expands in every direction.
 */
public class BFS2 extends PathFinder {

    private static final String NAME = "Breadth-First Search";

    public BFS2(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
                              Map<String, Component> componentMap, int sizeX, int sizeY) {
        super(jPanel, start, finish, componentMap, sizeX, sizeY);
    }

    @Override
    protected void search() throws InterruptedException {
        while (!checkIfFinished()) {
            if (this.noPath) {
                break;
            } else if (this.activeNodes.isEmpty()) {
                setCurrent();
            }

            Thread.sleep(10);
            this.iterator = this.activeNodes.listIterator();
            while (iterator.hasNext()) {
                Node n = iterator.next();
                this.current = n;
                iterator.remove();
                this.visitedNodes.add(n);
                addNeighborsToFreeCellList(iterator);
            }
        }
    }

    @Override
    protected void addNeighborsToFreeCellList(ListIterator<Node> it) {
        Node node;
        int x, y;

        for (int[] direction : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
            x = this.current.getX() + direction[0];
            y = this.current.getY() + direction[1];

            if (x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) {
                continue;
            }

            node = new Node(this.current, x, y);
            Cell c = this.map.get(Collections.singletonMap(x, y));
            if ((c.accessCheck() || c.finished()) && !findNeighbourInList(this.activeNodes, node) && !findNeighbourInList(this.visitedNodes, node)) {
                move(x, y);
                it.add(node);

                if (c.finished()) {
                    this.done = true;
                    return;
                }
            }
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

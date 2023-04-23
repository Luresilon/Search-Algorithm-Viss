package PathFinders;

import Imp.Support.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Constructs a basic pathfinder which uses depth-first search.
 */
public class DFSPathFinder extends PathFinder {

    private static final String NAME = "Depth-First Search";

    public DFSPathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
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
            } else if (this.activeNodes.isEmpty()) {
                setCurrent();
            }

            Thread.sleep(10);
            this.iterator = this.activeNodes.listIterator();
            if (this.iterator.hasNext()) {
                Node n = this.iterator.next();
                this.current = n;
                this.iterator.remove();
                this.visitedNodes.add(n);
                addNeighborsToFreeCellList(this.activeNodes.listIterator());
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

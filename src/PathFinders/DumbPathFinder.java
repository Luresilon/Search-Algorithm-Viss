package PathFinders;

import Imp.Support.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Constructs a basic pathfinder which expands in every direction.
 */
public class DumbPathFinder extends PathFinder{
    private static final String NAME = "dumb";

    public DumbPathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
                          Map<String, Component> componentMap, int sizeX, int sizeY) {
        super(jPanel, start, finish, componentMap, sizeX, sizeY);
    }

    /**
     * Expands upon each active node until a path is found.
     * Finish if no path found.
     * @throws InterruptedException this stops the thread.
     */
    @Override
    protected void search() throws InterruptedException {
        while (!checkIfFinished()){

            if (this.noPath){
                break;
            } else if(this.activeNodes.isEmpty() && this.start == null) {
                setCurrent();
            } else if (this.activeNodes.isEmpty()) {
                break;
            }

            //Checks and expands into all active nodes.
            Thread.sleep(10);
            this.iterator = this.activeNodes.listIterator();
            while(iterator.hasNext()){
                Node n = iterator.next();
                this.current = n;
                iterator.remove();
                this.visitedNodes.add(n);
                addNeighborsToFreeCellList(iterator);
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

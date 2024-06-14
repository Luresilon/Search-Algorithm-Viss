package PathFinders;

import Cells.*;
import Imp.Support.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * An abstract class for pathfinders.
 */
public abstract class PathFinder {

    protected final String NAME = "pathFinder";
    protected final Map<String,Component> componentMap;
    protected final List<Node> activeNodes = new ArrayList<>(), visitedNodes = new ArrayList<>();
    protected final GridBagConstraints g = new GridBagConstraints();
    protected final JPanel jPanel;
    protected ListIterator<Node> iterator = this.activeNodes.listIterator();
    protected Map<Map<Integer, Integer>, Cell> map;
    protected Map<Integer,Integer> start, finish;
    protected int totalExploredCells = 0;
    protected int sizeX, sizeY, i = 0;
    protected boolean done, noPath;
    protected Node current;

    public PathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
                      Map<String, Component> componentMap, int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.jPanel = jPanel;
        this.start = start;
        this.finish = finish;
        this.componentMap = componentMap;
    }

    /**
     * The run function of this class.
     * Used inside a thread to run the pathfinder.
     * If start is not set then start from the top of the map.
     * Will run along the top of the map until one of the start locations finds a finish cell or no path found.
     * @param map
     * @throws InterruptedException this stops the thread.
     */
    public void run(Map<Map<Integer, Integer>, Cell> map) throws InterruptedException {
        updateMap(map);

        if(this.start != null){
            Map.Entry<Integer, Integer> e = this.start.entrySet().iterator().next();
            this.current = new Node(e.getKey(),e.getValue());
        } else {
            this.current = new Node(0,0);
        }

        while (!this.done) {
            try{
                findPath();
            } catch (InterruptedException e){
                clear();
                throw new InterruptedException();
            }
        }
        this.done = false;
        throw new InterruptedException();
    }

    /**
     * Runs the search for a path.
     * Once search finishes finding the path, this will map the found path.
     * @throws InterruptedException this stops the thread.
     */
    protected void findPath() throws InterruptedException {
        this.visitedNodes.add(this.current);

        addNeighborsToFreeCellList(iterator);

        try {
            search();
        }catch (InterruptedException e){
            clear();
            throw e;
        }

        mapPath();

        clear();

        throw new InterruptedException();
    }

    /**
     * Intended for overriding. This decides how next nodes are found to move onto.
     * @throws InterruptedException this stops the thread.
     */
    protected void search() throws InterruptedException { }


    /**
     * This function decides where the found path should be traversed from.
     * If no start/finish set, it will go from top to bottom.
     * Otherwise, from given start to finish.
     */
    protected void mapPath() throws InterruptedException {
        if(this.finish != null){
            Map.Entry<Integer, Integer> e = this.finish.entrySet().iterator().next();

            this.current = this.activeNodes.stream()
                    .filter((n) -> (n.getX() == e.getKey() && n.getY() == e.getValue()))
                    .findAny()
                    .get();

            if(this.start != null){
                Map.Entry<Integer, Integer> s = this.start.entrySet().iterator().next();
                traverse(s.getKey(),s.getValue());
            } else {
                traverse(-1,1);
            }
        }
        else if(this.activeNodes.stream().anyMatch((n) -> n.getY() == this.sizeY-1)) {
            this.current = this.activeNodes.stream()
                    .filter((n) -> n.getY() == this.sizeY - 1)
                    .findAny()
                    .get();
            traverse(-1,1);
        }
    }

    /**
     * Function that traverses the found path and displays it.
     */
    void traverse(int x, int y) throws InterruptedException {
        int num = 0;
        while(this.current.getY() != y || this.current.getX() != x){
            this.current = this.current.getParent();
            this.g.gridx = this.current.getX();
            this.g.gridy = this.current.getY();
            this.jPanel.add(new Path(this.current.getX(),this.current.getY()),this.g);
            this.jPanel.revalidate();

            Thread.sleep(50);
            num++;
            System.out.println("Traversing: " + this.current.getX() + ", " + this.current.getY());
            if(this.current.getY() == y && x == -1){ break; }
        }
        System.out.println(num);
        System.out.println("Total explored cells: " + totalExploredCells); // Print total number of explored cells
    }

    /**
     * Clears the lists and resets the variables.
     * Makes the algorithm ready for another run.
     */
    protected void clear(){
        this.visitedNodes.clear();
        this.activeNodes.clear();
        this.iterator = this.activeNodes.listIterator();
        this.current = new Node(0,0);
        this.noPath = false;
        this.done = false;
        this.i = 0;
    }

    /**
     * Sets the current node.
     * Traverses the start nodes until it can find a path or end trying.
     */
    protected void setCurrent(){
        while(this.i < this.sizeX){
            if(this.map.get(Collections.singletonMap(this.i, 1)).accessCheck()
                    && !findNeighbourInList(this.visitedNodes, new Node(this.i, 1))){

                addNeighborsToFreeCellList(this.iterator);
                move(this.i,1);
                this.current = new Node(this.i,1);
                this.i++;
                return;
            }
            this.i++;
        }
        this.noPath = true;
        this.i=0;
    }

    /**
     * Checks if the neighbours can be traversed.
     * Adds them to a list of free cells if the cells are available.
     */
    protected void addNeighborsToFreeCellList(ListIterator<Node> it){

        Node node;

        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                if(x != 0 && y != 0) {
                    continue;
                }
                node = new Node(this.current, this.current.getX() + x, this.current.getY() + y);

                if((x != 0 || y != 0)
                        && this.current.getX() + x >= 0 && this.current.getX() + x < this.sizeX
                        && this.current.getY() + y >= 0 && this.current.getY() + y < this.sizeY
                        && (this.map.get(Collections.singletonMap(this.current.getX()+x, this.current.getY()+y)).accessCheck()
                        ||this.map.get(Collections.singletonMap(this.current.getX()+x, this.current.getY()+y)).finished())
                        && !findNeighbourInList(this.activeNodes, node) && !findNeighbourInList(this.visitedNodes, node)) {

                    move(this.current.getX()+x, this.current.getY()+y);
                    it.add(node);
                }
            }
        }
    }

    /**
     * Sets the cell at (x,y) into an explored cell.
     */
    protected void move(int x, int y){
        Cell c = this.map.get(Collections.singletonMap(x,y));
//        setExplored();
        if(!c.finished() && !c.started()) {
            totalExploredCells++;
            this.g.gridx=x;
            this.g.gridy=y;
            this.jPanel.add(new Explored(x,y),g);
            this.jPanel.revalidate();
        }
    }

    //Experimental Active thingy
    protected void setExplored(){
        for(Component c: this.jPanel.getComponents()){
            if(c instanceof Active && !isInActiveList(c.getX(),c.getY())){
                this.g.gridx=c.getX();
                this.g.gridy=c.getY();
                this.jPanel.add(new Active(c.getX(),c.getY()),g);
                this.jPanel.revalidate();
            }
        }
    }

    protected boolean isInActiveList(int x, int y){

        for(Node n : this.activeNodes){
            if(n.getX() == x && n.getY() == y){
                return true;
            }
        }

        return false;
    }
    /**
     * Checks if the neighbor is already in the list.
     * @param array a list of neighbours found around the node
     * @param node the node that is searched around
     * @return true if the node is in the list, false otherwise.
     */
    protected boolean findNeighbourInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.getX() == node.getX() && n.getY() == node.getY()));
    }

    /**
     * Checks if the algorithm finished running.
     * Happens when an active node is an instance of finished cell,
     * Or if the algorithm reached the end of the start line at the very top.
     * @return
     */
    protected boolean checkIfFinished() {
        if(this.finish != null){
            Map.Entry<Integer, Integer> e = this.finish.entrySet().iterator().next();
            return this.activeNodes.stream().anyMatch((n) -> (n.getX() == e.getKey() && n.getY() == e.getValue()));
        }

        return this.activeNodes.stream().anyMatch((n) -> (n.getY() == this.sizeY-1));
    }

    public void updateMap(Map<Map<Integer, Integer>, Cell> map){
        this.map = map;
    }

    public String getName(){
        return NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }

    public void setSizeX(int x){
        this.sizeX = x;
    }

    public void setSizeY(int y){
        this.sizeY = y;
    }

    public void setStart(Map<Integer, Integer> map){
        this.start = map;
    }

    public void setFinish(Map<Integer, Integer> map){
        this.finish = map;
    }
}

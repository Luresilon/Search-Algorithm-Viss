package Panels;

import PathFinders.*;
import Imp.GenerateMap;
import Imp.Timer;
import Components.VisComponents;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * A control panel which displays all the controls for this project.
 */
public class ControlPanel extends JPanel {
    private final JPanel grid = new JPanel(new GridLayout(20,1));
    private final Map<String,Component> componentMap;
    private final GenerateMap generateMap;
    private final MapPanel mapPanel;
    private final int x,y;
    public ControlPanel(int x,int y, GenerateMap generateMap, Map<String,Component> componentMap, MapPanel mapPanel){

        this.setLayout(new BorderLayout());
        this.add(this.grid, BorderLayout.CENTER);
        this.setLocation(100,100);

        this.x = x;
        this.y = y;
        this.generateMap = generateMap;
        this.componentMap = componentMap;
        this.mapPanel = mapPanel;

        construct();
    }

    public void construct(){
        PathFinder dumbPathFinder = new DumbPathFinder(this.mapPanel, null, null, this.componentMap,this.x,
                this.y);
//        PathFinder bfsPathFinder = new BFSPathFinder(this.mapPanel, null, null, this.componentMap, this.x, this.y);

        PathFinder greedyPathFinder = new GreedyPathFinder(this.mapPanel, null, null, this.componentMap, this.x, this.y);

        PathFinder BFS2 = new BFS2(this.mapPanel, null, null, this.componentMap, this.x, this.y);
        PathFinder DijkstraPathFinder = new DijkstraPathFinder(this.mapPanel, null, null, this.componentMap, this.x, this.y);
        PathFinder DFSPathFinder = new DFSPathFinder(this.mapPanel, null, null, this.componentMap, this.x, this.y);



        Imp.Timer timer = new Timer(this.grid, this.componentMap);

        VisComponents visComponents = new VisComponents(this.grid,  this.componentMap, this.x, this.y, timer,
                this.generateMap, this.mapPanel);

//        visComponents.populatePathFinderList(dumbPathFinder);
//        visComponents.populatePathFinderList(bfsPathFinder);
        visComponents.populatePathFinderList(greedyPathFinder);

        visComponents.populatePathFinderList(BFS2);
        visComponents.populatePathFinderList(DijkstraPathFinder);
        visComponents.populatePathFinderList(DFSPathFinder);

        visComponents.startButton();
        visComponents.timerLabel();
        visComponents.generateMazeButton();
        visComponents.generateEmptyMazeButton();
        visComponents.comboBox();
        visComponents.stopButton();
        visComponents.sizeInput();
        visComponents.startAndFinishInput();
        visComponents.resStartAndFinishButton();
        visComponents.editMap();
        visComponents.applyWall();
        visComponents.applyFloor();
        visComponents.stopEditing();

        visComponents.setHandlerComponentMap(this.componentMap);
    }
}

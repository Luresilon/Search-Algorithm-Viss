package Components;

import Imp.GenerateMap;
import Imp.Handler;
import Imp.Timer;
import Panels.MapPanel;
import PathFinders.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * A class that creates visual components for panel.
 */
public class VisComponents {

    private final ArrayList<PathFinder> pathFindersList = new ArrayList<>();
    private final Map<String, Component> componentMap;
    private final Handler handler;
    private final JPanel jPanel;
    private final int x, y;

    public VisComponents(JPanel jPanel, Map<String, Component> componentMap, int x, int y,
                         Timer timer, GenerateMap generateMap, MapPanel mapPanel){
        this.jPanel = jPanel;
        this.componentMap = componentMap;
        this.x = x;
        this.y = y;
        this.handler = new Handler(this.jPanel, this.x, this.y, timer, generateMap, mapPanel);
    }

    /**
     * Map size spinners.
     */
    public void sizeInput(){
        SpinnerModel spinnerModelX = new SpinnerNumberModel(this.x, 5, 40, 1);
        SpinnerModel spinnerModelY = new SpinnerNumberModel(this.y, 5, 40, 1);

        JSpinner jSpinnerX = new JSpinner(spinnerModelX);
        jSpinnerX.setBorder(BorderFactory.createTitledBorder("X Size"));

        JSpinner jSpinnerY = new JSpinner(spinnerModelY);
        jSpinnerY.setBorder(BorderFactory.createTitledBorder("Y Size"));

        jSpinnerX.addChangeListener(this.handler::sizeXChange);
        jSpinnerY.addChangeListener(this.handler::sizeYChange);

        this.componentMap.put("sizeX", jSpinnerX);
        this.componentMap.put("sizeY", jSpinnerY);

        this.jPanel.add(jSpinnerX);
        this.jPanel.add(jSpinnerY);
    }

    /**
     * Start and finish spinners
     */
    public void startAndFinishInput(){
        SpinnerNumberModel spinnerModelXS = new SpinnerNumberModel(0, 0, this.x-1, 1);
        SpinnerNumberModel spinnerModelYS = new SpinnerNumberModel(0, 0, this.y-1, 1);

        SpinnerNumberModel spinnerModelXF = new SpinnerNumberModel(0, 0, this.x-1, 1);
        SpinnerNumberModel spinnerModelYF = new SpinnerNumberModel(0, 0, this.y-1, 1);

        this.handler.setSxs(spinnerModelXS);
        this.handler.setSys(spinnerModelYS);
        this.handler.setSxf(spinnerModelXF);
        this.handler.setSyf(spinnerModelYF);

        JSpinner jSpinnerXS = new JSpinner(spinnerModelXS);
        jSpinnerXS.setBorder(BorderFactory.createTitledBorder("X Start"));

        JSpinner jSpinnerYS = new JSpinner(spinnerModelYS);
        jSpinnerYS.setBorder(BorderFactory.createTitledBorder("Y Start"));

        JSpinner jSpinnerXF = new JSpinner(spinnerModelXF);
        jSpinnerXF.setBorder(BorderFactory.createTitledBorder("X Finish"));

        JSpinner jSpinnerYF = new JSpinner(spinnerModelYF);
        jSpinnerYF.setBorder(BorderFactory.createTitledBorder("Y Finish"));

        jSpinnerXS.addChangeListener(this.handler::setXStart);
        jSpinnerYS.addChangeListener(this.handler::setYStart);

        jSpinnerXF.addChangeListener(this.handler::setXFinish);
        jSpinnerYF.addChangeListener(this.handler::setYFinish);

        this.componentMap.put("xs", jSpinnerXS);
        this.componentMap.put("ys", jSpinnerYS);
        this.componentMap.put("xf", jSpinnerXF);
        this.componentMap.put("yf", jSpinnerYF);

        this.jPanel.add(jSpinnerXS);
        this.jPanel.add(jSpinnerYS);
        this.jPanel.add(jSpinnerXF);
        this.jPanel.add(jSpinnerYF);
    }

    /**
     * Restart start and finish locations button.
     */
    public void resStartAndFinishButton(){
        JButton button = new JButton("Reset S&F");
        button.addActionListener(e -> this.handler.resetSF());

        this.componentMap.put("resetSF",button);
        this.jPanel.add(button);
    }

    /**
     * A dropdown box to select path finding algorithm.
     */
    public void comboBox(){
        JComboBox pathFinders = new JComboBox(this.pathFindersList.toArray());
        pathFinders.setSelectedIndex(-1);
        pathFinders.addActionListener(this.handler::selectPathFinder);

        this.componentMap.put("comboBox",pathFinders);
        this.jPanel.add(pathFinders);
    }

    /**
     * A function used to populate the list of path finding algorithms.
     * @param pathFinder Pathfinder to add to the list.
     */
    public void populatePathFinderList(PathFinder pathFinder){
        this.handler.populatePathFinderList(this.pathFindersList, pathFinder);
    }

    /**
     * Start button
     */
    public void startButton(){
        JButton button = new JButton("Start");
        button.addActionListener(e -> this.handler.start());

        button.setEnabled(false);
        this.componentMap.put("start",button);
        this.jPanel.add(button);
    }

    /**
     * Stop/reset button.
     */
    public void stopButton(){
        JButton button = new JButton("Stop/Reset");
        button.addActionListener(e -> {
            this.handler.stop();
        });

        this.componentMap.put("stop", button);
        this.jPanel.add(button);
    }

    /**
     * Generate random maze button.
     */
    public void generateMazeButton(){
        JButton button = new JButton("New Maze");
        button.addActionListener(e -> this.handler.newMaze());

        this.componentMap.put("newMaze", button);
        this.jPanel.add(button);
    }

    /**
     * Generate empty map button.
     */
    public void generateEmptyMazeButton(){
        JButton button = new JButton("Empty Maze");
        button.addActionListener(e -> this.handler.newEmptyMaze());

        this.componentMap.put("emptyMaze", button);
        this.jPanel.add(button);
    }

    /**
     * Timer label.
     * Used to display time taken by the pathfinder.
     */
    public void timerLabel(){
        Label label = new Label("Timer: 0");
        label.setForeground(Color.RED);
        this.componentMap.put("timer", label);

        this.jPanel.add(label);
    }

    /**
     * Edit map button.
     * Used to edit the map panel.
     */
    public void editMap(){
        JButton button = new JButton("Edit Map");
        button.addActionListener( e -> this.handler.editMap());

        this.componentMap.put("editMap", button);
        this.jPanel.add(button);
    }

    /**
     * Apply wall button.
     * Changes selected Cells into Wall cells.
     */
    public void applyWall(){
        JButton button = new JButton("Apply Wall");
        button.setEnabled(false);
        button.addActionListener( e -> this.handler.applyWall());

        this.componentMap.put("applyWall", button);
        this.jPanel.add(button);
    }

    /**
     * Apply floor button.
     * Changes selected Cells into Floor cells.
     */
    public void applyFloor(){
        JButton button = new JButton("Apply Floor");
        button.setEnabled(false);
        button.addActionListener( e -> this.handler.applyFloor());

        this.componentMap.put("applyFloor", button);
        this.jPanel.add(button);
    }

    /**
     * Stop editing button.
     * Used to stop/cancel editing.
     */
    public void stopEditing(){
        JButton button = new JButton("Stop Editing");
        button.setEnabled(false);
        button.addActionListener( e -> this.handler.cancelEditingMap());

        this.componentMap.put("cancelEditingMap", button);
        this.jPanel.add(button);
    }

    public void setHandlerComponentMap(Map<String, Component> componentMap){
        this.handler.setComponentMap(componentMap);
    }
}

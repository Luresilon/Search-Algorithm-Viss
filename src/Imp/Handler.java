package Imp;

import Cells.*;
import Imp.Support.Support;
import Panels.MapPanel;
import PathFinders.PathFinder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Handler Imp class.
 * This class is used for all the component implementation.
 */
public class Handler {

    private int x,y,startX = 0, startY = 0, finishX = 0, finishY =0;
    private boolean setStart, setFinish, setForChange, pathFinderSelected,mouseDown;
    private final Support support = new Support();
    private final GenerateMap generateMap;
    private final JPanel jPanel;
    private final Timer timer;
    private final MapPanel mapPanel;
    private SpinnerNumberModel sxs, sys, sxf, syf;
    private Map<String, Component> componentMap;
    private Thread movementT, timerT;
    private PathFinder pf;

    public Handler(JPanel jPanel, int x, int y, Timer timer,
                   GenerateMap generateMap, MapPanel mapPanel){
        this.generateMap = generateMap;
        this.jPanel = jPanel;
        this.timer = timer;
        this.x = x;
        this.y = y;
        this.mapPanel = mapPanel;
    }

    /**
     * Select a pathfinder algorithm to use.
     * @param e the choice from the combo box.
     */
    public void selectPathFinder(ActionEvent e){
        JComboBox jComboBox = (JComboBox) e.getSource();
        this.pf = (PathFinder) jComboBox.getSelectedItem();

        changeSelectedCellsBack();
        activateCells(false);

        this.pathFinderSelected = true;
        this.enableButtons();
        this.jPanel.revalidate();
    }

    /**
     * Starts the algorithm and timer.
     */
    public void start(){
        this.runThreads(this.pf);
        this.disableButtons();
    }

    /**
     * Populates the list of pathfinders.
     * @param list
     * @param pathFinder
     */
    public void populatePathFinderList(ArrayList<PathFinder> list, PathFinder pathFinder){
        list.add(pathFinder);
    }

    /**
     * Stops/Rests the algorithm/map.
     */
    public void stop(){
        if(this.movementT != null && this.movementT.isAlive()){this.movementT.interrupt();}
        removeExplored();
        this.generateMap.constructField();
        changeSelectedCellsBack();
        Label l = (Label) this.componentMap.get("timer");
        l.setText("Timer: 0");
        l.setForeground(Color.RED);
        this.enableButtons();
        this.mapPanel.revalidate();
    }

    /**
     * Creates a new random maze.
     */
    public void newMaze(){
        this.generateMap.generateRandomMaze();
        this.generateMap.constructField();
    }

    /**
     * Creates an empty map.
     */
    public void newEmptyMaze(){
        this.generateMap.generateRandomMaze(new Floor(0,0));
        this.generateMap.constructField();
    }

    /**
     * Changes map's size in the X axis.
     */
    public void sizeXChange(ChangeEvent e) {
        JSpinner jSpinner = (JSpinner)(e.getSource());
        this.x = (int) jSpinner.getValue();
        this.generateMap.setX(this.x);
        sizeChangeSupp();
    }

    /**
     * Changes map's size in the Y axis.
     */
    public void sizeYChange(ChangeEvent e) {
        JSpinner jSpinner = (JSpinner)(e.getSource());
        this.y = (int) jSpinner.getValue();
        this.generateMap.setY(this.y);
        sizeChangeSupp();
    }

    /**
     * Select the X coord for start location.
     */
    public void setXStart(ChangeEvent e) {
        JSpinner jSpinner = (JSpinner)(e.getSource());
        this.sxs.setMaximum(this.x-1);
        this.startX = (int) jSpinner.getValue();
        setSandFSupp(true);
    }

    /**
     * Select the Y coord for start location.
     */
    public void setYStart(ChangeEvent e) {
        JSpinner jSpinner = (JSpinner)(e.getSource());
        this.sys.setMaximum(this.y-1);
        this.startY = (int) jSpinner.getValue();
        setSandFSupp(true);
    }

    /**
     * Select the X coord for finish location.
     */
    public void setXFinish(ChangeEvent e) {
        JSpinner jSpinner = (JSpinner)(e.getSource());
        this.sxf.setMaximum(this.x-1);
        this.finishX = (int) jSpinner.getValue();
        setSandFSupp(false);
    }

    /**
     * Select the Y coord for finish location.
     */
    public void setYFinish(ChangeEvent e) {
        JSpinner jSpinner = (JSpinner)(e.getSource());
        this.syf.setMaximum(this.y-1);
        this.finishY = (int) jSpinner.getValue();
        setSandFSupp(false);
    }

    /**
     * Reset start and finish locations.
     */
    public void resetSF(){
        restSandFSpinners();

        this.setFinish = false;
        this.setStart = false;

        this.generateMap.setFinish(null);
        this.generateMap.setStart(null);
        this.generateMap.constructField();
        this.mapPanel.repaint();
    }

    /**
     * Enables map editing.
     */
    public void editMap(){
        activateCells(true);
        disableButtons();

        this.componentMap.get("cancelEditingMap").setEnabled(true);
        this.componentMap.get("applyWall").setEnabled(true);
        this.componentMap.get("applyFloor").setEnabled(true);
        this.componentMap.get("stop").setEnabled(false);
    }

    /**
     * Apply wall cells on selected cells.
     */
    public void applyWall(){
        convertTo(new Wall(0,0));
    }

    /**
     * Apply floor cells on selected cells.
     */
    public void applyFloor(){
        convertTo(new Floor(0,0));
    }

    /**
     * Stops/Cancels map editing.
     */
    public void cancelEditingMap(){
        activateCells(false);
        enableButtons();

        changeSelectedCellsBack();

        this.componentMap.get("cancelEditingMap").setEnabled(false);
        this.componentMap.get("applyWall").setEnabled(false);
        this.componentMap.get("applyFloor").setEnabled(false);
    }

    /**
     * Reverts any selected cells back to their original form if not applied.
     */
    private void changeSelectedCellsBack(){
        for(Component c: this.mapPanel.getComponents()){
            if(((Cell) c).isSetForChange()) {
                ( (Cell) c).action();
            }
        }
    }

    /**
     * Sets start and finish locations.
     * @param start if it is a start cell.
     */
    private void setSandFSupp(boolean start){
        if(start){
            this.generateMap.setStart(Collections.singletonMap(this.startX,this.startY));
            this.setStart = true;
        } else {
            this.generateMap.setFinish(Collections.singletonMap(this.finishX,this.finishY));
            this.setFinish = true;
        }
        this.generateMap.constructField();
        this.mapPanel.repaint();
     }

    /**
     * Change size map generation support.
     */
    private void sizeChangeSupp(){
        this.generateMap.generateRandomMaze();
        this.generateMap.constructField();
        this.mapPanel.repaint();
    }

    /**
     * Runs the given pathfinder and the timer inside threads.
     * @param pathFinder the pathfinder algorithm.
     */
    private void runThreads(PathFinder pathFinder){

        updatePathFinder(pathFinder);

        this.timerT = new Thread(() -> {
            try {
                this.timer.run();
                this.componentMap.get("timer").setForeground(Color.RED);
            } catch (Exception ex) {
                this.componentMap.get("timer").setForeground(Color.GREEN);
            }
        });
        this.movementT = new Thread(() -> {
            try {
                pathFinder.run(this.generateMap.getMap());
            } catch (Exception ex) {
                this.timerT.interrupt();
                //______________________________
//                restSandFSpinners();
//                for(int i = 0; i < 2; i++) { resetSF(); }
//                removeExplored();
                for(Component c :this.mapPanel.getComponents()) {
                    if(c instanceof Explored || c instanceof Start || c instanceof Finish){
                        this.mapPanel.remove(c);
                    }
                }
                //______________________________

            }
        });
        this.movementT.start();
        this.timerT.start();
    }

    /**
     * Update the pathfinder.
     * This is used to update the pathfinder of the start and finish locations.
     * @param pathFinder the pathfinder algorithm.
     */
    private void updatePathFinder(PathFinder pathFinder){
        pathFinder.setSizeX(this.x);
        pathFinder.setSizeY(this.y);

        if(this.setStart){ pathFinder.setStart(Collections.singletonMap(this.startX, this.startY)); }
        else {pathFinder.setStart(null);}
        if(this.setFinish){ pathFinder.setFinish(Collections.singletonMap(this.finishX, this.finishY));}
        else {pathFinder.setFinish(null);}
    }

    /**
     * Disables all unnecessary buttons when the pathfinder algorithm is running.
     */
    private void disableButtons() {
        for(Map.Entry<String,Component> e: this.componentMap.entrySet()){
            if(e.getValue() instanceof JButton && !e.getKey().equals("stop")
                    || e.getValue() instanceof JSpinner) {
                e.getValue().setEnabled(false);
            }
        }
    }

    /**
     * Enables all the necessary buttons.
     */
    private void enableButtons() {
        for(Component c: this.componentMap.values()){
            if(c instanceof JButton  || c instanceof JSpinner) { c.setEnabled(true);}
        }

        if(!pathFinderSelected) this.componentMap.get("start").setEnabled(false);
        this.componentMap.get("cancelEditingMap").setEnabled(false);
        this.componentMap.get("applyWall").setEnabled(false);
        this.componentMap.get("applyFloor").setEnabled(false);
    }

    /**
     * Restarts the start and finish spinners.
     */
    private void restSandFSpinners(){
        this.sxs.setValue(0);
        this.sys.setValue(0);
        this.sxf.setValue(0);
        this.syf.setValue(0);
    }

    /**
     * Removes any explored/path cells from the map.
     */
    private void removeExplored(){
        for(Component c :this.mapPanel.getComponents()) {
            if(c instanceof Explored || c instanceof Path){
                this.mapPanel.remove(c);
            }
        }
        this.mapPanel.repaint();
    }

    /**
     * Converts a cell into a new given cell type.
     * @param cell to convert into.
     */
    private void convertTo(Cell cell) {
        GridBagConstraints g = new GridBagConstraints();
        for(Map.Entry<Map<Integer,Integer>, Cell> m: this.generateMap.getMap().entrySet()){

            if(m.getValue().isSetForChange()){
                Map<Integer, Integer> me = m.getKey();
                int x = me.keySet().iterator().next();
                int y = me.values().iterator().next();

                g.gridx=x;
                g.gridy=y;

                Cell c = this.support.copyCellClassIntoNewCell(cell,x,y);
                this.mapPanel.remove(m.getValue());
                this.mapPanel.add(c,g);
                this.generateMap.getMap().replace(me,c);
                this.mapPanel.revalidate();
            }
        }
        this.generateMap.configBackUp();
        this.generateMap.constructField();
        this.mapPanel.repaint();
    }

    /**
     * Enables/disables all the cells
     * @param active
     */

    private void activateCells(boolean active){
        for(Component c :this.mapPanel.getComponents()) {
            if(c instanceof Cell){
                c.setEnabled(active);
            }
        }
        this.mapPanel.repaint();
    }

    public void setComponentMap(Map<String,Component> componentMap){
        this.componentMap = componentMap;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setSxs(SpinnerNumberModel s){
        this.sxs =s;
    }
    public void setSys(SpinnerNumberModel s){
        this.sys =s;
    }
    public void setSxf(SpinnerNumberModel s){
        this.sxf =s;
    }
    public void setSyf(SpinnerNumberModel s){
        this.syf =s;
    }
}

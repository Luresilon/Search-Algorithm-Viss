package Cells;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Map;
import javax.swing.*;

/**
 * This is an abstract Cell class used to create custom cells.
 * Each Cell is a block on the map that has a specific function.
 */
public abstract class Cell extends JButton {

    protected Map<Integer, Integer> pair;
    protected boolean setForChange, mouseDown;

    /**
     * Constructs an instance of a cell at coords (x,y).
     */
    public Cell(int x, int y) {
        this.pair = Collections.singletonMap(x, y);
        addActionListener(e-> action());

        setEnabled(false);
        setOpaque(true);
    }

    /**
     * Constructs an instance of a cell at coords (x,y).
     */
    public Cell(Map<Integer,Integer> map) {
        this.pair = map;
        addActionListener(e-> action());
        setEnabled(false);
        setOpaque(true);
    }

    /**
     * Action of the cell, changing colour is marked to be changed.
     * Cells marked for change can be altered.
     */
    public void action(){
        this.setForChange = !this.setForChange;
        if(this.getBackground() == Color.DARK_GRAY){
            this.setBackground(getColor());
        } else {
            this.setBackground(Color.DARK_GRAY);
        }
    }

    public boolean accessCheck() {
        return this instanceof Floor;
    }

    public boolean finished() {
        return this instanceof Finish;
    }

    public boolean started() {
        return this instanceof Start;
    }

    public boolean isActive() {
        return this instanceof Active;
    }

    public Map<Integer, Integer> gridLocation() {
        return this.pair;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(25, 25);
    }

    public boolean isSetForChange(){
        return this.setForChange;
    }

    public void setSetForChange(boolean setForChange){
        this.setForChange = setForChange;
    }

    public synchronized boolean isMouseDown(){
        return this.mouseDown;
    }

    protected Color getColor(){
        return Color.WHITE;
    }
}

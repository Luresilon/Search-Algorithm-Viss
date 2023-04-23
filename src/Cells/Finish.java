package Cells;

import javax.swing.*;
import java.awt.Color;
import java.util.Map;

/**
 * Marks finish block(s).
 * Program ends when this cell is reached.
 */
public class Finish extends Cell {

    private static final Color FLOOR_COLOUR = Color.GREEN, BORDER_COLOUR = Color.WHITE;

    public Finish(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    public Finish(Map<Integer,Integer> map) {
        super(map);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }
}

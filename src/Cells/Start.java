package Cells;

import javax.swing.*;
import java.awt.Color;
import java.util.Map;

/**
 * Marks start block(s).
 * Program starts at this block.
 */
public class Start extends Cell {

    private static final Color FLOOR_COLOUR = Color.RED, BORDER_COLOUR = Color.WHITE;

    public Start(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    public Start(Map<Integer,Integer> map) {
        super(map);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }

}

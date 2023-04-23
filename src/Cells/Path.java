package Cells;

import javax.swing.*;
import java.awt.Color;

/**
 * Marks path blocks.
 * This block is used to display a path from start to finish.
 */
public class Path extends Cell {

    private static final Color FLOOR_COLOUR = Color.ORANGE, BORDER_COLOUR = Color.WHITE;

    public Path(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }
}

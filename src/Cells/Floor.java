package Cells;

import javax.swing.*;
import java.awt.Color;

/**
 * Marks floor blocks.
 * Can be easily traversed.
 */
public class Floor extends Cell {

    private static final Color FLOOR_COLOUR = Color.LIGHT_GRAY, BORDER_COLOUR = Color.WHITE;

    public Floor(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }
}
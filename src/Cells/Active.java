package Cells;

import javax.swing.*;
import java.awt.Color;

/**
 * Marks active blocks
 */
public class Active extends Cell {

    private static final Color FLOOR_COLOUR = Color.YELLOW, BORDER_COLOUR = Color.WHITE;

    public Active(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }
}

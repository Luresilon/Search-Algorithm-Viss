package Cells;

import javax.swing.*;
import java.awt.Color;

/**
 * Marks explored blocks
 */
public class Explored extends Cell {

    private static final Color FLOOR_COLOUR = Color.BLUE, BORDER_COLOUR = Color.WHITE;

    public Explored(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }
}

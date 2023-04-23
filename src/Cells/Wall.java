package Cells;

import javax.swing.*;
import java.awt.Color;

/**
 * Marks wall blocks.
 * Cannot go through them.
 */
public class Wall extends Cell {

    private static final Color FLOOR_COLOUR = Color.BLACK, BORDER_COLOUR = Color.WHITE;

    public Wall(int x, int y) {
        super(x,y);
        setBackground(FLOOR_COLOUR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOUR));
    }

    @Override
    public Color getColor(){
        return FLOOR_COLOUR;
    }
}

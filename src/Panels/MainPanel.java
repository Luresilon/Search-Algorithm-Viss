package Panels;

import Cells.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The main Panel that combines the control panel and the map panel.
 */
public class MainPanel extends JPanel {

    private static final int X = 15, Y = 15;

    public MainPanel(){
        this.setLayout(new BorderLayout());

        Map<String, Component> componentMap = new HashMap<>();
        Map<Map<Integer, Integer>, Cell> map = new HashMap<>();
        MapPanel mapPanel = new MapPanel(X, Y, map);
        ControlPanel controlPanel = new ControlPanel(X, Y, mapPanel.getGenerateMap(), componentMap, mapPanel);

        this.add(mapPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.WEST);
    }
}

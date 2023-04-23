package Panels;

import Cells.Cell;
import Imp.GenerateMap;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * The map panel which is used to display the pathfinders.
 */
public class MapPanel extends JPanel{
    private final Map<Map<Integer, Integer>, Cell> map;
    private final int x,y;
    private GenerateMap generateMap;

    public MapPanel(int x, int y, Map<Map<Integer, Integer>, Cell> map){
        this.x=x;
        this.y=y;
        this.map = map;
        this.setLayout(new GridBagLayout());

        construct();
    }

    public void construct(){
        this.generateMap = new GenerateMap(this.map, this.x, this.y, null,null,this);
        this.generateMap.generateRandomMaze();
        this.generateMap.constructField();
    }

    public GenerateMap getGenerateMap(){
        return this.generateMap;
    }
}

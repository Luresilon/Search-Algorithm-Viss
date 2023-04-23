package Imp.Support;

import Cells.Cell;
import Cells.Finish;
import Cells.Floor;
import Cells.Start;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a support class.
 * This class is used for support functions which may be used in multiple classes.
 */
public class Support {

    /**
     * Creates a new instance of a Cell of the same class as the passed in cell.
     * @param cell Cell to copy class from.
     * @param x coord x.
     * @param y coord y.
     * @return The new cell.
     */
    public Cell copyCellClassIntoNewCell(Cell cell, int x, int y){
        try{
            Class<Cell> cellClass = (Class<Cell>) Class.forName(cell.getClass().getName());
            Constructor<Cell> cons = cellClass.getConstructor(int.class, int.class);
            return cons.newInstance(x,y);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Deep copies a given map.
     * Used to create map backups to refresh the field.
     * @param copyMap Map intended to be deep copied.
     * @return a new deep copy of a map.
     */
    public Map<Map<Integer,Integer>, Cell> deepCopyMap(Map<Map<Integer,Integer>, Cell> copyMap){
        Map<Map<Integer,Integer>, Cell> c = new HashMap<>();
        for(Map.Entry<Map<Integer,Integer>, Cell> e: copyMap.entrySet()){
            Map.Entry<Integer, Integer> m = e.getKey().entrySet().iterator().next();
            if(e.getValue() instanceof Start || e.getValue() instanceof Finish) {
                c.put(Collections.singletonMap(m.getKey(),m.getValue()), new Floor(m.getKey(), m.getValue()));
            } else {
                c.put(Collections.singletonMap(m.getKey(),m.getValue()), e.getValue());
            }
        }
        return c;
    }

}

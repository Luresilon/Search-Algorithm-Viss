package Imp;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Map;

/**
 * A timer class used to display the time taken for the algorithm to finish.
 */
public class Timer {

    private final JPanel jPanel;
    private final Map<String,Component> componentMap;

    public Timer(JPanel jPanel, Map<String, Component> componentMap){
        this.jPanel = jPanel;
        this.componentMap = componentMap;
    }

    public void run() throws InterruptedException {
        Timestamp tStart = new Timestamp(System.currentTimeMillis());
        while (true) {
            Thread.sleep(50);
            Timestamp tFinish = new Timestamp(System.currentTimeMillis());
            updateProgress(tStart, tFinish);
        }
    }

    private void updateProgress(Timestamp start, Timestamp finish) {
        SwingUtilities.invokeLater(() -> timer(start,finish));
    }

    private void timer(Timestamp start, Timestamp finish){
        Label l = (Label) this.componentMap.get("timer");

        long diff = finish.getTime() - start.getTime();

        l.setText("Timer: " + diff + "ms");
        this.jPanel.validate();
    }
}

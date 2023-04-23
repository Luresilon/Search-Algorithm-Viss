
import Panels.MainPanel;
import java.awt.*;
import javax.swing.*;

public class Main extends  JFrame{

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super("Path Finder");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.getContentPane().add(new MainPanel());

        this.setPreferredSize(new Dimension(1000,1000));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
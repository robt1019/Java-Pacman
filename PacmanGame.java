import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class PacmanGame implements Runnable{

    private JFrame window = new JFrame("Pacman");
    private JLabel title = new JLabel("Pacman");

    public static void main(String[] args) {
        PacmanGame app = new PacmanGame();
        SwingUtilities.invokeLater(app);
    }

    public void run(){

        String[] level1Map = new String[]{"xxxxxxxxxxxxxxxxxxxxxxxxx",
                                          "x...........x...........x",
                                          "x.xxxx.xxxx.x.xxxx.xxxx.x",
                                          "x.xxxx.xxxx.x.xxxx.xxxx.x",
                                          "x.......................x",
                                          "x.xxxx.x.xxxxxxx.x.xxxx.x",
                                          "x......x....x....x......x",
                                          "xxxxxx.xxxx x xxxx.xxxxxx",
                                          "     x.x         x.x     ",
                                          "xxxxxx.x xx   xx x.xxxxxx",
                                          "l     .  xx bixx  .     r",
                                          "xxxxxx.x xxxxxxx x.xxxxxx",
                                          "     x.x         x.x     ",
                                          "xxxxxx.x xxxxxxx x.xxxxxx",
                                          "x...........x...........x",
                                          "x.xx.xxxxxx.x.xxxxxx.xx.x",
                                          "x..x........p........x..x",
                                          "xx.x.x.xxxxxxxxxxx.x.x.xx",
                                          "x....x......x......x....x",
                                          "x.xxxxxxxxx.x.xxxxxxxxx.x",
                                          "x.......................x",
                                          "xxxxxxxxxxxxxxxxxxxxxxxxx"};

        PacMap map = new PacMap(level1Map);

        map.populate();

        window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
        window.add(title);
        window.add(map);

        window.setResizable(false);
        window.pack();

        window.setLocationByPlatform(true);
        window.setVisible(true);

        map.run();
    }
}

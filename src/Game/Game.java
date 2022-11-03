package Game;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class Game{
    public static void main(String[] args){
        System.setProperty("sun.java2d.opengl", "true");
        JFrame frame = new JFrame("Snake vs Snake");
        frame.add(new GamePanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon("Resources/snake-icon.png").getImage());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}

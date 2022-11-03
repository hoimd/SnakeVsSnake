package Model;

import Game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AppleManager{
    private static final Random random = new Random();
    private static BufferedImage appleIcon;
    private static Set<Point> apples;

    public AppleManager(int appleCount){
        apples = new HashSet<>();

        try{
            appleIcon = ImageIO.read(new File("Resources/apple.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

        for(int i = 0;i < appleCount;i++){
            newApple();
        }
    }

    private static void newApple(){
        int newX, newY;
        do{
            newX = random.nextInt(GamePanel.COLUMNS);
            newY = random.nextInt(GamePanel.ROWS);
        }while(GamePanel.board[newY][newX] != 0);

        apples.add(new Point(newX,newY));
        GamePanel.board[newY][newX] = 'A';
    }

    public static void removeAndRestock(Point p){
        apples.remove(p);
        newApple();
    }

    public void draw(Graphics2D g){
        for(Point p : apples){
            g.drawImage(appleIcon, p.x * 40, p.y * 40, 40, 40, null);
        }
    }
}

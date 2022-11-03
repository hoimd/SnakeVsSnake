package Model;

import Game.GamePanel;
import Model.Snakes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SnakeManager{
    private final Point[] startPoints = {new Point(0, 0), new Point(GamePanel.COLUMNS - 1, GamePanel.ROWS - 1)};
    private final char[] initDir = {'E', 'W'};
    private final ArrayList<Snake> snakes = new ArrayList<>();
    private final HashMap<Integer, Pair<Integer, String>> keyMap = new HashMap<>();
    private final ArrayList<Pair<String, Integer>> scoreTable = new ArrayList<>();
    private final BufferedImage[][] snakeSheets = new BufferedImage[2][3];
    private int snakeCount = 0;

    public SnakeManager(){
        try{
            snakeSheets[0][0] = ImageIO.read(new File("Resources/blue_snake/head.png"));
            snakeSheets[0][1] = ImageIO.read(new File("Resources/blue_snake/tail.png"));
            snakeSheets[0][2] = ImageIO.read(new File("Resources/blue_snake/body.png"));
            snakeSheets[1][0] = ImageIO.read(new File("Resources/orange_snake/head.png"));
            snakeSheets[1][1] = ImageIO.read(new File("Resources/orange_snake/tail.png"));
            snakeSheets[1][2] = ImageIO.read(new File("Resources/orange_snake/body.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addKeyboardControlledSnake(int leftKey, int rightKey, String name){
        Point startPoint = startPoints[snakeCount];
        char dir = initDir[snakeCount];
        BufferedImage[] snakeSheet = snakeSheets[snakeCount];
       
        keyMap.put(leftKey, new Pair<>(snakeCount, "LEFT"));
        keyMap.put(rightKey, new Pair<>(snakeCount, "RIGHT"));
        
        snakes.add(new Snake(startPoint, dir, name, snakeSheet));
        snakeCount++;
    }
    
    public void addAIControlledSnake(String name){
        Point startPoint = startPoints[snakeCount];
        char dir = initDir[snakeCount];
        BufferedImage[] snakeSheet = snakeSheets[snakeCount];

        snakes.add(new AI_Snake(startPoint, dir, name, snakeSheet));
        snakeCount++;
    }

    public void update(){
        for(Snake s : snakes){
            s.move();
        }
    }
    
    public void draw(Graphics2D g){
        for(Snake s : snakes){
            s.draw(g);
        }
    }

    public ArrayList<Pair<String,Integer>> getScoreTable(){
        scoreTable.clear();
        for(Snake s : snakes){
            scoreTable.add(new Pair<>(s.getName(), s.getApples()));
        }

        return scoreTable;
    }

    public boolean bothAlive(){
        return snakes.get(0).isAlive() && snakes.get(1).isAlive();
    }

    public String getWinnersName(){
        if(bothAlive()){
            if(snakes.get(0).getApples() == snakes.get(1).getApples()){
                return null;
            }else{
                if(snakes.get(0).getApples() > snakes.get(1).getApples()){
                    return snakes.get(0).getName();
                }else{
                    return snakes.get(1).getName();
                }
            }
        }else{
            if(snakes.get(0).isAlive()){
                return snakes.get(0).getName();
            }else{
                return snakes.get(1).getName();
            }
        }
    }

    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(keyMap.containsKey(keyCode)){
            int whichSnake = keyMap.get(keyCode).getKey();
            String turn = keyMap.get(keyCode).getValue();

            switch(turn){
                case "LEFT" -> snakes.get(whichSnake).turnLeft();
                case "RIGHT" -> snakes.get(whichSnake).turnRight();
            }
        }
    }
}

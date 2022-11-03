package Model.Snakes;

import Game.GamePanel;
import Model.AppleManager;

import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class Snake{
    private static final int initBodyParts = 4;
    private final String name;
    private boolean alive = true;
    private int applesEaten;
    private char direction;

    protected final Deque<Point> body = new LinkedList<>();
    private final BufferedImage[] snakeSheet;

    public Snake(Point initLocation, char initDirection, String name, BufferedImage[] snakeSheet){
        this.direction = initDirection;
        this.snakeSheet = snakeSheet;
        this.name = name;
        body.addFirst(initLocation);

        for(int i = 0;i < initBodyParts - 1;i++){
            Point newPos = new Point(body.getFirst());
            switch(direction){
                case 'N' -> newPos.y--;
                case 'S' -> newPos.y++;
                case 'W' -> newPos.x--;
                case 'E' -> newPos.x++;
            }

            body.addFirst(newPos);
        }

        for(Point p : body){
            GamePanel.board[p.y][p.x] = 'S';
        }
    }

    public void draw(Graphics2D g){
        Iterator<Point> it = body.iterator();

        Point p1 = it.next();
        Point p2 = it.next();
        Point p3 = null;

        Character dir1 = getVectorDirection(p1, p2);
        g.drawImage(getHeadImage(dir1), p1.x * 40, p1.y * 40, 40, 40, null);

        while(it.hasNext()){
            p3 = it.next();
            Character dir2 = getVectorDirection(p2, p3);
            String twoDir = dir2.toString() + dir1;
            g.drawImage(getBodyImage(twoDir), p2.x * 40, p2.y * 40, 40, 40, null);
            dir1 = dir2;
            p1 = p2;
            p2 = p3;
        }

        dir1 = getVectorDirection(p1, p2);
        g.drawImage(getTailImage(dir1), p3.x * 40, p3.y * 40, 40, 40, null);
    }

    protected Point getNewPos(){
        Point newPos = new Point(body.getFirst());
        switch(direction){
            case 'N' -> newPos.y--;
            case 'S' -> newPos.y++;
            case 'W' -> newPos.x--;
            case 'E' -> newPos.x++;
        }

        return newPos;
    }
    
    public void move(){
        Point newPos = getNewPos();

        if(!checkField(newPos)){
            alive = false;
            return;
        }

        if(GamePanel.board[newPos.y][newPos.x] == 'A'){
            applesEaten++;
            AppleManager.removeAndRestock(newPos);
        }else{
            GamePanel.board[body.getLast().y][body.getLast().x] = 0;
            body.removeLast();
        }

        body.addFirst(newPos);
        GamePanel.board[newPos.y][newPos.x] = 'S';
    }

    public void turnLeft(){
        switch(direction){
            case 'N' -> direction = 'W';
            case 'S' -> direction = 'E';
            case 'W' -> direction = 'S';
            case 'E' -> direction = 'N';
        }
    }

    public void turnRight(){
        switch(direction){
            case 'N' -> direction = 'E';
            case 'S' -> direction = 'W';
            case 'W' -> direction = 'N';
            case 'E' -> direction = 'S';
        }
    }

    private Character getVectorDirection(Point a, Point b){
        int dx = a.x - b.x;
        int dy = a.y - b.y;

        return switch(dx){
            case 1 -> 'E';
            case -1 -> 'W';
            default -> switch(dy){
                case 1 -> 'S';
                case -1 -> 'N';
                default -> null;
            };
        };
    }

    private BufferedImage getHeadImage(char direction){
        return switch(direction){
            case 'W' -> snakeSheet[0].getSubimage(0, 0, 40, 40);
            case 'N' -> snakeSheet[0].getSubimage(40, 0, 40, 40);
            case 'E' -> snakeSheet[0].getSubimage(80, 0, 40, 40);
            case 'S' -> snakeSheet[0].getSubimage(120, 0, 40, 40);
            default -> null;
        };
    }

    private BufferedImage getTailImage(char direction){
        return switch(direction){
            case 'W' -> snakeSheet[1].getSubimage(80, 0, 40, 40);
            case 'N' -> snakeSheet[1].getSubimage(120, 0, 40, 40);
            case 'E' -> snakeSheet[1].getSubimage(0, 0, 40, 40);
            case 'S' -> snakeSheet[1].getSubimage(40, 0, 40, 40);
            default -> null;
        };
    }

    private BufferedImage getBodyImage(String twoDirections){
        return switch(twoDirections){
            case "ES", "NW" -> snakeSheet[2].getSubimage(0, 0, 40, 40);
            case "WS", "NE" -> snakeSheet[2].getSubimage(40, 0, 40, 40);
            case "SE", "WN" -> snakeSheet[2].getSubimage(80, 0, 40, 40);
            case "EN", "SW" -> snakeSheet[2].getSubimage(120, 0, 40, 40);
            case "NN", "SS" -> snakeSheet[2].getSubimage(160, 0, 40, 40);
            case "EE", "WW" -> snakeSheet[2].getSubimage(200, 0, 40, 40);
            default -> null;
        };
    }

    protected boolean checkField(Point p){
        return isFieldInsideBoard(p) && isFieldNotTaken(p);
    }

    private boolean isFieldInsideBoard(Point p){
        return p.x >= 0 && p.x < GamePanel.COLUMNS && p.y >= 0 && p.y < GamePanel.ROWS;
    }

    private boolean isFieldNotTaken(Point p){
        return GamePanel.board[p.y][p.x] != 'S';
    }

    public int getApples(){
        return applesEaten;
    }

    public boolean isAlive(){
        return alive;
    }

    public String getName(){
        return name;
    }
}

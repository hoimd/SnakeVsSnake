package Model.Snakes;

import Game.GamePanel;

import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AI_Snake extends Snake{
    private final static Point[] directions = {new Point(-1, 0), new Point(0, -1), new Point(1, 0), new Point(0, 1)};
    private final HashMap<Point, Point> predecessor = new HashMap<>();
    private final Queue<Point> Q = new LinkedList<>();

    public AI_Snake(Point initLocation, char initDirection, String name, BufferedImage[] snakeSheet){
        super(initLocation, initDirection, name, snakeSheet);
    }

    @Override
    protected Point getNewPos(){
        Q.clear();
        predecessor.clear();

        Point head =  body.getFirst();
        Q.add(head);

        while(!Q.isEmpty()){
            Point v = Q.remove();

            if(GamePanel.board[v.y][v.x] == 'A'){
                Point newPos = v;
                while(!predecessor.get(newPos).equals(head)){
                    newPos = predecessor.get(newPos);
                }

                return newPos;
            }

            for(Point d : directions){
                Point u = new Point(v.x + d.x, v.y + d.y);
                if(checkField(u) && !predecessor.containsKey(u)){
                    predecessor.put(u, v);
                    Q.add(u);
                }
            }
        }

        for(Point d : directions){
            Point u = new Point(head.x + d.x, head.y + d.y);
            if(checkField(u)){
                return u;
            }
        }

        return super.getNewPos();
    }
}

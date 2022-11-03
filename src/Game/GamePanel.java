package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import Model.*;

public class GamePanel extends JPanel implements ActionListener{
    public static final int COLUMNS = 32;
    public static final int ROWS = 18;

    public static final int SCREEN_WIDTH = COLUMNS * 40;
    public static final int SCREEN_HEIGHT = ROWS * 40;

    private final int DELAY = 80;
    private final int DURATION = 60;
    private final int APPLES = 4;

    private int timeLeft;
    public static char[][] board;

    private final BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final Graphics2D g = (Graphics2D)image.getGraphics();
    
    private SnakeManager snakeManager;
    private AppleManager appleManager;
    private final Background background = new Background();
    private final Timer timer = new Timer(DELAY, this);

    public GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //newGame();
    }

    private void newGame(){
        board = new char[ROWS][COLUMNS];
        snakeManager = new SnakeManager();

        //snakeManager.addKeyboardControlledSnake(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, "Blue snake");
        snakeManager.addAIControlledSnake("Blue snake");
        snakeManager.addAIControlledSnake("Orange snake");

        appleManager = new AppleManager(APPLES);

        timeLeft = (DURATION + 1) * 1000;
        timer.start();
    }

    public void actionPerformed(ActionEvent e){
        update();
        draw();

        if(!snakeManager.bothAlive() || timeLeft < 1000){
            timer.stop();
            announceWinner();
        }

        drawToScreen();
    }

    private void update(){
        timeLeft -= DELAY;
        snakeManager.update();
    }

    private void draw(){
        background.draw(g);
        snakeManager.draw(g);
        appleManager.draw(g);
        drawTimeAndScore();
    }

    private void drawToScreen(){
        Graphics2D g2 = (Graphics2D)this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    private String timeToString(){
        String text = "Time: 0";
        int min = timeLeft / 1000 / 60;
        int sec = (timeLeft / 1000) - min * 60;

        text += (min > 0 ? min : "0") + ":";
        text += (sec < 10 ? "0" : "") + sec;

        return text;
    }

    private void drawTimeAndScore(){
        String text = timeToString();

        for(Pair<String, Integer> p : snakeManager.getScoreTable()){
            text += " | "  + p.getKey() + ": " + p.getValue();
        }

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 26));
        g.drawString(text, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(text)) / 2, g.getFont().getSize());
    }

    private void announceWinner(){
        String text = snakeManager.getWinnersName();

        if(text == null){
            text = "Tie";
        }else{
            text += " wins";
        }

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 64));
        g.drawString(text, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(text)) / 2, SCREEN_HEIGHT / 2);

        text = "Press SPACE for a new game";
        g.setFont(new Font("Ink Free", Font.BOLD, 26));
        g.drawString(text, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(text)) / 2, SCREEN_HEIGHT / 2 + 36);
    }

    private class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                newGame();
            }else{
                snakeManager.keyPressed(e);
            }
        }
    }
}

package Model;

import Game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background{
    private final Color tileColor1 = new Color(0x71B66A);
    private final Color tileColor2 = new Color(0xABCD5E);
    private final BufferedImage background = new BufferedImage(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

    public Background(){
        Color[] tileColors = {tileColor1, tileColor2};
        Graphics2D g = (Graphics2D)background.getGraphics();

        for(int i = 0;i < GamePanel.ROWS;i++){
            g.setColor(tileColors[i % 2]);
            for(int j = 0;j < GamePanel.COLUMNS;j++){
                g.fillRect(j * 40, i * 40, 40, 40);
                g.setColor(tileColors[(i + j + 1) % 2]);
            }
        }
    }

    public void draw(Graphics2D g){
        g.drawImage(background, 0, 0, null);
    }
}

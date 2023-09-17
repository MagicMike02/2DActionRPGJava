package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    final int originalTileSizes = 16; //16x16 pixels
    final int scale = 3; // scale the pixels in bigger resolution
    final int tileSize = originalTileSizes * scale; //48*48 tiles
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels

    Thread gameThread;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(gameThread != null){ // Game Loop

            //UPDATE: update information such as character position
            update();
            //DRAW: draw the screen with the uploaded information
            repaint(); // call paintComponent method
        }
    }

    public void update(){

    }
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);
        g2.fillRect(100, 100 , tileSize, tileSize);
        g2.dispose();

    }
}

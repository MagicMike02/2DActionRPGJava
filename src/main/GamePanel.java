package main;

import enity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSizes = 16; //16x16 pixels
    final int scale = 3; // scale the pixels in bigger resolution
    public final int tileSize = originalTileSizes * scale; //48*48 tiles
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels

    private static final long ONE_SEC_IN_NANOSEC = 1000000000;
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);


    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double)ONE_SEC_IN_NANOSEC / FPS;
        double delta = 0;

        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){ // Game Loop

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                //UPDATE: update information such as character position
                this.update();
                //DRAW: draw the screen with the uploaded information
                repaint(); // call paintComponent method

                delta--;
                drawCount++;
            }

            if(timer >= ONE_SEC_IN_NANOSEC) {
                System.out.println("FPS: " + drawCount);
                timer = 0;
                drawCount = 0;
            }
        }
    }

    public void update() {
        player.update();
    }


    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        g2.dispose();

    }
}

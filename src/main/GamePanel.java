package main;

import entity.Player;
import object.SuperObject;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSizes = 16; //16x16 pixels
    final int scale = 3; // scale the pixels in bigger resolution
    public final int tileSize = originalTileSizes * scale; //48*48 tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    private static final long ONE_SEC_IN_NANOSEC = 1000000000;

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

//    public final int worldWidth = tileSize * maxScreenCol;
//    public final int worldHeight = tileSize * maxScreenRow;

    int FPS = 60;

    //System
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();//background music
    Sound se = new Sound();//sound effetcs


    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    //Entity and Object
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10]; // 10 slots for objects -> can display up to 10 object in the same time

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();
        playMusic(0);//index of background music BlueBoyAdventure
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
                //System.out.println("FPS: " + drawCount);
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

        //TILE
        tileM.draw(g2);

        //OBJECTS
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                //System.out.println("OBJECTS: " + obj[i] + " -> " + obj[i].image);
                superObject.draw(g2, this);
            }
        }

        //PLAYER
        player.draw(g2);

        //UI
        ui.draw(g2);

        g2.dispose();

    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){ // Sound Effect
        se.setFile(i);
        se.play();
    }
}

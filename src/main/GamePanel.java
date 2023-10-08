package main;

import entity.Entity;
import entity.Player;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    int FPS = 60;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Sound music = new Sound();//background music
    public Sound se = new Sound();//sound effetcs
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);

    Thread gameThread;

    //ENTITY AND OBJECTS
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[10]; // 10 slots for objects -> can display up to 10 object in the same time
    public Entity[] npc = new Entity[10];
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME SETTIGNS
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
//        playMusic(0);//index of background music BlueBoyAdventure
        gameState = titleState;
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

        if(gameState == playState){
            //PLAYER
            player.update();
            //NPC
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update();
                }
            }
        }

        if(gameState == pauseState){
            //nothing
        }

    }


    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState){
            ui.draw(g2);

        }
        //OTHERS
        else{
            //TILE
            tileM.draw(g2);

            entityList.add(player);

            //add entities to the list
            for (Entity entity : npc) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : obj) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            //SORT the list
            entityList.sort(Comparator.comparingInt(e -> e.worldY));

            //Draw Entities in the sorted order
            for(Entity entity : entityList) {
                entity.draw(g2);
            }

            //Empty Entity list
            entityList.clear();

            //UI
            ui.draw(g2);

            //DEBUG
            if (keyH.checkDrawTime) {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                g2.setColor(Color.WHITE);
                g2.drawString("DrawT Time : " + passed, 10, 400);
                System.out.println("Drat Time : " + passed);
            }

        }




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

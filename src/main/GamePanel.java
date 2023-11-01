package main;

import entity.Entity;
import entity.Player;
import tile_interactive.InteractiveTile;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSizes = 16; //16x16 pixels
    final int scale = 3; // scale the pixels in bigger resolution
    public final int tileSize = originalTileSizes * scale; //48*48 tiles
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;

    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = true;

    private static final long ONE_SEC_IN_NANOSEC = 1000000000;

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;


    public Entity projectiles;

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

    Config config = new Config(this);
    Thread gameThread;

    //ENTITY AND OBJECTS
    public Player player = new Player(this, keyH);
    public Entity[][] obj = new Entity[maxMap][20]; // 10 slots for objects -> can display up to 10 object in the same time
    public Entity[][] npc = new Entity[maxMap][10];
    public Entity[][] monster = new Entity[maxMap][20];
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];

    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();

    //GAME SETTIGNS
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTiles();
//      playMusic(0); //index of background music BlueBoyAdventure
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if(fullScreenOn) {
            setFullScreen();
        }
    }

    public void retry(){
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        aSetter.setNPC();
        aSetter.setMonster();
    }
    public void restart(){
        player.setDefaultValues();
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        player.setItems();

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTiles();
    }

    public void setFullScreen(){
        //GET LOCAL SCREEN DEVIDE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) ONE_SEC_IN_NANOSEC / FPS;
        double delta = 0;

        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) { // Game Loop

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                //UPDATE: update information such as character position
                this.update();
                //DRAW: draw the screen with the uploaded information

                //repaint(); // call paintComponent method
                drawToTempScreen(); // draw the component in the buffered image
                drawToScreen(); // draw the buffered image to the screen
                delta--;
                drawCount++;
            }

            if (timer >= ONE_SEC_IN_NANOSEC) {
                //System.out.println("FPS: " + drawCount);
                timer = 0;
                drawCount = 0;
            }
        }
    }

    public void update() {

        if (gameState == playState) {
            //PLAYER
            player.update();

            //NPC
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            //MONSTER
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    if (monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }
                    if (!monster[currentMap][i].alive) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            //PROJECTILES
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

            //PARTICLE
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    if (!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }

            //INTERACTIVE TILES
            for (int i = 0; i < iTile[1].length; i++) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                }
            }
        }

        if (gameState == pauseState) {
            //nothing
        }

    }


    public void drawToTempScreen(){
        //DEBUG
        long drawStart = 0;
        if (keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        }
        //OTHERS
        else {
            //TILE
            tileM.draw(g2);

            for (int i = 0; i < iTile[1].length; i++) {
                if (iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(g2);
                }
            }

            //add entities to the list
            entityList.add(player);

            for (Entity entity : npc[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : obj[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : monster[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : projectileList) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : particleList) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            //SORT the list
            entityList.sort(Comparator.comparingInt(e -> e.worldY));

            //Draw Entities in the sorted order
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            //Empty Entity list
            entityList.clear();

            //UI
            ui.draw(g2);
        }

        //DEBUG
        if (keyH.showDebugText) {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.setColor(Color.WHITE);
                int x = 10;
                int y = 400;
                int lineHeight = 20;


                g2.drawString("WorldX " + player.worldX,x,y);
                y += lineHeight;
                g2.drawString("WorldY " + player.worldY, x, y);
                y += lineHeight;

                g2.drawString("Col " + (player.worldX + player.solidArea.x)/tileSize, x, y);
                y += lineHeight;

                g2.drawString("Row " + (player.worldY + player.solidArea.y)/tileSize, x, y);
                y += lineHeight;

                g2.drawString("DrawT Time : " + passed, x, y);

            }
    }

    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }


    //Draw components directly into the jpanel of this class
    /*
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //DEBUG
        long drawStart = 0;
        if (keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);

        }
        //OTHERS
        else {
            //TILE
            tileM.draw(g2);

            for (int i = 0; i < iTile.length; i++) {
                if (iTile[i] != null) {
                    iTile[i].draw(g2);
                }
            }

            //add entities to the list
            entityList.add(player);

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

            for (Entity entity : monster) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : projectileList) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            for (Entity entity : particleList) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            //SORT the list
            entityList.sort(Comparator.comparingInt(e -> e.worldY));

            //Draw Entities in the sorted order
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            //Empty Entity list
            entityList.clear();

            //UI
            ui.draw(g2);
        }

        //DEBUG
        if (keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            int x = 10;
            int y = 400;
            int lineHeight = 20;


            g2.drawString("WorldX " + player.worldX,x,y);
            y += lineHeight;
            g2.drawString("WorldY " + player.worldY, x, y);
            y += lineHeight;

            g2.drawString("Col " + (player.worldX + player.solidArea.x)/tileSize, x, y);
            y += lineHeight;

            g2.drawString("Row " + (player.worldY + player.solidArea.y)/tileSize, x, y);
            y += lineHeight;

            g2.drawString("DrawT Time : " + passed, x, y);

        }


        g2.dispose();
    }
*/
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) { // Sound Effect
        se.setFile(i);
        se.play();
    }

    public int getFPS() {
        return FPS;
    }
}

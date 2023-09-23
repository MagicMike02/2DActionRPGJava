package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;

    int hasKey = 0;

    public Player(GamePanel gp, KeyHandler key){
        this.gp = gp;
        this.keyH = key;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        //collisioon area for the player (smalled than a fill pixel)
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY  = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;


        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        this.worldX = gp.tileSize * 23;
        this.worldY = gp.tileSize * 21;
        //this.speed = 4;
        this.speed = 8; //TODO: CANCELLARE -> Solo per testare il game
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_right_2.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void update(){

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){ // Moving animation
            if (keyH.upPressed) {
                direction = "up";
            }
            else if (keyH.downPressed) {
                direction = "down";
            }
            else if (keyH.leftPressed) {
                direction = "left";
            }
            else if (keyH.rightPressed) {
                direction = "right";
            }

            //Check Tile Collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //Check Object Collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);


            //if collision is False, player can move
            if(!collisionOn){
                switch(direction){
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" ->  worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            spriteCounter++;

            if(spriteCounter > 18){
                if(spriteNum == 1) spriteNum = 2;
                else spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gp.obj[i].name;
            switch(objectName){
                case "Key" -> {
                    gp.playSE(1); // Coin sound Effect
                    hasKey++;
                    gp.obj[i] = null; // delete the Key touched
                    System.out.println("Key: " + hasKey);
                }
                case "Door" -> {
                    if(hasKey > 0){
                        gp.playSE(3); // Door sound Effect
                        gp.obj[i] = null; // delete the Key touched
                        hasKey--;
                        //System.out.println("Key: " + hasKey);
                    }
                    System.out.println("Key: " + hasKey);
                }
                case "Boots" -> {
                    gp.playSE(2); // Boots sound Effect
                    speed += 1;
                    gp.obj[i] = null; // delete the Boots touched
                }
            }

        }
    }


    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(x, y , gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up" -> image = spriteNum == 1 ? up1 : up2;
            case "down" -> image = spriteNum == 1 ? down1 : down2;
            case "left" ->  image = spriteNum == 1 ? left1 : left2;
            case "right" ->  image = spriteNum == 1 ? right1 : right2;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}

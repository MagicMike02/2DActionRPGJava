package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;

//    public int hasKey = 0;

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
        this.speed = 4;
        //this.speed = 8; //TODO: CANCELLARE -> Solo per testare il game
        direction = "down";
    }

    public void getPlayerImage(){
        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
    }

    public BufferedImage setup(String imageName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image =  ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
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
        g2.drawImage(image, screenX, screenY, null);
    }
}

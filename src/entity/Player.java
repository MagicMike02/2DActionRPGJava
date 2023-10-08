package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;

    public Player(GamePanel gp, KeyHandler key){
        super(gp);

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
        getImage();
    }

    public void setDefaultValues(){
        this.worldX = gp.tileSize * 23;
        this.worldY = gp.tileSize * 21;
//        this.worldX = gp.tileSize * 10;
//        this.worldY = gp.tileSize * 13;

        this.speed = 4;
        direction = "down";

        //Player Status
        maxLife = 6; // 3 full heart: 1 = half Heart; 2 = 1 full heart ....
        life = maxLife;
    }

    public void getImage(){
        up1 = setup("Player/Walking sprites/boy_up_1");
        up2 = setup("Player/Walking sprites/boy_up_2");
        down1 = setup("Player/Walking sprites/boy_down_1");
        down2 = setup("Player/Walking sprites/boy_down_2");
        left1 = setup("Player/Walking sprites/boy_left_1");
        left2 = setup("Player/Walking sprites/boy_left_2");
        right1 = setup("Player/Walking sprites/boy_right_1");
        right2 = setup("Player/Walking sprites/boy_right_2");
    }

    public void update(){

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed){ // Moving animation
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

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

//            CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK EVENT
            gp.eHandler.checkEvent();

            //if collision is False, player can move
            if(collisionOn == false && gp.keyH.enterPressed == false){
                switch(direction){
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" ->  worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }
            //reset
            gp.keyH.enterPressed = false;


            spriteCounter++;

            if(spriteCounter > 18){
                if(spriteNum == 1) spriteNum = 2;
                else spriteNum = 1;
                spriteCounter = 0;
            }
        }

        //allows to player to get dmg just once each second and not once per frame
        // giving him an invincibility time which last a second
        if(invincible == true){
            invincibilityCounter++;
            if(invincibilityCounter > gp.getFPS()) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }
    }

    public void pickUpObject(int i){
        if(i != 999){

        }
    }

    public void interactNPC(int i){
        if(i != 999){
            if (gp.keyH.enterPressed){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false){
                life -=  1;
                invincible = true;
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

        //Invincible visual effects
        if(invincible == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);

        //Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));


        //DEBUG
//        g2.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2.setColor(Color.WHITE);
//        g2.drawString("InvisibleCounter: "+invincibilityCounter,10,400);
    }
}

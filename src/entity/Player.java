package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;

    public Player(GamePanel gp, KeyHandler key) {
        super(gp);

        this.keyH = key;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        //collisioon area for the player (smalled than a fill pixel)
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues() {
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

    public void getImage() {
        up1 = setup("Player/Walking sprites/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("Player/Walking sprites/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("Player/Walking sprites/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("Player/Walking sprites/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("Player/Walking sprites/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("Player/Walking sprites/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("Player/Walking sprites/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("Player/Walking sprites/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {
        attackUp1 = setup("Player/Attacking sprites/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("Player/Attacking sprites/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setup("Player/Attacking sprites/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("Player/Attacking sprites/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);

        attackLeft1 = setup("Player/Attacking sprites/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("Player/Attacking sprites/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);

        attackRight1 = setup("Player/Attacking sprites/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("Player/Attacking sprites/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
    }

    public void update() {

        if (invincible == true) {
            //must be outside the keyPressed if
            invincibilityCounter++;
            if (invincibilityCounter > gp.getFPS()) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }

        if (attacking) {
            attacking();
        }
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) { // Moving animation
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
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
            if (collisionOn == false && gp.keyH.enterPressed == false) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }
            //reset
            gp.keyH.enterPressed = false;


            spriteCounter++;

            if (spriteCounter > 18) {
                if (spriteNum == 1) spriteNum = 2;
                else spriteNum = 1;
                spriteCounter = 0;
            }


        }
        //allows to player to get dmg just once each second and not once per frame
        // giving him an invincibility time which last a second



    }

    public void attacking() {
        spriteCounter++;

        // Animation
        if (spriteCounter <= 5) { // first attack image in the first 5 frames
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter < 25) { // second attack image in the 6-25 frames
            spriteNum = 2;

            // Check if attack landed on a monster -> need to check the weapon position not the player position
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the attack
            switch(direction) {
                case "up" -> {worldY -= attackArea.height;}
                case "down" -> {worldY += attackArea.height;}
                case "left" -> {worldX -= attackArea.width;}
                case "right" -> {worldX += attackArea.width;}
            }

            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collusion with updated worldX worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            demageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;


        }
        if (spriteCounter > 25) { // Last animation attack in the last frames
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {

        }
    }

    public void interactNPC(int i) {
        if (gp.keyH.enterPressed) {
            if (i != 999) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            } else {
                gp.playSE(7);
                attacking = true;
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible) {
                gp.playSE(6);
                life -= 1;
                invincible = true;
            }
        }
    }

    public void demageMonster(int i) {
        if (i != 999) {
            if(!gp.monster[i].invincible) {
                gp.playSE(5);
                gp.monster[i].life -= 1;
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                //monster dies
                if(gp.monster[i].life <= 0){
//                    gp.monster[i] = null;
                    gp.monster[i].dying = true;
                }
            }
        }
        else{
            System.out.println("Miss!");
        }
    }

    public void draw(Graphics2D g2) {
        //g2.setColor(Color.white);
        //g2.fillRect(x, y , gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        // screen coordinates for animations
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up" -> {
                if (attacking) { // Attacking animations
                    tempScreenY = screenY - gp.tileSize;// adjusting screen position for the animation in this direction
                    image = spriteNum == 1 ? attackUp1 : attackUp2;
                } else { // Not attacking, normal walking animation
                    image = spriteNum == 1 ? up1 : up2;
                }
            }
            case "down" -> {
                if (attacking) { // Attacking animations
                    image = spriteNum == 1 ? attackDown1 : attackDown2;
                } else { // Not attacking, normal walking animation
                    image = spriteNum == 1 ? down1 : down2;
                }
            }
            case "left" -> {
                if (attacking) { // Attacking animations
                    tempScreenX = screenX - gp.tileSize; // adjusting screen position for the animation in this direction
                    image = spriteNum == 1 ? attackLeft1 : attackLeft2;
                } else { // Not attacking, normal walking animation
                    image = spriteNum == 1 ? left1 : left2;
                }
            }
            case "right" -> {
                if (attacking) { // Attacking animations
                    image = spriteNum == 1 ? attackRight1 : attackRight2;
                } else { // Not attacking, normal walking animation
                    image = spriteNum == 1 ? right1 : right2;
                }
            }
        }

        //Invincible visual effects
        if (invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));


        //DEBUG
        g2.setFont(new Font("Arial", Font.PLAIN, 26));
        g2.setColor(Color.WHITE);
        g2.drawString("InvisibleCounter: "+invincibilityCounter,10,400);
    }
}

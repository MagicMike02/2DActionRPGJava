package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    public final GamePanel gp;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage image, image2, image3;

    public Rectangle solidArea = new Rectangle(0, 0, 40, 40);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    String[] dialogues = new String[20];


    // STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;


    // COUNTER
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibilityCounter = 0;


    // CHARACTER ATTRIBUTES
    public String name;
    public int type; //0=player, 1=npc, 2=monster
    public int speed;
    public int maxLife;
    public int life;


    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {

    }

    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);

        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);

        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        //allows monster to deal dmg with THEY touch the player
        if(this.type == 2 && contactPlayer == true) {
            //check if player is in invincible state
            if(gp.player.invincible == false){
                gp.player.life -= 1;
                gp.player.invincible = true;
            }

        }

        //if collision is False, player can move
        if (collisionOn == false) {
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        spriteCounter++;
        if (spriteCounter > 18) {
            if (spriteNum == 1) spriteNum = 2;
            else spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    image = spriteNum == 1 ? up1 : up2;
                    break;
                case "down":
                    image = spriteNum == 1 ? down1 : down2;
                    break;
                case "left":
                    image = spriteNum == 1 ? left1 : left2;
                    break;
                case "right":
                    image = spriteNum == 1 ? right1 : right2;
                    break;
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "right" -> direction = "left";
            case "left" -> direction = "right";
        }
    }
}

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
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;

    public Rectangle solidArea = new Rectangle(0, 0, 40, 40); // area of the entity
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0); // attack area/range (depending on the weapon long/short-wide/narrow)

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
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;


    // COUNTER
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibilityCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    public int shotAvailableCounter = 0;


    // CHARACTER ATTRIBUTES
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;


    // ITEM ATTRIBUTES
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;

    // TYPE
    public int type; //0=player, 1=npc, 2=monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;


    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {

    }

    public void damageReaction() {

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

    public void use(Entity entity) {
    }

    public void checkDrop() {

    }

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) { // null valu ein the obj array and put the dropped item
                gp.obj[i] = droppedItem;

                //the dead monster coordinates
                gp.obj[i].worldX = worldX;
                gp.obj[i].worldY = worldY;
                break; // IMPORTANT to not copy the same item in all the null value in the list

            }
        }
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
        if (this.type == type_monster && contactPlayer) {
            damagePlayer(attack);

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

        //allows to entity to get dmg just once in a period of time and not once per frame
        // giving him an invincibility time which last a some short time
        if (invincible == true) {
            invincibilityCounter++;
            if (invincibilityCounter > gp.getFPS() - 20) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }

        //check a part of the condition which allows the entity to shoot each 30 frames
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }


    public BufferedImage setup(String imageName, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imageName + ".png"));
            image = uTool.scaleImage(image, width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void damagePlayer(int attack) {
        //check if player is in invincible state
        if (!gp.player.invincible) {
            gp.playSE(6);

            int damage = attack - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }

            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            image = switch (direction) {
                case "up" -> spriteNum == 1 ? up1 : up2;
                case "down" -> spriteNum == 1 ? down1 : down2;
                case "left" -> spriteNum == 1 ? left1 : left2;
                case "right" -> spriteNum == 1 ? right1 : right2;
                default -> image;
            };


            //Monster HealthBar
            if (type == 2 && hpBarOn) {
                double oneScale = (double) gp.tileSize / maxLife; // scale the hpbar based on the enemie width (for slimes is 1 tile)
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12); // Border HealthBar

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10); // screenY-15 to display it up the monster

                hpBarCounter++;

                //show counter for 10 seconds
                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0; // if player attack refresh counter
                changeAlpha(g2, 0.4F);
            }

            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            changeAlpha(g2, 1F);

        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int frequency = 5;
        if (dyingCounter <= frequency) changeAlpha(g2, 1f);
        if (dyingCounter > frequency * 2 && dyingCounter <= frequency * 3) changeAlpha(g2, 0f);
        if (dyingCounter > frequency * 3 && dyingCounter <= frequency * 4) changeAlpha(g2, 1f);
        if (dyingCounter > frequency * 4 && dyingCounter <= frequency * 5) changeAlpha(g2, 0f);
        if (dyingCounter > frequency * 5 && dyingCounter <= frequency * 6) changeAlpha(g2, 1f);
        if (dyingCounter > frequency * 6 && dyingCounter <= frequency * 7) changeAlpha(g2, 0f);
        if (dyingCounter > frequency * 7 && dyingCounter <= frequency * 8) changeAlpha(g2, 1f);
        if (dyingCounter > frequency * 8) {
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }


}
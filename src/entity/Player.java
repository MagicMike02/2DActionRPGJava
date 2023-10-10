package entity;

import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

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

        setDefaultValues();
        getImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        this.worldX = gp.tileSize * 23;
        this.worldY = gp.tileSize * 21;
//        this.worldX = gp.tileSize * 10;
//        this.worldY = gp.tileSize * 13;

        this.speed = 4;
        direction = "down";

        //Player Status
        level = 1;
        maxLife = 6; // 3 full heart: 1 = half Heart; 2 = 1 full heart ....
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        //ammo = 10;
        strength = 1; // the more strength the more dmg gives
        dexterity = 1; // the more dexterity the less dmg receives
        exp = 0;
        nextLevelExp = 5;
        coin = 0;

        // WEAPON
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        //projectile = new OBJ_Rock(gp);

        attack = getAttack(); //total attack value is decided by strength and weapon
        defense = getDefense(); // total defense value is decided by dexterity and shield
    }

    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
    }

    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
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
        if(currentWeapon.type == type_sword){
            attackUp1 = setup("Player/Attacking sprites/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("Player/Attacking sprites/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);

            attackDown1 = setup("Player/Attacking sprites/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("Player/Attacking sprites/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);

            attackLeft1 = setup("Player/Attacking sprites/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("Player/Attacking sprites/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);

            attackRight1 = setup("Player/Attacking sprites/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("Player/Attacking sprites/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }

        if(currentWeapon.type == type_axe){
            attackUp1 = setup("Player/Attacking sprites/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("Player/Attacking sprites/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);

            attackDown1 = setup("Player/Attacking sprites/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("Player/Attacking sprites/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);

            attackLeft1 = setup("Player/Attacking sprites/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("Player/Attacking sprites/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);

            attackRight1 = setup("Player/Attacking sprites/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("Player/Attacking sprites/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }

           }

    public void update() {

        // Invincible state
        if (invincible) {
            //allows to player to get dmg just once each second and not once per frame
            //giving him an invincibility time which last a second
            //must be outside the keyPressed if
            invincibilityCounter++;
            if (invincibilityCounter > gp.getFPS()) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }

        //ATTACKING
        if (attacking) {
            attacking();
        }
        //COLLISION, AND EVENTS
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

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK INTERACTIVE TILES COLLISION
            gp.cChecker.checkEntity(this, gp.iTile);

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

            if(keyH.enterPressed && !attackCanceled){
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            //reset
            attackCanceled = false;
            gp.keyH.enterPressed = false;


            //ANIMATION
            spriteCounter++;
            if (spriteCounter > 18) {
                if (spriteNum == 1) spriteNum = 2;
                else spriteNum = 1;
                spriteCounter = 0;
            }

        }


        //PLAYER PROJECTILE CHECKER
        // (shoot 1 at a tile since we check if it's still alive or not, and if it has not passed 30 frames and if it has resource for it)
        if(gp.keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.haveResource(this)){

            // SET DEFAULT COORDINATES, DIRECTION AND USER
            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT THE COST (MANA, AMMO,...)
            projectile.subtractResource(this);

            // ADD IT TO THE LIST
            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;

            gp.playSE(10);

        }

        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        if(life > maxLife){
            life = maxLife;
        }

        if(mana > maxMana){
            mana = maxMana;
        }
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
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            //After checking collision restore original data
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

            // PICKUP ONLY ITEMS
            if(gp.obj[i].type == type_pickupOnly){
                gp.obj[i].use(this);
                gp.obj[i] = null;
            }
            else{
                // INVENTORY ITEMS
                String text;
                if(inventory.size() != maxInventorySize){
                    inventory.add(gp.obj[i]);
                    gp.playSE(1);
                    text = "Got a " + gp.obj[i].name + "!";
                }
                else{
                    text = "You cannot carry any more items!";
                }
                gp.ui.addMessage(text);
                gp.obj[i] = null;
            }


        }
    }

    public void interactNPC(int i) {
        if (gp.keyH.enterPressed) {
            if (i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible && !gp.monster[i].dying) {
                gp.playSE(6);

                int damage = gp.monster[i].attack - defense;
                if (damage < 0){
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i, int attack) {
        if (i != 999) {
            if(!gp.monster[i].invincible) {
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;
                if (damage < 0){
                    damage = 0;
                }

                gp.monster[i].life -= damage;
                gp.ui.addMessage(damage + " damage!");

                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                //monster dies
                if(gp.monster[i].life <= 0){
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
                    gp.ui.addMessage("Exp +" + gp.monster[i].exp);
                   exp += gp.monster[i].exp;
                   checkLevelUp();

                }
            }
        }
        else{
//            System.out.println("Miss!");
        }
    }

    public void damageInteractiveTile(int i){
        if(i != 999 && gp.iTile[i].destructible
                && gp.iTile[i].isCorrectItem(this) && !gp.iTile[i].invincible){
           gp.iTile[i].playSE();
           gp.iTile[i].life--;
           gp.iTile[i].invincible = true;

           if(gp.iTile[i].life == 0 ) {
               gp.iTile[i] = gp.iTile[i].getDestroyedForm();
           }
        }
    }

    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n" + "You feel stronger!";


        }
    }

    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe){
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }

            if(selectedItem.type == type_shield){
                currentShield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
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
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));


        //DEBUG
//        g2.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2.setColor(Color.WHITE);
//        g2.drawString("InvisibleCounter: "+invincibilityCounter,10,400);
    }
}

package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {
    GamePanel gp;
    private Graphics2D g2;

    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;

    public boolean messageOn = false;

    public ArrayList<String> message = new ArrayList<>();
    public ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNumber = 0;
    public int titleScreenState = 0; // 0: the first screen; 1: the second screen

    public int slotCol = 0;
    public int slotRow = 0;

    int subState = 0;
    int counter;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("Font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getClassLoader().getResourceAsStream("Font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        //Create HUD OBJ
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;


    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        //g2.setFont(purisaB);

        //gives hint on how to render font (Kinda makes it smoother)
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        //PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }

        //PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        //DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory();
        }

        //OPTION STATE
        if (gp.gameState == gp.optionState) {
            drawOptionsScreen();
        }

        //GAME OVER STATE
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        //GAME OVER STATE
        if (gp.gameState == gp.transitionState) {
            drawTransition();
        }
    }



    public void addMessage(String text){
        message.add(text);
        messageCounter.add(0);
    }

    public void drawPlayerLife() {

        //gp.player.life = 5; //debug

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //DRAW MAX LIFE
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        //RESET paint position
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gp.player.life) {
            //Draw 1st half heart
            g2.drawImage(heart_half, x, y, null);
            i++;

            //if current life (life) has more that half heart, draw the full heart
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }

            //go paint next part of the next heart
            i++;
            x += gp.tileSize;
        }

        // DRAW MAX MANA
        x = gp.tileSize/2 - 5;
        y = (int)(gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        // DRAW MANA
        x = gp.tileSize/2 - 5;
        y = (int)(gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }

    }

    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));

        for(int i = 0; i < message.size(); i++){
            if(message.get(i) != null){
                g2.setColor(Color.BLACK);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.WHITE);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // messagecounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;
                if(messageCounter.get(i) > 108){
                    message.remove(i); // remove
                    messageCounter.remove(i);
                }

            }
        }
    }

    public void drawTitleScreen() {

        if (titleScreenState == 0) {
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Blue Boy Adventure";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 3;

            //Shadows
            g2.setColor(Color.gray);
            g2.drawString(text, x + 5, y + 5);

            //Main Color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //Blue Boy Image
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 2;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "New Game";
            x = getXForCenteredText(text);
            y += gp.tileSize * 3.5;
            g2.drawString(text, x, y);
            if (commandNumber == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Load Game";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Quit";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }
        // CLASS SELECTION SCREEN
        /*
        else if (titleScreenState == 1) {
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select your class!";
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXForCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if (commandNumber == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Thief";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Sorcerer";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Back";
            x = getXForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNumber == 3) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }
*/
    }

    private void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));

        //SHADOW
        text = "Game Over";
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text,x, y);

        //MAIN TEXT
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        //RETRY
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXForCenteredText(text);
        y = y +gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNumber == 0){
            g2.drawString(">", x-40, y);
        }


        //BACK to the Tile Screen
        text = "Quit";
        x = getXForCenteredText(text);
        y = y + 55;
        g2.drawString(text, x, y);
        if(commandNumber == 1){
            g2.drawString(">", x-40, y);
        }

    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";

        int x = this.getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);

    }

    public void drawDialogueScreen() {
        //Dialogue Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 34F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) { // write dialogues better with format by spitting the strings after \n simbol (g2 doesnt read \n)
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen() {
        // Create a frame
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;

        final int lineHeight = 35;

        // NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;

        g2.drawString("Life", textX, textY);
        textY += lineHeight;

        g2.drawString("Mana", textX, textY);
        textY += lineHeight;

        g2.drawString("Strength", textX, textY);
        textY += lineHeight;

        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;

        g2.drawString("Attack", textX, textY);
        textY += lineHeight;

        g2.drawString("Defense", textX, textY);
        textY += lineHeight;

        g2.drawString("Exp", textX, textY);
        textY += lineHeight;

        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;

        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 10;  // more space under

        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;  // more space under

        g2.drawString("Shield", textX, textY);
        textY += lineHeight;


        // VALUES
        int tailX = (frameX + frameWidth) - 30;

        //Reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;


        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Weapon Images
        g2.drawImage(gp.player.currentWeapon.down1, tailX-gp.tileSize, textY-24, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX-gp.tileSize, textY-24, null);

    }


    public void drawInventory(){

        //FRAME
        int frameX = gp.tileSize * 12;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;

        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // DRAW PLAYER ITEMS
        for(int i = 0; i < gp.player.inventory.size(); i++){

            // HIGHLIGHT EQUIP CURSOR
            if(gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield){
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            if(i==4 || i==9 || i==14){
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // CURSOR to select 1 of the 5x4 matrix of inventory
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(cursorX, cursorY, cursorWidth, cursorHeight);

        // DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;

        // DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        //find item index in the array based on the position in the matrix of the inventory (position in row/col)
        int itemIndex = getItemIndexOnSlot();

        // draw description only if an item is selected
        if(itemIndex < gp.player.inventory.size()){
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            String description = gp.player.inventory.get(itemIndex).description;

            for(String line : description.split("\n")){
                g2.drawString(line,textX, textY);
                textY += 32;
            }

        }
    }

    public void drawOptionsScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        //SUB SCREEN
        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState){
            case 0: option_top(frameX, frameY); break;
            case 1: option_fullScreenNotification(frameX, frameY); break;
            case 2: option_control(frameX, frameY); break;
            case 3: option_endGameConfirmation(frameX, frameY); break;
        }
        gp.keyH.enterPressed = false;
    }

    public void option_top(int frameX, int frameY){
        int textX;
        int textY;

        // TITLE
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //FULLSCREN ON/OFF
        textX = frameX + gp.tileSize;
        textY = textY + gp.tileSize * 2;
        g2.drawString("Full Screen", textX, textY);
        if(commandNumber == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                gp.fullScreenOn = !gp.fullScreenOn;
                subState = 1;
            }
        }

        //MUSIC
        textY = textY + gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNumber == 1){
            g2.drawString(">", textX-25, textY);
        }

        //SE
        textY = textY + gp.tileSize;
        g2.drawString("SE", textX, textY);
        if(commandNumber == 2){
            g2.drawString(">", textX-25, textY);
        }

        //CONTROL
        textY = textY + gp.tileSize;
        g2.drawString("Control", textX, textY);
        if(commandNumber == 3){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 2;
                commandNumber = 0;
            }
        }

        //END GAME
        textY = textY + gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if(commandNumber == 4){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 3;
                commandNumber = 0;
            }
        }

        //BACK
        textY = textY + gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if(commandNumber == 5){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                gp.gameState = gp.playState;
                commandNumber = 0;
            }
        }

        //FULL SCREEN CHECKBOX
        textX = frameX + (int)(gp.tileSize * 4.5);
        textY = frameY + gp.tileSize *2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY,24, 24);
        if(gp.fullScreenOn){
            g2.fillRect(textX, textY, 24, 24);
        }


        //MUSIC VOLUME divided in 5 levels
        textY = textY + gp.tileSize;
        g2.drawRect(textX, textY,120, 24); // 120/24 = 24
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        //SE VOLUME divided in 5 levels
        textY = textY + gp.tileSize;
        g2.drawRect(textX, textY,120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        //Save current options
        gp.config.saveConfig();
    }

    public void option_fullScreenNotification(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        //NOTIFICATION
        currentDialogue = "The change will take \neffect after restarting \nthe game";
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // BACK
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNumber == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed) {
                subState = 0;
            }
        }

    }
    public void option_control(int frameX, int frameY){
        int textX;
        int textY;

        // TITLE
        String text = "Control";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //CONTROLLS
        textX = frameX + gp.tileSize;
        textY = textY + gp.tileSize;

        g2.drawString("Move", textX, textY); textY = textY + gp.tileSize;
        g2.drawString("Confirm/Attack", textX, textY); textY = textY + gp.tileSize;
        g2.drawString("Shoot/Cast", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("Character Screen", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("Pause", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("Options", textX, textY);textY = textY + gp.tileSize;

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("ENTER", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("F", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("C", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("P", textX, textY);textY = textY + gp.tileSize;
        g2.drawString("ESC", textX, textY);textY = textY + gp.tileSize;

        //BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNumber == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 0;
            }
        }

    }

    private void option_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        //TEXT
        currentDialogue = "Quit the game and \nreturn to the title screen?";
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //YES
        String text = "Yes";
        textX = getXForCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text, textX, textY);
        if(commandNumber == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 0;
                gp.music.stop();
                gp.gameState = gp.titleState;
            }
        }

        //NO
        text = "No";
        textX = getXForCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNumber == 1){
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 0;
                commandNumber = 4;
            }
        }

    }


    public void drawTransition(){
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);

        if(counter == 50) {
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;

            //next event won't hit if player doesn't move first a tile away
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }

    }
    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);

        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public int getXForAlignToRight(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }
}

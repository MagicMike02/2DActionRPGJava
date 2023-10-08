package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    private Graphics2D g2;

    Font maruMonica, purisaB;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNumber = 0;


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

    }

    public void showMessage(String text) {
        this.message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(maruMonica);
        //g2.setFont(purisaB);

        //gives hint on how to render font (Kinda makes it smoother)
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        //TITLE STATE
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        //PLAY STATE
        if(gp.gameState == gp.playState){
            //do playstate stuff
        }

        //PAUSE STATE
        if(gp.gameState == gp.pauseState){
             this.drawPauseScreen();
        }

        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState){
            this.drawDialogueScreen();
        }

    }

    public void drawTitleScreen(){

        g2.setColor(new Color(0,0,0));
        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Blue Boy Adventure";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;

        //Shadows
        g2.setColor(Color.gray);
        g2.drawString(text, x+5, y+5);

        //Main Color
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //Blue Boy Image
        x = gp.screenWidth/2 - (gp.tileSize*2)/2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "New Game";
        x = getXForCenteredText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if(commandNumber == 0){
            g2.drawString(">", x-gp.tileSize, y);
        }


        text = "Load Game";
        x = getXForCenteredText(text);
        y += gp.tileSize ;
        g2.drawString(text, x, y);
        if(commandNumber == 1){
            g2.drawString(">", x-gp.tileSize, y);
        }

        text = "Quit";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNumber == 2){
            g2.drawString(">", x-gp.tileSize, y);
        }


    }

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";

        int x = this.getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);

    }

    public void drawDialogueScreen(){
        //Dialogue Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 34F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")){ // write dialogues better with format by spitting the strings after \n simbol (g2 doesnt read \n)
            g2.drawString(line,x,y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0);
        g2.setColor(c);

        g2.fillRoundRect(x, y, width, height,35, 35);

        c = new Color(255,255,255, 210);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10,25,25);
    }
    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}

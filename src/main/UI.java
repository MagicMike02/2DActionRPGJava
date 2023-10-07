package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Font arial_40, arial_80B;
//    BufferedImage keyImage;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    private Graphics2D g2;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

//        display key image in the ui
//        OBJ_Key key = new OBJ_Key(gp);
//        keyImage = key.image;
    }

    public void showMessage(String text) {
        this.message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
        g2.setColor(Color.WHITE);

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

    public void drawDialogueScreen(){
        //Dialogue Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
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

    public void drawPauseScreen(){
        String text = "PAUSED";

        int x = this.getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);

    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}

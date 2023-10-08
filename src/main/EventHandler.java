package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect[][] eventRect;

    //Make an event be triggered again, after the player moves at least 1 tile away
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
            }
        }


    }

    public void checkEvent() {

        //check if the player is more than 1 tile away from the last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance,yDistance);
        if(distance > gp.tileSize){
            canTouchEvent = true;
        }

        if(canTouchEvent){
            if(hit(27, 16, "right")) damagePit(27, 16, gp.dialogueState);
            if(hit(23, 19, "any")) damagePit(27, 16, gp.dialogueState);

            if(hit(26, 17, "down")) teleport(26, 17, gp.dialogueState);
            if(hit(23, 12, "up")) healthPool(23, 12, gp.dialogueState);

        }

    }

    //checks the event collision
    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;
        //Get player solidArea Position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        //Get eventRect solidArea Position
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        //Check if there is a collision
        if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false){
            if(gp.player.direction.contentEquals((reqDirection)) || reqDirection.contentEquals("any")){
                hit = true;

                //memorize the position of the last event
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void teleport(int col, int row, int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!";
        gp.player.worldX = gp.tileSize * 37;
        gp.player.worldY = gp.tileSize * 9;
    }

    public void damagePit(int col, int row, int gameState){
        gp.gameState = gameState;
        gp.playSE(6);
        gp.ui.currentDialogue = "You walked into a pit!";
        gp.player.life -= 1;
//        eventRect[col][row].eventDone = true;
        canTouchEvent = false;
    }
    public void healthPool(int col, int row, int gameState){
        //Need to press enter to get healing
        if(gp.keyH.enterPressed){
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.playSE(2);
            gp.ui.currentDialogue = "You drink water. \nYour life has been recovered";
            gp.player.life = gp.player.maxLife;
            gp.aSetter.setMonster();
        }
    }

}

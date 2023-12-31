package main;

import entity.Entity;

public class EventHandler {
    GamePanel gp;
    EventRect[][][] eventRect;

    //Make an event be triggered again, after the player moves at least 1 tile away
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;

        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;

                if(row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
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
            if(hit(0, 27, 16, "right")) damagePit(gp.dialogueState);
            else if(hit(0, 23, 19, "any")) damagePit(gp.dialogueState);
            else if(hit(0, 23, 12, "up")) healthPool(gp.dialogueState);
            else if(hit(0, 10, 39, "any")) teleport(1,12,13);
            else if(hit(1,12,13, "any")) teleport(0, 10, 39);
            else if(hit(1,12,9, "up")) speak(gp.npc[1][0]);
        }

    }



    //checks the event collision
    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;

        if(map == gp.currentMap) {
            //Get player solidArea Position
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            //Get eventRect solidArea Position
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            //Check if there is a collision
            if(gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false){
                if(gp.player.direction.contentEquals((reqDirection)) || reqDirection.contentEquals("any")){
                    hit = true;

                    //memorize the position of the last event
                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;
    }


    public void teleport (int map, int col, int row) {

        gp.gameState = gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;
        canTouchEvent = false;

        gp.playSE(13);
    }

    public void damagePit(int gameState){
        gp.gameState = gameState;
        gp.playSE(6);
        gp.ui.currentDialogue = "You walked into a pit!";
        gp.player.life -= 1;
//        eventRect[col][row].eventDone = true;
        canTouchEvent = false;
    }
    public void healthPool(int gameState){
        //Need to press enter to get healing
        if(gp.keyH.enterPressed){
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.playSE(2);
            gp.ui.currentDialogue = "You drink water. \nYour life and mana has been recovered";
            gp.player.life = gp.player.maxLife;

            gp.player.mana = gp.player.maxMana;

            gp.aSetter.setMonster();
        }
    }

    private void speak(Entity entity) {
        if(gp.keyH.enterPressed){
            gp.gameState = gp.dialogueState;
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }

}

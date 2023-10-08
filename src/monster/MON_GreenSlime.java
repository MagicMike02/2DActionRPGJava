package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_GreenSlime extends Entity {

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        type = 2;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage(){
        up1 = setup("Monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("Monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("Monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("Monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("Monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("Monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("Monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("Monster/greenslime_down_2", gp.tileSize, gp.tileSize);
    }

    public void setAction(){

        //pick up an action each 120 frame (2 seconds)
        actionLockCounter++;
        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick up a number from 1 to 100

            if(i <= 25){
                direction = "up";
            }
            if(i > 25 && i <= 50){
                direction = "down";
            }
            if(i > 50 && i <= 75){
                direction = "right";
            }
            if(i > 75 && i <= 100){
                direction = "left";
            }

            actionLockCounter = 0;
        }



    }

    public void damageReaction(){

        //change direction same as the player (seems that he runs away from the player)
        actionLockCounter = 0;
        direction = gp.player.direction;

    }
}

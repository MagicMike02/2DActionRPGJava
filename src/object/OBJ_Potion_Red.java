package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {


    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        type = type_consumable;
        name = "Red Potion";
        value = 5;

        down1  = setup("Object/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 25;

    }

    public void use(Entity entity){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + "!\n"
                +  "Your life has been recovered by " + value + ".";

        entity.life += value;

        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(2);
    }



}

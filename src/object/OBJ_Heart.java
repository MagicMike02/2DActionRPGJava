package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp){
        super(gp);
        type = type_pickupOnly;

        name = "Key";
        value = 2;

        down1 = setup("Object/heart_full", gp.tileSize, gp.tileSize);

        image = setup("Object/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("Object/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("Object/heart_blank", gp.tileSize, gp.tileSize);

    }

    public void use(Entity entity){
        gp.playSE(2);
        gp.ui.addMessage("Life + " + value);
        entity.life += value;
    }


}

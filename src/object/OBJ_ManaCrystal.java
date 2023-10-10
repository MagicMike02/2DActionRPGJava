package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {
    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);

        name = "Mana Crystal";
        type = type_pickupOnly;
        value = 1;

        down1 = setup("Object/manacrystal_full", gp.tileSize, gp.tileSize);

        image = setup("Object/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("Object/manacrystal_blank", gp.tileSize, gp.tileSize);
    }

    public void use(Entity entity){
        gp.playSE(2);
        gp.ui.addMessage("Mana + " + value);
        entity.mana += value;
    }
}

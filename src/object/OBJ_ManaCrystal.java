package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {
    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);

        name = "Mana Crystal";

        image = setup("Object/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("Object/manacrystal_blank", gp.tileSize, gp.tileSize);
    }
}

package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Key extends Entity {

    public OBJ_Key(GamePanel gp){
        super(gp);
        name = "Key";
        down1  = setup("Object/key", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\n It opens a door.";

    }

}

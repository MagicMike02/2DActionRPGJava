package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Rock extends Projectile {

    public OBJ_Rock(GamePanel gp) {
        super(gp);
        name = "Rock";
        speed = 8;
        maxLife = 80; // 80 frames of duration of the projectile
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();

    }

    public void getImage(){
        up1 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down1 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left1 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right1 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("Projectile/rock_down_1", gp.tileSize, gp.tileSize);
    }

    public boolean haveResource(Entity user){
        boolean haveResource = false;
        if(user.ammo >= useCost){
            haveResource = true;
        }
        return haveResource;
    }

    public void subtractResource(Entity user){
        user.ammo -= useCost;
    }

}

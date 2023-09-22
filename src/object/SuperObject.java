package object;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.GamePanel;
public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    public void draw(Graphics2D g2, GamePanel gp){

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        System.out.println("check if draw item");
        if( worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                System.out.println("draw ->" + image);
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }

    }

}

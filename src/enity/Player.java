package enity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    public Player(GamePanel gp, KeyHandler key){
        this.gp = gp;
        this.keyH = key;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        this.x = 100;
        this.y = 100;
        this.speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Player/Walking sprites/boy_right_2.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void update(){

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){ // Moving animation
            if (keyH.upPressed) {
                direction = "up";
                y -= speed;
            }
            else if (keyH.downPressed) {
                direction = "down";
                y += speed;
            }
            else if (keyH.leftPressed) {
                direction = "left";
                x -= speed;
            }
            else if (keyH.rightPressed) {
                direction = "right";
                x += speed;
            }
            spriteCounter++;
            if( spriteCounter > 18){
                if(spriteNum == 1) spriteNum = 2;
                else spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }
    public void draw(Graphics2D g2){

        //g2.setColor(Color.white);
        //g2.fillRect(x, y , gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up" -> image = spriteNum == 1 ? up1 : up2;
            case "down" -> image = spriteNum == 1 ? down1 : down2;
            case "left" ->  image = spriteNum == 1 ? left1 : left2;
            case "right" ->  image = spriteNum == 1 ? right1 : right2;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}

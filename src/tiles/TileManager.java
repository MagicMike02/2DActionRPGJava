package tiles;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10]; // number of different tiles in the game

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage(){
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/Old version/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/Old version/wall.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/Old version/water.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/Old version/earth.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/Old version/tree.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tiles/Old version/sand.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

// LOADMAP CHAT GPT
    public void loadMap() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Map/world01.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int col = 0;
            int row = 0;
            String line;

            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                List<Integer> numbers = parseLine(line);

                for (int num : numbers) {
                    if (col < gp.maxWorldCol) {
                        mapTileNum[col][row] = num;
                        col++;
                    }
                }

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public void loadMap(){
        try{
            InputStream is = getClass().getClassLoader().getResourceAsStream("Map/world01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow)   {
                String line = br.readLine();

                while(col < gp.maxWorldCol){
                    List<Integer> numbers = parseLine(line);

                    for (int num : numbers) {
                        if (col < gp.maxWorldCol) {
                            mapTileNum[col][row] = num;
                            col++;
                        }
                    }
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
*/
    private List<Integer> parseLine(String line) {
        String[] numStrings = line.split(" ");
        List<Integer> numbers = new ArrayList<>();

        for (String numStr : numStrings) {
            try {
                int num = Integer.parseInt(numStr);
                numbers.add(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return numbers;
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
           int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            //boundaries of the camera to just render the pixel seen
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }


            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}

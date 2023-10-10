package tile_interactive;

import main.GamePanel;

public class IT_Trunk extends InteractiveTile{

    public IT_Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row);

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("Tiles_Interactive/trunk", gp.tileSize, gp.tileSize);

        // solid area of the trunk (deafult is an entire tile and player could not walk on it)
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }
}

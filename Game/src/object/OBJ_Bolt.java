package object;

import main.gamepanel;

public class OBJ_Bolt extends SuperObject {
    public OBJ_Bolt(gamepanel gp) {
        name = "Bolt";
        image = setup("bolt", gp);
        drawOffsetX = 0;
        drawOffsetY = 0;
        solidArea.x = gp.TileSize - 28 - 5;
        solidArea.y = gp.TileSize - 28 - 7;
        solidArea.width = 21;
        solidArea.height = 21;
    }
}
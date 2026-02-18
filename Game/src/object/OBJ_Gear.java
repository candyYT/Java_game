package object;

import main.gamepanel;
import javax.swing.*;
import java.awt.*;

public class OBJ_Gear extends SuperObject {
    private ImageIcon gifImage;

    public OBJ_Gear(gamepanel gp) {
        name = "Gear";
        drawOffsetX = -14;
        drawOffsetY = 28;

        try {
            gifImage = new ImageIcon(getClass().getResource("/objects/gear.gif"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        solidArea.x = drawOffsetX;
        solidArea.y = drawOffsetY;
        solidArea.width = 32;
        solidArea.height = 32;
    }

    public void draw(Graphics2D g2, gamepanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        screenX += drawOffsetX;
        screenY += drawOffsetY;

        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {
            if (gifImage != null) {
                gifImage.paintIcon(null, g2, screenX, screenY);
            }
        }
    }
}
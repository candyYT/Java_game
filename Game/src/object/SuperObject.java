package object;

import main.gamepanel;
import javax.swing.*;
import java.awt.*;

public class SuperObject {
    public ImageIcon image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public int drawOffsetX;//смещение в тайле
    public int drawOffsetY;
    public Rectangle solidArea = new Rectangle(0,0,60,60);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;


    public void draw(Graphics2D g2, gamepanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        screenX += drawOffsetX;
        screenY += drawOffsetY;

        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {
            image.paintIcon(null, g2, screenX, screenY);
        }
    }
}
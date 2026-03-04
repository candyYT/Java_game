package object;

import main.UtilityTool;
import main.gamepanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public int drawOffsetX;
    public int drawOffsetY;
    public Rectangle solidArea = new Rectangle(0, 0, 60, 60);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public BufferedImage setup(String imagePath, gamepanel gp) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/objects/" + imagePath + ".png"));
            img = uTool.scaleImage(img, 2 * gp.TileSize, 2 * gp.TileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
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
            if (image != null) {
                g2.drawImage(image, screenX, screenY, null);
            }
        }
    }
}
package object;

import main.UtilityTool;
import main.gamepanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SuperObject {
    public ImageIcon image;
    public BufferedImage image_;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public int drawOffsetX;//смещение в тайле
    public int drawOffsetY;
    public Rectangle solidArea = new Rectangle(0,0,60,60);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;


    public BufferedImage setup(String imagePath, gamepanel gp){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image_ = null;
        try {
            image_ = ImageIO.read(getClass().getResourceAsStream("/objects/"+ imagePath+".png"));
            image_ = uTool.scaleImage(image_, 2*gp.TileSize, 2*gp.TileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image_;
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
            image.paintIcon(null, g2, screenX, screenY);
        }
    }
}
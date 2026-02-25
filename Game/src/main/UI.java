package main;

import object.OBJ_Gear;
import object.OBJ_Heart;
import object.SuperObject;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    gamepanel gp;
    BufferedImage heart_full, heart_empty;
    Font arial_40;
    BufferedImage gearIcon;
    public boolean messageOn = false;
    public String message = "";

    public UI(gamepanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);

        try {
            gearIcon = ImageIO.read(getClass().getResourceAsStream("/objects/gear_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        OBJ_Heart heart = new OBJ_Heart(gp);
        heart_full = heart.heart_full;
        heart_empty = heart.heart_empty;
    }
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(gearIcon, gp.TileSize, gp.TileSize, gp.TileSize, gp.TileSize, null);
        g2.drawString("x " + gp.player.moneyCount, 50, 50);
        drawPlayerLife(g2);
        if (messageOn){
            g2.drawString(message, 1880, 50);
        }
    }

    public void drawPlayerLife(Graphics2D g2) {
        int x = 50;
        int y = 110;

        for (int i = 0; i < gp.player.maxLife; i++) {
            g2.drawImage(heart_empty, x, y, gp.TileSize, gp.TileSize, null);
            x += gp.TileSize;
        }

        x = 50;

        for (int i = 0; i < gp.player.life; i++) {
            g2.drawImage(heart_full, x, y, gp.TileSize, gp.TileSize, null);
            x += gp.TileSize;
        }
    }
}
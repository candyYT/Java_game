package main;

import object.OBJ_Gear;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;

public class UI {
    gamepanel gp;
    Font arial_40;
    BufferedImage gearIcon;
    public UI(gamepanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Gear gear = new OBJ_Gear();
        try{gearIcon = ImageIO.read(getClass().getResourceAsStream("/objects/gear_1.png"));}catch (IOException e){
            e.printStackTrace();
        }
    }
    public  void draw(Graphics2D g2){
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(gearIcon, gp.TileSize, gp.TileSize, gp.TileSize,gp.TileSize, null);
        g2.drawString("x "+gp.player.moneyCount,50 , 50);
    }
}

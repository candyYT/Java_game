package entity;

import main.UtilityTool;
import main.gamepanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    gamepanel gp;
    int originalTileSize;
    float scale;
    int TileSize;
    int ScreenRow;
    int ScreenHeight;
    int floor;
    public double velocityY;
    public double gravity;
    public double jumpForce;
    public int worldX, worldY;
    public int speed;
    public boolean onGround;
    public boolean isJumping;
    public boolean isFalling;

    public BufferedImage Moving1left, Moving2left, Standing1right, Standing2right, Moving1right, Moving2right, Standing1left, Standing2left, JumpingLeft, JumpingRight, FallingLeft, FallingRight;
    public String direction;
    public int spriteCounter=0;
    public int spriteNum=1;
    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn;
    public String name;
    public int actionLockCounter;

    public int maxLife;
    public int life;

    public Entity(gamepanel gp){
        this.gp = gp;
    }
    public void setAction(){}
    public void update(){

        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn){
            switch (direction){
                case "right": worldX +=speed; break;
                case "left": worldX -=speed; break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 20){
            if (spriteNum==1){
                spriteNum=2;
            } else if (spriteNum==2) {
                spriteNum=1;
            }
            spriteCounter = 0;
        }

    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {
            if (direction == null) {
                direction = "right";
            }

            switch (direction){
                case "left":
                    if (spriteNum == 1){
                        image = Moving1left;
                    }
                    if (spriteNum == 2){
                        image = Moving2left;
                    }
                    break;
                case "right":
                    if (spriteNum == 1){
                        image = Moving1right;
                    }
                    if (spriteNum == 2){
                        image = Moving2right;
                    }
                    break;
                case "standRight":
                    if (spriteNum == 1){
                        image = Standing1right;
                    }
                    if (spriteNum == 2){
                        image = Standing2right;
                    }
                    break;
                case "standLeft":
                    if (spriteNum == 1){
                        image = Standing1left;
                    }
                    if (spriteNum == 2){
                        image = Standing2left;
                    }
                    break;
                default:
                    image = Standing1right;
                    break;
            }
            if (gp.showHitboxes) {
                g2.setColor(Color.RED);
                g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
        g2.drawImage(image, screenX, screenY, 2*gp.TileSize, 2*gp.TileSize, null);
        }
    }
    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, 2*gp.TileSize, 2*gp.TileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
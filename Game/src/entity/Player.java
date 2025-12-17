package entity;

import main.gamepanel;
import main.Movement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    gamepanel GamePanel;
    Movement movement;
    public Player(gamepanel Gamepanel, Movement movement){
        this.GamePanel = Gamepanel;
        this.movement = movement;
        setDefaultStats();
        getPlayerImage();
    }
public void setDefaultStats(){
        originalTileSize = 32;
        scale = (float) 60 /32;
        TileSize= (int) ((originalTileSize*scale));
        ScreenRow=18;
        ScreenHeight = TileSize*ScreenRow;
        x = 0;

        y = 780;
        speed = 10;
        velocityY = 0;
        gravity = 0.8;
        floor = ScreenHeight - TileSize * 5;
        jumpForce = -15;
        

}
public void getPlayerImage(){
        try {
        Moving1left = ImageIO.read(getClass().getResourceAsStream("/player/Moving1.png"));
        Moving2left = ImageIO.read(getClass().getResourceAsStream("/player/Moving2.png"));
        Standing1 = ImageIO.read(getClass().getResourceAsStream("/player/Standing1.png"));
        Standing2 = ImageIO.read(getClass().getResourceAsStream("/player/Standing2.png"));
        Moving1right = ImageIO.read(getClass().getResourceAsStream("/player/Moving1right.png"));
        Moving2right = ImageIO.read(getClass().getResourceAsStream("/player/Moving2right.png"));
        Standing1Left = ImageIO.read(getClass().getResourceAsStream("/player/Standing1Left.png"));
        Standing2Left = ImageIO.read(getClass().getResourceAsStream("/player/Standing2Left.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
}
public void update(){

    if (Movement.jump && onGround) {
        velocityY = jumpForce;
        onGround = false;
        isJumping = true;
    }

    if (!onGround) {
        direction = "jump";
        y += (int) velocityY;
        velocityY += gravity;

        if (y >= floor) {
            y = floor;
            velocityY = 0;
            onGround = true;
            isJumping = false;
        }
    }
    if (movement.left){
        direction = "right";
        x-=speed;
    } else if (movement.right) {
        direction = "left";
        x+=speed;
    } else if (movement.lastPressed=='a') {direction="standLeft";} else {direction = "standRight";}
    spriteCounter++;
    if (spriteCounter > 20){
        if (spriteNum==1){spriteNum=2;} else if (spriteNum==2) {spriteNum=1;
        }
        spriteCounter = 0;
    }
}
public void draw(Graphics2D g2){
    BufferedImage image = null;

    switch (direction){
        case "left":
            if (spriteNum == 1){
            image = Moving1left;}
            if (spriteNum == 2){
                image = Moving2left;
            }
            break;
        case "right":
            if (spriteNum == 1){
                image = Moving1right;}
            if (spriteNum == 2){
                image = Moving2right;
            }
            break;
        case "standRight":
            if (spriteNum == 1){
                image = Standing1;}
            if (spriteNum == 2){
                image = Standing2;
            }
            break;
        case "standLeft":
            if (spriteNum == 1){
                image = Standing1Left;}
            if (spriteNum == 2){
                image = Standing2Left;}
            break;
    }
    g2.drawImage(image, x, y, TileSize, TileSize*2, null);

}
}

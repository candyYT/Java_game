package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
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

    public BufferedImage Moving1left, Moving2left, Standing1, Standing2, Moving1right, Moving2right,Standing1Left, Standing2Left, JumpingLeft, JumpingRight, FallingLeft, FallingRight;
    public String direction;
    public int spriteCounter=0;
    public int spriteNum=1;
    public Rectangle solidArea;
    public boolean collision = false;
    public boolean collisionOn;

}
package entity;

import java.awt.image.BufferedImage;

public class Entity {
    int originalTileSize;
    float scale;
    int TileSize;
    int ScreenRow;
    int ScreenHeight;
    int floor;
    double velocityY;
    double gravity;
    double jumpForce;
    public int x,y;
    public int speed;
    boolean onGround;
    boolean isJumping;

    public BufferedImage Moving1left, Moving2left, Standing1, Standing2, Moving1right, Moving2right,Standing1Left, Standing2Left;
    public String direction;
    public int spriteCounter=0;
    public int spriteNum=1;

}

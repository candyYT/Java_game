package entity;

import main.UtilityTool;
import main.gamepanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    protected gamepanel gp;
    int originalTileSize;
    float scale;
    int TileSize;
    int ScreenRow;
    int ScreenHeight;
    int floor;
    public double velocityY = 0;
    public double gravity = 0.8;
    public boolean onGround = false;
    public double jumpForce;
    public int worldX, worldY;
    public int speed;
    public boolean isJumping;
    public boolean isFalling;
    protected int knockbackSpeedX = 0;
    protected int knockbackSpeedY = 0;
    protected int knockbackFrames = 0;
    protected final int knockbackDuration = 10;
    public BufferedImage Moving1left, Moving2left, Standing1right, Standing2right, Moving1right, Moving2right, Standing1left, Standing2left, JumpingLeft, JumpingRight, FallingLeft, FallingRight;
    public BufferedImage Attack1left, Attack2left, Attack1right, Attack2right;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn;
    public String name;
    public int actionLockCounter;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public boolean attacking = false;
    public int attackCounter = 0;
    public int maxLife;
    public int life;
    public int dropChance = 0;
    public int dropAmount = 0;
    public int aggroRangeX = 0;
    public int aggroRangeY = 0;
    public int aggroOffsetX = 0;
    public int aggroOffsetY = 0;
    public boolean aggro = false;

    public boolean knockbackable = true;

    public Entity(gamepanel gp) {
        this.gp = gp;
    }

    public void knockbackFromPlayer(int directionSign, int strength) {
        knockbackSpeedX = strength * directionSign / knockbackDuration;
        knockbackSpeedY = -strength / knockbackDuration;
        knockbackFrames = knockbackDuration;
        onGround = false;
        velocityY = knockbackSpeedY;
    }

    public void checkAggro() {
        if (aggroRangeX == 0 || gp.player == null) return;
        int centerX = worldX + solidArea.x + solidArea.width / 2;
        int centerY = worldY + solidArea.y + solidArea.height / 2;
        int leftX, rightX, topY, bottomY;
        if ("right".equals(direction)) {
            leftX = centerX + aggroOffsetX;
            rightX = leftX + aggroRangeX;
        } else if ("left".equals(direction)) {
            rightX = centerX - aggroOffsetX;
            leftX = rightX - aggroRangeX;
        } else {
            leftX = centerX + aggroOffsetX;
            rightX = leftX + aggroRangeX;
        }
        topY = centerY + aggroOffsetY;
        bottomY = topY + aggroRangeY;
        Rectangle aggroArea = new Rectangle(leftX, topY, aggroRangeX, aggroRangeY);
        Rectangle playerRect = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );
        aggro = aggroArea.intersects(playerRect);
    }

    public void update() {
        checkAggro();
        if (!onGround) {
            velocityY += gravity;
        }
        int deltaX = 0;
        int deltaY = 0;
        if (knockbackFrames > 0) {
            deltaX = knockbackSpeedX;
            deltaY = (int) velocityY;
            knockbackFrames--;
            if (knockbackFrames == 0) {
                knockbackSpeedX = 0;
                knockbackSpeedY = 0;
            }
        } else {
            setAction();
            if (!collisionOn) {
                if (direction.equals("right")) deltaX = speed;
                else if (direction.equals("left")) deltaX = -speed;
            }
            deltaY = (int) velocityY;
        }
        if (deltaX != 0) {
            int oldX = worldX;
            worldX += deltaX;
            collisionOn = false;
            gp.cChecker.checkTile(this);
            if (collisionOn) {
                worldX = oldX;
                if (knockbackFrames > 0) {
                    knockbackFrames = 0;
                    knockbackSpeedX = 0;
                    knockbackSpeedY = 0;
                }
            }
        }
        if (deltaY != 0) {
            int oldY = worldY;
            worldY += deltaY;
            collisionOn = false;
            gp.cChecker.checkTile(this);
            if (collisionOn) {
                worldY = oldY;
                if (deltaY > 0) {
                    onGround = true;
                    velocityY = 0;
                } else if (deltaY < 0) {
                    velocityY = 0;
                }
                if (knockbackFrames > 0) {
                    knockbackFrames = 0;
                    knockbackSpeedX = 0;
                    knockbackSpeedY = 0;
                }
            } else {
                onGround = false;
            }
        }
        spriteCounter++;
        if (spriteCounter > 20) {
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    protected void setAction() {}

    public void takeDamage(int amount) {
        if (!invincible) {
            life -= amount;
            if (life < 0) life = 0;
            invincible = true;
            invincibleCounter = 0;
        }
    }

    public void die() {}

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
            switch (direction) {
                case "left":
                    if (spriteNum == 1) image = Moving1left;
                    if (spriteNum == 2) image = Moving2left;
                    break;
                case "right":
                    if (spriteNum == 1) image = Moving1right;
                    if (spriteNum == 2) image = Moving2right;
                    break;
                case "standRight":
                    if (spriteNum == 1) image = Standing1right;
                    if (spriteNum == 2) image = Standing2right;
                    break;
                case "standLeft":
                    if (spriteNum == 1) image = Standing1left;
                    if (spriteNum == 2) image = Standing2left;
                    break;
                default:
                    image = Standing1right;
                    break;
            }
            if (gp.showHitboxes) {
                g2.setColor(Color.RED);
                g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
            g2.drawImage(image, screenX, screenY, 2 * gp.TileSize, 2 * gp.TileSize, null);
        }
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
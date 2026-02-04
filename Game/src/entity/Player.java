package entity;

import main.Dash;
import main.gamepanel;
import main.Movement;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    gamepanel GamePanel;
    Movement movement;
    public Dash dash;

    public final int screenY;
    public final int screenX;
    public int moneyCount = 0;

    private boolean canDash = true;
    private float dashCooldownTimer = 0;

    public Player(gamepanel Gamepanel, Movement movement){
        this.GamePanel = Gamepanel;
        this.movement = movement;
        this.dash = new Dash(movement);

        screenX = GamePanel.ScreenWidth/2;
        screenY = GamePanel.TileSize*11;

        solidArea = new Rectangle();
        solidArea.x = 3;
        solidArea.y = 20;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 56;
        solidArea.height = 100;

        setDefaultStats();
        getPlayerImage();
    }

    public void setDefaultStats(){
        originalTileSize = 32;
        scale = (float) 60 /32;
        TileSize= (int) ((originalTileSize*scale));
        ScreenRow=18;
        ScreenHeight = TileSize*ScreenRow;


        worldX = GamePanel.TileSize * 22;
        worldY = GamePanel.TileSize * 20;

        speed = 10;
        velocityY = 0;
        gravity = 0.8;
        floor = TileSize*13;
        jumpForce = -15;
        onGround = false;
        isJumping = false;
        isFalling = false;
        canDash = true;
        dashCooldownTimer = 0;
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
        int oldWorldX = worldX;
        int oldWorldY = worldY;

        if (!canDash) {
            dashCooldownTimer -= (float) 1 / 60;
            if (dashCooldownTimer <= 0) {
                canDash = true;
                dashCooldownTimer = 0;
            }
        }

        if (movement.shift && canDash) {
            dash.startDash(movement.lastPressed);
            canDash = false;
            dashCooldownTimer = 0.6F;
        }

        dash.update();

        if (Movement.jump && onGround){
            velocityY = jumpForce;
            isJumping = true;
            onGround = false;
            isFalling = false;
        }

        if (isJumping && velocityY >= 0) {
            isJumping = false;
            isFalling = true;
        }

        if (!onGround){
            velocityY += gravity;
            worldY += (int) velocityY;
        } else {
            velocityY = 0;
        }

        if (movement.left){
            direction = "left";
        } else if (movement.right) {
            direction = "right";
        } else if (movement.lastPressed=='a') {
            direction="standLeft";
        } else {
            direction = "standRight";
        }

        collisionOn = false;
        GamePanel.cChecker.checkTile(this);
        int objIndex = GamePanel.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        if (dash.shouldApplyDashMovement()) {
            applyDashMovement();
        } else {
            if (movement.left && !collisionOn){
                worldX -= speed;
            }
            if (movement.right && !collisionOn){
                worldX += speed;
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

        if (worldX != oldWorldX || worldY != oldWorldY) {
            GamePanel.tileManager.checkTransition(worldX, worldY);
        }
    }

    private void applyDashMovement() {
        collisionOn = false;
        GamePanel.cChecker.checkTile(this);

        if (!collisionOn) {
            if (dash.dashDirection.equals("left")) {
                worldX -= dash.dashSpeed;
            } else if (dash.dashDirection.equals("right")) {
                worldX += dash.dashSpeed;
            }
        } else {
            dash.stopDash();
        }
    }

    public void pickUpObject(int i){
        if(i!=999){
            String objectName = GamePanel.obj[i].name;

            switch (objectName){
                case "Gear":
                    moneyCount++;
                    GamePanel.obj[i]=null;
                    System.out.println("Gear collected! Total: " + moneyCount);
                    break;
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        switch (direction){
            case "right":
                if (spriteNum == 1){
                    image = Moving1left;
                }
                if (spriteNum == 2){
                    image = Moving2left;
                }
                break;
            case "left":
                if (spriteNum == 1){
                    image = Moving1right;
                }
                if (spriteNum == 2){
                    image = Moving2right;
                }
                break;
            case "standRight":
                if (spriteNum == 1){
                    image = Standing1;
                }
                if (spriteNum == 2){
                    image = Standing2;
                }
                break;
            case "standLeft":
                if (spriteNum == 1){
                    image = Standing1Left;
                }
                if (spriteNum == 2){
                    image = Standing2Left;
                }
                break;
        }
        if (image != null) {
            g2.drawImage(image, screenX, screenY, TileSize, TileSize*2, null);
        }
    }
}
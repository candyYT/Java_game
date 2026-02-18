package entity;

import main.Dash;
import main.gamepanel;
import main.Movement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    Movement movement;
    public Dash dash;

    public boolean invincible = false;
    public int invincibleCounter = 0;
    public final int invincibleDuration = 30; //неуязвимость в кадрах

    private int knockbackSpeedX = 0;
    private int knockbackSpeedY = 0;
    private int knockbackFrames = 0;
    private final int knockbackDuration = 10;
    private String withBolt = "_with_bolt";

    public final int screenY;
    public final int screenX;
    public int moneyCount = 0;
    public boolean hasBolt = false;

    public Player(gamepanel gp, Movement movement){
        super(gp);
        this.gp = gp;
        this.movement = movement;
        this.dash = new Dash(movement);

        screenX = this.gp.ScreenWidth/2;
        screenY = this.gp.ScreenHeight/2+gp.TileSize;

        solidArea = new Rectangle();
        solidArea.x = 20;
        solidArea.y = 10;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 2*gp.TileSize-42;
        solidArea.height = 2*gp.TileSize-20;


        setDefaultStats();
        getPlayerImage();
        direction = "standRight";
    }

    public void setDefaultStats(){
        originalTileSize = 32;
        scale = (float) 60 /32;
        TileSize= (int) ((originalTileSize*scale));
        ScreenRow=18;
        ScreenHeight = TileSize*ScreenRow;



        worldX = gp.TileSize * 22;
        worldY = gp.TileSize * 20;

        maxLife = 6;
        life = maxLife;

        speed = 10;
        velocityY = 0;
        gravity = 0.8;
        floor = TileSize*13;
        jumpForce = -15;
        onGround = false;
        isJumping = false;
        isFalling = false;
    }

    public void getPlayerImage(){
        if(hasBolt){
            Moving1left = setup("/player/Moving_left_1"+withBolt);
            Moving2left = setup("/player/Moving_left_2"+withBolt);
            Moving1right = setup("/player/Moving_right_1"+withBolt);
            Moving2right = setup("/player/Moving_right_2"+withBolt);
            Standing1right = setup("/player/Standing_right_1"+withBolt);
            Standing2right = setup("/player/Standing_right_2"+withBolt);
            Standing1left = setup("/player/Standing_left_1"+withBolt);
            Standing2left = setup("/player/Standing_left_2"+withBolt);


        }
        else {
        Moving1left = setup("/player/Moving_left_1");
        Moving2left = setup("/player/Moving_left_2");
        Moving1right = setup("/player/Moving_right_1");
        Moving2right = setup("/player/Moving_right_2");
        Standing1right = setup("/player/Standing_right_1");
        Standing2right = setup("/player/Standing_right_2");
        Standing1left = setup("/player/Standing_left_1");
        Standing2left = setup("/player/Standing_left_2");
        }



    }

    public void update(){
        int oldWorldX = worldX;
        int oldWorldY = worldY;

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

        if (movement.shiftPressed && !dash.isDashing) {
            dash.startDash(movement.lastPressed);
            movement.shiftPressed = false;
        }

        if (dash.shouldApplyDashMovement()) {
            applyDashMovement();
            collisionOn = false;
            gp.cChecker.checkTile(this);
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            if (collisionOn && dash.isDashing) {
                worldX = oldWorldX;
                dash.stopDash();
            }
        } else {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

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

        if (!invincible) {
            for (int i = 0; i < gp.monster.length; i++) {
                if (gp.monster[i] != null) {
                    Rectangle playerRect = new Rectangle(
                            worldX + solidArea.x,
                            worldY + solidArea.y,
                            solidArea.width,
                            solidArea.height
                    );
                    Rectangle monsterRect = new Rectangle(
                            gp.monster[i].worldX + gp.monster[i].solidArea.x,
                            gp.monster[i].worldY + gp.monster[i].solidArea.y,
                            gp.monster[i].solidArea.width,
                            gp.monster[i].solidArea.height
                    );

                    if (playerRect.intersects(monsterRect)) {
                        life--;
                        if (life < 0) life = 0;


                        int targetX;
                        if (worldX < gp.monster[i].worldX) {
                            targetX = -200;
                        } else {
                            targetX = 200;
                        }
                        int targetY = -200;
                        knockbackSpeedX = targetX / knockbackDuration;
                        knockbackSpeedY = targetY / knockbackDuration;
                        knockbackFrames = knockbackDuration;

                        invincible = true;
                        invincibleCounter = 0;

                        break;
                    }
                }
            }
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > invincibleDuration) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (knockbackFrames > 0) {

            int oldX = worldX;
            int oldY = worldY;

            worldX += knockbackSpeedX;
            worldY += knockbackSpeedY;

            collisionOn = false;
            gp.cChecker.checkTile(this);

            if (collisionOn) {
                worldX = oldX;
                worldY = oldY;
                knockbackFrames = 0;
                knockbackSpeedX = 0;
                knockbackSpeedY = 0;
            } else {
                knockbackFrames--;
                if (knockbackFrames == 0) {
                    knockbackSpeedX = 0;
                    knockbackSpeedY = 0;
                }
            }
        }

        if (life <= 0) {
            moneyCount = 0;
            gp.respawn();
        }

        if (worldX != oldWorldX || worldY != oldWorldY) {
            gp.tileManager.checkTransition(worldX, worldY);
        }
    }

    private void applyDashMovement() {
        if (!collisionOn) {
            int dashDistanceToWall = calculateDashDistanceToWall();

            if (dashDistanceToWall >= 1) {
                int actualDash = dash.dashSpeed;
                if (dashDistanceToWall < dash.dashSpeed) {
                    actualDash = dashDistanceToWall;
                }

                if (dash.dashDirection.equals("left")) {
                    worldX -= actualDash;
                } else if (dash.dashDirection.equals("right")) {
                    worldX += actualDash;
                }
            } else {
                dash.stopDash();
            }
        } else {
            dash.stopDash();
        }
    }

    private int calculateDashDistanceToWall() {
        int maxDistance = dash.dashSpeed;
        int checkDistance = 1;
        int entityLeftWorldX = worldX + solidArea.x;
        int entityRightWorldX = worldX + solidArea.x + solidArea.width;
        int entityTopWorldY = worldY + solidArea.y;
        int entityBottomWorldY = worldY + solidArea.y + solidArea.height;

        int entityTopRow = entityTopWorldY / gp.TileSize;
        int entityBottomRow = entityBottomWorldY / gp.TileSize;

        if (dash.dashDirection.equals("left")) {
            for (int distance = checkDistance; distance <= maxDistance; distance += checkDistance) {
                int checkLeftCol = (entityLeftWorldX - distance) / gp.TileSize;

                for (int row = entityTopRow; row < entityBottomRow; row++) {
                    if (row >= 0 && row < gp.maxWorldRow && checkLeftCol >= 0 && checkLeftCol < gp.maxWorldCol) {
                        int tileNum = gp.tileManager.mapTileNum[checkLeftCol][row];
                        if (tileNum >= 0 && tileNum < gp.tileManager.tile.length &&
                                gp.tileManager.tile[tileNum] != null &&
                                gp.tileManager.tile[tileNum].collision) {
                            int safeDistance = distance - checkDistance;
                            if (safeDistance < 0) {
                                safeDistance = 0;
                            }
                            return safeDistance;
                        }
                    }
                }
            }
            return maxDistance;
        }
        else if (dash.dashDirection.equals("right")) {
            for (int distance = checkDistance; distance <= maxDistance; distance += checkDistance) {
                int checkRightCol = (entityRightWorldX + distance) / gp.TileSize;

                for (int row = entityTopRow; row < entityBottomRow; row++) {
                    if (row >= 0 && row < gp.maxWorldRow && checkRightCol >= 0 && checkRightCol < gp.maxWorldCol) {
                        int tileNum = gp.tileManager.mapTileNum[checkRightCol][row];
                        if (tileNum >= 0 && tileNum < gp.tileManager.tile.length &&
                                gp.tileManager.tile[tileNum] != null &&
                                gp.tileManager.tile[tileNum].collision) {
                            int safeDistance = distance - checkDistance;
                            if (safeDistance < 0) {
                                safeDistance = 0;
                            }
                            return safeDistance;
                        }
                    }
                }
            }
            return maxDistance;
        }

        return maxDistance;
    }

    public void pickUpObject(int i){
        if(i != 999 && i >= 0 && i < gp.obj.length && gp.obj[i] != null){
            String objectName = gp.obj[i].name;
            if (objectName != null) {
                switch (objectName){
                    case "Gear":
                        moneyCount++;
                        gp.obj[i]=null;
                        //GamePanel.ui.showMessage("+1");
                        break;
                    case "Bolt":
                        hasBolt = true;
                        getPlayerImage();
                        gp.obj[i]=null;
                        break;
                }
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        if (direction == null || movement.left&& movement.right) {
            direction = "standRight";
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

        if (image == null) {
            image = Standing1right;
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, null);
        }
        if (gp.showHitboxes) {
            g2.setColor(Color.RED);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
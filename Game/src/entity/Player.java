package entity;

import main.Dash;
import main.gamepanel;
import main.Movement;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    Movement movement;
    public Dash dash;
    public final int invincibleDuration = 30;
    private int knockbackSpeedX = 0;
    private int knockbackSpeedY = 0;
    private int knockbackFrames = 0;
    private final int knockbackDuration = 10;
    private String withBolt = "_with_bolt";
    public final int screenY;
    public final int screenX;
    public int moneyCount = 0;
    public boolean hasBolt = false;
    int attackDuration = 30;
    public int attackCooldown = 0;
    public final int attackCooldownDuration = 10;
    private BufferedImage currentAttackImage;
    private int attackOffsetX;
    public int staminaMax = 100;
    public int stamina = staminaMax;
    public double staminaRegen = 0.5;
    public int attackStaminaCost = 20;
    public int dashStaminaCost = 30;
    public int healthLevel = 0;
    public int damageLevel = 0;
    public int staminaLevel = 0;
    public int baseCostHealth = 30;
    public int costIncreaseHealth = 30;
    public int baseCostDamage = 40;
    public int costIncreaseDamage = 35;
    public int baseCostStamina = 25;
    public int costIncreaseStamina = 22;
    public int healthPerLevel = 1;
    public int damagePerLevel = 1;
    public int staminaPerLevel = 20;
    public int attackDamage = 1;
    public boolean onCheckpoint = false;

    public Player(gamepanel gp, Movement movement) {
        super(gp);
        this.gp = gp;
        this.movement = movement;
        this.dash = new Dash(movement, this);
        screenX = this.gp.ScreenWidth / 2;
        screenY = this.gp.ScreenHeight / 2 + gp.TileSize;
        solidArea = new Rectangle();
        solidArea.x = 20;
        solidArea.y = 10;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 2 * gp.TileSize - 42;
        solidArea.height = 2 * gp.TileSize - 20;
        attackArea.width = 29 * 4;
        attackArea.height = 24 * 2;
        setDefaultStats();
        getPlayerImage();
        getPlayerAttackImage();
        direction = "standRight";
        attackOffsetX = 0;
    }

    public void setDefaultStats() {
        originalTileSize = 32;
        scale = (float) 60 / 32;
        TileSize = (int) ((originalTileSize * scale));
        ScreenRow = 18;
        ScreenHeight = TileSize * ScreenRow;
        worldX = gp.TileSize * 22;
        worldY = gp.TileSize * 20;
        maxLife = 6;
        life = maxLife;
        speed = 10;
        velocityY = 0;
        gravity = 0.8;
        floor = TileSize * 13;
        jumpForce = -15;
        onGround = false;
        isJumping = false;
        isFalling = false;
    }

    public void getPlayerImage() {
        if (hasBolt) {
            Moving1left = setup("/player/Moving_left_1" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Moving2left = setup("/player/Moving_left_2" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Moving1right = setup("/player/Moving_right_1" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Moving2right = setup("/player/Moving_right_2" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Standing1right = setup("/player/Standing_right_1" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Standing2right = setup("/player/Standing_right_2" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Standing1left = setup("/player/Standing_left_1" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
            Standing2left = setup("/player/Standing_left_2" + withBolt, gp.TileSize * 2, gp.TileSize * 2);
        } else {
            Moving1left = setup("/player/Moving_left_1", gp.TileSize * 2, gp.TileSize * 2);
            Moving2left = setup("/player/Moving_left_2", gp.TileSize * 2, gp.TileSize * 2);
            Moving1right = setup("/player/Moving_right_1", gp.TileSize * 2, gp.TileSize * 2);
            Moving2right = setup("/player/Moving_right_2", gp.TileSize * 2, gp.TileSize * 2);
            Standing1right = setup("/player/Standing_right_1", gp.TileSize * 2, gp.TileSize * 2);
            Standing2right = setup("/player/Standing_right_2", gp.TileSize * 2, gp.TileSize * 2);
            Standing1left = setup("/player/Standing_left_1", gp.TileSize * 2, gp.TileSize * 2);
            Standing2left = setup("/player/Standing_left_2", gp.TileSize * 2, gp.TileSize * 2);
        }
    }

    public void getPlayerAttackImage() {
        Attack1left = setup("/player/Attack_left_1", gp.TileSize * 4, gp.TileSize * 2);
        Attack2left = setup("/player/Attack_left_2", gp.TileSize * 4, gp.TileSize * 2);
        Attack1right = setup("/player/Attack_right_1", gp.TileSize * 4, gp.TileSize * 2);
        Attack2right = setup("/player/Attack_right_2", gp.TileSize * 4, gp.TileSize * 2);
    }

    public void update() {
        int oldWorldX = worldX;
        int oldWorldY = worldY;
        if (stamina < staminaMax) {
            stamina += staminaRegen;
            if (stamina > staminaMax) stamina = staminaMax;
        }
        dash.update();
        if (Movement.jump && onGround) {
            velocityY = jumpForce;
            isJumping = true;
            onGround = false;
            isFalling = false;
        }
        if (isJumping && velocityY >= 0) {
            isJumping = false;
            isFalling = true;
        }
        if (!onGround) {
            velocityY += gravity;
            worldY += (int) velocityY;
        } else {
            velocityY = 0;
        }
        if (movement.left) {
            direction = "left";
        } else if (movement.right) {
            direction = "right";
        } else if (movement.lastPressed == 'a') {
            direction = "standLeft";
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
            if (movement.left && !collisionOn) {
                worldX -= speed;
            }
            if (movement.right && !collisionOn) {
                worldX += speed;
            }
            spriteCounter++;
            if (spriteCounter > 20) {
                if (spriteNum == 1) spriteNum = 2;
                else if (spriteNum == 2) spriteNum = 1;
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
        if (Movement.interact) {
            if (onCheckpoint) {
                gp.inUpgradeMenu = true;
            } else if (!hasBolt) {
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] != null && "Bolt".equals(gp.obj[i].name)) {
                        Rectangle playerRect = new Rectangle(
                                worldX + solidArea.x,
                                worldY + solidArea.y,
                                solidArea.width,
                                solidArea.height
                        );
                        Rectangle objRect = new Rectangle(
                                gp.obj[i].worldX + gp.obj[i].solidArea.x,
                                gp.obj[i].worldY + gp.obj[i].solidArea.y,
                                gp.obj[i].solidArea.width,
                                gp.obj[i].solidArea.height
                        );
                        if (playerRect.intersects(objRect)) {
                            hasBolt = true;
                            getPlayerImage();
                            gp.obj[i] = null;
                            break;
                        }
                    }
                }
            }
            Movement.interact = false;
        }
        if (life <= 0) {
            moneyCount = 0;
            gp.respawn();
        }
        if (movement.mouseLeftPressed) {
            if (hasBolt && !attacking && attackCooldown == 0 && stamina >= attackStaminaCost) {
                attacking = true;
                attackCounter = 0;
                stamina -= attackStaminaCost;
            }
            movement.mouseLeftPressed = false;
        }
        if (attacking) {
            attacking();
        }
        if (attackCooldown > 0) {
            attackCooldown--;
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

    public void attacking() {
        attackCounter++;
        if (attackCounter <= attackDuration / 2) {
            if (direction.equals("left") || direction.equals("standLeft")) {
                currentAttackImage = Attack1left;
                attackOffsetX = -2 * gp.TileSize;
            } else {
                currentAttackImage = Attack1right;
                attackOffsetX = 0;
            }
        } else {
            if (direction.equals("left") || direction.equals("standLeft")) {
                currentAttackImage = Attack2left;
                attackOffsetX = -2 * gp.TileSize;
            } else {
                currentAttackImage = Attack2right;
                attackOffsetX = 0;
            }
        }
        if (attackCounter == attackDuration / 2) {
            checkAttackCollision();
        }
        if (attackCounter > attackDuration) {
            attacking = false;
            attackCounter = 0;
            attackCooldown = attackCooldownDuration;
            getPlayerImage();
        }
    }

    private void checkAttackCollision() {
        int originalWorldX = worldX;
        int originalSolidX = solidArea.x;
        int originalSolidY = solidArea.y;
        int originalSolidWidth = solidArea.width;
        int originalSolidHeight = solidArea.height;
        switch (direction) {
            case "left", "standLeft" -> worldX -= attackArea.width;
            case "right", "standRight" -> worldX += attackArea.width;
        }
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;
        Rectangle attackRect = new Rectangle(
                worldX + solidArea.x,
                worldY + solidArea.y,
                solidArea.width,
                solidArea.height
        );
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null) {
                Rectangle monsterRect = new Rectangle(
                        gp.monster[i].worldX + gp.monster[i].solidArea.x,
                        gp.monster[i].worldY + gp.monster[i].solidArea.y,
                        gp.monster[i].solidArea.width,
                        gp.monster[i].solidArea.height
                );
                if (attackRect.intersects(monsterRect)) {
                    gp.monster[i].takeDamage(attackDamage);
                    int dirSign = (direction.equals("right") || direction.equals("standRight")) ? 1 : -1;
                    gp.monster[i].knockbackFromPlayer(dirSign, 200);
                }
            }
        }
        worldX = originalWorldX;
        solidArea.x = originalSolidX;
        solidArea.y = originalSolidY;
        solidArea.width = originalSolidWidth;
        solidArea.height = originalSolidHeight;
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
                            if (safeDistance < 0) safeDistance = 0;
                            return safeDistance;
                        }
                    }
                }
            }
            return maxDistance;
        } else if (dash.dashDirection.equals("right")) {
            for (int distance = checkDistance; distance <= maxDistance; distance += checkDistance) {
                int checkRightCol = (entityRightWorldX + distance) / gp.TileSize;
                for (int row = entityTopRow; row < entityBottomRow; row++) {
                    if (row >= 0 && row < gp.maxWorldRow && checkRightCol >= 0 && checkRightCol < gp.maxWorldCol) {
                        int tileNum = gp.tileManager.mapTileNum[checkRightCol][row];
                        if (tileNum >= 0 && tileNum < gp.tileManager.tile.length &&
                                gp.tileManager.tile[tileNum] != null &&
                                gp.tileManager.tile[tileNum].collision) {
                            int safeDistance = distance - checkDistance;
                            if (safeDistance < 0) safeDistance = 0;
                            return safeDistance;
                        }
                    }
                }
            }
            return maxDistance;
        }
        return maxDistance;
    }

    public void pickUpObject(int i) {
        if (i != 999 && i >= 0 && i < gp.obj.length && gp.obj[i] != null) {
            String objectName = gp.obj[i].name;
            if (objectName != null) {
                switch (objectName) {
                    case "Gear":
                        moneyCount++;
                        String gearId = "Gear_" + gp.obj[i].worldX / gp.TileSize + "_" + gp.obj[i].worldY / gp.TileSize;
                        gp.collectedObjects.computeIfAbsent(gp.currentMap, k -> new java.util.HashSet<>()).add(gearId);
                        gp.obj[i] = null;
                        break;
                    case "Bolt":
                        if (!hasBolt) {
                            hasBolt = true;
                            getPlayerImage();
                            String boltId = "Bolt_" + gp.obj[i].worldX / gp.TileSize + "_" + gp.obj[i].worldY / gp.TileSize;
                            gp.collectedObjects.computeIfAbsent(gp.currentMap, k -> new java.util.HashSet<>()).add(boltId);
                            gp.obj[i] = null;
                        }
                        break;
                }
            }
        }
    }

    public void upgradeStat(String stat) {
        int cost = 0;
        switch (stat) {
            case "Health":
                cost = baseCostHealth + healthLevel * costIncreaseHealth;
                if (moneyCount >= cost) {
                    moneyCount -= cost;
                    healthLevel++;
                    maxLife += healthPerLevel;
                    life = maxLife;
                }
                break;
            case "Damage":
                cost = baseCostDamage + damageLevel * costIncreaseDamage;
                if (moneyCount >= cost) {
                    moneyCount -= cost;
                    damageLevel++;
                    attackDamage += damagePerLevel;
                }
                break;
            case "Stamina":
                cost = baseCostStamina + staminaLevel * costIncreaseStamina;
                if (moneyCount >= cost) {
                    moneyCount -= cost;
                    staminaLevel++;
                    staminaMax += staminaPerLevel;
                    stamina = staminaMax;
                }
                break;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int drawX = screenX;
        if (direction == null || (movement.left && movement.right)) {
            direction = "standRight";
        }
        if (attacking) {
            image = currentAttackImage;
            drawX = screenX + attackOffsetX;
        } else {
            switch (direction) {
                case "left":
                    image = (spriteNum == 1) ? Moving1left : Moving2left;
                    break;
                case "right":
                    image = (spriteNum == 1) ? Moving1right : Moving2right;
                    break;
                case "standRight":
                    image = (spriteNum == 1) ? Standing1right : Standing2right;
                    break;
                case "standLeft":
                    image = (spriteNum == 1) ? Standing1left : Standing2left;
                    break;
                default:
                    image = Standing1right;
            }
        }
        if (image == null) {
            image = Standing1right;
        }
        g2.drawImage(image, drawX, screenY, null);
        if (gp.showHitboxes) {
            g2.setColor(Color.RED);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
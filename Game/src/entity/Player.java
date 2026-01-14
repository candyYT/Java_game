package entity;

import main.gamepanel;
import main.Movement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    gamepanel GamePanel;
    Movement movement;
    public final int screenY;
    public final int screenX;

    public Player(gamepanel Gamepanel, Movement movement) {
        this.GamePanel = Gamepanel;
        this.movement = movement;

        screenX = GamePanel.ScreenWidth / 2 - (GamePanel.TileSize / 2);
        screenY = GamePanel.TileSize * 13;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = GamePanel.TileSize - 16;
        solidArea.height = GamePanel.TileSize * 2 - 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultStats();
        getPlayerImage();
    }

    public void setDefaultStats() {
        originalTileSize = 32;
        scale = (float) 60 / 32;
        TileSize = (int) ((originalTileSize * scale));
        ScreenRow = 18;
        ScreenHeight = TileSize * ScreenRow;
        worldX = GamePanel.TileSize * 17;
        worldY = GamePanel.TileSize * 10;
        speed = 5; // уменьшил скорость для более плавного управления
        velocityY = 0;
        gravity = 0.5;
        jumpForce = -13; // уменьшил силу прыжка
        onGround = false;
        isFalling = true;
        direction = "standRight";
    }

    public void getPlayerImage() {
        try {
            Moving1left = ImageIO.read(getClass().getResourceAsStream("/player/Moving1.png"));
            Moving2left = ImageIO.read(getClass().getResourceAsStream("/player/Moving2.png"));
            Standing1 = ImageIO.read(getClass().getResourceAsStream("/player/Standing1.png"));
            Standing2 = ImageIO.read(getClass().getResourceAsStream("/player/Standing2.png"));
            Moving1right = ImageIO.read(getClass().getResourceAsStream("/player/Moving1right.png"));
            Moving2right = ImageIO.read(getClass().getResourceAsStream("/player/Moving2right.png"));
            Standing1Left = ImageIO.read(getClass().getResourceAsStream("/player/Standing1Left.png"));
            Standing2Left = ImageIO.read(getClass().getResourceAsStream("/player/Standing2Left.png"));

            // Загружаем изображения прыжка и падения
            try {
                JumpingLeft = ImageIO.read(getClass().getResourceAsStream("/player/JumpingLeft.png"));
                JumpingRight = ImageIO.read(getClass().getResourceAsStream("/player/JumpingRight.png"));
                FallingLeft = ImageIO.read(getClass().getResourceAsStream("/player/FallingLeft.png"));
                FallingRight = ImageIO.read(getClass().getResourceAsStream("/player/FallingRight.png"));
            } catch (Exception e) {
                JumpingLeft = Standing1Left;
                JumpingRight = Standing1;
                FallingLeft = Standing1Left;
                FallingRight = Standing1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Обработка прыжка
        if (Movement.jump && onGround) {
            velocityY = jumpForce;
            onGround = false;
            isJumping = true;
            isFalling = false;
        }

        // Применяем гравитацию
        if (!onGround) {
            velocityY += gravity;

            // Ограничиваем максимальную скорость падения
            if (velocityY > 15) {
                velocityY = 15;
            }

            // Обновляем вертикальную позицию
            worldY += velocityY;

            // Определяем состояние (прыжок/падение)
            if (velocityY > 0) {
                isJumping = false;
                isFalling = true;
            }
        }

        // Проверяем коллизию по вертикали
        boolean verticalCollision = GamePanel.cChecker.checkVerticalCollision(this);

        if (verticalCollision) {
            if (velocityY > 0) { // Приземление
                onGround = true;
                isJumping = false;
                isFalling = false;
                velocityY = 0;
            } else if (velocityY < 0) { // Удар головой
                velocityY = 0;
            }
        } else if (!onGround && velocityY >= 0) {
            onGround = false;
        }

        // Определяем направление для анимации
        String animationDirection = direction;
        if (isJumping) {
            animationDirection = direction.startsWith("left") ? "jumpLeft" : "jumpRight";
        } else if (isFalling) {
            animationDirection = direction.startsWith("left") ? "fallLeft" : "fallRight";
        }

        // Обработка горизонтального движения
        boolean moving = false;
        if (movement.left && !movement.right) {
            direction = "left";
            moving = true;
        } else if (movement.right && !movement.left) {
            direction = "right";
            moving = true;
        } else {
            // Сохраняем последнее направление для стоячей анимации
            if (direction.equals("left") || direction.equals("jumpLeft") ||
                    direction.equals("fallLeft") || direction.equals("standLeft")) {
                direction = "standLeft";
            } else {
                direction = "standRight";
            }
        }

        // Проверяем коллизию по горизонтали только если двигаемся
        if (moving) {
            boolean horizontalCollision = GamePanel.cChecker.checkHorizontalCollision(this);
            if (!horizontalCollision) {
                if (direction.equals("left")) {
                    worldX -= speed;
                } else if (direction.equals("right")) {
                    worldX += speed;
                }
            }
        }

        // Обновление анимации
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        String drawDirection = direction;

        // Определяем направление для отрисовки
        if (isJumping) {
            drawDirection = direction.startsWith("left") || direction.equals("standLeft") ?
                    "jumpLeft" : "jumpRight";
        } else if (isFalling) {
            drawDirection = direction.startsWith("left") || direction.equals("standLeft") ?
                    "fallLeft" : "fallRight";
        }

        switch (drawDirection) {
            case "left":
                image = (spriteNum == 1) ? Moving1left : Moving2left;
                break;
            case "right":
                image = (spriteNum == 1) ? Moving1right : Moving2right;
                break;
            case "standRight":
                image = (spriteNum == 1) ? Standing1 : Standing2;
                break;
            case "standLeft":
                image = (spriteNum == 1) ? Standing1Left : Standing2Left;
                break;
            case "jumpLeft":
                image = JumpingLeft;
                break;
            case "jumpRight":
                image = JumpingRight;
                break;
            case "fallLeft":
                image = FallingLeft;
                break;
            case "fallRight":
                image = FallingRight;
                break;
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, TileSize, TileSize * 2, null);
        }

        // Отладка: хитбокс (можно включить для тестирования)
        // g2.setColor(new Color(255, 0, 0, 100));
        // g2.fillRect(screenX + solidArea.x, screenY + solidArea.y,
        //            solidArea.width, solidArea.height);
    }
}
package main;

import entity.Entity;

public class CollisionChecker {
    gamepanel GamePanel;

    public CollisionChecker(gamepanel GamePanel) {
        this.GamePanel = GamePanel;
    }

    // Проверка вертикальной коллизии
    public boolean checkVerticalCollision(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Тайлы под ногами (для приземления)
        if (entity.velocityY >= 0) {
            int checkRow = (entityBottomWorldY + 1) / GamePanel.TileSize;

            if (checkRow >= 0 && checkRow < GamePanel.maxWorldRow) {
                int leftCol = entityLeftWorldX / GamePanel.TileSize;
                int rightCol = entityRightWorldX / GamePanel.TileSize;

                if (leftCol >= 0 && leftCol < GamePanel.maxWorldCol &&
                        rightCol >= 0 && rightCol < GamePanel.maxWorldCol) {

                    int tileNum1 = GamePanel.tileManager.mapTileNum[leftCol][checkRow];
                    int tileNum2 = GamePanel.tileManager.mapTileNum[rightCol][checkRow];

                    // Если хотя бы один из тайлов под ногами - твердый
                    if (tileNum1 < GamePanel.tileManager.tile.length &&
                            tileNum2 < GamePanel.tileManager.tile.length) {
                        return GamePanel.tileManager.tile[tileNum1].collision ||
                                GamePanel.tileManager.tile[tileNum2].collision;
                    }
                }
            }
        }

        // Тайлы над головой (для прыжка)
        else if (entity.velocityY < 0) {
            int checkRow = (entityTopWorldY - 1) / GamePanel.TileSize;

            if (checkRow >= 0 && checkRow < GamePanel.maxWorldRow) {
                int leftCol = entityLeftWorldX / GamePanel.TileSize;
                int rightCol = entityRightWorldX / GamePanel.TileSize;

                if (leftCol >= 0 && leftCol < GamePanel.maxWorldCol &&
                        rightCol >= 0 && rightCol < GamePanel.maxWorldCol) {

                    int tileNum1 = GamePanel.tileManager.mapTileNum[leftCol][checkRow];
                    int tileNum2 = GamePanel.tileManager.mapTileNum[rightCol][checkRow];

                    if (tileNum1 < GamePanel.tileManager.tile.length &&
                            tileNum2 < GamePanel.tileManager.tile.length) {
                        return GamePanel.tileManager.tile[tileNum1].collision ||
                                GamePanel.tileManager.tile[tileNum2].collision;
                    }
                }
            }
        }

        return false;
    }

    // Проверка горизонтальной коллизии
    public boolean checkHorizontalCollision(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityTopRow = entityTopWorldY / GamePanel.TileSize;
        int entityBottomRow = entityBottomWorldY / GamePanel.TileSize;

        // Движение влево
        if (entity.direction.equals("left")) {
            int checkCol = (entityLeftWorldX - entity.speed) / GamePanel.TileSize;

            if (checkCol >= 0 && checkCol < GamePanel.maxWorldCol) {
                for (int row = entityTopRow; row <= entityBottomRow; row++) {
                    if (row >= 0 && row < GamePanel.maxWorldRow) {
                        int tileNum = GamePanel.tileManager.mapTileNum[checkCol][row];
                        if (tileNum < GamePanel.tileManager.tile.length &&
                                GamePanel.tileManager.tile[tileNum].collision) {
                            return true;
                        }
                    }
                }
            }
        }
        // Движение вправо
        else if (entity.direction.equals("right")) {
            int checkCol = (entityRightWorldX + entity.speed) / GamePanel.TileSize;

            if (checkCol >= 0 && checkCol < GamePanel.maxWorldCol) {
                for (int row = entityTopRow; row <= entityBottomRow; row++) {
                    if (row >= 0 && row < GamePanel.maxWorldRow) {
                        int tileNum = GamePanel.tileManager.mapTileNum[checkCol][row];
                        if (tileNum < GamePanel.tileManager.tile.length &&
                                GamePanel.tileManager.tile[tileNum].collision) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
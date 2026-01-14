package main;

import entity.Entity;

public class CollisionChecker {
    gamepanel GamePanel;
    public CollisionChecker(gamepanel GamePanel){
        this.GamePanel = GamePanel;
    }
    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.x + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / GamePanel.TileSize;
        int entityRightCol = entityRightWorldX / GamePanel.TileSize;
        int entityTopRow = entityTopWorldY / GamePanel.TileSize;
        int entityBottomRow = entityBottomWorldY / GamePanel.TileSize;

        int tileNum1, tileNum2;
        switch(entity.direction){
            case "jump":
                entityTopRow = (int) ((entityTopWorldY - entity.velocityY)/GamePanel.TileSize);
                tileNum1 = GamePanel.tileManager.mapTileNum [entityLeftCol] [entityTopRow];
                tileNum2 = GamePanel.tileManager.mapTileNum [entityRightCol] [entityTopRow];
                if (GamePanel.tileManager.tile[tileNum1].collision == true || GamePanel.tileManager.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            case "standLeft":
                entityBottomRow = (int) ((entityBottomWorldY - entity.gravity)/GamePanel.TileSize);
                tileNum1 = GamePanel.tileManager.mapTileNum [entityLeftCol] [entityBottomRow];
                tileNum2 = GamePanel.tileManager.mapTileNum [entityRightCol] [entityBottomRow];
                if (GamePanel.tileManager.tile[tileNum1].collision == true || GamePanel.tileManager.tile[tileNum2].collision == true){
                    entity.onGround = true;
                } else {
                    entity.onGround = false;
                }
                break;
            case "standRight":
                entityBottomRow = (int) ((entityBottomWorldY - entity.gravity)/GamePanel.TileSize);
                tileNum1 = GamePanel.tileManager.mapTileNum [entityLeftCol] [entityBottomRow];
                tileNum2 = GamePanel.tileManager.mapTileNum [entityRightCol] [entityBottomRow];
                if (GamePanel.tileManager.tile[tileNum1].collision == true || GamePanel.tileManager.tile[tileNum2].collision == true){
                    entity.onGround = true;
                }
                break;
            case "left":
                break;
            case "right":
                break;

        }

    }
}
package main;

import entity.Entity;
import entity.Player;

public class CollisionChecker {
    gamepanel GamePanel;

    public CollisionChecker(gamepanel GamePanel){
        this.GamePanel = GamePanel;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / GamePanel.TileSize;
        int entityRightCol = entityRightWorldX / GamePanel.TileSize;
        int entityTopRow = entityTopWorldY / GamePanel.TileSize;
        int entityBottomRow = entityBottomWorldY / GamePanel.TileSize;

        int tileNum1, tileNum2;

            tileNum1 = GamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            tileNum2 = GamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];

            boolean GroundCollision = GamePanel.tileManager.tile[tileNum1].collision || GamePanel.tileManager.tile[tileNum2].collision;

            if(GroundCollision){//на земле
                if(!entity.onGround && entity.velocityY >= 0){
                    entity.worldY = entityBottomRow * GamePanel.TileSize - entity.solidArea.y - entity.solidArea.height;
                    entity.velocityY = 0;
                    entity.onGround = true;
                    entity.isFalling = false;
                    entity.isJumping = false;
                }
            } else {
                if(entity.onGround){
                    entity.onGround = false;
                    entity.isFalling = true;
                }
            }



        if(entity.isJumping && entity.velocityY < 0)//над персом
        {
            int ceilingRow = entityTopRow - 1;
            if(ceilingRow >= 0){
                tileNum1 = GamePanel.tileManager.mapTileNum[entityLeftCol][ceilingRow];
                tileNum2 = GamePanel.tileManager.mapTileNum[entityRightCol][ceilingRow];

                if(GamePanel.tileManager.tile[tileNum1].collision || GamePanel.tileManager.tile[tileNum2].collision)// удар головой
                {
                    entity.worldY = (ceilingRow + 1) * GamePanel.TileSize - entity.solidArea.y;
                    entity.velocityY = 0.5;
                    entity.isJumping = false;
                    entity.isFalling = true;
                }
            }
        }
        //горизонт коллизия
        switch(entity.direction){
            case "left":
                int leftSpeed = entity.speed;
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (player.dash.isDashing && player.dash.dashDirection.equals("left")) {
                        leftSpeed = player.dash.dashSpeed;
                    }
                }
                int checkLeftCol = (entityLeftWorldX - leftSpeed) / GamePanel.TileSize;
                boolean leftCollision = false;

                for (int row = entityTopRow; row < entityBottomRow; row++) {
                    if (row >= 0 && row < GamePanel.maxWorldRow && checkLeftCol >= 0) {
                        tileNum1 = GamePanel.tileManager.mapTileNum[checkLeftCol][row];
                        if(GamePanel.tileManager.tile[tileNum1].collision) {
                            leftCollision = true;
                            entity.worldX = (checkLeftCol + 1) * GamePanel.TileSize - entity.solidArea.x;
                            break;
                        }
                    }
                }
                entity.collisionOn = leftCollision;
                break;

            case "right":
                int rightSpeed = entity.speed;
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (player.dash.isDashing && player.dash.dashDirection.equals("right")) {
                        rightSpeed = player.dash.dashSpeed;
                    }
                }
                int checkRightCol = (entityRightWorldX + rightSpeed) / GamePanel.TileSize;
                boolean rightCollision = false;
                for (int row = entityTopRow; row < entityBottomRow; row++) {
                    if (row >= 0 && row < GamePanel.maxWorldRow && checkRightCol < GamePanel.maxWorldCol) {
                        tileNum1 = GamePanel.tileManager.mapTileNum[checkRightCol][row];
                        if(GamePanel.tileManager.tile[tileNum1].collision) {
                            rightCollision = true;

                            int wallLeftX = checkRightCol * GamePanel.TileSize;
                            entity.worldX = wallLeftX - entity.solidArea.x - entity.solidArea.width - 1;
                            break;
                        }
                    }
                }
                entity.collisionOn = rightCollision;
                break;
        }

    }

}

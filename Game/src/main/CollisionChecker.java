package main;

import entity.Entity;
import entity.Player;

public class CollisionChecker {
    gamepanel GamePanel;

    public CollisionChecker(gamepanel GamePanel){
        this.GamePanel = GamePanel;
    }

    public void checkTile(Entity entity){
        if (GamePanel.tileManager == null || GamePanel.tileManager.tile == null || GamePanel.tileManager.mapTileNum == null) {
            return;
        }

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / GamePanel.TileSize;
        int entityRightCol = entityRightWorldX / GamePanel.TileSize;
        int entityTopRow = entityTopWorldY / GamePanel.TileSize;
        int entityBottomRow = entityBottomWorldY / GamePanel.TileSize;

        if (entityLeftCol < 0) entityLeftCol = 0;
        if (entityRightCol >= GamePanel.maxWorldCol) entityRightCol = GamePanel.maxWorldCol - 1;
        if (entityTopRow < 0) entityTopRow = 0;
        if (entityBottomRow >= GamePanel.maxWorldRow) entityBottomRow = GamePanel.maxWorldRow - 1;

        int tileNum1, tileNum2;

        try {
            if (entityLeftCol < GamePanel.maxWorldCol && entityBottomRow < GamePanel.maxWorldRow &&
                    entityRightCol < GamePanel.maxWorldCol) {

                tileNum1 = GamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = GamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];

                boolean GroundCollision = false;

                if (tileNum1 >= 0 && tileNum1 < GamePanel.tileManager.tile.length &&
                        GamePanel.tileManager.tile[tileNum1] != null) {
                    if (GamePanel.tileManager.tile[tileNum1].collision) {
                        GroundCollision = true;
                    }
                }
                if (tileNum2 >= 0 && tileNum2 < GamePanel.tileManager.tile.length &&
                        GamePanel.tileManager.tile[tileNum2] != null) {
                    if (GamePanel.tileManager.tile[tileNum2].collision) {
                        GroundCollision = true;
                    }
                }

                if(GroundCollision){
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
            }
        } catch (Exception e) {
        }

        try {
            if(entity.isJumping && entity.velocityY < 0)
            {
                int ceilingRow = entityTopRow - 1;
                if(ceilingRow >= 0 && entityLeftCol < GamePanel.maxWorldCol && entityRightCol < GamePanel.maxWorldCol){
                    tileNum1 = GamePanel.tileManager.mapTileNum[entityLeftCol][ceilingRow];
                    tileNum2 = GamePanel.tileManager.mapTileNum[entityRightCol][ceilingRow];

                    boolean ceilingCollision = false;

                    if (tileNum1 >= 0 && tileNum1 < GamePanel.tileManager.tile.length &&
                            GamePanel.tileManager.tile[tileNum1] != null) {
                        if (GamePanel.tileManager.tile[tileNum1].collision) {
                            ceilingCollision = true;
                        }
                    }
                    if (tileNum2 >= 0 && tileNum2 < GamePanel.tileManager.tile.length &&
                            GamePanel.tileManager.tile[tileNum2] != null) {
                        if (GamePanel.tileManager.tile[tileNum2].collision) {
                            ceilingCollision = true;
                        }
                    }

                    if(ceilingCollision)
                    {
                        entity.worldY = (ceilingRow + 1) * GamePanel.TileSize - entity.solidArea.y;
                        entity.velocityY = 0.5;
                        entity.isJumping = false;
                        entity.isFalling = true;
                    }
                }
            }
        } catch (Exception e) {
        }

        try {
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
                        if (row >= 0 && row < GamePanel.maxWorldRow && checkLeftCol >= 0 && checkLeftCol < GamePanel.maxWorldCol) {
                            tileNum1 = GamePanel.tileManager.mapTileNum[checkLeftCol][row];
                            if (tileNum1 >= 0 && tileNum1 < GamePanel.tileManager.tile.length &&
                                    GamePanel.tileManager.tile[tileNum1] != null &&
                                    GamePanel.tileManager.tile[tileNum1].collision) {
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
                        if (row >= 0 && row < GamePanel.maxWorldRow && checkRightCol >= 0 && checkRightCol < GamePanel.maxWorldCol) {
                            tileNum1 = GamePanel.tileManager.mapTileNum[checkRightCol][row];
                            if (tileNum1 >= 0 && tileNum1 < GamePanel.tileManager.tile.length &&
                                    GamePanel.tileManager.tile[tileNum1] != null &&
                                    GamePanel.tileManager.tile[tileNum1].collision) {
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
        } catch (Exception e) {
        }
    }

    public int checkObject (Entity entity, boolean player){
        int index = 999;

        try {
            for (int i = 0; i < GamePanel.obj.length; i++){
                if (GamePanel.obj[i] != null){
                    entity.solidArea.x = entity.worldX + entity.solidArea.x;
                    entity.solidArea.y = entity.worldY + entity.solidArea.y;

                    GamePanel.obj[i].solidArea.x = GamePanel.obj[i].worldX + GamePanel.obj[i].solidArea.x;
                    GamePanel.obj[i].solidArea.y = GamePanel.obj[i].worldY + GamePanel.obj[i].solidArea.y;

                    switch (entity.direction){
                        case "left":
                            entity.solidArea.x -= entity.speed;
                            if (entity.solidArea.intersects(GamePanel.obj[i].solidArea)){
                                if (GamePanel.obj[i].collision == true){
                                    entity.collisionOn = true;
                                }
                                if(player == true){
                                    index = i;
                                }
                            }
                            break;
                        case "right":
                            entity.solidArea.x += entity.speed;
                            if (entity.solidArea.intersects(GamePanel.obj[i].solidArea)){
                                if (GamePanel.obj[i].collision == true){
                                    entity.collisionOn = true;
                                }
                                if(player == true){
                                    index = i;
                                }
                            }
                            break;
                    }
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    GamePanel.obj[i].solidArea.x = GamePanel.obj[i].solidAreaDefaultX;
                    GamePanel.obj[i].solidArea.y = GamePanel.obj[i].solidAreaDefaultY;
                }
            }
        } catch (Exception e) {
        }

        return index;
    }
}
package tile;

import main.UtilityTool;
import main.gamepanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    gamepanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(gamepanel gp) {
        this.gp = gp;
        tile = new Tile[1010];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        for (int i = 0; i < tile.length; i++) {
            tile[i] = null;
        }

        getTileImage();
        loadMap("/maps/" + gp.currentMap);
    }

    public void getTileImage() {
        setup(0, "air", false);
        setup(5, "dirt_loc1_5", true);
        setup(2, "dirt_rock_loc1_4", true);
        setup(999, "air", false); // переход м/ду 1 и 2 картой
        setup(999, "air", false);//переход м/ду 2 и 1 картой
        setup(100, "checkpoint", false);
        setup(101, "checkpoint", false);

    }
    public void setup(int index, String imagePath, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/" + imagePath + ".png")));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.TileSize, gp.TileSize);
            tile[index].collision = collision;

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row = 0;
            while (row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] numbers = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    if (col < numbers.length) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[col][row] = num;
                    } else {
                        mapTileNum[col][row] = 0;
                    }
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkCheckpoint(int playerWorldX, int playerWorldY) {
        int playerCol = playerWorldX / gp.TileSize;
        int playerRow = playerWorldY / gp.TileSize;
        int playerCenterCol = (playerWorldX + gp.player.solidArea.width/2) / gp.TileSize;
        int playerCenterRow = (playerWorldY + gp.player.solidArea.height/2) / gp.TileSize;

        int[][] checkPoints = {
                {playerCol, playerRow},
                {playerCenterCol, playerCenterRow}
        };

        for (int[] point : checkPoints) {
            int col = point[0];
            int row = point[1];

            if (col >= 0 && col < gp.maxWorldCol &&
                    row >= 0 && row < gp.maxWorldRow) {

                int tileNum = mapTileNum[col][row];

                if (tileNum == 100 || tileNum == 101 || tileNum == 1000 || tileNum == 1001) {
                    gp.checkpointX = col * gp.TileSize + gp.TileSize/2;
                    gp.checkpointY = row * gp.TileSize;
                    gp.checkpointMap = gp.currentMap;
                    gp.checkpointActivated = true;
                }
            }
        }
    }

    public void checkTransition(int playerWorldX, int playerWorldY) {
        checkCheckpoint(playerWorldX, playerWorldY);

        if (playerWorldY >= gp.deathY) {
            if (gp.checkpointActivated) {
                gp.respawn();
            } else {
                gp.player.life = gp.player.maxLife;
                gp.player.worldX = 22 * gp.TileSize;
                gp.player.worldY = 20 * gp.TileSize;
                gp.player.onGround = true;
                gp.player.velocityY = 0;
                gp.player.isJumping = false;
                gp.player.isFalling = false;
            }
            return;
        }

        int playerCol = playerWorldX / gp.TileSize;
        int playerRow = playerWorldY / gp.TileSize;
        int playerCenterCol = (playerWorldX + gp.player.solidArea.width/2) / gp.TileSize;
        int playerCenterRow = (playerWorldY + gp.player.solidArea.height/2) / gp.TileSize;

        int[][] checkPoints = {
                {playerCol, playerRow},
                {playerCenterCol, playerCenterRow}
        };

        for (int[] point : checkPoints) {
            int col = point[0];
            int row = point[1];

            if (col >= 0 && col < gp.maxWorldCol &&
                    row >= 0 && row < gp.maxWorldRow) {

                int tileNum = mapTileNum[col][row];

                for (int i = 0; i < gp.transitionCount; i++) {
                    gamepanel.MapTransition transition = gp.transitions[i];
                    if (transition != null && tileNum == transition.fromMapTile) {
                        gp.changeMap(transition.toMap, transition.targetX, transition.targetY);
                        return;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            if (tileNum < 0 || tileNum >= tile.length) {
                worldCol++;
                if(worldCol == gp.maxWorldCol){
                    worldCol = 0;
                    worldRow++;
                }
                continue;
            }

            if (tile[tileNum] == null) {
                tile[tileNum] = new Tile();
            }

            if (tile[tileNum].image == null) {
                if (tileNum == 100 || tileNum == 101 || tileNum == 1000 || tileNum == 1001) {
                    try {
                        InputStream is = getClass().getResourceAsStream("/tiles/checkpoint.png");
                        if (is != null) {
                            tile[tileNum].image = ImageIO.read(is);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            int worldX = worldCol * gp.TileSize;
            int worldY = worldRow * gp.TileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

                if (tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.TileSize, gp.TileSize, null);
                }
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
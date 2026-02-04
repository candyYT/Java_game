package tile;
import main.gamepanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    gamepanel GamePanel;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(gamepanel GamePanel) {
        this.GamePanel = GamePanel;
        tile = new Tile[1000];
        mapTileNum = new int[GamePanel.maxWorldCol][GamePanel.maxWorldRow];
        getTileImage();
        loadMap("/maps/" + GamePanel.currentMap);
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/air.png")));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/wall.jpg")));
            tile[1].collision = true;
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt_loc1_5.png")));
            tile[5].collision = true;
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt_roots_1st_loc_3.png")));
            tile[3].collision = true;

            tile[999] = new Tile();
            tile[999].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/brick_wall_dark.png")));
            tile[998] = new Tile();
            tile[998].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/brick_wall_dark.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row = 0;
            while (row < GamePanel.maxWorldRow) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] numbers = line.split(" ");
                for (int col = 0; col < GamePanel.maxWorldCol; col++) {
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

    public void checkTransition(int playerWorldX, int playerWorldY) {
        int playerCol = playerWorldX / GamePanel.TileSize;
        int playerRow = playerWorldY / GamePanel.TileSize;
        int playerCenterCol = (playerWorldX + GamePanel.player.solidArea.width/2) / GamePanel.TileSize;
        int playerCenterRow = (playerWorldY + GamePanel.player.solidArea.height/2) / GamePanel.TileSize;

        int[][] checkPoints = {
                {playerCol, playerRow},
                {playerCenterCol, playerCenterRow}
        };

        for (int[] point : checkPoints) {
            int col = point[0];
            int row = point[1];

            if (col >= 0 && col < GamePanel.maxWorldCol &&
                    row >= 0 && row < GamePanel.maxWorldRow) {

                int tileNum = mapTileNum[col][row];

                for (int i = 0; i < GamePanel.transitionCount; i++) {
                    gamepanel.MapTransition transition = GamePanel.transitions[i];
                    if (transition != null && tileNum == transition.fromMapTile) {
                        GamePanel.changeMap(transition.toMap, transition.targetX, transition.targetY);
                        return;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < GamePanel.maxWorldCol && worldRow < GamePanel.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            if (tileNum < 0 || tileNum >= tile.length || tile[tileNum] == null) {
                worldCol++;
                if(worldCol == GamePanel.maxWorldCol){
                    worldCol = 0;
                    worldRow++;
                }
                continue;
            }

            int worldX = worldCol * GamePanel.TileSize;
            int worldY = worldRow * GamePanel.TileSize;
            int screenX = worldX - GamePanel.player.worldX + GamePanel.player.screenX;
            int screenY = worldY - GamePanel.player.worldY + GamePanel.player.screenY;

            if (worldX + GamePanel.TileSize > GamePanel.player.worldX - GamePanel.player.screenX &&
                    worldX - GamePanel.TileSize < GamePanel.player.worldX + GamePanel.player.screenX &&
                    worldY + GamePanel.TileSize > GamePanel.player.worldY - GamePanel.player.screenY &&
                    worldY - GamePanel.TileSize < GamePanel.player.worldY + GamePanel.player.screenY) {

                if (tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, GamePanel.TileSize, GamePanel.TileSize, null);
                }
            }
            worldCol++;
            if (worldCol == GamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
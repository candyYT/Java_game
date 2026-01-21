package tile;
import main.gamepanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    gamepanel GamePanel;
    public Tile [] tile;
    public int[][] mapTileNum;
    public TileManager(gamepanel GamePanel){
        this.GamePanel = GamePanel;
        tile = new Tile[10];
        mapTileNum = new int [GamePanel.maxWorldCol] [GamePanel.maxWorldRow];
        getTileImage();
        loadMap("/maps/map_sample.txt"); // путь к карте
    }
    public void getTileImage(){
        try{
            tile[0]=new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/air.png"));
            tile[1]=new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.jpg"));
            tile[1].collision = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while (col<GamePanel.maxWorldCol && row < GamePanel.maxWorldRow){
                String line = br.readLine();
                while (col<GamePanel.maxWorldCol){
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum [col] [row] = num;
                    col++;
                }
                if (col == GamePanel.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        //g2.drawImage(tile[0].image, 0,GamePanel.ScreenHeight-GamePanel.TileSize*2, GamePanel.TileSize, GamePanel.TileSize, null);
        int worldCol = 0;
        int worldRow = 0;


        while (worldCol<GamePanel.maxWorldCol && worldRow<GamePanel.maxWorldRow){
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * GamePanel.TileSize;
            int worldY = worldRow * GamePanel.TileSize;
            int screenX = worldX- GamePanel.player.worldX + GamePanel.player.screenX;
            int screenY = worldY - GamePanel.player.worldY + GamePanel.player.screenY;

            if (worldX + GamePanel.TileSize > GamePanel.player.worldX - GamePanel.player.screenX &&
                    worldX - GamePanel.TileSize < GamePanel.player.worldX + GamePanel.player.screenX &&
                    worldY + GamePanel.TileSize > GamePanel.player.worldY - GamePanel.player.screenY &&
                   worldY - GamePanel.TileSize < GamePanel.player.worldY + GamePanel.player.screenY)
            {
            g2.drawImage(tile[tileNum].image, screenX, screenY, GamePanel.TileSize, GamePanel.TileSize, null);
            }
            worldCol++;
            if(worldCol == GamePanel.maxWorldCol){
                worldCol=0;
                worldRow++;
            }
        }
    }
}

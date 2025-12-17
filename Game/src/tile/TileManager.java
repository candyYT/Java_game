package tile;
import main.gamepanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {
    gamepanel GamePanel;
    Tile [] tile;
    public TileManager(gamepanel GamePanel){
        this.GamePanel = GamePanel;
        tile = new Tile[1];
        getTileImage();
    }
    public void getTileImage(){
        try{
            tile[0]=new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.jpg"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        //g2.drawImage(tile[0].image, 0,GamePanel.ScreenHeight-GamePanel.TileSize*2, GamePanel.TileSize, GamePanel.TileSize, null);
        int col = 0;
        int row = 0;
        int x = 0;
        int y = GamePanel.ScreenHeight-GamePanel.TileSize*3;
        while (col<GamePanel.ScreenCol && row<GamePanel.ScreenRow){
            g2.drawImage(tile[0].image, x, y, GamePanel.TileSize, GamePanel.TileSize, null);
            col++;
            x+=GamePanel.TileSize;
            if(col == GamePanel.ScreenCol){
                col=0;
                x=0;
                row++;
                y+=GamePanel.TileSize;
            }
        }
    }
}

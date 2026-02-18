package object;

import main.gamepanel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class OBJ_Heart extends SuperObject {
    private final gamepanel gp;
    public BufferedImage heart_full, heart_empty;

    public OBJ_Heart(gamepanel gp) {
        this.gp = gp;
        name = "Heart";
        heart_full = setup("heart_life_1", gp);
        heart_empty = setup("heart_life_2", gp);
    }
}
package object;

import javax.swing.*;

public class OBJ_Gear extends SuperObject {
    public OBJ_Gear(){
        name = "Gear";
        // Смещение на тайле
        drawOffsetX = -14;
        drawOffsetY = 28;

        try {
            image = new ImageIcon(getClass().getResource("/objects/gear.gif"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        solidArea.x = drawOffsetX;
        solidArea.y = drawOffsetY;
        solidArea.width = 32;
        solidArea.height = 32;
    }
}
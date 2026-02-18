package monster;

import entity.Entity;
import main.gamepanel;

public class MON_CorruptedOne extends Entity {
    public MON_CorruptedOne (gamepanel gp){
        super(gp);
        name = "Corrupted One";
        speed = 5;
        maxLife = 4;
        life = maxLife;
        solidArea.x = 2*8;
        solidArea.y = 2*19;
        solidArea.height = 2*45;
        solidArea.width = 2*32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        direction = "right";

        getImage();
    }
    public void getImage(){
        Moving1left = setup("/monster_textures/corrupted_one_left_1");
        Moving2left = setup("/monster_textures/corrupted_one_left_2");
        Moving1right = setup("/monster_textures/corrupted_one_right_1");
        Moving2right = setup("/monster_textures/corrupted_one_right_2");
    }
    public void setAction(){
        actionLockCounter++;
        if (actionLockCounter == 50){
            direction = "right";
        } else if (actionLockCounter == 100) {
            direction = "left";
            actionLockCounter = 0;
        }

    }
}

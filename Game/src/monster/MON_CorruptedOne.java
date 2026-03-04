package monster;

import entity.Entity;
import main.gamepanel;
import object.OBJ_Gear;

public class MON_CorruptedOne extends Entity {
    public int dropChance = 100;
    public int dropAmount = 3;

    public MON_CorruptedOne(gamepanel gp) {
        super(gp);
        name = "Corrupted One";
        onGround = true;
        speed = 5;
        maxLife = 4;
        life = maxLife;
        solidArea.x = 2 * 8;
        solidArea.y = 2 * 19;
        solidArea.height = 2 * 45;
        solidArea.width = 2 * 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        aggroRangeX = 400;
        aggroRangeY = 200;
        aggroOffsetX = 50;
        aggroOffsetY = -100;
        direction = "right";
        getImage(gp);
    }

    public void getImage(gamepanel gp) {
        Moving1left = setup("/monster_textures/corrupted_one_left_1", gp.TileSize * 2, gp.TileSize * 2);
        Moving2left = setup("/monster_textures/corrupted_one_left_2", gp.TileSize * 2, gp.TileSize * 2);
        Moving1right = setup("/monster_textures/corrupted_one_right_1", gp.TileSize * 2, gp.TileSize * 2);
        Moving2right = setup("/monster_textures/corrupted_one_right_2", gp.TileSize * 2, gp.TileSize * 2);
    }

    public void setAction() {
        if (aggro) {
            int monsterCenterX = worldX + solidArea.x + solidArea.width / 2;
            int playerCenterX = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width / 2;
            if (playerCenterX < monsterCenterX) {
                direction = "left";
            } else {
                direction = "right";
            }
        } else {
            actionLockCounter++;
            if (actionLockCounter == 50) {
                direction = "right";
            } else if (actionLockCounter == 100) {
                direction = "left";
                actionLockCounter = 0;
            }
        }
    }

    @Override
    public void die() {
        String id = name + "_" + worldX / gp.TileSize + "_" + worldY / gp.TileSize;
        gp.killedMonsters.computeIfAbsent(gp.currentMap, k -> new java.util.HashSet<>()).add(id);
        if (Math.random() * 100 < dropChance) {
            for (int i = 0; i < dropAmount; i++) {
                for (int j = 0; j < gp.obj.length; j++) {
                    if (gp.obj[j] == null) {
                        OBJ_Gear gear = new OBJ_Gear(gp);
                        gear.worldX = worldX;
                        gear.worldY = worldY;
                        gp.obj[j] = gear;
                        break;
                    }
                }
            }
        }
    }
}
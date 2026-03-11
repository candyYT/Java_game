package monster;

import entity.Entity;
import main.gamepanel;
import java.awt.*;

public class MON_ClawMinion extends Entity {
    private int contactDamage = 1;

    public MON_ClawMinion(gamepanel gp) {
        super(gp);
        name = "Claw Minion";
        speed = 3;
        maxLife = 1;
        life = maxLife;

        // Небольшой хитбокс
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 120;
        solidArea.height = 120;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        direction = "right";
        getImages();
    }

    private void getImages() {
        // Текстуры должны лежать в папке /monster_textures/claw_minion/
        Moving1left = setup("/monster_textures/claw_minion/walk_left_1", gp.TileSize, gp.TileSize);
        Moving2left = setup("/monster_textures/claw_minion/walk_left_2", gp.TileSize, gp.TileSize);
        Moving1right = setup("/monster_textures/claw_minion/walk_right_1", gp.TileSize, gp.TileSize);
        Moving2right = setup("/monster_textures/claw_minion/walk_right_2", gp.TileSize, gp.TileSize);
    }

    @Override
    public void setAction() {
        // Движение в сторону игрока
        if (gp.player.worldX > worldX) {
            direction = "right";
        } else {
            direction = "left";
        }
    }

    @Override
    public void update() {
        super.update(); // базовое обновление (движение, гравитация, коллизии)

        // Проверка столкновения с игроком
        Rectangle minionRect = new Rectangle(worldX + solidArea.x, worldY + solidArea.y,
                solidArea.width, solidArea.height);
        Rectangle playerRect = new Rectangle(gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width, gp.player.solidArea.height);
        if (minionRect.intersects(playerRect)) {
            gp.player.takeDamage(contactDamage);
        }
    }

    @Override
    public void die() {
        // Миньон просто исчезает, дополнительных действий не требуется
    }
}
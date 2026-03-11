package boss;

import entity.Entity;
import main.gamepanel;

public abstract class Boss extends Entity {
    public Boss(gamepanel gp) {
        super(gp);
    }

    @Override
    public void die() {
        // Вызываем обработчик в gamepanel
        gp.onBossDefeated(this);
        super.die();
    }
}
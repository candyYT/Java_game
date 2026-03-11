package boss;

import entity.Entity;
import main.gamepanel;
import monster.MON_ClawMinion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class MON_Claw extends Boss {

    // ==================== ФАЗЫ ====================
    private int phase = 1;
    private final int phaseHealthThreshold1 = 60; // 60% -> фаза 2
    private final int phaseHealthThreshold2 = 30; // 30% -> фаза 3

    // ==================== СОСТОЯНИЯ ====================
    private enum BossState {
        IDLE, WALK, ATTACK1, ATTACK2, SPIN, SUMMON, DASH_PREPARE, DASH, BACKOFF, STUN
    }
    private BossState state = BossState.IDLE;
    private BossState nextState = null;
    private int stateTimer = 0;
    private int cooldownTimer = 0;

    // ==================== АНИМАЦИЯ ====================
    private int animCounter = 0;
    private int animFrame = 0;
    private final int FRAME_DURATION = 10; // УМЕНЬШЕНО ДЛЯ БЫСТРОЙ АНИМАЦИИ
    private BufferedImage currentImage;

    // ==================== ИЗОБРАЖЕНИЯ ====================
    public BufferedImage idleLeft, idleRight;
    public BufferedImage walkLeft1, walkLeft2, walkRight1, walkRight2;
    public BufferedImage attack1Left1, attack1Left2, attack1Right1, attack1Right2;
    public BufferedImage attack2Left1, attack2Left2, attack2Right1, attack2Right2;
    public BufferedImage spinLeft1, spinLeft2, spinRight1, spinRight2;
    public BufferedImage summonLeft, summonRight;

    // ==================== ПАРАМЕТРЫ АТАК ====================
    private int attack1Damage = 1;
    private int attack2Damage = 1;
    private int spinDamage = 1;

    // Параметры, изменяемые с фазой
    private int attackRange = 100;
    private int chargeRange = 300;
    private int spinRange = 150;

    private final int dashSpeed = 12;
    private int dashDistance;
    private int dashRemaining;

    private int normalSpeed;

    // ==================== МИНЬОНЫ ====================
    private final int summonCount = 2;
    private boolean hasSummoned = false;

    // ==================== СТАН ====================
    private final int stunDuration = 30;

    // Флаг для контроля нанесения урона в ATTACK2
    private boolean attack2DamageDealt = false;

    public MON_Claw(gamepanel gp) {
        super(gp);
        name = "Claw";
        normalSpeed = 3;
        speed = normalSpeed;
        maxLife = 30;
        life = maxLife;
        direction = "right";
        knockbackable = false;

        solidArea.x = 16 * 4;
        solidArea.y = 16 * 4;
        solidArea.width = 112;
        solidArea.height = 180;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImages();
    }

    private void getImages() {
        idleLeft = setup("/monster_textures/claw/claw_idle_left", gp.TileSize * 2, gp.TileSize * 2);
        idleRight = setup("/monster_textures/claw/claw_idle_right", gp.TileSize * 2, gp.TileSize * 2);

        walkLeft1 = setup("/monster_textures/claw/claw_walk_left_1", gp.TileSize * 2, gp.TileSize * 2);
        walkLeft2 = setup("/monster_textures/claw/claw_walk_left_2", gp.TileSize * 2, gp.TileSize * 2);
        walkRight1 = setup("/monster_textures/claw/claw_walk_right_1", gp.TileSize * 2, gp.TileSize * 2);
        walkRight2 = setup("/monster_textures/claw/claw_walk_right_2", gp.TileSize * 2, gp.TileSize * 2);

        attack1Left1 = setup("/monster_textures/claw/claw_attack1_left_1", gp.TileSize * 2, gp.TileSize * 2);
        attack1Left2 = setup("/monster_textures/claw/claw_attack1_left_2", gp.TileSize * 2, gp.TileSize * 2);
        attack1Right1 = setup("/monster_textures/claw/claw_attack1_right_1", gp.TileSize * 2, gp.TileSize * 2);
        attack1Right2 = setup("/monster_textures/claw/claw_attack1_right_2", gp.TileSize * 2, gp.TileSize * 2);

        attack2Left1 = setup("/monster_textures/claw/claw_attack2_left_1", gp.TileSize * 2, gp.TileSize * 2);
        attack2Left2 = setup("/monster_textures/claw/claw_attack2_left_2", gp.TileSize * 2, gp.TileSize * 2);
        attack2Right1 = setup("/monster_textures/claw/claw_attack2_right_1", gp.TileSize * 2, gp.TileSize * 2);
        attack2Right2 = setup("/monster_textures/claw/claw_attack2_right_2", gp.TileSize * 2, gp.TileSize * 2);

        spinLeft1 = setup("/monster_textures/claw/claw_spin_left_1", gp.TileSize * 2, gp.TileSize * 2);
        spinLeft2 = setup("/monster_textures/claw/claw_spin_left_2", gp.TileSize * 2, gp.TileSize * 2);
        spinRight1 = setup("/monster_textures/claw/claw_spin_right_1", gp.TileSize * 2, gp.TileSize * 2);
        spinRight2 = setup("/monster_textures/claw/claw_spin_right_2", gp.TileSize * 2, gp.TileSize * 2);

        summonLeft = setup("/monster_textures/claw/claw_summon_left", gp.TileSize * 2, gp.TileSize * 2);
        summonRight = setup("/monster_textures/claw/claw_summon_right", gp.TileSize * 2, gp.TileSize * 2);
    }

    @Override
    public void update() {
        if (life <= 0) {
            die();
            return;
        }

        // Проверка смены фазы и обновление параметров
        int healthPercent = life * 100 / maxLife;
        if (phase == 1 && healthPercent <= phaseHealthThreshold1) {
            phase = 2;
            normalSpeed = 5;
            attackRange = 150;
            spinRange = 225;
            chargeRange = 450;
        } else if (phase == 2 && healthPercent <= phaseHealthThreshold2) {
            phase = 3;
            normalSpeed = 6;
            attackRange = 200;
            spinRange = 300;
            chargeRange = 600;
        }

        // Таймеры
        if (cooldownTimer > 0) cooldownTimer--;
        if (stateTimer > 0) stateTimer--;

        // Завершение состояния
        if (stateTimer <= 0) {
            if (nextState != null) {
                setState(nextState);
                nextState = null;
            } else {
                chooseNextState();
            }
        }

        // Логика текущего состояния (кроме STUN)
        if (state != BossState.STUN) {
            performState();
        }

        // Анимация
        updateAnimation();

        // Родительский update (движение, гравитация, коллизии)
        super.update();

        // Если во время рывка столкнулись со стеной – прерываем
        if (state == BossState.DASH && collisionOn) {
            setState(BossState.STUN);
        }
    }

    private void setState(BossState newState) {
        speed = normalSpeed;
        state = newState;
        stateTimer = getStateDuration(newState);
        animFrame = 0;
        animCounter = 0;

        if (newState == BossState.ATTACK2) {
            attack2DamageDealt = false;
        }

        switch (state) {
            case DASH_PREPARE:
                int targetX = gp.player.worldX;
                int dx = targetX - worldX;
                dashDistance = Math.min(Math.abs(dx) - 100, 400);
                if (dashDistance < 50) dashDistance = 50;
                dashRemaining = dashDistance;
                direction = (dx > 0) ? "right" : "left";
                speed = 0;
                break;
            case DASH:
                speed = dashSpeed;
                break;
            case SUMMON:
                hasSummoned = true;
                break;
            case BACKOFF:
                speed = normalSpeed + 1;
                break;
        }
    }

    private int getStateDuration(BossState s) {
        switch (s) {
            case IDLE:    return 30; // сократили, но почти не используется
            case WALK:    return 100; // увеличено для заметности анимации
            case ATTACK1: return 30;
            case ATTACK2: return 70;
            case SPIN:    return 80;
            case SUMMON:  return 90;
            case DASH_PREPARE: return 20;
            case DASH:    return 50;
            case BACKOFF: return 100; // увеличено
            case STUN:    return stunDuration;
            default:      return 40;
        }
    }

    private void chooseNextState() {
        // Если есть кулдаун, вместо IDLE выбираем движение
        if (cooldownTimer > 0) {
            // Решаем, идти к игроку или отступать в зависимости от дистанции
            int dx = Math.abs(worldX - gp.player.worldX);
            double distance = Math.sqrt(dx * dx + Math.abs(worldY - gp.player.worldY) * Math.abs(worldY - gp.player.worldY));
            if (distance < attackRange) {
                nextState = BossState.BACKOFF; // если близко – отступаем
            } else {
                nextState = BossState.WALK; // если далеко – идём
            }
            // Кулдаун продолжает уменьшаться, но мы уже выбрали действие
            return;
        }

        int dx = Math.abs(worldX - gp.player.worldX);
        int dy = Math.abs(worldY - gp.player.worldY);
        double distance = Math.sqrt(dx * dx + dy * dy);
        int rand = (int) (Math.random() * 100);

        if (gp.player.worldX > worldX) direction = "right";
        else direction = "left";

        // Ближняя дистанция – шанс удара
        if (distance < attackRange) {
            int attackChance = 60;
            if (rand < attackChance) {
                nextState = BossState.ATTACK1;
                cooldownTimer = 40;
                return;
            } else {
                nextState = BossState.BACKOFF;
                cooldownTimer = 30;
                return;
            }
        }

        // Средняя дистанция – подготовка рывка
        if (distance > attackRange * 2 && distance < chargeRange) {
            nextState = BossState.DASH_PREPARE;
            cooldownTimer = 40;
            return;
        }

        if (phase == 1) {
            if (distance < attackRange * 1.5) {
                nextState = BossState.ATTACK1;
                cooldownTimer = 40;
            } else if (distance > chargeRange * 0.7) {
                nextState = BossState.DASH_PREPARE;
                cooldownTimer = 45;
            } else {
                nextState = BossState.WALK;
            }
        } else if (phase == 2) {
            if (distance < attackRange * 1.5) {
                nextState = (rand < 50) ? BossState.ATTACK1 : BossState.SPIN;
                cooldownTimer = 45;
            } else if (distance > chargeRange * 0.6) {
                nextState = BossState.DASH_PREPARE;
                cooldownTimer = 50;
            } else {
                nextState = BossState.WALK;
            }
        } else { // фаза 3
            if (!hasSummoned && life < maxLife * 0.4) {
                nextState = BossState.SUMMON;
                cooldownTimer = 80;
            } else {
                if (distance < attackRange * 1.5) {
                    if (rand < 30) nextState = BossState.ATTACK1;
                    else if (rand < 60) nextState = BossState.SPIN;
                    else nextState = BossState.ATTACK2;
                    cooldownTimer = 50;
                } else if (distance < chargeRange) {
                    nextState = BossState.DASH_PREPARE;
                    cooldownTimer = 60;
                } else {
                    nextState = BossState.WALK;
                }
            }
        }
    }

    private void performState() {
        switch (state) {
            case WALK:
                if (gp.player.worldX > worldX) direction = "right";
                else direction = "left";
                break;

            case BACKOFF:
                if (gp.player.worldX > worldX) direction = "left";
                else direction = "right";
                break;

            case DASH:
                if (dashRemaining > 0) {
                    int step = Math.min(dashSpeed, dashRemaining);
                    if (direction.equals("right")) worldX += step;
                    else worldX -= step;
                    dashRemaining -= step;
                }
                break;

            case ATTACK1:
                if (stateTimer == getStateDuration(BossState.ATTACK1) / 2) {
                    checkAttackHit(attackRange, attack1Damage);
                }
                break;

            case ATTACK2:
                int half = getStateDuration(BossState.ATTACK2) / 2;
                if (stateTimer > half) {
                    if (gp.player.worldX > worldX) direction = "right";
                    else direction = "left";
                    speed = dashSpeed;
                } else {
                    speed = 0;
                    if (!attack2DamageDealt && animFrame == 1) {
                        checkAttackHit(attackRange, attack2Damage);
                        attack2DamageDealt = true;
                    }
                }
                break;

            case SPIN:
                speed = 0;
                // Предупредительная фаза: первые 20 кадров урона нет
                int spinDuration = getStateDuration(BossState.SPIN); // 80
                if (stateTimer < spinDuration - 20) {
                    checkSpinHit(spinRange, spinDamage);
                }
                break;

            case SUMMON:
                speed = 0;
                if (stateTimer == getStateDuration(BossState.SUMMON) / 2) {
                    summonMinions();
                }
                break;
        }
    }

    private void updateAnimation() {
        animCounter++;
        if (animCounter >= FRAME_DURATION) {
            animFrame = (animFrame == 0) ? 1 : 0;
            animCounter = 0;
        }

        if (state == BossState.IDLE) {
            currentImage = direction.equals("left") ? idleLeft : idleRight;
        } else if (state == BossState.WALK || state == BossState.BACKOFF) {
            if (direction.equals("left")) {
                currentImage = (animFrame == 0) ? walkLeft1 : walkLeft2;
            } else {
                currentImage = (animFrame == 0) ? walkRight1 : walkRight2;
            }
        } else if (state == BossState.ATTACK1) {
            if (direction.equals("left")) {
                currentImage = (animFrame == 0) ? attack1Left1 : attack1Left2;
            } else {
                currentImage = (animFrame == 0) ? attack1Right1 : attack1Right2;
            }
        } else if (state == BossState.ATTACK2) {
            if (direction.equals("left")) {
                currentImage = (animFrame == 0) ? attack2Left1 : attack2Left2;
            } else {
                currentImage = (animFrame == 0) ? attack2Right1 : attack2Right2;
            }
        } else if (state == BossState.SPIN) {
            if (direction.equals("left")) {
                currentImage = (animFrame == 0) ? spinLeft1 : spinLeft2;
            } else {
                currentImage = (animFrame == 0) ? spinRight1 : spinRight2;
            }
        } else if (state == BossState.SUMMON) {
            currentImage = direction.equals("left") ? summonLeft : summonRight;
        } else if (state == BossState.STUN) {
            if (direction.equals("left")) {
                currentImage = attack1Left1;
            } else {
                currentImage = attack1Right1;
            }
        } else {
            currentImage = direction.equals("left") ? idleLeft : idleRight;
        }
    }

    private Rectangle getCurrentAttackArea() {
        if (state == BossState.ATTACK1 || state == BossState.ATTACK2 || state == BossState.SPIN) {
            Rectangle monsterRect = new Rectangle(worldX + solidArea.x, worldY + solidArea.y,
                    solidArea.width, solidArea.height);
            if (state == BossState.SPIN) {
                int centerX = monsterRect.x + monsterRect.width / 2;
                int centerY = monsterRect.y + monsterRect.height / 2;
                return new Rectangle(centerX - spinRange, centerY - spinRange, spinRange * 2, spinRange * 2);
            } else {
                int range = attackRange;
                if (direction.equals("right")) {
                    return new Rectangle(monsterRect.x + monsterRect.width, monsterRect.y,
                            range, monsterRect.height);
                } else {
                    return new Rectangle(monsterRect.x - range, monsterRect.y,
                            range, monsterRect.height);
                }
            }
        }
        return null;
    }

    private void checkAttackHit(int range, int damage) {
        Rectangle monsterRect = new Rectangle(worldX + solidArea.x, worldY + solidArea.y,
                solidArea.width, solidArea.height);
        Rectangle attackArea;
        if (direction.equals("right")) {
            attackArea = new Rectangle(monsterRect.x + monsterRect.width, monsterRect.y,
                    range, monsterRect.height);
        } else {
            attackArea = new Rectangle(monsterRect.x - range, monsterRect.y,
                    range, monsterRect.height);
        }
        Rectangle playerRect = new Rectangle(gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width, gp.player.solidArea.height);

        if (attackArea.intersects(playerRect)) {
            gp.player.takeDamage(damage);
            int knockbackDir = direction.equals("right") ? 1 : -1;
            gp.player.knockbackFromPlayer(knockbackDir, 200);
        }
    }

    private void checkSpinHit(int radius, int damage) {
        int centerX = worldX + solidArea.x + solidArea.width / 2;
        int centerY = worldY + solidArea.y + solidArea.height / 2;
        int playerCenterX = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width / 2;
        int playerCenterY = gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height / 2;

        double dist = Math.hypot(playerCenterX - centerX, playerCenterY - centerY);
        if (dist < radius) {
            gp.player.takeDamage(damage);
            gp.player.knockbackFromPlayer(0, 200);
        }
    }

    private void summonMinions() {
        for (int i = 0; i < summonCount; i++) {
            for (int j = 0; j < gp.monster.length; j++) {
                if (gp.monster[j] == null) {
                    MON_ClawMinion minion = new MON_ClawMinion(gp);
                    minion.worldX = worldX + 60 * (i - 1);
                    minion.worldY = worldY + gp.TileSize;
                    gp.monster[j] = minion;
                    break;
                }
            }
        }
    }

    @Override
    public void takeDamage(int amount) {
        if (!invincible) {
            life -= amount;
            if (life < 0) life = 0;
            invincible = true;
            invincibleCounter = 0;
        }
    }

    @Override
    public void die() {
        String id = name + "_" + worldX / gp.TileSize + "_" + worldY / gp.TileSize;
        gp.killedMonsters.computeIfAbsent(gp.currentMap, k -> new HashSet<>()).add(id);
        if (gp.player != null) {
            gp.player.moneyCount += 50;
        }
        gp.bossFightActive = false;
        super.die();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (life <= 0) return;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

            if (currentImage != null) {
                g2.drawImage(currentImage, screenX, screenY, 4 * gp.TileSize, 4 * gp.TileSize, null);
            }

            if (gp.showHitboxes) {
                g2.setColor(Color.RED);
                g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
        }
    }
}
package main;

import entity.Player;
import object.OBJ_Heart;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    gamepanel gp;
    BufferedImage heart_full, heart_empty;
    Font arial_40;
    BufferedImage gearIcon;
    public boolean messageOn = false;
    public String message = "";
    private String[] statNames = {"Health", "Damage", "Stamina"};
    private String selectedStat = "Health";
    private Color backgroundColor = new Color(0, 0, 0, 200);
    private Color panelColor = new Color(30, 30, 30);
    private Color borderColor = new Color(255, 215, 0);
    private Color textColor = new Color(220, 220, 220);
    private Color selectedColor = new Color(255, 215, 0);
    private Color costColor = new Color(100, 200, 255);
    private Color instructionColor = new Color(150, 150, 150);
    private Font titleFont = new Font("Serif", Font.BOLD, 64);
    private Font statFont = new Font("Monospaced", Font.PLAIN, 36);
    private Font instructionFont = new Font("SansSerif", Font.ITALIC, 28);
    private int panelWidth = 800;
    private int panelHeight = 600;
    private int panelX, panelY;
    private int titleY = 80;
    private int startY = 180;
    private int lineHeight = 70;
    private int leftMargin = 80;

    public UI(gamepanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        try {
            gearIcon = ImageIO.read(getClass().getResourceAsStream("/objects/gear_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        OBJ_Heart heart = new OBJ_Heart(gp);
        heart_full = heart.heart_full;
        heart_empty = heart.heart_empty;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(gearIcon, gp.TileSize, gp.TileSize, gp.TileSize, gp.TileSize, null);
        g2.drawString("x " + gp.player.moneyCount, 50, 50);
        drawPlayerLife(g2);
        if (messageOn) {
            g2.drawString(message, 1880, 50);
        }
        if (gp.inUpgradeMenu) {
            drawUpgradeMenu(g2);
        }
    }

    public void drawPlayerLife(Graphics2D g2) {
        int x = 50;
        int y = 110;
        for (int i = 0; i < gp.player.maxLife; i++) {
            g2.drawImage(heart_empty, x, y, gp.TileSize, gp.TileSize, null);
            x += gp.TileSize;
        }
        x = 50;
        for (int i = 0; i < gp.player.life; i++) {
            g2.drawImage(heart_full, x, y, gp.TileSize, gp.TileSize, null);
            x += gp.TileSize;
        }
    }

    private void drawUpgradeMenu(Graphics2D g2) {
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
        panelX = (gp.ScreenWidth - panelWidth) / 2;
        panelY = (gp.ScreenHeight - panelHeight) / 2;
        g2.setColor(panelColor);
        g2.fillRect(panelX, panelY, panelWidth, panelHeight);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(panelX, panelY, panelWidth, panelHeight);
        g2.setFont(titleFont);
        g2.setColor(borderColor);
        String title = "UPGRADE";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, panelX + (panelWidth - titleWidth) / 2, panelY + titleY);
        Player p = gp.player;
        g2.setFont(statFont);
        int y = panelY + startY;
        for (int i = 0; i < statNames.length; i++) {
            String stat = statNames[i];
            boolean isSelected = stat.equals(selectedStat);
            int level, cost;
            String valueText = "";
            switch (stat) {
                case "Health":
                    level = p.healthLevel;
                    cost = p.baseCostHealth + level * p.costIncreaseHealth;
                    valueText = "+" + p.healthPerLevel + " HP";
                    break;
                case "Damage":
                    level = p.damageLevel;
                    cost = p.baseCostDamage + level * p.costIncreaseDamage;
                    valueText = "+" + p.damagePerLevel + " DMG";
                    break;
                case "Stamina":
                    level = p.staminaLevel;
                    cost = p.baseCostStamina + level * p.costIncreaseStamina;
                    valueText = "+" + p.staminaPerLevel + " END";
                    break;
                default:
                    level = 0;
                    cost = 0;
            }
            String nameLevel = String.format("%s Lv.%d", stat, level);
            String costStr = String.format("Cost: %d", cost);
            String gainStr = valueText;
            g2.setColor(isSelected ? selectedColor : textColor);
            g2.drawString(nameLevel, panelX + leftMargin, y);
            g2.setFont(instructionFont);
            g2.setColor(costColor);
            g2.drawString(costStr, panelX + leftMargin + 300, y);
            g2.setColor(isSelected ? selectedColor : textColor);
            g2.drawString(gainStr, panelX + leftMargin + 500, y);
            y += lineHeight;
        }
        y = panelY + panelHeight - 80;
        g2.setFont(instructionFont);
        g2.setColor(instructionColor);
        String controls = "↑/↓ select   E buy   ESC exit";
        int controlsWidth = g2.getFontMetrics().stringWidth(controls);
        g2.drawString(controls, panelX + (panelWidth - controlsWidth) / 2, y);
    }

    public void handleMenuInput(int keyCode) {
        if (!gp.inUpgradeMenu) return;
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            int currentIndex = -1;
            for (int i = 0; i < statNames.length; i++) {
                if (statNames[i].equals(selectedStat)) {
                    currentIndex = i;
                    break;
                }
            }
            if (keyCode == KeyEvent.VK_UP) {
                currentIndex = (currentIndex - 1 + statNames.length) % statNames.length;
            } else {
                currentIndex = (currentIndex + 1) % statNames.length;
            }
            selectedStat = statNames[currentIndex];
        } else if (keyCode == KeyEvent.VK_E) {
            gp.player.upgradeStat(selectedStat);
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            gp.inUpgradeMenu = false;
        }
    }
}
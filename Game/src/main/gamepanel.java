package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class gamepanel extends JPanel implements Runnable {
    int originalTileSize = 32;
    float scale = (float) 60 / 32;
    public int TileSize = (int) ((originalTileSize * scale));
    public int ScreenCol = 32;
    public int ScreenRow = 18;
    public int ScreenWidth = TileSize * ScreenCol;
    public int ScreenHeight = TileSize * ScreenRow;

    public final int maxWorldCol = 140;
    public final int maxWorldRow = 33;

    int FPS = 60;
    public String currentMap = "loc1.txt";

    public MapTransition[] transitions = new MapTransition[10]; // 10 слотов для переходов
    public int transitionCount = 0; // Счетчик реально добавленных переходов

    public TileManager tileManager = new TileManager(this);
    Movement movement = new Movement();
    public UI ui = new UI(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, movement);
    public SuperObject[] obj = new SuperObject[10];

    public static class MapTransition {
        public int fromMapTile;
        public String toMap;
        public int targetX;
        public int targetY;

        public MapTransition(int fromMapTile, String toMap, int targetX, int targetY) {
            this.fromMapTile = fromMapTile;
            this.toMap = toMap;
            this.targetX = targetX;
            this.targetY = targetY;
        }
    }

    public gamepanel() {
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(movement);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();

        transitions[0] = new MapTransition(999, "loc2.txt", 10 * TileSize, 16 * TileSize);
        transitions[1] = new MapTransition(998, "loc1.txt", 93 * TileSize, 20 * TileSize);
        transitionCount = 2;
    }

    public void changeMap(String newMap, int playerX, int playerY) {
        currentMap = newMap;
        tileManager.loadMap("/maps/" + newMap);


        player.worldX = playerX;
        player.worldY = playerY;


        player.onGround = true;
        player.velocityY = 0;
        player.isJumping = false;
        player.isFalling = false;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        player.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }
}
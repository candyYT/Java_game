package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;
import javax.swing.*;
import java.awt.*;

public class gamepanel extends JPanel implements Runnable {
    int originalTileSize = 32;
    float scale = (float) 60 / 32;
    public int TileSize = (int) ((originalTileSize * scale));
    public int ScreenCol = 32;
    public int ScreenRow = 18;
    public int ScreenWidth = TileSize * ScreenCol;
    public int ScreenHeight = TileSize * ScreenRow;

    public boolean showHitboxes = false;

    public final int maxWorldCol = 140;
    public final int maxWorldRow = 50;

    int FPS = 60;
    public String currentMap = "loc1.txt";

    public MapTransition[] transitions = new MapTransition[10];
    public int transitionCount = 0;

    public TileManager tileManager = new TileManager(this);
    Movement movement = new Movement();
    public UI ui = new UI(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);


    public Player player = new Player(this, movement);
    public SuperObject[] obj = new SuperObject[10];
    public Entity[] monster = new Entity[10];

    public int checkpointX;
    public int checkpointY;
    public String checkpointMap;
    public boolean checkpointActivated = false;
    public final int deathY = 35 * TileSize;  //смерть от высоты

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
        checkpointX = 22 * TileSize;
        checkpointY = 20 * TileSize;
        checkpointMap = "loc1.txt";
    }

    public void setupGame() {
        transitions[0] = new MapTransition(999, "loc2.txt", 10 * TileSize, 27 * TileSize);
        transitions[1] = new MapTransition(998, "loc1.txt", 93 * TileSize, 12 * TileSize);
        transitionCount = 2;

        objectLoader.loadObjects(this, currentMap);
    }

    public void changeMap(String newMap, int playerX, int playerY) {
        currentMap = newMap;
        tileManager.loadMap("/maps/" + newMap);
        objectLoader.loadObjects(this, newMap);
        objectLoader.loadMonsters(this, newMap);
        player.worldX = playerX;
        player.worldY = playerY;
        player.onGround = true;
        player.velocityY = 0;
        player.isJumping = false;
        player.isFalling = false;
    }

    public void respawn() {
        if (!currentMap.equals(checkpointMap)) {
            changeMap(checkpointMap, checkpointX, checkpointY);
        } else {
            objectLoader.loadMonsters(this, currentMap);

            player.moneyCount = 0;
            player.worldX = checkpointX-TileSize;
            player.worldY = checkpointY-TileSize;
            player.onGround = true;
            player.velocityY = 0;
            player.isJumping = false;
            player.isFalling = false;
        }
        player.life = player.maxLife;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
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
        for(int i = 0; i<monster.length; i++){
            if(monster[i]!=null){
                monster[i].update();
            }
        }
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
        for(int i = 0; i<monster.length; i++){
            if (monster[i]!=null){
                monster[i].draw(g2);
            }
        }
        player.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }
}
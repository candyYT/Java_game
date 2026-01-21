package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;


public class gamepanel extends JPanel implements Runnable{
int originalTileSize = 32;
float scale = (float) 60 /32;
public int TileSize= (int) ((originalTileSize*scale));
public int ScreenCol = 32;
public int ScreenRow=18;
public int  ScreenWidth = TileSize*ScreenCol;
public int ScreenHeight = TileSize*ScreenRow;

public final int maxWorldCol = ScreenCol * 2; //изменяемо
public final int maxWorldRow = 42; //изменяемо


    int FPS = 60;

    TileManager tileManager = new TileManager(this);
    Movement movement = new Movement();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
     public Player player = new Player(this, movement);



    public gamepanel(){
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(movement);
        this.setFocusable(true);

    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
    while (gameThread != null){
currentTime = System.nanoTime();
delta += (currentTime-lastTime)/drawInterval;
lastTime=currentTime;
if (delta>=1){
update();
repaint();
delta--;
}

    }
    }
    public void update(){
    player.update();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileManager.draw(g2);

        player.draw(g2);
        g2.dispose();
    }
}



package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamepanel GamePanel = new gamepanel();
        window.add(GamePanel);
        window.pack();
        window.setVisible(true);
        GamePanel.setupGame();
        GamePanel.startGameThread();
    }
}

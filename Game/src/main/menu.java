package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class menu extends JFrame {


    private Image a = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/progectmenu.png");
    private Image b = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/startgame-Photoroom.png");
    private Image c = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/options-Photoroom.png");
    private Image d = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/achievements-Photoroom.png");
    private Image e = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/credits-Photoroom.png");
    private Image f = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/quit-Photoroom.png");


    private final int options_x = 875;
    private final int options_y = 730;
    private final int width1 = 180;
    private final int height1 = 50;
    private final int start_x = 850;
    private final int start_y = 680;
    private final int width2 = 190;
    private final int height2 = 60;


    menu() {
        setTitle("main.Main Menu");
        setSize(1920, 1080);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (x >= start_x && x <= start_x + width2 &&
                        y >= start_y && y <= start_y + height2) {
                    startGame();}
                else if (x >= options_x && x <= options_x + width1 &&
                        y >= options_y && y <= options_y + height1) {
                    openOptionsMenu();




                }
            }
        });


        setVisible(true);
    }


    @Override
    public void paint(Graphics g) {
        g.drawImage(a, 0, 0, this);
        g.drawImage(b, 850, 680, this);
        g.drawImage(c, 875, 730, this);
        g.drawImage(d, 838, 780, this);
        g.drawImage(e, 838, 830, this);
        g.drawImage(f, 833, 880, this);
    }
    private void startGame() {


        new gamepanel();
    }


    private void openOptionsMenu() {
        JFrame optionsFrame = new JFrame("Optionsmenu");
        optionsFrame.setSize(1920, 1080);
        Image s = Toolkit.getDefaultToolkit().getImage("C:/Users/sevat/Pictures/infa_progect/optionsmenu.PNG");


        JPanel window = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(s, 0, 0, getWidth(), getHeight(), this);
            }
        };


        optionsFrame.add(window);
        optionsFrame.setVisible(true);
    }


    public static void main(String[] args) {
        new menu();
    }
}

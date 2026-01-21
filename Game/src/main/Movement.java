package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Movement implements KeyListener {
   public boolean left;
    public boolean right;
    public static boolean jump;
    public boolean shift;
    public char lastPressed='d';
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
    int buttonPressed = e.getKeyCode();
    if (buttonPressed==KeyEvent.VK_A){
    left = true;
    lastPressed = 'a';
    }
    if (buttonPressed==KeyEvent.VK_D){
    right = true;
    lastPressed = 'd';
        }
        if (buttonPressed==KeyEvent.VK_SPACE){
            jump=true;

        }
        if (buttonPressed == KeyEvent.VK_SHIFT) {
            shift = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int buttonPressed = e.getKeyCode();
        if (buttonPressed==KeyEvent.VK_A){
            left = false;

        }
        if (buttonPressed==KeyEvent.VK_D){
            right = false;
        }
        if (buttonPressed==KeyEvent.VK_SPACE){
            jump=false;}
        if (buttonPressed == KeyEvent.VK_SHIFT) {
            shift = false;}
    }
}

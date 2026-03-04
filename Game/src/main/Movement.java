package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Movement implements KeyListener, MouseListener {
    public boolean left;
    public boolean right;
    public static boolean jump;
    public boolean shift;
    public boolean shiftPressed;
    public static boolean interact;
    public char lastPressed = 'd';

    public boolean mouseLeftPressed = false;

    private gamepanel gp;

    public Movement(gamepanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int buttonPressed = e.getKeyCode();

        if (gp.inUpgradeMenu) {
            gp.handleMenuInput(buttonPressed);
            return;
        }

        if (buttonPressed == KeyEvent.VK_A) {
            left = true;
            lastPressed = 'a';
        }
        if (buttonPressed == KeyEvent.VK_D) {
            right = true;
            lastPressed = 'd';
        }
        if (buttonPressed == KeyEvent.VK_SPACE) {
            jump = true;
        }
        if (buttonPressed == KeyEvent.VK_SHIFT) {
            shift = true;
            shiftPressed = true;
        }
        if (buttonPressed == KeyEvent.VK_E) {
            interact = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int buttonPressed = e.getKeyCode();
        if (buttonPressed == KeyEvent.VK_A) {
            left = false;
        }
        if (buttonPressed == KeyEvent.VK_D) {
            right = false;
        }
        if (buttonPressed == KeyEvent.VK_SPACE) {
            jump = false;
        }
        if (buttonPressed == KeyEvent.VK_SHIFT) {
            shift = false;
            shiftPressed = false;
        }
        if (buttonPressed == KeyEvent.VK_E) {
            interact = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseLeftPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseLeftPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
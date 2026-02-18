package control;

import model.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BulletControl implements KeyListener {
    private BulletsModel model;
    private boolean isJPressed = false;

    public BulletControl(BulletsModel bulletsmodel) {
        this.model = bulletsmodel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_J) {
            if (!isJPressed) {
                model.keyTappedNewly();
                isJPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_J) {
            isJPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}

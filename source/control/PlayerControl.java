package control;

import model.PlayerModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerControl implements KeyListener {
    private PlayerModel model;

    public PlayerControl(PlayerModel model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        // キーが押されたらフラグを true にする
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) model.setUp(true);
        if (code == KeyEvent.VK_S) model.setDown(true);
        if (code == KeyEvent.VK_A) model.setLeft(true);
        if (code == KeyEvent.VK_D) model.setRight(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // キーが離されたらフラグを false にする
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) model.setUp(false);
        if (code == KeyEvent.VK_S) model.setDown(false);
        if (code == KeyEvent.VK_A) model.setLeft(false);
        if (code == KeyEvent.VK_D) model.setRight(false);
    }
}
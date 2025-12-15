package control;

import model.PlayerModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BulletControl implements KeyListener {
    // ★ PlayerModel を操作対象にする
    private PlayerModel model; 
    private boolean isJPressed = false;

    public BulletControl(PlayerModel model) {
        this.model = model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_J) {
            if (!isJPressed) {
                // ★ PlayerModel のメソッドを呼ぶ
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
package control;

import model.GunModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BulletControl implements KeyListener {
    private GunModel model;
    private boolean isJPressed = false; // 押しっぱなし防止フラグ

    public BulletControl(GunModel model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Jキーが押され、かつ「まだ押された状態として処理されていない」場合
        if (e.getKeyCode() == KeyEvent.VK_J) {
            if (!isJPressed) {
                model.keyTappedNewly();                 
                isJPressed = true; // フラグを立てる
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // キーを離したらフラグを戻す
        if (e.getKeyCode() == KeyEvent.VK_J) {
            isJPressed = false;
        }
    }
}
package control;

import model.BulletModel;
import model.PlayerModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BulletControl implements KeyListener {
    private BulletModel[] bulletModels;
    private PlayerModel playerModel;
    private boolean isJPressed = false;

    public BulletControl(BulletModel[] bulletModels, PlayerModel playerModel) {
        this.bulletModels = bulletModels;
        this.playerModel = playerModel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_J) {
            if (!isJPressed) {
                fire(); 
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

    private void fire() {
        for (BulletModel bullet : bulletModels) {
            // 「今、この弾は画面上に存在していない（=使っていない）か？」を確認
            if (!bullet.bulletExist()) {
                // プレイヤーの場所（X, Y）に弾をセットして「発射状態」にする
                bullet.shoot(playerModel.getPlayerX(), playerModel.getPlayerY());
                break;
            }
        }
    }
}
package view;

import model.BulletModel;
import control.BulletControl;

import javax.swing.JPanel;
import java.awt.Graphics;

public class BulletView extends JPanel {
    private BulletModel[] models; // BUlletModel の配列

    public BulletView(BulletModel[] ms, BulletControl c) { // 与えられた BulletModel 配列を受け取る
        this.models = ms;
    }

    // updateメソッドとpaintComponentメソッドを消去した

    // 弾を描画するメソッド
    public void drawBullets(Graphics g) {
        if (g == null) { // nullチェック
            return;
        }
        
        // 弾の描画
        for (BulletModel m : this.models) {
            if (m.bulletExist()) { // 弾が存在する場合のみ描画
                // getPlayerX() と getPlayerY() で弾の位置を取得できるっぽい
                g.fillOval(m.getPlayerX(), m.getPlayerY(), 5, 5); // 弾を半径5の円で描画
            }
        }
    }
}
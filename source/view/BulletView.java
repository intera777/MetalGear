package view;

import GameConfig.*;
import model.*;

import java.awt.Color;
import java.awt.Graphics;

public class BulletView { // extends JPanel は消去した. どうやらパネルが重なっちゃうのは不適切らしいため.
    // BulletModel[] → BulletsModel に変更
    private BulletsModel models;

    public BulletView(BulletsModel ms) {
        this.models = ms;
    }

    // 弾を描画するメソッド
    public void drawBullet(Graphics g, int offsetX, int offsetY) {
        BulletsModel arr_bullet = models; // 弾の配列を取得

        for (BulletModel bullet : arr_bullet.getBullets()) {
            if (bullet.bulletExist()) { // 弾が存在する場合のみ描画
                g.setColor(Color.RED);

                // 弾の相対座標 (弾の中心の座標)
                int drawX = bullet.getBulletX() + offsetX - ConstSet.BULLET_SIZE / 2;
                int drawY = bullet.getBulletY() + offsetY - ConstSet.BULLET_SIZE / 2;

                g.fillRect(drawX, drawY, ConstSet.BULLET_SIZE, ConstSet.BULLET_SIZE);
            }
        }
    }
}

package view;

import model.*;
import control.*;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class BulletView extends JPanel {
    private BulletModel[] models; // BUlletModel の配列

    public BulletView(BulletModel[] ms, BulletControl c) {
        this.models = ms;
        this.addKeyListener(c);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // 背景の初期化

        BulletModel[] arr_bullet = models; // 弾の配列を取得

        for (BulletModel bullet : arr_bullet) {
            if (bullet.bulletExist()) { // 弾が存在する場合のみ描画
                g.setColor(Color.RED);
                g.fillRect(bullet.getBulletX(), bullet.getBulletY(), 5, 5);
            }
        }
    }
}

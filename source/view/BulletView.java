package view;

import model.BulletModel;
import control.BulletControl;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

public class BulletView extends JPanel implements Observer {
    private BulletModel[] models; // BUlletModel の配列

    public BulletView(BulletModel[] ms, BulletControl c) {
        this.models = ms;

        for (BulletModel m : ms) {
            m.addObserver(this); // BulletViewが各BulletModelを監視するように登録
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.repaint(); // 再描画
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // 背景の初期化

        BulletModel[] arr_bullet = models; // 弾の配列を取得

        for (BulletModel bullet : arr_bullet) {
            if (bullet.bulletExist()) { // 弾が存在する場合のみ描画
                g.fillRect(bullet.getBulletX(), bullet.getBulletY(), 5, 5);
            }
        }
    }
}
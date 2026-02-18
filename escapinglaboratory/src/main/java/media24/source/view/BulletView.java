package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BulletView { // extends JPanel は消去した. どうやらパネルが重なっちゃうのは不適切らしいため.
    private BulletsModel models;

    // 画像保持用の変数
    private Image bulletImage;

    public BulletView(BulletsModel ms) {
        this.models = ms;
        loadImages();
    }

    private void loadImages() {
        try {
            bulletImage = ImageIO.read(getClass().getResource(ConstSet.IMG_PATH_BULLET));
        } catch (IOException e) {
            // 読み込み失敗時のデバック用
            System.err.println("読み込み失敗");
            System.err.println("探した場所: " + ConstSet.IMG_PATH_BULLET);
            e.printStackTrace();
        }
    }

    // 弾を描画するメソッド
    public void drawBullet(Graphics g, int offsetX, int offsetY) {
        BulletsModel arr_bullet = models; // 弾の配列を取得

        for (BulletModel bullet : arr_bullet.getBullets()) {
            if (bullet.bulletExist()) { // 弾が存在する場合のみ描画

                // 弾の相対座標 (弾の中心の座標)
                int drawX = bullet.getBulletX() + offsetX - ConstSet.BULLET_SIZE / 2;
                int drawY = bullet.getBulletY() + offsetY - ConstSet.BULLET_SIZE / 2;

                g.drawImage(bulletImage, drawX, drawY, null);
            }
        }
    }
}

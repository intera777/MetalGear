package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PlayerView {
    private PlayerModel model;
    private BufferedImage[][] sprites; // [行][列] で保持

    public PlayerView(PlayerModel m) {
        this.model = m;
        loadSprites();
    }

    // プレーヤーのスプライトを読み込む
    private void loadSprites() { // try...catchは例外処理. この存在により、強制終了を防ぐ.
        try {
            BufferedImage sheet = ImageIO.read(new File(ConstSet.IMG_PATH_HEROIN));
            sprites = new BufferedImage[4][3]; // 4方向 × 3コマ

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 3; col++) {
                    sprites[row][col] = sheet.getSubimage(
                        col * ConstSet.PLAYER_SIZE, // キャラチップの列に関して32pxだけすらす
                        row * ConstSet.PLAYER_SIZE, // キャラチップの行に関して32pxだけずらす
                        ConstSet.PLAYER_SIZE,
                        ConstSet.PLAYER_SIZE
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // プレイヤーを画面中央に描画するメソッド
    protected void drawPlayer(Graphics g, int drawX, int drawY) {
        int row = model.getPlayerDirection();
        int col = model.getAnimationFrame();
        g.drawImage(sprites[row][col], drawX, drawY, ConstSet.PLAYER_SIZE, ConstSet.PLAYER_SIZE, null);
    }
}

// EnemyView
package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EnemyView {
    private EnemiesModel enemiesModel;
    private BufferedImage[][] sprites; // スプライトを保持する変数

    // --- 追加: エフェクト用画像 ---
    private BufferedImage alertImg; // "!.png"
    private BufferedImage questionImg; // "?.png"

    public EnemyView(EnemiesModel esm) {
        this.enemiesModel = esm;
        loadEnemySprites();
        loadEffectImages(); // エフェクト画像の読み込み
    }

    // 敵のスプライトを分割して読み込む
    private void loadEnemySprites() {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResource(ConstSet.IMG_PATH_ENEMY));

            // 5行（右・上・左・下・死亡）× 3コマで初期化
            sprites = new BufferedImage[5][3];

            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 3; col++) {
                    if (row == 4 && col > 0) {
                        sprites[row][col] = sprites[row][0];
                        continue;
                    }

                    sprites[row][col] = sheet.getSubimage(col * ConstSet.ENEMY_SIZE,
                            row * ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE);
                }
            }
        } catch (IOException e) {
            System.err.println("敵のスプライトの読み込みに失敗しました。");
            e.printStackTrace();
        }
    }

    // --- 追加: エフェクト画像の読み込み ---
    private void loadEffectImages() {
        try {
            // パスは ConstSet に IMG_PATH_ALERT, IMG_PATH_LOST が定義されている想定
            // 定義がない場合は直接文字列で指定してください
            alertImg = ImageIO.read(getClass().getResource("/resources/others/!.png"));
            questionImg =
                    ImageIO.read(getClass().getResource("/resources/others/questionmark.png"));
        } catch (IOException e) {
            System.err.println("エフェクト画像（!/?）の読み込みに失敗しました。");
            // 画像がない場合にヌルポインタで落ちないよう、予備の処理をしてもOK
        }
    }

    public void drawEnemies(Graphics g, int offsetX, int offsetY) {
        for (EnemyModel enemy : enemiesModel.getEnemies()) {
            if (enemy == null)
                continue;
            renderEnemy(g, enemy, offsetX, offsetY);
        }
    }

    public void renderEnemy(Graphics g, EnemyModel enemy, int offsetX, int offsetY) {
        int drawX = enemy.getEnemyX() + offsetX - ConstSet.ENEMY_SIZE / 2;
        int drawY = enemy.getEnemyY() + offsetY - ConstSet.ENEMY_SIZE / 2;

        int row;
        int col;

        // 状態によって参照するスプライトの行を決める
        if (enemy.getEnemyCondition() == ConstSet.ENEMY_DEAD) {
            row = 4;
            col = 0;
        } else {
            row = enemy.getEnemyDirection(); // 0:右, 1:上, 2:左, 3:下
            col = enemy.getAnimationFrame(); // 0, 1, 2
        }

        // 1. 敵本体の描画
        g.drawImage(sprites[row][col], drawX, drawY, ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE,
                null);

        // 2. ダメージ時の赤点滅
        if (enemy.getEnemyCondition() == ConstSet.ENEMY_DAMAGED) {
            g.setColor(new Color(255, 0, 0, 120));
            g.fillRect(drawX, drawY, ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE);
        }

        // 3. --- 追加: 頭上エフェクト（! / ?）の描画 ---
        int effectType = enemy.getEffectType(); // EnemyModel側で 1なら!, 2なら? と設定されている
        if (effectType != 0) {
            BufferedImage effect = (effectType == 1) ? alertImg : questionImg;
            if (effect != null) {
                // 敵の頭上中央に配置
                int effectX = drawX + (ConstSet.ENEMY_SIZE - effect.getWidth()) / 2;
                int effectY = drawY - effect.getHeight() - 5; // 敵の頭から5px浮かす

                g.drawImage(effect, effectX, effectY, null);
            }
        }
    }
}

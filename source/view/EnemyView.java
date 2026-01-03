// EnemyView
package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnemyView {
    private EnemiesModel enemiesModel;
    private BufferedImage[][] sprites; // スプライトを保持する変数

    public EnemyView(EnemiesModel esm) {
        this.enemiesModel = esm;
        loadEnemySprites();
    }

    // 敵のスプライトを分割して読み込む
    private void loadEnemySprites() {
        try {
            BufferedImage sheet = ImageIO.read(new File(ConstSet.IMG_PATH_ENEMY));
            
            // 5行（右・上・左・下・死亡）× 3コマで初期化
            sprites = new BufferedImage[5][3];

            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 3; col++) {
                    // 死亡行(row=4)は1コマしかないので、範囲外エラーを防ぐ処理
                    if (row == 4 && col > 0) {
                        sprites[row][col] = sprites[row][0]; // 死亡コマを使い回す
                        continue;
                    }

                    sprites[row][col] = sheet.getSubimage(
                        col * ConstSet.ENEMY_SIZE,
                        row * ConstSet.ENEMY_SIZE,
                        ConstSet.ENEMY_SIZE,
                        ConstSet.ENEMY_SIZE
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("敵のスプライトの読み込みに失敗しました。");
            e.printStackTrace();
        }
    }

    public void drawEnemies(Graphics g, int offsetX, int offsetY) {
        for (EnemyModel enemy : enemiesModel.getEnemies()) {
            if (enemy == null) continue;
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
            row = 4; // 死亡コマがある行
            col = 0;
        } else {
            row = enemy.getEnemyDirection(); // 0:右, 1:上, 2:左, 3:下
            col = enemy.getAnimationFrame(); // 0, 1, 2
        }

        // 画像の描画
        g.drawImage(sprites[row][col], drawX, drawY, ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE, null);

        // ダメージ時の赤点滅（メタルギア風の演出）
        if (enemy.getEnemyCondition() == ConstSet.ENEMY_DAMAGED) {
            g.setColor(new Color(255, 0, 0, 120)); // 半透明の赤
            g.fillRect(drawX, drawY, ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE);
        }
    }
}

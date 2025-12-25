// EnemyView
package view;

import GameConfig.*;
import model.*;

import java.awt.Color;
import java.awt.Graphics;

public class EnemyView {
    private EnemiesModel enemiesModel;

    public EnemyView(EnemiesModel esm) {
        this.enemiesModel = esm;
    }

    public void drawEnemies(Graphics g, int offsetX, int offsetY) {
        for (EnemyModel enemy : enemiesModel.getEnemies()) {
            // nullチェック
            if (enemy == null)
                continue;
            renderEnemy(g, enemy, offsetX, offsetY);
        }
    }

    public void renderEnemy(Graphics g, EnemyModel enemy, int offsetX, int offsetY) {
        int size = ConstSet.ENEMY_SIZE;
        int drawX = enemy.getEnemyX() + offsetX;
        int drawY = enemy.getEnemyY() + offsetY;
        int direction = enemy.getEnemyDirection();
        int condition = enemy.getEnemyCondition();

        // 敵の状態に応じて色を変える
        if (condition == ConstSet.ENEMY_DAMAGED) {
            g.setColor(Color.PINK);
        } else if (condition == ConstSet.ENEMY_ALIVE) {
            g.setColor(Color.ORANGE); // 通常時の色
        } else {
            return; // DEAD状態の敵は描画しない
        }

        // 敵本体の描画
        g.fillRect(drawX - size / 2, drawY - size / 2, size, size);

        // 敵の向きを視覚化
        g.setColor(Color.BLACK);
        drawDirectionLine(g, drawX, drawY, direction);
    }

    // 敵が向いている方向に目印を描画 → 0:右, 1:上, 2:左, 3:下
    // 後々向いている方向によって画像が変わるような仕様？
    private void drawDirectionLine(Graphics g, int x, int y, int dir) {
        int half = ConstSet.ENEMY_SIZE / 2;
        switch (dir) {
            case ConstSet.RIGHT:
                g.drawLine(x, y, x + half, y);
                break; // 右
            case ConstSet.UP:
                g.drawLine(x, y, x, y - half);
                break; // 上
            case ConstSet.LEFT:
                g.drawLine(x, y, x - half, y);
                break; // 左
            case ConstSet.DOWN:
                g.drawLine(x, y, x, y + half);
                break; // 下
        }
    }
}

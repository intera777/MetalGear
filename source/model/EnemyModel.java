package model;

import java.util.Arrays;
import GameConfig.*;

public class EnemyModel {
    private int enemyX;
    private int enemyY;
    private int enemycondition;
    private int enemydirection; // 敵が向いている方向.0123の順で右上左下.


    public EnemyModel(int startX, int startY) {
        this.enemyX = startX;
        this.enemyY = startY;
    }

    public int getEnemyX() { // 敵のX座標を取得.
        return enemyX;
    }

    public int getEnemyY() { // 敵のY座標を取得.
        return enemyY;
    }

    public int getEnemyCondition() {// 敵の状態を取得.
        return enemycondition;
    }

    public int getEnemyDirection() {// 敵の向きを取得.
        return enemydirection;
    }

    public void setEnemyIndex(int x, int y) { // 敵のX座標とY座標を設定.
        this.enemyX = x;
        this.enemyY = y;
    }

    public void setEmenyCondition(int condition) {// 敵の状態を設定.
        this.enemycondition = condition;
    }

    public void setEnemyDirection(int direction) {// 敵の向きを設定.
        this.enemydirection = direction;
    }

    public boolean isObstacleExist(MapModel mm) {// 敵と障害物が重なっていないか判定するメソッド.
        if (Arrays.stream(MapData.OBSTACLES)
                .anyMatch(temp -> temp == mm.getTile(enemyX + ConstSet.ENEMY_SIZE / 2 - 1,
                        enemyY + ConstSet.ENEMY_SIZE / 2 - 1))
                || Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(enemyX - ConstSet.ENEMY_SIZE / 2,
                                enemyY - ConstSet.ENEMY_SIZE / 2))
                || Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(enemyX - ConstSet.ENEMY_SIZE / 2,
                                enemyY + ConstSet.ENEMY_SIZE / 2 - 1))
                || Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(enemyX + ConstSet.ENEMY_SIZE / 2 - 1,
                                enemyY - ConstSet.ENEMY_SIZE / 2))) {
            return true;
        } else {
            return false;
        }
    }

    // プレイヤーが前方にいるときに銃を撃つメソッド.
    public void shootBullet(PlayerModel pm) {
        if (getEnemyDirection() == 0) {
        } else if (getEnemyDirection() == 1) {

        } else if (getEnemyDirection() == 2) {

        } else if (getEnemyDirection() == 3) {

        }

    }



}

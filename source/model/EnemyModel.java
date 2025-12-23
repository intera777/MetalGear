package model;

import java.util.Arrays;
import GameConfig.*;

public class EnemyModel {
    private int enemyX;
    private int enemyY;
    private int enemycondition;
    private int enemydirection; // 敵が向いている方向.0123の順で右上左下.
    private int shoot_timer; // 弾を撃っている状態の管理用.
    private int moving_timer; // 敵の移動の管理用.


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

    public boolean isShooting() { // 弾を撃っている状態ならtrue, そうでなければfalse.
        return shoot_timer > 0;
    }



    public boolean isObstacleExist(MapModel mm) {// 敵と障害物が重なっていないか判定するメソッド.
        int halfSize = ConstSet.ENEMY_SIZE / 2;
        int[] cornerTiles = {mm.getTile(enemyX + halfSize - 1, enemyY + halfSize - 1),
                mm.getTile(enemyX - halfSize, enemyY - halfSize),
                mm.getTile(enemyX - halfSize, enemyY + halfSize - 1),
                mm.getTile(enemyX + halfSize - 1, enemyY - halfSize)};

        // いずれかの隅が障害物タイルに一致するかどうかを判定します。
        // ストリームを何度も生成するのを避け、効率化しています。
        for (int tile : cornerTiles) {
            for (int obstacle : MapData.OBSTACLES) {
                if (tile == obstacle) {
                    return true;
                }
            }
        }
        return false;
    }

    // プレイヤーが前方にいるときに銃を撃つメソッド.
    public void shootBullet(PlayerModel pm) {
        if (getEnemyDirection() == ConstSet.RIGHT) {
        } else if (getEnemyDirection() == ConstSet.UP) {

        } else if (getEnemyDirection() == ConstSet.LEFT) {

        } else if (getEnemyDirection() == ConstSet.DOWN) {

        }

    }

    public void updateEnemyPosition(MapModel mm) {

    }



}

package model;

import java.util.Arrays;
import GameConfig.*;

public class EnemyModel {
    private int enemyX;
    private int enemyY;
    private int enemycondition;
    private int enemydirection; // 敵が向いている方向.0123の順で右上左下.
    private int shoot_timer; // 弾を撃っている状態の管理用.
    private int moving_timer; // 敵の移動の管理用

    private int enemyHP = 3; // 敵のHP. 初期値は3.
    private int damage_timer = 0; // ダメージを受けた際の無敵時間兼、色変更用タイマー.

    public EnemyModel(int startX, int startY) {
        this.enemyX = startX;
        this.enemyY = startY;
        this.enemyHP = 3; // 初期HPを設定
        this.enemycondition = ConstSet.ENEMY_ALIVE;
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

    /**
     * 敵のHPを減少させ、ダメージタイマーを設定します.
     *
     * @param damage 受けるダメージ量
     */
    public void decreaseHP(int damage) {
        // 生存状態(ALIVE)の時のみダメージを受ける
        if (this.enemycondition == ConstSet.ENEMY_ALIVE) {
            this.enemyHP -= damage;
            if (this.enemyHP <= 0) {
                this.enemycondition = ConstSet.ENEMY_DEAD;
            } else {
                this.enemycondition = ConstSet.ENEMY_DAMAGED;
                this.damage_timer = 15; // 15フレーム(約0.5秒)間、ダメージ状態にする
            }
        }
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
    public void shootBullet(PlayerModel pm, BulletsModel bm) {
        // 射撃クールダウン中でないか、プレイヤーがnullでないかを確認
        if (isShooting() || pm == null) {
            return;
        }

        int playerX = pm.getPlayerX();
        int playerY = pm.getPlayerY();
        // 敵の視界の幅（Y軸またはX軸方向のずれの許容範囲）

        boolean playerInSight = false;

        switch (getEnemyDirection()) {
            case ConstSet.RIGHT:
                // プレイヤーが右側かつ、Y座標がほぼ同じ場合
                if (playerX > this.enemyX
                        && Math.abs(playerY - this.enemyY) < ConstSet.SIGHTRANGE) {
                    playerInSight = true;
                }
                break;
            case ConstSet.UP:
                // プレイヤーが上側かつ、X座標がほぼ同じ場合
                if (playerY < this.enemyY
                        && Math.abs(playerX - this.enemyX) < ConstSet.SIGHTRANGE) {
                    playerInSight = true;
                }
                break;
            case ConstSet.LEFT:
                // プレイヤーが左側かつ、Y座標がほぼ同じ場合
                if (playerX < this.enemyX
                        && Math.abs(playerY - this.enemyY) < ConstSet.SIGHTRANGE) {
                    playerInSight = true;
                }
                break;
            case ConstSet.DOWN:
                // プレイヤーが下側かつ、X座標がほぼ同じ場合
                if (playerY > this.enemyY
                        && Math.abs(playerX - this.enemyX) < ConstSet.SIGHTRANGE) {
                    playerInSight = true;
                }
                break;
        }

        if (playerInSight) {
            bm.shootFromEnemy(this); // BulletsModelに弾の発射を依頼
            this.shoot_timer = 60; // 射撃後のクールダウンを設定 (FPS:30 の場合 2秒)
        }
    }

    public void updateEnemyPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {
        // 死亡していたら何もしない
        if (this.enemycondition == ConstSet.ENEMY_DEAD) {
            return;
        }

        // ダメージ状態からの復帰処理
        if (this.enemycondition == ConstSet.ENEMY_DAMAGED) {
            if (damage_timer > 0) {
                damage_timer--;
            }
            if (damage_timer <= 0) {
                this.enemycondition = ConstSet.ENEMY_ALIVE;
            }
        }
        if (shoot_timer > 0) {
            shoot_timer--;
        }
        // プレイヤーが視界にいれば弾を撃つ
        shootBullet(pm, bm);
    }
}

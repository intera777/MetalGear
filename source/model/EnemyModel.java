package model;

import java.util.Arrays;
import GameConfig.*;

public class EnemyModel {
    private int enemyX;
    private int enemyY;
    private int enemycondition; // 敵の状態.
    private int enemydirection; // 敵が向いている方向.0123の順で右上左下.
    private int shoot_timer; // 弾を撃っている状態の管理用.

    private int enemyHP = 3; // 敵のHP. 初期値は3.
    private int damage_timer = 0; // ダメージを受けた際の無敵時間兼、色変更用タイマー.

    // 巡回用データ.
    private int[] waypointsX;
    private int[] waypointsY;
    private int currentWaypointIndex = 0;

    public EnemyModel(int startX, int startY) {
        this.enemyX = startX;
        this.enemyY = startY;
        this.enemyHP = 3; // 初期HPを設定
        this.enemycondition = ConstSet.ENEMY_ALIVE;
    }

    /**
     * 巡回ルートを持つ敵を生成するコンストラクタ.
     * 
     * @param startX 開始X座標
     * @param startY 開始Y座標
     * @param waypointsX 巡回地点のX座標配列
     * @param waypointsY 巡回地点のY座標配列
     */
    public EnemyModel(int startX, int startY, int[] waypointsX, int[] waypointsY) {
        this(startX, startY); // 既存のコンストラクタを呼び出す
        this.waypointsX = waypointsX;
        this.waypointsY = waypointsY;
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

        // いずれもプレイヤーと敵の距離が10ブロック以内なら検知.
        switch (getEnemyDirection()) {
            case ConstSet.RIGHT:
                // プレイヤーが右側かつ、Y座標がほぼ同じ場合
                if (playerX > this.enemyX && Math.abs(playerY - this.enemyY) < ConstSet.SIGHTRANGE
                        && playerX - this.enemyX < ConstSet.TILE_SIZE * 10) {
                    playerInSight = true;
                }
                break;
            case ConstSet.UP:
                // プレイヤーが上側かつ、X座標がほぼ同じ場合
                if (playerY < this.enemyY && Math.abs(playerX - this.enemyX) < ConstSet.SIGHTRANGE
                        && this.enemyY - playerY < ConstSet.TILE_SIZE * 10) {
                    playerInSight = true;
                }
                break;
            case ConstSet.LEFT:
                // プレイヤーが左側かつ、Y座標がほぼ同じ場合
                if (playerX < this.enemyX && Math.abs(playerY - this.enemyY) < ConstSet.SIGHTRANGE
                        && this.enemyX - playerX < ConstSet.TILE_SIZE * 10) {
                    playerInSight = true;
                }
                break;
            case ConstSet.DOWN:
                // プレイヤーが下側かつ、X座標がほぼ同じ場合
                if (playerY > this.enemyY && Math.abs(playerX - this.enemyX) < ConstSet.SIGHTRANGE
                        && playerY - this.enemyY < ConstSet.TILE_SIZE * 10) {
                    playerInSight = true;
                }
                break;
        }

        if (playerInSight) {
            bm.shootFromEnemy(this); // BulletsModelに弾の発射を依頼
            this.shoot_timer = 60; // 射撃後のクールダウンを設定 (FPS:30 の場合 2秒)
        }
    }


    // 設定した巡回ルートに沿って敵を移動させる.
    private void moveAlongPatrolRoute() {
        // 巡回ルートが設定されていない、またはポイントが1つ以下の場合は移動しない.
        if (waypointsX == null || waypointsX.length <= 1) {
            return;
        }

        int targetX = waypointsX[currentWaypointIndex];
        int targetY = waypointsY[currentWaypointIndex];

        // 現在の目標地点を取得.

        // 目標地点に到達したら目標地点を次の座標へ.
        if (enemyX == targetX && enemyY == targetY) {
            // 剰余を使ってループをつくる.
            currentWaypointIndex = (currentWaypointIndex + 1) % waypointsX.length;
            return; // このフレームでは移動しないのでここでメソッドを終了.
        }

        if (enemyX < targetX) {
            enemyX = Math.min(enemyX + ConstSet.ENEMY_SPEED, targetX);
            setEnemyDirection(ConstSet.RIGHT);
        } else if (enemyX > targetX) {
            enemyX = Math.max(enemyX - ConstSet.ENEMY_SPEED, targetX);
            setEnemyDirection(ConstSet.LEFT);
        } else if (enemyY < targetY) { // X座標が合ったらY軸方向の移動
            enemyY = Math.min(enemyY + ConstSet.ENEMY_SPEED, targetY);
            setEnemyDirection(ConstSet.DOWN);
        } else if (enemyY > targetY) {
            enemyY = Math.max(enemyY - ConstSet.ENEMY_SPEED, targetY);
            setEnemyDirection(ConstSet.UP);
        }

    }

    public void updateEnemyPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {
        // 死亡していたら何もしない
        if (this.enemycondition == ConstSet.ENEMY_DEAD) {
            return;
        } else if (this.enemycondition == ConstSet.ENEMY_DAMAGED) { // ダメージ状態からの復帰処理.
            if (damage_timer > 0) {
                damage_timer--;
            }
            if (damage_timer <= 0) {
                this.enemycondition = ConstSet.ENEMY_ALIVE;
            }
        } else {
            // 敵が次の目的地へ向けて最短経路で動く機能をここで実装.
            moveAlongPatrolRoute();
        }
        if (shoot_timer > 0) {
            shoot_timer--;
        }
        // プレイヤーが視界にいれば弾を撃つ
        shootBullet(pm, bm);
    }
}

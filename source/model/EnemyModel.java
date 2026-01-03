package model;

import java.util.*;
import GameConfig.*;

public class EnemyModel {
    private int enemyX;
    private int enemyY;
    private int enemycondition; // 敵の状態.
    private int enemydirection; // 敵が向いている方向.0123の順で右上左下.
    private int shoot_timer; // 弾を撃っている状態の管理用.
    private int player_track = 0; // 1のときはプレイヤーを追尾.

    private int enemyHP = 3; // 敵のHP. 初期値は3.
    private int damage_timer = 0; // ダメージを受けた際の無敵時間兼、色変更用タイマー.

    // 巡回用データ.
    private int[] waypointsX;
    private int[] waypointsY;
    private int currentWaypointIndex = 0;

    // アニメーションに用いる変数
    private int animationTick = 0; // アニメーションの経過時間
    private int animationFrame = 0; // 0, 1, 2 のいずれか

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
            // プレイヤーと敵の距離がマンハッタン距離で32*14以内だったらplayer_track=1,そうでなければ0になる.
            int distance = Math.abs(this.enemyX - pm.getPlayerX())
                    + Math.abs(this.enemyY - pm.getPlayerY());
            if (distance <= ConstSet.TILE_SIZE * 7) {
                this.player_track = 1;
            } else {
                this.player_track = 0;
            }

            // player_trackの値が1だったらプレイヤーのいる位置に向けて最短経路で動く.障害物も加味した最短経路の計算を行えるようにする.
            if (this.player_track == 1) {
                moveTowardsPlayer(mm, pm);
            } else {
                // 敵が次の目的地へ向けて最短経路で動く機能をここで実装.
                moveAlongPatrolRoute();
            }
        }

        // 移動中は敵のコマを更新
        updateAnimation();

        if (shoot_timer > 0) {
            shoot_timer--;
        }
        // プレイヤーが視界にいれば弾を撃つ
        shootBullet(pm, bm);
    }

    // プレイヤーを追尾するかどうかの変数の値を書き換える.
    public void changePlayerTrack(int state) {
        this.player_track = state;
    }

    public int getPlayerTrack() {
        return this.player_track;
    }

    private void moveTowardsPlayer(MapModel mm, PlayerModel pm) {
        int startX = this.enemyX / ConstSet.TILE_SIZE;
        int startY = this.enemyY / ConstSet.TILE_SIZE;
        int targetX = pm.getPlayerX() / ConstSet.TILE_SIZE;
        int targetY = pm.getPlayerY() / ConstSet.TILE_SIZE;

        int nextTile = getNextStep(startX, startY, targetX, targetY, mm.getMap());

        if (nextTile != -1) {
            int w = mm.getMap()[0].length;
            int nextX = nextTile % w;
            int nextY = nextTile / w;

            int targetPixelX = nextX * ConstSet.TILE_SIZE + ConstSet.TILE_SIZE / 2;
            int targetPixelY = nextY * ConstSet.TILE_SIZE + ConstSet.TILE_SIZE / 2;

            if (enemyX < targetPixelX) {
                enemyX = Math.min(enemyX + ConstSet.ENEMY_SPEED, targetPixelX);
                setEnemyDirection(ConstSet.RIGHT);
            } else if (enemyX > targetPixelX) {
                enemyX = Math.max(enemyX - ConstSet.ENEMY_SPEED, targetPixelX);
                setEnemyDirection(ConstSet.LEFT);
            } else if (enemyY < targetPixelY) {
                enemyY = Math.min(enemyY + ConstSet.ENEMY_SPEED, targetPixelY);
                setEnemyDirection(ConstSet.DOWN);
            } else if (enemyY > targetPixelY) {
                enemyY = Math.max(enemyY - ConstSet.ENEMY_SPEED, targetPixelY);
                setEnemyDirection(ConstSet.UP);
            }
        }
    }

    private int getNextStep(int startX, int startY, int targetX, int targetY, int[][] map) {
        int h = map.length;
        int w = map[0].length;
        int start = startY * w + startX;
        int target = targetY * w + targetX;

        if (start == target)
            return start;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        Map<Integer, Integer> cameFrom = new HashMap<>();
        cameFrom.put(start, null);

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (curr == target)
                break;

            int cx = curr % w;
            int cy = curr / w;

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];
                if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                    int next = ny * w + nx;
                    if (!cameFrom.containsKey(next)) {
                        boolean isObstacle = false;
                        for (int obs : MapData.OBSTACLES) {
                            if (map[ny][nx] == obs) {
                                isObstacle = true;
                                break;
                            }
                        }
                        if (!isObstacle) {
                            queue.add(next);
                            cameFrom.put(next, curr);
                        }
                    }
                }
            }
        }
        if (!cameFrom.containsKey(target))
            return -1;
        int curr = target;
        while (cameFrom.get(curr) != null && cameFrom.get(curr) != start) {
            curr = cameFrom.get(curr);
        }
        return curr;
    }

    public int getAnimationFrame() { // ゲッター
        return animationFrame;
    }

    private void updateAnimation() {
        animationTick++;
        // 10フレームごとに1コマ進める
        if (animationTick % 10 == 0) {
            animationFrame = (animationFrame + 1) % 3; // 0, 1, 2 を繰り返す
        }
    }

}

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

    // --- エフェクトとBGM用 ---
    private int effect_type = 0;    // 0:なし, 1:発見(!), 2:見失い(?)
    private int effect_timer = 0;   // エフェクト表示時間用タイマー
    private boolean bgm_alert = false; // 現在アラートBGMを流すべき状態か
    
    // --- フリーズ用タイマー ---
    private int freeze_timer = 0;   // 発見時に足を止めるタイマー

    // --- 追加: 永続追跡用のタイマー ---
    private int tracking_duration = 0; // 追跡開始からの経過時間

    public EnemyModel(int startX, int startY) {
        this.enemyX = startX;
        this.enemyY = startY;
        this.enemyHP = 3; 
        this.enemycondition = ConstSet.ENEMY_ALIVE;
    }

    public EnemyModel(int startX, int startY, int[] waypointsX, int[] waypointsY) {
        this(startX, startY);
        this.waypointsX = waypointsX;
        this.waypointsY = waypointsY;
    }

    // --- ゲッター・セッター ---
    public int getEnemyX() { return enemyX; }
    public int getEnemyY() { return enemyY; }
    public int getEnemyCondition() { return enemycondition; }
    public int getEnemyDirection() { return enemydirection; }
    public void setEnemyIndex(int x, int y) { this.enemyX = x; this.enemyY = y; }
    public void setEmenyCondition(int condition) { this.enemycondition = condition; }
    public void setEnemyDirection(int direction) { this.enemydirection = direction; }
    public boolean isShooting() { return shoot_timer > 0; }
    public int getAnimationFrame() { return animationFrame; }
    public int getPlayerTrack() { return this.player_track; }
    public int getEffectType() { return (effect_timer > 0) ? effect_type : 0; }
    public boolean isBgmAlert() { return this.bgm_alert; }
    
    // EnemiesModelから参照用
    public int getTrackingDuration() { return this.tracking_duration; }

    public void decreaseHP(int damage) {
        // ダメージを受けても追跡が止まらないよう、ENEMY_DAMAGED中も判定を有効にする
        if (this.enemycondition == ConstSet.ENEMY_ALIVE || this.enemycondition == ConstSet.ENEMY_DAMAGED) {
            this.enemyHP -= damage;
            if (this.enemyHP <= 0) {
                this.enemycondition = ConstSet.ENEMY_DEAD;
                this.bgm_alert = false; 
            } else {
                this.enemycondition = ConstSet.ENEMY_DAMAGED;
                this.damage_timer = 15;
            }
        }
    }

    public boolean isObstacleExist(MapModel mm) {
        int halfSize = ConstSet.ENEMY_SIZE / 2;
        int[] cornerTiles = {
            mm.getTile(enemyX + halfSize - 1, enemyY + halfSize - 1),
            mm.getTile(enemyX - halfSize, enemyY - halfSize),
            mm.getTile(enemyX - halfSize, enemyY + halfSize - 1),
            mm.getTile(enemyX + halfSize - 1, enemyY - halfSize)
        };
        for (int tile : cornerTiles) {
            for (int obstacle : MapData.OBSTACLES) {
                if (tile == obstacle) return true;
            }
        }
        return false;
    }

    public void shootBullet(PlayerModel pm, BulletsModel bm) {
        // 死亡時以外、かつ追跡中なら射撃判定
        if (player_track == 0 || freeze_timer > 0 || isShooting() || 
            pm == null || this.enemycondition == ConstSet.ENEMY_DEAD) {
            return;
        }

        if (isPlayerInSight(pm, 6)) { 
            bm.shootFromEnemy(this);
            this.shoot_timer = 60;
        }
    }

    private boolean isPlayerInSight(PlayerModel pm, int tileRange) {
        int px = pm.getPlayerX();
        int py = pm.getPlayerY();
        int range = ConstSet.TILE_SIZE * tileRange;

        switch (getEnemyDirection()) {
            case ConstSet.RIGHT:
                return (px > enemyX && px - enemyX < range && Math.abs(py - enemyY) < ConstSet.SIGHTRANGE);
            case ConstSet.UP:
                return (py < enemyY && enemyY - py < range && Math.abs(px - enemyX) < ConstSet.SIGHTRANGE);
            case ConstSet.LEFT:
                return (px < enemyX && enemyX - px < range && Math.abs(py - enemyY) < ConstSet.SIGHTRANGE);
            case ConstSet.DOWN:
                return (py > enemyY && py - enemyY < range && Math.abs(px - enemyX) < ConstSet.SIGHTRANGE);
        }
        return false;
    }

    private void moveAlongPatrolRoute() {
        if (waypointsX == null || waypointsX.length <= 1) return;

        int targetX = waypointsX[currentWaypointIndex];
        int targetY = waypointsY[currentWaypointIndex];

        if (enemyX == targetX && enemyY == targetY) {
            currentWaypointIndex = (currentWaypointIndex + 1) % waypointsX.length;
            return; 
        }

        if (enemyX < targetX) {
            enemyX = Math.min(enemyX + ConstSet.ENEMY_SPEED, targetX);
            setEnemyDirection(ConstSet.RIGHT);
        } else if (enemyX > targetX) {
            enemyX = Math.max(enemyX - ConstSet.ENEMY_SPEED, targetX);
            setEnemyDirection(ConstSet.LEFT);
        } else if (enemyY < targetY) {
            enemyY = Math.min(enemyY + ConstSet.ENEMY_SPEED, targetY);
            setEnemyDirection(ConstSet.DOWN);
        } else if (enemyY > targetY) {
            enemyY = Math.max(enemyY - ConstSet.ENEMY_SPEED, targetY);
            setEnemyDirection(ConstSet.UP);
        }
    }

    public void updateEnemyPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {
        if (this.enemycondition == ConstSet.ENEMY_DEAD) return;

        // 被弾タイマー更新
        if (this.enemycondition == ConstSet.ENEMY_DAMAGED) {
            if (damage_timer > 0) damage_timer--;
            if (damage_timer <= 0) this.enemycondition = ConstSet.ENEMY_ALIVE;
        }

        double distance = Math.sqrt(Math.pow(this.enemyX - pm.getPlayerX(), 2) + Math.pow(this.enemyY - pm.getPlayerY(), 2));
        
        // 全体が永続アラート状態なら、強制的に追跡モードへ同期
        if (EnemiesModel.isPermanentAlert) {
            this.player_track = 1;
            this.bgm_alert = true;
        }

        if (this.player_track == 0) {
            // 発見ロジック
            boolean detected = false;
            if (distance <= ConstSet.TILE_SIZE * 3) detected = true; // 近接
            else if (isPlayerInSight(pm, 5)) detected = true; // 視界

            if (detected) {
                this.player_track = 1;
                this.bgm_alert = true;
                this.effect_type = 1; // "!"
                this.effect_timer = 45; // 1.5秒「！」頭上に発現
                this.freeze_timer = 45; // 1.5秒フリーズ
                this.tracking_duration = 0; 
            }
        } else {
            // 追跡中ロジック
            tracking_duration++; 

            // 「全体が永続アラート」でなく、かつ「自分の追跡時間が5秒未満」の場合のみ見失う可能性がある
            if (!EnemiesModel.isPermanentAlert && tracking_duration < 150) {
                if (distance >= ConstSet.TILE_SIZE * 6) {
                    this.player_track = 0;
                    this.bgm_alert = false;
                    this.effect_type = 2; // "?"
                    this.effect_timer = 90; // 3秒「?」頭上に発現
                    this.freeze_timer = 90; // 3秒フリーズ
                }
            }
        }

        // 移動制御
        if (freeze_timer > 0) {
            freeze_timer--;
            this.animationFrame = 0; 
        } else {
            if (this.player_track == 1) {
                moveTowardsPlayer(mm, pm);
            } else {
                moveAlongPatrolRoute();
            }
            updateAnimation();
        }

        if (shoot_timer > 0) shoot_timer--;
        if (effect_timer > 0) effect_timer--; 
        
        shootBullet(pm, bm);
    }

    public void changePlayerTrack(int state) {
        this.player_track = state;
        this.bgm_alert = (state == 1);
        if (state == 1) this.tracking_duration = 0;
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
        int h = map.length; int w = map[0].length;
        int start = startY * w + startX; int target = targetY * w + targetX;
        if (start == target) return start;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        Map<Integer, Integer> cameFrom = new HashMap<>();
        cameFrom.put(start, null);

        int[] dx = {0, 0, -1, 1}; int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (curr == target) break;
            int cx = curr % w; int cy = curr / w;
            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i]; int ny = cy + dy[i];
                if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                    int next = ny * w + nx;
                    if (!cameFrom.containsKey(next)) {
                        boolean isObstacle = false;
                        for (int obs : MapData.OBSTACLES) { if (map[ny][nx] == obs) { isObstacle = true; break; } }
                        if (!isObstacle) { queue.add(next); cameFrom.put(next, curr); }
                    }
                }
            }
        }
        if (!cameFrom.containsKey(target)) return -1;
        int curr = target;
        while (cameFrom.get(curr) != null && cameFrom.get(curr) != start) { curr = cameFrom.get(curr); }
        return curr;
    }

    private void updateAnimation() {
        animationTick++;
        if (animationTick % 10 == 0) animationFrame = (animationFrame + 1) % 3;
    }
}
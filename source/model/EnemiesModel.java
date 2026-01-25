package model;

import java.util.ArrayList;
import GameConfig.*;

public class EnemiesModel {
    private ArrayList<EnemyModel> enemies;
    private int discoveryCountThisFrame = 0;

    // --- 増援管理用の変数 ---
    private int reinforcementTimer = 0;
    private final int SPAWN_INTERVAL = 30 * 2; // 2秒ごとに増援 (30FPS)
    private final int MAX_ENEMIES = 10;        // 画面内の最大敵数制限
    private static final int TS = ConstSet.TILE_SIZE; // タイルのサイズ

    // マップ全体が「永続アラート状態」かどうかの共有フラグ
    // これが true になると増援が始まり、二度と止まらなくなる
    public static boolean isPermanentAlert = false;

    public int countNewDiscoveries() {
        return discoveryCountThisFrame;
    }

    public EnemiesModel() {
        this.enemies = new ArrayList<EnemyModel>();
        isPermanentAlert = false; // インスタンス化時にリセット
    }

    public void updateEnemiesPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {

        // 各敵の位置や状態を更新
        for (EnemyModel enemy : enemies) {
            if (enemy != null) {
                enemy.updateEnemyPosition(mm, pm, bm);
                // 誰か一人が120フレーム（4秒）追跡したら、全体を永続アラートにする
                if (enemy.getTrackingDuration() >= 150) {
                    isPermanentAlert = true;
                }
            }
        }

        // このフレームでの新規発見数を一括集計
        this.discoveryCountThisFrame = calculateDiscoveryCount();

        // 発見があったら減点
        if (discoveryCountThisFrame > 0) { 
            pm.penaltyForAlert(); 
        }

        // HPが0になった敵をリストから削除する. マップ内の敵数が減ったら減点.
        int beforeCount = enemies.size();
        enemies.removeIf(enemy -> enemy.getEnemyCondition() == ConstSet.ENEMY_DEAD);
        int afterCount = enemies.size();
        if (afterCount < beforeCount) {
            // 減った分だけペナルティ
            for (int i = 0; i < (beforeCount - afterCount); i++) {
                pm.penaltyForKill(); // PlayerModelに新規作成するメソッド
            }
        }
        // 増援チェック
        checkAndSpawnReinforcements(mm, pm);
    }

    /**
     * 内部でのみ使用。実際に敵のフラグをチェックして数を数える。
     */
    private int calculateDiscoveryCount() {
        int count = 0;
        for (EnemyModel enemy : enemies) {
            if (enemy != null && enemy.pullJustDetected()) {
                count++;
            }
        }
        return count;
    }

    // 増援の発生を管理するメソッド
    private void checkAndSpawnReinforcements(MapModel mm, PlayerModel pm) {
        // 3秒経過して「永続アラート状態」になった後から増援を開始する
        if (isPermanentAlert) {
            reinforcementTimer++;

            if (reinforcementTimer >= SPAWN_INTERVAL) {
                if (enemies.size() < MAX_ENEMIES) {
                    spawnFromStairs(mm);
                }
                reinforcementTimer = 0; // スポーンしたらタイマーリセット
            }
        } else {
            // 3秒経つまではタイマーを動かさない
            reinforcementTimer = 0;
        }
    }

    /**
     * マップごとの階段位置に敵を生成する
     */
    private void spawnFromStairs(MapModel mm) {
        int[][] currentMap = mm.getMap();
        int spawnX = -100;
        int spawnY = -100;

        // 現在のマップに応じて階段の座標を決定
        if (currentMap == MapData.MAPA1) {
            spawnX = 2 * TS; 
            spawnY = 15 * TS; 
        } else if (currentMap == MapData.MAPB0) {
            spawnX = 4 * TS;
            spawnY = 23 * TS;
        } else if (currentMap == MapData.MAPC0) {
            spawnX = 58 * TS;
            spawnY = 31 * TS;
        }

        if (spawnX != -100) {
            EnemyModel newEnemy = new EnemyModel(spawnX, spawnY);
            
            // 出現した瞬間に追跡モードにする
            newEnemy.changePlayerTrack(1);
            
            enemies.add(newEnemy);
            System.out.println("増援部隊が階段から出現！ (2秒間隔モード)"); // 後で消去する
        }
    }

    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }

    public void setEnemiesForMap(int[][] map) {
        enemies.clear();
        reinforcementTimer = 0; 
        isPermanentAlert = false; // マップ移動時はアラートを解除

        if (map == MapData.MAPA0) {
            // 敵なし
        } else if (map == MapData.MAPA1) {
            enemies.add(new EnemyModel(MapData.MAPA1_E0X[0], MapData.MAPA1_E0Y[0], MapData.MAPA1_E0X, MapData.MAPA1_E0Y));
            enemies.add(new EnemyModel(MapData.MAPA1_E1X[0], MapData.MAPA1_E1Y[0], MapData.MAPA1_E1X, MapData.MAPA1_E1Y));
            enemies.add(new EnemyModel(MapData.MAPA1_E2X[0], MapData.MAPA1_E2Y[0], MapData.MAPA1_E2X, MapData.MAPA1_E2Y));
        } else if (map == MapData.MAPB0) {
            enemies.add(new EnemyModel(MapData.MAPB0_E0X[0], MapData.MAPB0_E0Y[0], MapData.MAPB0_E0X, MapData.MAPB0_E0Y));
            enemies.add(new EnemyModel(MapData.MAPB0_E1X[0], MapData.MAPB0_E1Y[0], MapData.MAPB0_E1X, MapData.MAPB0_E1Y));
        } else if (map == MapData.MAPC0) {
            enemies.add(new EnemyModel(MapData.MAPC0_E0X[0], MapData.MAPC0_E0Y[0], MapData.MAPC0_E0X, MapData.MAPC0_E0Y));
            enemies.add(new EnemyModel(MapData.MAPC0_E1X[0], MapData.MAPC0_E1Y[0], MapData.MAPC0_E1X, MapData.MAPC0_E1Y));
            enemies.add(new EnemyModel(MapData.MAPC0_E2X[0], MapData.MAPC0_E2Y[0], MapData.MAPC0_E2X, MapData.MAPC0_E2Y));
            enemies.add(new EnemyModel(MapData.MAPC0_E3X[0], MapData.MAPC0_E3Y[0], MapData.MAPC0_E3X, MapData.MAPC0_E3Y));
            enemies.add(new EnemyModel(MapData.MAPC0_E4X[0], MapData.MAPC0_E4Y[0], MapData.MAPC0_E4X, MapData.MAPC0_E4Y));
        }
    }

    public boolean isAnyEnemyPursuing(PlayerModel playerModel) {
        return isPermanentAlert;
    }

    public boolean isAnyEnemyTracking() {
        // 絶望モードかどうかに関わらず、誰か一人でも player_track == 1 なら true
        for (EnemyModel enemy : this.enemies) {
            if (enemy != null && enemy.getEnemyCondition() != ConstSet.ENEMY_DEAD 
                && enemy.getPlayerTrack() == 1) {
                return true;
            }
        }
        return false;
    }
}
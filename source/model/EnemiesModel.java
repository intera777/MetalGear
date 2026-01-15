package model;

import java.util.ArrayList;

import GameConfig.*;

public class EnemiesModel {
    private ArrayList<EnemyModel> enemies;

    public EnemiesModel() {
        this.enemies = new ArrayList<EnemyModel>();
    }

    public void updateEnemiesPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {
        // 各敵の位置や状態を更新
        for (EnemyModel enemy : enemies) {
            if (enemy != null) {
                enemy.updateEnemyPosition(mm, pm, bm);
            }
        }
        // HPが0になった敵をリストから削除する
        enemies.removeIf(enemy -> enemy.getEnemyCondition() == ConstSet.ENEMY_DEAD);
    }

    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }

    public void setEnemiesForMap(int[][] map) {
        enemies.clear();
        if (map == MapData.MAPA0) {
            // 敵なし.
        } else if (map == MapData.MAPA1) {
            // MAPA1の敵を巡回ルートと共に生成
            enemies.add(new EnemyModel(MapData.MAPA1_E0X[0], MapData.MAPA1_E0Y[0],
                    MapData.MAPA1_E0X, MapData.MAPA1_E0Y));
            enemies.add(new EnemyModel(MapData.MAPA1_E1X[0], MapData.MAPA1_E1Y[0],
                    MapData.MAPA1_E1X, MapData.MAPA1_E1Y));
            enemies.add(new EnemyModel(MapData.MAPA1_E2X[0], MapData.MAPA1_E2Y[0],
                    MapData.MAPA1_E2X, MapData.MAPA1_E2Y));
        } else if (map == MapData.MAPA2) {
        }
    }

    /**
     * いずれかの敵がプレイヤーを追跡しているかどうかを判定します。
     * 
     * @param playerModel プレイヤーのモデル
     * @return 1体でも追跡していればtrue
     */
    public boolean isAnyEnemyPursuing(PlayerModel playerModel) {
        final int ENEMY_SIGHT_DISTANCE = ConstSet.TILE_SIZE * 8; // 敵が追跡を開始する距離
        for (EnemyModel enemy : this.enemies) {
            if (enemy == null || enemy.getEnemyCondition() == ConstSet.ENEMY_DEAD)
                continue;

            int dx = enemy.getEnemyX() - playerModel.getPlayerX();
            int dy = enemy.getEnemyY() - playerModel.getPlayerY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < ENEMY_SIGHT_DISTANCE) {
                return true; // 簡易的に、距離が近い＝追尾中とみなす
            }
        }
        return false;
    }
}

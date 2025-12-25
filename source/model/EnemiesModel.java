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

}

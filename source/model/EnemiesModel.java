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
            enemies.add(new EnemyModel(50 * ConstSet.TILE_SIZE, 5 * ConstSet.TILE_SIZE));
            enemies.add(new EnemyModel(45 * ConstSet.TILE_SIZE, 8 * ConstSet.TILE_SIZE));
        } else if (map == MapData.MAPA2) {
        }
    }

}

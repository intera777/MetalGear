package model;

import java.util.ArrayList;

import GameConfig.*;

public class EnemiesModel {
    private ArrayList<EnemyModel> enemies;

    public EnemiesModel() {
        this.enemies = new ArrayList<EnemyModel>();
    }

    public void updateEnemiesPosition(MapModel mm) {
        // nullの可能性も加味しながらループで処理.
        for (EnemyModel enemy : enemies) {
            if (enemy != null) {
                enemy.updateEnemyPosition(mm);
            }
        }
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
            enemies.add(new EnemyModel(12 * ConstSet.TILE_SIZE, 6 * ConstSet.TILE_SIZE));
        }
    }

}

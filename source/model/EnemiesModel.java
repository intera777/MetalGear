package model;

import GameConfig.ConstSet;

public class EnemiesModel {
    private EnemyModel[] enemies;

    public EnemiesModel() {
        this.enemies = new EnemyModel[ConstSet.MAX_ENEMIES];
    }

    public void updateEnemiesPosition(MapModel mm) {
        // nullの可能性も加味しながらループで処理.
        for (EnemyModel enemy : enemies) {
            if (enemy != null) {
                enemy.updateEnemyPosition(mm);
            }
        }
    }

    public EnemyModel[] getEnemies() {
        return enemies;
    }

}

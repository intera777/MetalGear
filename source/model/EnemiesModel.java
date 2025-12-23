package model;

public class EnemiesModel {
    private EnemyModel[] enemies;

    public EnemiesModel(EnemyModel[] em) {
        this.enemies = em;
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

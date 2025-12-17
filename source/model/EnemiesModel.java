package model;

public class EnemiesModel {
    private EnemyModel[] enemies;

    public EnemiesModel(EnemyModel[] em) {
        this.enemies = new EnemyModel[100];
        for (int i = 0; i < em.length; i++) {
            enemies[i] = em[i];
        }
    }


}

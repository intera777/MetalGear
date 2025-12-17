package model;

import GameConfig.*;

public class EnemyModel {
    private int enemyX;
    private int enemyY;
    private int enemycondition;

    public EnemyModel(int startX, int startY) {
        this.enemyX = startX;
        this.enemyY = startY;
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
}

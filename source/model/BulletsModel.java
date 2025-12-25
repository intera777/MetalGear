package model;

import GameConfig.*;

public class BulletsModel {
    private BulletModel[] bullets;

    public BulletsModel(PlayerModel pm) {
        this.bullets = new BulletModel[ConstSet.MAX_BULLETS];
        for (int i = 0; i < ConstSet.MAX_BULLETS; i++) {
            bullets[i] = new BulletModel(pm);
        }
    }

    public BulletModel[] getBullets() {
        return bullets;
    }

    public void keyTappedNewly() {
        for (int i = 0; i < ConstSet.MAX_BULLETS; i++) {
            if (!bullets[i].bulletExist()) {
                bullets[i].shootBullet();
                break;
            }
        }
    }

    public void shootFromEnemy(EnemyModel enemy) {
        for (int i = 0; i < ConstSet.MAX_BULLETS; i++) {
            if (!bullets[i].bulletExist()) {
                bullets[i].shootBulletFromEnemy(enemy);
                break;
            }
        }
    }

    // 弾とプレイヤーの当たり判定を行うプライベートメソッド
    private boolean checkPlayerCollision(BulletModel bullet, PlayerModel player) {
        // プレイヤーの矩形範囲 (中心座標からの相対位置で計算)
        int playerLeft = player.getPlayerX() - ConstSet.PLAYER_SIZE / 2;
        int playerRight = player.getPlayerX() + ConstSet.PLAYER_SIZE / 2;
        int playerTop = player.getPlayerY() - ConstSet.PLAYER_SIZE / 2;
        int playerBottom = player.getPlayerY() + ConstSet.PLAYER_SIZE / 2;

        // 弾の矩形範囲 (中心座標からの相対位置で計算)
        int bulletLeft = bullet.getBulletX() - ConstSet.BULLET_SIZE / 2;
        int bulletRight = bullet.getBulletX() + ConstSet.BULLET_SIZE / 2;
        int bulletTop = bullet.getBulletY() - ConstSet.BULLET_SIZE / 2;
        int bulletBottom = bullet.getBulletY() + ConstSet.BULLET_SIZE / 2;

        // 矩形が重なっているか判定 (重なっていなければtrueになる条件の否定)
        return playerLeft < bulletRight && playerRight > bulletLeft && playerTop < bulletBottom
                && playerBottom > bulletTop;
    }

    // 弾と敵の当たり判定を行うプライベートメソッド
    private boolean checkEnemyCollision(BulletModel bullet, EnemyModel enemy) {
        // 敵の矩形範囲
        int enemyLeft = enemy.getEnemyX() - ConstSet.ENEMY_SIZE / 2;
        int enemyRight = enemy.getEnemyX() + ConstSet.ENEMY_SIZE / 2;
        int enemyTop = enemy.getEnemyY() - ConstSet.ENEMY_SIZE / 2;
        int enemyBottom = enemy.getEnemyY() + ConstSet.ENEMY_SIZE / 2;

        // 弾の矩形範囲
        int bulletLeft = bullet.getBulletX() - ConstSet.BULLET_SIZE / 2;
        int bulletRight = bullet.getBulletX() + ConstSet.BULLET_SIZE / 2;
        int bulletTop = bullet.getBulletY() - ConstSet.BULLET_SIZE / 2;
        int bulletBottom = bullet.getBulletY() + ConstSet.BULLET_SIZE / 2;

        return enemyLeft < bulletRight && enemyRight > bulletLeft && enemyTop < bulletBottom
                && enemyBottom > bulletTop;
    }

    public void updateBulletsPosition(MapModel mm, PlayerModel pm, EnemiesModel em) {
        for (BulletModel bullet : bullets) {
            if (bullet.bulletExist()) {
                if (bullet.isShotByPlayer()) {
                    // プレイヤーの弾：敵との当たり判定
                    boolean hit = false;
                    for (EnemyModel enemy : em.getEnemies()) {
                        if (checkEnemyCollision(bullet, enemy)) {
                            enemy.decreaseHP(1); // 敵のHPを1減らす
                            bullet.destroy(); // 弾を消滅させる
                            hit = true;
                            break; // 1発の弾は1体の敵にのみ当たる
                        }
                    }
                    if (!hit) {
                        bullet.updateBulletPosition(mm); // 当たっていなければ移動
                    }
                } else {
                    // 敵の弾：プレイヤーとの当たり判定
                    if (checkPlayerCollision(bullet, pm)) {
                        pm.decreaseHP(1); // プレイヤーのHPを1減らす
                        bullet.destroy(); // 弾を消滅させる
                    } else {
                        bullet.updateBulletPosition(mm);
                    }
                }
            }
        }
    }
}

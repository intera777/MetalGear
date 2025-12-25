package model;

import GameConfig.*;

public class BulletModel {
      private final int SPEED = 32;

      private int x = -100;
      private int y = -100;
      private int speed_x = 0;
      private int speed_y = 0;
      private boolean exist = false; // フィールド上に存在していればtrue.
      private boolean shotByPlayer; // プレイヤーによって撃たれた弾ならtrue
      private int bulletDirection;
      PlayerModel playermodel;

      public BulletModel(PlayerModel pm) {
            playermodel = pm;
      }

      // 弾を発射する(弾を新しくフィールド内に投入する)メソッド.
      public void shootBullet() {
            if (!exist) {
                  exist = true;
                  this.shotByPlayer = true; // プレイヤーが撃った弾
                  // プレイヤーの向きに応じて弾の初期位置と速度を設定.
                  if (playermodel.getPlayerDirection() == ConstSet.RIGHT) {
                        x = playermodel.getPlayerX() + ConstSet.PLAYER_SIZE / 2
                                    + ConstSet.BULLET_SIZE / 2;
                        y = playermodel.getPlayerY();
                        speed_x = SPEED;
                        speed_y = 0;
                        bulletDirection = ConstSet.RIGHT;
                  } else if (playermodel.getPlayerDirection() == ConstSet.UP) {
                        x = playermodel.getPlayerX();
                        y = playermodel.getPlayerY()
                                    - (ConstSet.PLAYER_SIZE / 2 + ConstSet.BULLET_SIZE / 2) - 1;
                        speed_x = 0;
                        speed_y = -SPEED;
                        bulletDirection = ConstSet.UP;
                  } else if (playermodel.getPlayerDirection() == ConstSet.LEFT) {
                        x = playermodel.getPlayerX()
                                    - (ConstSet.PLAYER_SIZE / 2 + ConstSet.BULLET_SIZE / 2) - 1;
                        y = playermodel.getPlayerY();
                        speed_x = -SPEED;
                        speed_y = 0;
                        bulletDirection = ConstSet.LEFT;
                  } else if (playermodel.getPlayerDirection() == ConstSet.DOWN) {
                        x = playermodel.getPlayerX();
                        y = playermodel.getPlayerY() + ConstSet.PLAYER_SIZE / 2
                                    + ConstSet.BULLET_SIZE / 2;
                        speed_x = 0;
                        speed_y = SPEED;
                        bulletDirection = ConstSet.DOWN;
                  }
            }
      }

      // 敵が弾を発射するメソッド
      public void shootBulletFromEnemy(EnemyModel enemy) {
            if (!exist) {
                  exist = true;
                  this.shotByPlayer = false; // 敵が撃った弾
                  // 敵の向きに応じて弾の初期位置と速度を設定
                  if (enemy.getEnemyDirection() == ConstSet.RIGHT) {
                        x = enemy.getEnemyX() + ConstSet.ENEMY_SIZE / 2 + ConstSet.BULLET_SIZE / 2;
                        y = enemy.getEnemyY();
                        speed_x = SPEED;
                        speed_y = 0;
                        bulletDirection = ConstSet.RIGHT;
                  } else if (enemy.getEnemyDirection() == ConstSet.UP) {
                        x = enemy.getEnemyX();
                        y = enemy.getEnemyY() - (ConstSet.ENEMY_SIZE / 2 + ConstSet.BULLET_SIZE / 2)
                                    - 1;
                        speed_x = 0;
                        speed_y = -SPEED;
                        bulletDirection = ConstSet.UP;
                  } else if (enemy.getEnemyDirection() == ConstSet.LEFT) {
                        x = enemy.getEnemyX() - (ConstSet.ENEMY_SIZE / 2 + ConstSet.BULLET_SIZE / 2)
                                    - 1;
                        y = enemy.getEnemyY();
                        speed_x = -SPEED;
                        speed_y = 0;
                        bulletDirection = ConstSet.LEFT;
                  } else if (enemy.getEnemyDirection() == ConstSet.DOWN) {
                        x = enemy.getEnemyX();
                        y = enemy.getEnemyY() + ConstSet.ENEMY_SIZE / 2 + ConstSet.BULLET_SIZE / 2;
                        speed_x = 0;
                        speed_y = SPEED;
                        bulletDirection = ConstSet.DOWN;
                  }
            }
      }

      // 弾のX座標を取得するメソッド.
      public int getBulletX() {
            return x;
      }

      // 弾のY座標を取得するメソッド.
      public int getBulletY() {
            return y;
      }

      // 弾が現在フィールド上に存在するかどうかを返すメソッド.
      public boolean bulletExist() {
            return exist;
      }

      // プレイヤーが撃った弾かどうかを返す
      public boolean isShotByPlayer() {
            return shotByPlayer;
      }

      // 弾の進んでいる方向.
      public int getBulletDirection() {
            return bulletDirection;
      }


      // 銃弾と障害物が当たっているか判定するメソッド.遷移ポイントも障害物に含む.
      public boolean isObstacleHit(MapModel mm) {
            int halfSize = ConstSet.BULLET_SIZE / 2;
            int[] cornerTiles = {mm.getTile(x + halfSize - 1, y + halfSize - 1),
                        mm.getTile(x - halfSize, y - halfSize),
                        mm.getTile(x - halfSize, y + halfSize - 1),
                        mm.getTile(x + halfSize - 1, y - halfSize)};

            // 弾の四隅のいずれかが障害物または遷移ポイント上にあるか判定します。
            // getTileやストリームの不要な繰り返しをなくし、効率化しています。
            for (int tile : cornerTiles) {
                  if (tile >= 100) { // 遷移ポイント
                        return true;
                  }
                  for (int obstacle : MapData.OBSTACLES) {
                        if (tile == obstacle) {
                              return true;
                        }
                  }
            }
            return false;
      }

      // 弾を消滅させるメソッド
      public void destroy() {
            exist = false;
            x = -100;
            y = -100;
            speed_x = 0;
            speed_y = 0;
      }

      // 銃弾の位置を更新するメソッド.
      public void updateBulletPosition(MapModel mm) {
            x += speed_x;
            y += speed_y;
            // 画面外または障害物に当たったら消滅.
            if (x < 0 || mm.getMap()[0].length * ConstSet.TILE_SIZE < x || y < 0
                        || mm.getMap().length * ConstSet.TILE_SIZE < y || isObstacleHit(mm)) {
                  destroy();
            }
      }
}

package model;

import java.util.Arrays;
import GameConfig.*;

public class BulletModel {
      private final int SPEED = 32;

      private int x = -100;
      private int y = -100;
      private int speed_x = 0;
      private int speed_y = 0;
      private boolean exist = false; // フィールド上に存在していればtrue.
      PlayerModel playermodel;

      public BulletModel(PlayerModel pm) {
            playermodel = pm;
      }

      // 弾を発射する(弾を新しくフィールド内に投入する)メソッド.
      public void shootBullet() {
            if (!exist) {
                  exist = true;
                  // プレイヤーの向きに応じて弾の初期位置と速度を設定.
                  if (playermodel.getPlayerDirection() == 0) {
                        x = playermodel.getPlayerX() + 32;
                        y = playermodel.getPlayerY();
                        speed_x = SPEED;
                        speed_y = 0;
                  } else if (playermodel.getPlayerDirection() == 1) {
                        x = playermodel.getPlayerX();
                        y = playermodel.getPlayerY() - 32;
                        speed_x = 0;
                        speed_y = -SPEED;
                  } else if (playermodel.getPlayerDirection() == 2) {
                        x = playermodel.getPlayerX() - 32;
                        y = playermodel.getPlayerY();
                        speed_x = -SPEED;
                        speed_y = 0;
                  } else if (playermodel.getPlayerDirection() == 3) {
                        x = playermodel.getPlayerX();
                        y = playermodel.getPlayerY() + 32;
                        speed_x = 0;
                        speed_y = SPEED;
                  }
            }
      }

      public int getBulletX() {
            return x;
      }

      public int getBulletY() {
            return y;
      }

      // 弾が現在フィールド上に存在するかどうかを返すメソッド.
      public boolean bulletExist() {
            return exist;
      }

      // 銃弾と障害物が当たっているか判定するメソッド.
      public boolean isObstacleHit(MapModel mm) {
            if (Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(x + ConstSet.BULLET_SIZE / 2 - 1, y))
                        || Arrays.stream(MapData.OBSTACLES).anyMatch(
                                    temp -> temp == mm.getTile(x - ConstSet.BULLET_SIZE / 2 + 1, y))
                        || Arrays.stream(MapData.OBSTACLES).anyMatch(
                                    temp -> temp == mm.getTile(x, y + ConstSet.BULLET_SIZE / 2 - 1))
                        || Arrays.stream(MapData.OBSTACLES).anyMatch(temp -> temp == mm.getTile(x,
                                    y - ConstSet.BULLET_SIZE / 2 + 1))) {
                  return true;
            } else {
                  return false;
            }
      }

      // 銃弾の位置を更新するメソッド.
      public void updateBulletPosition(MapModel mm) {
            x += speed_x;
            y += speed_y;
            // 画面外または障害物に当たったら消滅.
            if (x < 0 || mm.getMap()[0].length * ConstSet.TILE_SIZE <= x || y < 0
                        || mm.getMap().length * ConstSet.TILE_SIZE <= y || isObstacleHit(mm)) {
                  exist = false;
                  x = -100;
                  y = -100;
                  speed_x = 0;
                  speed_y = 0;
            }
      }
}

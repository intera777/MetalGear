package model;

import
import java.util.Observable;

public class BulletModel extends Observable {
      private int x = -100;
      private int y = -100;
      private int speed_x = 0;
      private int speed_y = 0;
      private boolean exist = false; // フィールド上に存在していればtrue.

      public void shootBullet(PlayerModel playermodel) {
            if (!exist) {
                  exist = true;
                  if (playermodel.getPlayerDirection() == 0) {
                        x = playermodel.getPlayerX() + 32;
                        speed_x = 5;
                        speed_y = 0;
                  } else if (playermodel.getPlayerDirection() == 1) {
                        y = playermodel.getPlayerY() - 32;
                        speed_x = 0;
                        speed_y = -5;
                  } else if (playermodel.getPlayerDirection() == 2) {
                        x = playermodel.getPlayerX() - 32;
                        speed_x = -5;
                        speed_y = 0;
                  } else if (playermodel.getPlayerDirection() == 3) {
                        y = playermodel.getPlayerY() + 32;
                        speed_x = 0;
                        speed_y = 5;
                  }
            }
      }

      public void keyTappedNewly() {

      shootBullet(PlayerModel playermodel);

      }

      public int getPlayerX() {
            return x;
      }

      public int getPlayerY() {
            return y;
      }

      public boolean bulletExist() {
            return exist;
      }

      public void updateBulletPosition() {

      }
}

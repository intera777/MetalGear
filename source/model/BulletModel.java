package model;

import java.util.Observable;

public class BulletModel extends Observable {
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

      public void shootBullet() {
            if (!exist) {
                  exist = true;
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

      public void keyTappedNewly() {
            shootBullet();
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
            x+=speed_x;
            y+=speed_y;
            if(x<0||)
      }
}

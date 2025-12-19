package model;

import java.util.Map;
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

    public void updateBulletsPosition(MapModel mm) {
        for (int i = 0; i < ConstSet.MAX_BULLETS; i++) {
            if (bullets[i].bulletExist()) {
                bullets[i].updateBulletPosition(mm);
            }
        }
    }
}

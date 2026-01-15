package model;

import java.util.ArrayList;

import GameConfig.*;

public class GuardsmenModel {
      private ArrayList<GuardsmanModel> guardsmen;

      public GuardsmenModel() {
            this.guardsmen = new ArrayList<GuardsmanModel>();
      }

      public void updateGuardsmenPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {
            // 各敵の位置や状態を更新
            for (GuardsmanModel guardsman : guardsmen) {
                  if (guardsman != null) {
                        guardsman.updateGuardsmanPosition(mm, pm, bm);
                  }
            }
      }

      public ArrayList<GuardsmanModel> getguardsmen() {
            return guardsmen;
      }

      public void setguardsmenForMap(int[][] map) {
            guardsmen.clear();
      }
}

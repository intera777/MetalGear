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

      public void setGuardsmenForMap(int[][] map) {
            guardsmen.clear();
            int HS = ConstSet.TILE_SIZE / 2;
            if (map == MapData.MAPD0) {
                  guardsmen.add(new GuardsmanModel(ConstSet.TILE_SIZE * 7 + HS,
                              ConstSet.TILE_SIZE * 16 + HS));
                  guardsmen.add(new GuardsmanModel(ConstSet.TILE_SIZE * 15 + HS,
                              ConstSet.TILE_SIZE * 16 + HS));
            }
      }
}

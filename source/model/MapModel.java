package model;

import GameConfig.*;

public class MapModel {
    PlayerModel playermodel;
    private static int currentMap[][];

    public MapModel(PlayerModel pm) {
        playermodel = pm;
    }

    // プレイヤーが現在いる位置のマップタイルを取得するメソッド.
    public int getMapTile(int map[][]) {
        int tileX = playermodel.getPlayerX() / ConstSet.TILE_SIZE;
        int tileY = playermodel.getPlayerY() / ConstSet.TILE_SIZE;
        return map[tileY][tileX];
    }

    public int[][] getMap() {
        return currentMap;
    }

    public void updateMap(int currentMap[][]) {

    }

}

package model;

import GameConfig.*;

public class MapModel {
    PlayerModel playermodel;
    private static int currentMap[][];

    public MapModel(PlayerModel pm) {
        playermodel = pm;
    }

    // プレイヤーが現在いる位置のマップタイルを取得するメソッド.
    public int getMapTile() {
        int tileX = playermodel.getPlayerX() / ConstSet.TILE_SIZE;
        int tileY = playermodel.getPlayerY() / ConstSet.TILE_SIZE;
        return currentMap[tileY][tileX];
    }

    public int[][] getMap() {
        return currentMap;
    }

    public void updateMap(PlayerModel pm) {
        if (getMapTile() > 100) {
            changeMap(pm);
        }
    }

    public void changeMap(PlayerModel pm) {
        if (getMapTile() == MapData.TO_A1_FROM_A0) {
            currentMap = MapData.MAPA1;
            pm.playerPositionSet(ConstSet.TILE_SIZE * 54, ConstSet.TILE_SIZE * 2);
        } else if (getMapTile() == MapData.TO_A0_FROM_A1) {
            currentMap = MapData.MAPA0;
            pm.playerPositionSet(ConstSet.TILE_SIZE * 2, ConstSet.TILE_SIZE * 6);
        }
    }

    public void setCurrentMap(int map[][]) {
        currentMap = map;
    }

}

package model;

import GameConfig.*;

public class MapModel {
    PlayerModel playermodel;
    EnemiesModel enemiesModel;
    private static int currentMap[][];

    public MapModel(PlayerModel pm, EnemiesModel em) {
        playermodel = pm;
        enemiesModel = em;
    }

    // プレイヤーが現在いる位置のマップタイルを取得するメソッド.
    public int getPlayerTile() {
        int tileX = playermodel.getPlayerX() / ConstSet.TILE_SIZE;
        int tileY = playermodel.getPlayerY() / ConstSet.TILE_SIZE;
        return currentMap[tileY][tileX];
    }

    // 指定された座標のタイルを取得するメソッド.
    public int getTile(int x, int y) {
        return currentMap[y / ConstSet.TILE_SIZE][x / ConstSet.TILE_SIZE];
    }

    // 現在プレイヤーがいるマップを取得するメソッド.
    public int[][] getMap() {
        return currentMap;
    }

    // プレイヤーが遷移ポイントに達したかを確認し、達していたらマップを変更するメソッド.
    public void updateMap(PlayerModel pm) {
        if (getPlayerTile() > 100) {
            changeMap(pm);
        }
    }

    // 遷移ポイントに達したときにマップを変更するメソッド.
    public void changeMap(PlayerModel pm) {
        if (getPlayerTile() == MapData.TO_A1_FROM_A0) {
            setCurrentMap(MapData.MAPA1);
            pm.playerPositionSet(ConstSet.TILE_SIZE * 54, ConstSet.TILE_SIZE * 2);
        } else if (getPlayerTile() == MapData.TO_A0_FROM_A1) {
            setCurrentMap(MapData.MAPA0);
            pm.playerPositionSet(ConstSet.TILE_SIZE * 2, ConstSet.TILE_SIZE * 6);
        } else if (getPlayerTile() == MapData.TO_A2_FROM_A1) {
            setCurrentMap(MapData.MAPA2);
            pm.playerPositionSet(ConstSet.TILE_SIZE * 8, ConstSet.TILE_SIZE * 4);
        } else if (getPlayerTile() == MapData.TO_A1_FROM_A2) {
            setCurrentMap(MapData.MAPA1);
            pm.playerPositionSet(ConstSet.TILE_SIZE * 2, ConstSet.TILE_SIZE * 15);
        } else if (getPlayerTile() == MapData.GAME_CLEAR) {
            System.exit(0);
        }
    }

    // 現在のマップを直接指定するメソッド.
    public void setCurrentMap(int map[][]) {
        currentMap = map;
        if (enemiesModel != null) {
            enemiesModel.setEnemiesForMap(map);
        }
    }

}

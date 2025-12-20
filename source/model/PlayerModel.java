package model;

import GameConfig.*;

import java.util.Arrays;

public class PlayerModel {
    // PlayerXとPlayerYはどちらもプレイヤーの画像の中心の座標.
    private int playerX = -100; // 初期位置
    private int playerY = -100; // 初期位置
    private int playerDirection = 0; // プレイヤーが向いている方向.0123の順で右上左下.

    // キーの状態管理フラグ
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    // Viewのタイマーから定期的に呼ばれるメソッド
    public void updatePlayerPosition(MapModel mm) {
        // フラグを見て座標を更新
        if (isUpPressed || isDownPressed) {
            if (isUpPressed) {
                playerY -= ConstSet.PLAYER_SPEED;
                playerDirection = 1;
                while (isObstacleExist(mm)) {
                    playerY += 1;
                }
            }
            if (isDownPressed) {
                playerY += ConstSet.PLAYER_SPEED;
                playerDirection = 3;
                while (isObstacleExist(mm)) {
                    playerY -= 1;
                }
            }
        } else if (isLeftPressed || isRightPressed) {
            if (isLeftPressed) {
                playerX -= ConstSet.PLAYER_SPEED;
                playerDirection = 2;
                while (isObstacleExist(mm)) {
                    playerX += 1;
                }
            }
            if (isRightPressed) {
                playerX += ConstSet.PLAYER_SPEED;
                playerDirection = 0;
                while (isObstacleExist(mm)) {
                    playerX -= 1;
                }
            }
        }


        if (playerX < ConstSet.TILE_SIZE / 2)
            playerX = ConstSet.TILE_SIZE / 2;
        if (playerY < ConstSet.TILE_SIZE / 2)
            playerY = ConstSet.TILE_SIZE / 2;
        if (playerX > mm.getMap()[0].length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2)
            playerX = mm.getMap()[0].length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2;
        if (playerY > mm.getMap().length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2)
            playerY = mm.getMap().length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2;

    }

    // フラグをセットするメソッド
    public void setUp(boolean pressed) {
        this.isUpPressed = pressed;
    }

    public void setDown(boolean pressed) {
        this.isDownPressed = pressed;
    }

    public void setLeft(boolean pressed) {
        this.isLeftPressed = pressed;
    }

    public void setRight(boolean pressed) {
        this.isRightPressed = pressed;
    }

    // プレイヤーのX座標を取得する.
    public int getPlayerX() {
        return playerX;
    }

    // プレイヤーのY座標を取得する.
    public int getPlayerY() {
        return playerY;
    }

    // プレイヤーの向いている方向を取得する.
    public int getPlayerDirection() {
        return playerDirection;
    }

    // プレイヤーの座標を直接セットする.
    public void playerPositionSet(int tx, int ty) {
        playerX = tx;
        playerY = ty;
    }

    // プレイヤーと障害物が重なっていないか判定するメソッド.
    public boolean isObstacleExist(MapModel mm) {
        if (Arrays.stream(MapData.OBSTACLES)
                .anyMatch(temp -> temp == mm.getTile(playerX + ConstSet.PLAYER_SIZE / 2 - 1,
                        playerY + ConstSet.PLAYER_SIZE / 2 - 1))
                || Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(playerX - ConstSet.PLAYER_SIZE / 2,
                                playerY - ConstSet.PLAYER_SIZE / 2))
                || Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(playerX - ConstSet.PLAYER_SIZE / 2,
                                playerY + ConstSet.PLAYER_SIZE / 2 - 1))
                || Arrays.stream(MapData.OBSTACLES)
                        .anyMatch(temp -> temp == mm.getTile(playerX + ConstSet.PLAYER_SIZE / 2 - 1,
                                playerY - ConstSet.PLAYER_SIZE / 2))) {
            return true;
        } else {
            return false;
        }
    }
}

// 通知テスト用

package model;

import java.util.Observable;

public class PlayerModel extends Observable {
    private int playerX = 250; // 初期位置
    private int playerY = 200;
    private final int SPEED = 3;
    private final int PLAYER_SIZE = 30; // プレイヤーのサイズ
    private int playerDirection = 0; // プレイヤーが向いている方向.0123の順で右上左下.

    // キーの状態管理フラグ
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    // Viewのタイマーから定期的に呼ばれるメソッド
    public void updatePlayerPosition() {
        // フラグを見て座標を更新
        if (isUpPressed || isDownPressed) {
            if (isUpPressed) {
                playerY -= SPEED;
                playerDirection = 1;
            }
            if (isDownPressed) {
                playerY += SPEED;
                playerDirection = 3;
            }
        } else if (isLeftPressed || isRightPressed) {
            if (isLeftPressed) {
                playerX -= SPEED;
                playerDirection = 2;
            }
            if (isRightPressed) {
                playerX += SPEED;
                playerDirection = 0;
            }
        }


        // 画面外に出ないように制限 (600x800のウインドウを想定)
        // ※正確な境界値はウインドウ枠のサイズにもよりますが、ここでは安全策をとっています
        if (playerX < 0)
            playerX = 0;
        if (playerY < 0)
            playerY = 0;
        if (playerX > 770)
            playerX = 770; // 800 - 幅
        if (playerY > 570)
            playerY = 570; // 600 - 高さ

        // 変更を通知
        setChanged();
        notifyObservers();
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

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }
}

// 通知テスト用

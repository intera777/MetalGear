package model;

import java.util.Observable;

public class PlayerModel extends Observable {
    private int playerX = 250; // 初期位置
    private int playerY = 200;
    private final int SPEED = 1;
    private final int PLAYER_SIZE = 30; // プレイヤーのサイズ

    // キーの状態管理フラグ
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    // Viewのタイマーから定期的に呼ばれるメソッド
    public void updatePosition() {
        // フラグを見て座標を更新
        if(isUpPressed || isDownPressed){
            if (isUpPressed)    playerY -= SPEED;
            if (isDownPressed)  playerY += SPEED;
        } else if(isLeftPressed || isRightPressed){
            if (isLeftPressed)  playerX -= SPEED;
            if (isRightPressed) playerX += SPEED;
        }
        

        // 画面外に出ないように制限 (600x800のウインドウを想定)
        // ※正確な境界値はウインドウ枠のサイズにもよりますが、ここでは安全策をとっています
        if (playerX < 0) playerX = 0;
        if (playerY < 0) playerY = 0;
        if (playerX > 560) playerX = 560; // 600 - 幅
        if (playerY > 740) playerY = 740; // 800 - 高さ

        // 変更を通知
        setChanged();
        notifyObservers();
    }

    // フラグをセットするメソッド
    public void setUp(boolean pressed)    { this.isUpPressed = pressed; }
    public void setDown(boolean pressed)  { this.isDownPressed = pressed; }
    public void setLeft(boolean pressed)  { this.isLeftPressed = pressed; }
    public void setRight(boolean pressed) { this.isRightPressed = pressed; }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
}
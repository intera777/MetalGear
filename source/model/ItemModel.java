package model;

import GameConfig.ConstSet;

public class ItemModel {
    private int itemX;
    private int itemY;
    private int itemType; // 0: 弾薬, 1: 回復 など
    private boolean isPickedUp = false;

    public ItemModel(int x, int y, int type) {
        this.itemX = x;
        this.itemY = y;
        this.itemType = type;
    }

    // ゲッター
    public int getItemX() { return itemX; }
    public int getItemY() { return itemY; }
    public int getItemType() { return itemType; }
    public boolean isPickedUp() { return isPickedUp; }

    // 取得状態の設定
    public void setPickedUp(boolean pickedUp) {
        this.isPickedUp = pickedUp;
    }
}
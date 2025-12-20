package GameConfig;

import java.util.Arrays;

public final class ConstSet {

    private ConstSet() {
        // インスタンス化防止
    }


    // ゲーム全般の定数.
    public static final int WINDOW_WIDTH = 800; // ウインドウの幅.
    public static final int WINDOW_HEIGHT = 608; // ウインドウの高さ.
    public static final int TILE_SIZE = 32;// タイルの一辺の長さ.
    // プレイヤーの定数.
    public static final int PLAYER_SIZE = 32; // プレイヤーの一辺の長さ.
    public static final int PLAYER_SPEED = 3; // プレイヤーの移動速度.
    // 弾の定数.
    public static final int BULLET_SIZE = 8; // 弾の一辺の長さ.

    // 敵の定数.
    public static final int ENEMY_DEAD = 0; // 敵が死んでいるとき.
    public static final int ENEMY_ALIVE = 1; // 敵が生きているとき.

    public static final int MAX_BULLETS = 5;// フィールド上に存在できる弾の最大数.
}

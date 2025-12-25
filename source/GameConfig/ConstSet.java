package GameConfig;

public final class ConstSet {

    private ConstSet() {
        // インスタンス化防止
    }


    // ゲーム全般の定数.
    public static final int WINDOW_WIDTH = 800; // ウインドウの幅.
    public static final int WINDOW_HEIGHT = 608; // ウインドウの高さ.
    public static final int TILE_SIZE = 32;// タイルの一辺の長さ.
    public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;

    // プレイヤーの定数.
    public static final int PLAYER_SIZE = 32; // プレイヤーの一辺の長さ.
    public static final int PLAYER_SPEED = 3; // プレイヤーの移動速度.
    // 弾の定数.
    public static final int BULLET_SIZE = 8; // 弾の一辺の長さ.

    // 敵の定数.
    public static final int MAX_ENEMIES = 10; // フィールド上に存在できる敵の最大数.
    public static final int ENEMY_SIZE = 32; // 敵の一辺の長さ.
    public static final int ENEMY_DEAD = 0; // 敵が死んでいるとき.
    public static final int ENEMY_ALIVE = 1; // 敵が生きているとき.
    public static final int ENEMY_DAMAGED = 100; // 敵がダメージを受けたとき.
    public static final int ENEMY_SPEED = 2; // 敵の移動速度.

    public static final int SIGHTRANGE = ENEMY_SIZE / 2; // 敵の司会の範囲,この範囲に入ると敵は射撃してくる.


    // ゲームオーバー画面の定数.
    public static final int FINISH_GAME = 0; // ゲームとプログラムを終了.
    public static final int RESTART_GAME = 1; // ゲームを再スタート.

    public static final int MAX_BULLETS = 5;// フィールド上に存在できる弾の最大数.


    // 各タイルのパス
    public static final String IMG_PATH_FLOOR = "../resources/tiles/floor.png";
    public static final String IMG_PATH_WALL = "../resources/tiles/wall.png";
    public static final String IMG_PATH_VERTICAL_STAIR = "../resources/tiles/vertical_stair.png";
    public static final String IMG_PATH_BED = "../resources/tiles/bed.png";
}

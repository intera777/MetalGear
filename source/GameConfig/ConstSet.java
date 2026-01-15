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


        // 敵の定数.
        public static final int MAX_ENEMIES = 10; // フィールド上に存在できる敵の最大数.
        public static final int ENEMY_SIZE = 32; // 敵の一辺の長さ.
        public static final int ENEMY_DEAD = 0; // 敵が死んでいるとき.
        public static final int ENEMY_ALIVE = 1; // 敵が生きているとき.
        public static final int ENEMY_DAMAGED = 100; // 敵がダメージを受けたとき.
        public static final int ENEMY_SPEED = 2; // 敵の移動速度.

        public static final int SIGHTRANGE = ENEMY_SIZE / 2; // 敵の司会の範囲,この範囲に入ると敵は射撃してくる.


        // メニュー画面の定数.
        public enum MainMenuItems {
                START_GAME, FINISH_GAME
        }

        // ゲームオーバー画面の定数.
        public static final int FINISH_GAME = 0; // ゲームとプログラムを終了.
        public static final int RESTART_GAME = 1; // ゲームを再スタート.

        public static final int MAX_BULLETS = 5;// フィールド上に存在できる弾の最大数.
        // 弾の定数.
        public static final int BULLET_SIZE = 6; // 弾の一辺の長さ.

        // 各タイルのパス
        public static final String IMG_PATH_FLOOR = "../resources/tiles/floordark.png"; // 暗くした部分
        public static final String IMG_PATH_WALL_TOP_NORTH =
                        "../resources/tiles/wall_top_north.png";
        public static final String IMG_PATH_WALL_NORTH = "../resources/tiles/walldark.png"; // 暗くした部分
        public static final String IMG_PATH_WALL_TOP_SOUTH =
                        "../resources/tiles/wall_top_south.png";
        public static final String IMG_PATH_WALL_TOP_EAST = "../resources/tiles/wall_top_east.png";
        public static final String IMG_PATH_WALL_TOP_WEST = "../resources/tiles/wall_top_west.png";
        public static final String IMG_PATH_CORNER_NORTH_WEST =
                        "../resources/tiles/corner_northwest.png";
        public static final String IMG_PATH_CORNER_NORTH_EAST =
                        "../resources/tiles/corner_northeast.png";
        public static final String IMG_PATH_CORNER_SOUTH_EAST =
                        "../resources/tiles/corner_southeast.png";
        public static final String IMG_PATH_CORNER_SOUTH_WEST =
                        "../resources/tiles/corner_southwest.png";
        public static final String IMG_PATH_VERTICAL_STAIR = "../resources/tiles/stair_MAPA2.png"; // 3×6マス階段が共通規格になるか分からないけど,
                                                                                                   // とりあえず命名は一般形にした.
        public static final String IMG_PATH_BED = "../resources/tiles/bed.png";
        public static final String IMG_PATH_CONTAINER1T2 = "../resources/tiles/container1T2.png";
        public static final String IMG_PATH_CONTAINER2T2 = "../resources/tiles/container2T2.png";

        // ゴム弾のパス
        public static final String IMG_PATH_BULLET = "../resources/weapon/Bullet.png";

        // 棒回しするモブの GIF ファイルのパス
        public static final String IMG_PATH_SPIN_MOB = "../resources/mob/SpinMob.gif";

        // プレーヤーキットのパス
        public static final String IMG_PATH_HEROIN = "../resources/player/HeroinKit2.png";
        public static final String IMG_PATH_ENEMY = "../resources/enemy/ViranKit.png";

        // メインメニューの背景画像のパス
        public static final String IMG_PATH_MAINMENU_BG =
                        "../resources/background/MainMenuBackgroundWithTitle.png";

        // BGMのパス
        public static final String SOUND_PATH_BGM = "../resources/sound/maou_bgm_neorock82.wav";
        public static final String BGM_TITLE = "../resources/sound/title.wav";
        public static final String BGM_NORMAL="../resources/sound/normal.wav";
        public static final String BGM_GAMEOVER="../resources/sound/gameover.wav";

"
}

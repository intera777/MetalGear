import control.*;
import model.*;
import view.*;
import GameConfig.*;

import javax.swing.*;


public class Metalgear extends JFrame {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("MetalGear");
        frame.setSize(ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // MVCモデルの生成
        GameModel gamemodel = new GameModel();

        // Playerクラス関連のオブジェクトの生成.
        PlayerControl playercontrol = new PlayerControl(gamemodel.getPlayerModel());
        PlayerView playerview = new PlayerView(gamemodel.getPlayerModel());

        // Bulletクラス関連のオブジェクトの生成.
        BulletControl bulletcontrol = new BulletControl(gamemodel.getBulletsModel());
        BulletView bulletview = new BulletView(gamemodel.getBulletsModel());

        // Enemyクラス関連のオブジェクト生成
        EnemyView enemyview = new EnemyView(gamemodel.getEnemiesModel());

        // Mapクラス関連のオブジェクトを生成.
        MapView mapview = new MapView(gamemodel.getMapModel(), gamemodel.getPlayerModel());

        // GameOverMenuクラス関連のオブジェクトを生成.
        GameOverMenuView gameovermenuview = new GameOverMenuView(gamemodel.getGameOverMenuModel());
        GameOverMenuControl gameovermenucontrol =
                new GameOverMenuControl(gamemodel.getGameOverMenuModel());


        // 画面を描画するクラスの生成.
        GameView gameview = new GameView(gamemodel.getPlayerModel(),
                gamemodel.getGameOverMenuModel(), mapview, enemyview, playerview, bulletview,
                gameovermenuview, playercontrol, bulletcontrol, gameovermenucontrol);
        frame.add(gameview);


        frame.setVisible(true);

        final int FPS = 30; // フレームレート.
        GameState gamestate = new GameState(GameState.GAME_OVER); // ゲームモードの設定.

        gamemodel.getPlayerModel().playerPositionSet(
                4 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2, 4 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
        gamemodel.getMapModel().setCurrentMap(MapData.MAPA0);

        // ゲームループ本体.
        while (true) {
            // ゲームの状態に応じて更新処理を切り替える
            switch (gamestate.getCurrentState()) {
                case GameState.MENU:
                    // メニュー画面の更新処理（例：選択項目の移動など）
                    // 今は特に何もしない
                    break;
                case GameState.PLAYING:
                    // プレイ中の更新処理
                    gamemodel.getPlayerModel().updatePlayerPosition(gamemodel.getMapModel());
                    gamemodel.getEnemiesModel().updateEnemiesPosition(gamemodel.getMapModel(),
                            gamemodel.getPlayerModel(), gamemodel.getBulletsModel());
                    gamemodel.getBulletsModel().updateBulletsPosition(gamemodel.getMapModel(),
                            gamemodel.getPlayerModel(), gamemodel.getEnemiesModel());
                    gamemodel.getMapModel().updateMap(gamemodel.getPlayerModel());

                    // プレイヤーが死亡したかチェック
                    if (gamemodel.getPlayerModel().isDead()) {
                        gamestate.setGameState(GameState.GAME_OVER);
                    }
                    break;
                case GameState.GAME_OVER:
                    // リスタートする場合のため初期位置をリセット.
                    gamemodel.getPlayerModel().playerPositionSet(
                            4 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2,
                            4 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
                    // プレイヤーのステータス(HPなど)をリセット
                    gamemodel.getPlayerModel().resetStatus();
                    gamemodel.getMapModel().setCurrentMap(MapData.MAPA0);

                    break;
            }
            gameview.repaint();
            try {
                // 約0.033秒停止.
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                // 停止中に割り込まれた時の処理.
                e.printStackTrace();
            }
        }

    }
}

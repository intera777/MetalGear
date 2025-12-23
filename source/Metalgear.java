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

        // Playerクラス関連のオブジェクトの生成.
        PlayerModel playermodel = new PlayerModel();
        PlayerControl playercontrol = new PlayerControl(playermodel);
        PlayerView playerview = new PlayerView(playermodel);

        // Bulletクラス関連のオブジェクトの生成.
        BulletsModel bulletsmodel = new BulletsModel(playermodel);
        BulletControl bulletcontrol = new BulletControl(bulletsmodel);
        BulletView bulletview = new BulletView(bulletsmodel);

        // Mapクラス関連のオブジェクトを生成.
        MapModel mapmodel = new MapModel(playermodel);
        MapView mapview = new MapView(mapmodel, playermodel);

        // Enemyクラス関連のオブジェクト生成
        EnemiesModel ememiesmodel = new EnemiesModel();



        // 画面を描画するクラスの生成.
        GameView gameview = new GameView(playermodel, mapview, playerview, bulletview,
                playercontrol, bulletcontrol);
        frame.add(gameview);

        frame.setVisible(true);

        final int FPS = 30; // フレームレート.
        GameState gamestate = new GameState(GameState.PLAYING); // ゲームモードの設定.

        playermodel.playerPositionSet(4 * ConstSet.TILE_SIZE, 4 * ConstSet.TILE_SIZE);
        mapmodel.setCurrentMap(MapData.MAPA0);

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
                    playermodel.updatePlayerPosition(mapmodel);
                    bulletsmodel.updateBulletsPosition(mapmodel);
                    mapmodel.updateMap(playermodel);
                    break;
                case GameState.GAME_OVER:
                    // ゲームオーバー画面の更新処理
                    // 今は特に何もしない
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

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

        // modelクラスのオブジェクトを持ってくる.
        PlayerModel playermodel = gamemodel.getPlayerModel();
        MapModel mapmodel = gamemodel.getMapModel();
        GameOverMenuModel gameovermenumodel = gamemodel.getGameOverMenuModel();
        EnemiesModel enemiesmodel = gamemodel.getEnemiesModel();
        BulletsModel bulletsmodel = gamemodel.getBulletsModel();

        // Playerクラス関連のオブジェクトの生成.
        PlayerControl playercontrol = new PlayerControl(playermodel);
        PlayerView playerview = new PlayerView(playermodel);

        // Bulletクラス関連のオブジェクトの生成.
        BulletControl bulletcontrol = new BulletControl(bulletsmodel);
        BulletView bulletview = new BulletView(bulletsmodel);

        // Enemyクラス関連のオブジェクト生成
        EnemyView enemyview = new EnemyView(enemiesmodel);

        // Mapクラス関連のオブジェクトを生成.
        MapView mapview = new MapView(mapmodel, playermodel);

        // GameOverMenuクラス関連のオブジェクトを生成.
        GameOverMenuView gameovermenuview = new GameOverMenuView(gameovermenumodel);
        GameOverMenuControl gameovermenucontrol = new GameOverMenuControl(gameovermenumodel);

        // Dialogueクラス関連のオブジェクト生成
        DialogueBoxControl dialogueBoxControl =
                new DialogueBoxControl(gamemodel.getDialogueBoxesModel());
        DialogueBoxView dialogueBoxView = new DialogueBoxView(gamemodel.getDialogueBoxesModel());

        // 画面を描画するクラスの生成.
        GameView gameview = new GameView(playermodel, gameovermenumodel, mapview, enemyview,
                playerview, bulletview, gameovermenuview, playercontrol, bulletcontrol,
                gameovermenucontrol, dialogueBoxView, dialogueBoxControl);
        frame.add(gameview);


        frame.setVisible(true);

        final int FPS = 30; // フレームレート.
        GameState gamestate = new GameState(GameState.GAME_OVER); // ゲームモードの設定.

        playermodel.playerPositionSet(10 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2,
                12 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
        mapmodel.setCurrentMap(MapData.MAP0);

        int previousState = -1;
        int dialogue_count = 1;

        // ゲームループ本体.
        while (true) {
            int currentstate = GameState.getCurrentState();
            if (previousState != GameState.getCurrentState()) {
                int[] pathX = {playermodel.getPlayerX(),
                        playermodel.getPlayerX() - ConstSet.TILE_SIZE * 8};
                int[] pathY = {playermodel.getPlayerY(), playermodel.getPlayerY()};
                playermodel.startScriptedMovement(pathX, pathY);
                if (DialogueSet.dialogue_count == 1) { // セリフセット1を表示.
                    gamemodel.getDialogueBoxesModel().setDialogues(DialogueSet.nameset1,
                            DialogueSet.dialogueset1);
                } else if (DialogueSet.dialogue_count == 3) {
                    gamemodel.getDialogueBoxesModel().setDialogues(DialogueSet.nameset2,
                            DialogueSet.dialogueset2);
                }
            }
            // ゲームの状態に応じて更新処理を切り替える
            switch (currentstate) {
                case GameState.MENU:
                    // メニュー画面の更新処理（例：選択項目の移動など）
                    // 今は特に何もしない
                    break;
                case GameState.PLAYING:
                    // プレイ中の更新処理

                    // スクリプト移動と最初の会話が終わった後に次の会話を表示
                    if (DialogueSet.dialogue_count == 3
                            && !gamemodel.getDialogueBoxesModel().isVisible()) {
                        gamemodel.getDialogueBoxesModel().setDialogues(DialogueSet.nameset2,
                                DialogueSet.dialogueset2);
                    }

                    // 会話中はゲームの更新を止める
                    if (!gamemodel.getDialogueBoxesModel().isVisible()) {
                        playermodel.updatePlayerPosition(mapmodel);
                        enemiesmodel.updateEnemiesPosition(mapmodel, playermodel, bulletsmodel);
                        bulletsmodel.updateBulletsPosition(mapmodel, playermodel, enemiesmodel);
                        mapmodel.updateMap(playermodel);
                    }

                    // プレイヤーが死亡したかチェック
                    if (playermodel.isDead()) {
                        gamestate.setGameState(GameState.GAME_OVER);
                    }
                    break;
                case GameState.GAME_OVER:
                    // リスタートする場合のため初期位置をリセット.
                    playermodel.playerPositionSet(
                            10 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2,
                            12 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
                    // プレイヤーのステータス(HPなど)をリセット
                    playermodel.resetStatus();
                    mapmodel.setCurrentMap(MapData.MAP0);

                    break;
            }
            previousState = currentstate;
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

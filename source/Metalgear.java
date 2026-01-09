import control.*;
import model.*;
import sound.BGMManager;
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
        MainMenuModel mainmenumodel = gamemodel.getMainMenuModel();
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

        // MainMenuクラス関連のオブジェクトを生成.
        MainMenuView mainmenuview = new MainMenuView(mainmenumodel);
        MainMenuControl mainmenucontrol = new MainMenuControl(mainmenumodel);

        // GameOverMenuクラス関連のオブジェクトを生成.
        GameOverMenuView gameovermenuview = new GameOverMenuView(gameovermenumodel);
        GameOverMenuControl gameovermenucontrol = new GameOverMenuControl(gameovermenumodel);

        // MainMenuクラス関連のオブジェクトを生成.

        // Dialogueクラス関連のオブジェクト生成
        DialogueBoxControl dialogueBoxControl =
                new DialogueBoxControl(gamemodel.getDialogueBoxesModel());
        DialogueBoxView dialogueBoxView = new DialogueBoxView(gamemodel.getDialogueBoxesModel());

        // HPBarクラス関連のオブジェクト生成
        HPBarView hpBarView = new HPBarView();

        // 画面を描画するクラスの生成.
        GameView gameview = new GameView(playermodel, mainmenumodel, gameovermenumodel, mapview,
                enemyview, playerview, bulletview, mainmenuview, gameovermenuview, hpBarView,
                playercontrol, bulletcontrol, mainmenucontrol, gameovermenucontrol, dialogueBoxView,
                dialogueBoxControl);
        frame.add(gameview);


        gamemodel.getDialogueBoxesModel().setGameView(gameview);

        frame.setVisible(true);

        // BGMマネージャーを生成し、BGMをループ再生
        BGMManager bgmManager = new BGMManager();
        bgmManager.load(ConstSet.SOUND_PATH_BGM);
        bgmManager.setVolume(0.6f); // 音量を30%に設定 (0.0f ~ 1.0f)
        bgmManager.loop();

        final int FPS = 30; // フレームレート.
        GameState.setCurrentState(GameState.State.MENU); // 最初はメインメニュー画面から開始.
        playermodel.playerPositionSet(10 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2,
                12 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
        mapmodel.setCurrentMap(MapData.MAP0);

        // ゲームループ本体.
        while (true) {
            GameState.State currentstate = GameState.getCurrentState();

            // ゲームの状態に応じて更新処理を切り替える
            switch (currentstate) {
                case MENU:
                    // メニュー画面の更新処理（例：選択項目の移動など）
                    // 今は特に何もしない
                    break;
                case PLAYING:
                    // プロローグのイベントシーケンス管理
                    // 各ダイアログやイベントが一度だけ実行されるように、!isVisible() や !isScripted() でチェックします。
                    if (DialogueSet.dialogueState == DialogueSet.DialogueState.PROLOGUE
                            && !gamemodel.getDialogueBoxesModel().isVisible()) {
                        gamemodel.getDialogueBoxesModel().setDialogues(DialogueSet.DIALOGUE_SET_0);
                    }

                    if (DialogueSet.dialogueState == DialogueSet.DialogueState.AFTER_PROLOGUE_DIALOGUE
                            && !gamemodel.getDialogueBoxesModel().isVisible()) { // セリフセット1を表示.
                        gamemodel.getDialogueBoxesModel().setDialogues(DialogueSet.DIALOGUE_SET_1);
                    }

                    // スクリプト移動の開始
                    if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION
                            && !playermodel.isScripted() && !gameview.isPerspectiveMoving()) {
                        int[] pathX = {playermodel.getPlayerX(),
                                playermodel.getPlayerX() - ConstSet.TILE_SIZE * 8};
                        int[] pathY = {playermodel.getPlayerY(), playermodel.getPlayerY()};
                        playermodel.startScriptedMovement(pathX, pathY);
                    }
                    // スクリプト移動後の会話
                    if (DialogueSet.dialogueState == DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE
                            && !gamemodel.getDialogueBoxesModel().isVisible()) {
                        gamemodel.getDialogueBoxesModel().setDialogues(DialogueSet.DIALOGUE_SET_2);
                    }

                    // 会話中はゲームの更新を止める
                    if (!gamemodel.getDialogueBoxesModel().isVisible()
                            && !gameview.isPerspectiveMoving()) {
                        playermodel.updatePlayerPosition(mapmodel);
                        enemiesmodel.updateEnemiesPosition(mapmodel, playermodel, bulletsmodel);
                        bulletsmodel.updateBulletsPosition(mapmodel, playermodel, enemiesmodel);
                        mapmodel.updateMap();
                    }

                    // プレイヤーが死亡したかチェック
                    if (playermodel.isDead()) {
                        GameState.setCurrentState(GameState.State.GAME_OVER);
                    }
                    break;
                case GAME_OVER:
                    // リスタートする場合のため初期位置をリセット.
                    playermodel.playerPositionSet(3 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2,
                            6 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
                    // プレイヤーのステータス(HPなど)をリセット
                    playermodel.resetStatus();
                    mapmodel.setCurrentMap(MapData.MAPA0);

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

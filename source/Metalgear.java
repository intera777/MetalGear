import control.*;
import model.*;
import sound.BGMManager;
import sound.SoundEffectManager;
import view.*;
import GameConfig.*;
import GameConfig.DialogueSet.DialogueState;
import javax.swing.*;
import javax.sound.sampled.Clip;

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
        GameClearMenuModel gameclearmenumodel = gamemodel.getGameClearMenuModel();
        DialogueBoxesModel dialogueboxesmodel = gamemodel.getDialogueBoxesModel();
        GuardsmenModel guardsmenmodel = gamemodel.getGuardsmenModel();
        ItemsModel itemsModel = gamemodel.getItemsModel();

        // Playerクラス関連のオブジェクトの生成.
        PlayerControl playercontrol = new PlayerControl(playermodel);
        PlayerView playerview = new PlayerView(playermodel);

        // Bulletクラス関連 of オブジェクトの生成.
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

        // GameClearMenuクラス関連のオブジェクトを生成.
        GameClearMenuView gameclearmenuview = new GameClearMenuView(gameclearmenumodel);
        GameClearMenuControl gameclearmenucontrol = new GameClearMenuControl(gameclearmenumodel);

        // Dialogueクラス関連のオブジェクト生成
        DialogueBoxControl dialogueBoxControl =
                new DialogueBoxControl(gamemodel.getDialogueBoxesModel());
        DialogueBoxView dialogueBoxView = new DialogueBoxView(gamemodel.getDialogueBoxesModel());

        // Guardsmanクラス関連のオブジェクトを生成.
        GuardsmanView guardsmanview = new GuardsmanView(guardsmenmodel);

        // HPBarクラス関連のオブジェクト生成
        HPBarView hpBarView = new HPBarView();

        // アイテムクラスに関するオブジェクト生成
        ItemView itemView = new ItemView(itemsModel);

        // 画面を描画するクラスの生成.
        GameView gameview = new GameView(playermodel, mainmenumodel, gameovermenumodel,
                gameclearmenumodel, mapview, enemyview, guardsmanview, playerview, bulletview,
                mainmenuview, gameovermenuview, gameclearmenuview, hpBarView, itemView, playercontrol,
                bulletcontrol, mainmenucontrol, gameovermenucontrol, gameclearmenucontrol,
                dialogueBoxView, dialogueBoxControl);
        frame.add(gameview);

        gamemodel.getDialogueBoxesModel().setGameView(gameview);

        frame.setVisible(true);

        // --- BGM/SEマネージャーを生成 ---
        // BGM
        BGMManager titleBgmManager = new BGMManager();
        titleBgmManager.load(ConstSet.BGM_TITLE);
        titleBgmManager.setVolume(0.6f);

        BGMManager mainBgmManager = new BGMManager();
        mainBgmManager.load(ConstSet.BGM_NORMAL);
        mainBgmManager.setVolume(0.6f);

        BGMManager pursueBgmManager = new BGMManager();
        pursueBgmManager.load(ConstSet.BGM_PURSUE);
        pursueBgmManager.setVolume(0.7f); // 追跡BGMは少し大きめ

        BGMManager gameoverBgmManager = new BGMManager();
        gameoverBgmManager.load(ConstSet.BGM_GAMEOVER);
        gameoverBgmManager.setVolume(0.6f);

        // ループするSE
        BGMManager footstepSEManager = new BGMManager();
        footstepSEManager.load(ConstSet.SE_footstep);
        footstepSEManager.setVolume(0.8f); // 足音は少し小さめ

        // 一度だけ再生するSEはClipを直接扱う
        Clip noticeSEClip = SoundEffectManager.loadClip(ConstSet.SE_ENEMY_NOTICE, 0.8f);
        Clip bulletShootClip = SoundEffectManager.loadClip(ConstSet.SE_BULLET_SHOOT, 0.7f);
        Clip itemSelectedClip = SoundEffectManager.loadClip(ConstSet.SE_ITEM_SELECTED, 0.8f);
        Clip itemGetClip = SoundEffectManager.loadClip(ConstSet.SE_GET_ITEM, 0.8f);

        // --- ゲームの初期設定 ---
        final int FPS = 30; // フレームレート.
        GameState.setCurrentState(GameState.State.MENU); // 最初はメインメニュー画面から開始.
        playermodel.playerPositionSet(10 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2,
                12 * ConstSet.TILE_SIZE); // プレイヤーの初期位置を設定.
        mapmodel.setCurrentMap(MapData.MAP0);

        // --- 状態管理用の変数 ---
        GameState.State previousState = null;
        boolean wasPursuing = false;
        boolean isFootstepPlaying = false;
        int previousBulletCount = 0;
        int previousMainMenuIndex = mainmenumodel.getSelectedIndex();
        boolean isClearSequenceStarted = false;
        Timer scoreTimer = null;
        Timer menuTimer = null;

        // ゲームループ本体.
        while (true) {
            GameState.State currentstate = GameState.getCurrentState();

            boolean isComingFromEnd = ( // リセットする条件
                previousState == GameState.State.GAME_CLEAR ||
                previousState == GameState.State.GAME_OVER
            );
            
            // リセット処理をここに集中させた
            if (isComingFromEnd && currentstate == GameState.State.PLAYING) {
                // 1. 動いているタイマーがあれば即座に止める
                if (scoreTimer != null) scoreTimer.stop();
                if (menuTimer != null) menuTimer.stop();

                // データをリセット
                playermodel.resetStatus();
                playermodel.playerPositionSet(3 * ConstSet.TILE_SIZE - ConstSet.PLAYER_SIZE / 2, 6 * ConstSet.TILE_SIZE);
                mapmodel.setCurrentMap(MapData.MAPA0);
                itemsModel.setItemsForMap(MapData.MAPA0);
        
                // フラグ類
                gameclearmenumodel.setCurrentPhase(GameClearMenuModel.Phase.BACKGROUND_ONLY);
                isClearSequenceStarted = false; 
                EnemiesModel.isPermanentAlert = false;
                DialogueSet.dialogueState = DialogueState.MAIN_GAMEPLAY;
            }

            // BGM用（絶望モード）とSE用（単純な発見）の判定を分ける
            boolean isDespairMode = false;

            if (currentstate == GameState.State.PLAYING) {
                // 戦闘BGMを開始させるのは、永久アラーム(絶望モード)になったときにしたい
                isDespairMode = enemiesmodel.isAnyEnemyPursuing(playermodel);
            }

            // --- BGM切り替え処理 ---
            // ゲームの状態(MENU, PLAYING, GAME_OVER)または追跡状態が変わった時にBGMを切り替える
            if (currentstate != previousState || isDespairMode != wasPursuing) {
                // 念のため全てのBGMを停止
                titleBgmManager.stop();
                mainBgmManager.stop();
                gameoverBgmManager.stop();
                pursueBgmManager.stop();

                // 新しい状態に応じたBGMを再生
                switch (currentstate) {
                    case MENU:
                        titleBgmManager.loop();
                        break;
                    case PLAYING:
                        if (isDespairMode) {
                            pursueBgmManager.loop();
                        } else {
                            mainBgmManager.loop();
                        }
                        break;
                    case GAME_OVER:
                        gameoverBgmManager.loop();
                        break;
                }
            }
            previousState = currentstate;
            wasPursuing = isDespairMode;

            // --- ゲーム状態ごとの更新処理 ---
            switch (currentstate) {
                case MENU:
                    // 選択項目が変更されたらSEを再生
                    int currentMainMenuIndex = mainmenumodel.getSelectedIndex();
                    if (currentMainMenuIndex != previousMainMenuIndex) {
                        SoundEffectManager.playClip(itemSelectedClip);
                        previousMainMenuIndex = currentMainMenuIndex;
                    }
                    break;
                case PLAYING:

                    if (DialogueSet.dialogueState == DialogueSet.DialogueState.GAME_OVER) {
                        GameState.setCurrentState(GameState.State.GAME_OVER);
                    }

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

                    // 警備員に捕まった時の会話.
                    if (DialogueSet.dialogueState == DialogueSet.DialogueState.GUARDSMAN
                            && !gamemodel.getDialogueBoxesModel().isVisible()) {
                        gamemodel.getDialogueBoxesModel()
                                .setDialogues(DialogueSet.DIALOGUE_GUARDSMAN);
                    }

                    // 会話中はゲームの更新を止める
                    if (!gamemodel.getDialogueBoxesModel().isVisible()
                            && !gameview.isPerspectiveMoving()) {

                        // 更新前の状態を保存
                        int prevPlayerX = playermodel.getPlayerX();
                        int prevPlayerY = playermodel.getPlayerY();

                        // 警備員がプレイヤーを追尾しているときはプレイヤーに対する操作を無効化.
                        boolean canControl = true;
                        if (!guardsmenmodel.getguardsmen().isEmpty()) {
                            boolean isTracking = false;
                            for (GuardsmanModel gm : guardsmenmodel.getguardsmen()) {
                                if (gm != null && gm.getPlayerTrack() != 0) {
                                    isTracking = true;
                                    break;
                                }
                            }
                            canControl = !isTracking;
                        }
                        playermodel.setInputEnabled(canControl);

                        // 各モデルの更新処理
                        playermodel.updatePlayerPosition(mapmodel);
                        enemiesmodel.updateEnemiesPosition(mapmodel, playermodel, bulletsmodel);
                        guardsmenmodel.updateGuardsmenPosition(mapmodel, playermodel, bulletsmodel);

                        int discoveryCount = enemiesmodel.countNewDiscoveries();
                        for (int i = 0; i < discoveryCount; i++) {
                            // 見つかった数だけ再生を試みる
                            SoundEffectManager.playClip(noticeSEClip);
                        }

                        if (!dialogueboxesmodel.isVisible()) {
                            for (GuardsmanModel gm : guardsmenmodel.getguardsmen()) {
                                if (gm != null && gm.getPlayerTrack() == 1) {
                                    int distance = Math
                                            .abs(gm.getguardsmanX() - playermodel.getPlayerX())
                                            + Math.abs(
                                                    gm.getguardsmanY() - playermodel.getPlayerY());
                                    if (distance <= ConstSet.TILE_SIZE) {
                                        gm.changePlayerTrack(0);
                                        if (DialogueSet.dialogueState == DialogueSet.DialogueState.MAIN_GAMEPLAY) {
                                            DialogueSet.dialogueState =
                                                    DialogueSet.DialogueState.GUARDSMAN; // ここで状態を設定
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        // --- SE再生処理 ---
                        // プレイヤーの足音
                        boolean isPlayerMoving = (playermodel.getPlayerX() != prevPlayerX
                                || playermodel.getPlayerY() != prevPlayerY);
                        if (isPlayerMoving && !isFootstepPlaying) {
                            footstepSEManager.loop();
                            isFootstepPlaying = true;
                        } else if (!isPlayerMoving && isFootstepPlaying) {
                            footstepSEManager.stop();
                            isFootstepPlaying = false;
                        }

                        // 弾の発射音 (弾が消滅する前に判定する必要がある)
                        int currentBulletCount = bulletsmodel.countActiveBullets();
                        if (currentBulletCount > previousBulletCount) {
                            SoundEffectManager.playClip(bulletShootClip);
                        }

                        // Itemモデルの更新処理とアイテム取得音を兼ねている
                        if (itemsModel.updateItems(playermodel)) {
                            SoundEffectManager.playClip(itemGetClip);
                        }

                        // 残りのモデル更新と、次フレームのための状態保存
                        bulletsmodel.updateBulletsPosition(mapmodel, playermodel, enemiesmodel);
                        mapmodel.updateMap();
                        previousBulletCount = bulletsmodel.countActiveBullets();
                    } else {
                        if (isFootstepPlaying) {
                            footstepSEManager.stop();
                            isFootstepPlaying = false;
                        }
                    }

                    // プレイヤーが死亡したかチェック
                    if (playermodel.isDead()) {
                        GameState.setCurrentState(GameState.State.GAME_OVER);
                    }
                    break;
                case GAME_OVER:
                    if (isFootstepPlaying) {
                        footstepSEManager.stop();
                        isFootstepPlaying = false;
                    }

                    break;
                case GAME_CLEAR:
                    if (isFootstepPlaying) {
                        footstepSEManager.stop();
                        isFootstepPlaying = false;
                    }

                    // ゲームクリア時の演出
                    if (!isClearSequenceStarted) {
                        isClearSequenceStarted = true;

                        // スコアを確定させてランクを判定
                        gameclearmenumodel.setFinalResult(playermodel.getScore());
        
                        // クリア背景画像のみ
                        gameclearmenumodel.setCurrentPhase(GameClearMenuModel.Phase.BACKGROUND_ONLY);

                        // 2秒後に暗転・スコア表示へ
                        scoreTimer = new Timer(2000, e -> {
                            gameclearmenumodel.setCurrentPhase(GameClearMenuModel.Phase.SCORE_DISPLAY);
                            gameview.repaint();
                        });
                        scoreTimer.setRepeats(false);
                        scoreTimer.start();

                        // 4秒後にメニュー出現へ
                        menuTimer = new Timer(4000, e -> {
                            gameclearmenumodel.setCurrentPhase(GameClearMenuModel.Phase.MENU_DISPLAY);
                            gameview.repaint();
                        });
                        menuTimer.setRepeats(false);
                        menuTimer.start();
                    }

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
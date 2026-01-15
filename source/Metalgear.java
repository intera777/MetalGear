import control.*;
import model.*;
import sound.BGMManager;
import view.*;
import GameConfig.*;

import java.io.File;
import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
        Clip noticeSEClip = loadClip(ConstSet.SE_ENEMY_NOTICE, 0.8f);
        Clip bulletShootClip = loadClip(ConstSet.SE_BULLET_SHOOT, 0.7f);

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

        // ゲームループ本体.
        while (true) {
            GameState.State currentstate = GameState.getCurrentState();
            boolean isPursuing = false;
            if (currentstate == GameState.State.PLAYING) {
                isPursuing = isAnyEnemyPursuing(enemiesmodel, playermodel);
            }

            // --- BGM切り替え処理 ---
            // ゲームの状態(MENU, PLAYING, GAME_OVER)または追跡状態が変わった時にBGMを切り替える
            if (currentstate != previousState || isPursuing != wasPursuing) {
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
                        if (isPursuing) {
                            pursueBgmManager.loop();
                            // 追跡が始まった瞬間に通知音を再生
                            if (!wasPursuing) {
                                playClip(noticeSEClip);
                            }
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
            wasPursuing = isPursuing;

            // --- ゲーム状態ごとの更新処理 ---
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

                        // 更新前の状態を保存
                        int prevPlayerX = playermodel.getPlayerX();
                        int prevPlayerY = playermodel.getPlayerY();

                        // 各モデルの更新処理
                        playermodel.updatePlayerPosition(mapmodel);
                        enemiesmodel.updateEnemiesPosition(mapmodel, playermodel, bulletsmodel);

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
                        int currentBulletCount = countActiveBullets(bulletsmodel);
                        if (currentBulletCount > previousBulletCount) {
                            playClip(bulletShootClip);
                        }

                        // 残りのモデル更新と、次フレームのための状態保存
                        bulletsmodel.updateBulletsPosition(mapmodel, playermodel, enemiesmodel);
                        mapmodel.updateMap();
                        previousBulletCount = countActiveBullets(bulletsmodel);
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

    /**
     * 指定されたパスからオーディオクリップを読み込み、音量を設定します。
     * 
     * @param path オーディオファイルのパス
     * @param volume 音量 (0.0f - 1.0f)
     * @return 読み込んだClipオブジェクト、失敗した場合はnull
     */
    private static Clip loadClip(String path, float volume) {
        try {
            File audioFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // 音量調整
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }
            return clip;
        } catch (Exception e) {
            System.err.println("SEファイルの読み込みに失敗: " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 指定されたクリップを最初から再生します。
     * 
     * @param clip 再生するClipオブジェクト
     */
    private static void playClip(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * いずれかの敵がプレイヤーを追跡しているかどうかを判定します。
     * 
     * @param enemiesModel 敵のモデル
     * @param playerModel プレイヤーのモデル
     * @return 1体でも追跡していればtrue
     */
    private static boolean isAnyEnemyPursuing(EnemiesModel enemiesModel, PlayerModel playerModel) {
        final int ENEMY_SIGHT_DISTANCE = ConstSet.TILE_SIZE * 8; // 敵が追跡を開始する距離
        for (EnemyModel enemy : enemiesModel.getEnemies()) {
            if (enemy == null || enemy.getEnemyCondition() == ConstSet.ENEMY_DEAD)
                continue;

            int dx = enemy.getEnemyX() - playerModel.getPlayerX();
            int dy = enemy.getEnemyY() - playerModel.getPlayerY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < ENEMY_SIGHT_DISTANCE) {
                return true; // 簡易的に、距離が近い＝追尾中とみなす
            }
        }
        return false;
    }

    /**
     * 現在画面に存在している弾の数を数えます。
     * 
     * @param bulletsModel 弾のモデル
     * @return アクティブな弾の数
     */
    private static int countActiveBullets(BulletsModel bulletsModel) {
        int count = 0;
        for (BulletModel bullet : bulletsModel.getBullets()) {
            if (bullet.bulletExist()) {
                count++;
            }
        }
        return count;
    }
}

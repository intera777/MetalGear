package view;

import GameConfig.*;
import GameConfig.DialogueSet.DialogueState;
import control.*;

import model.*;

import javax.swing.JPanel;

// import java.awt.* って宣言してもエラーが生じるため, 個別にインポート
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public class GameView extends JPanel {
    private PlayerModel playerModel; // 使ってないけど一応保持しておく
    private GameOverMenuModel gameOverMenuModel; // なんか必要らしい
    private MainMenuModel mainMenuModel;
    private MapView mapView;
    private PlayerView playerView;
    private EnemyView enemyView;
    private BulletView bulletView;
    private MainMenuView mainMenuView;
    private GameOverMenuView gameOverMenuView;
    private DialogueBoxView dialogueBoxView;
    private HPBarView hpBarView;

    private boolean isPerspectiveMoving;
    private int timer = 0; // 視点移動などのイベントの時間管理用.

    public GameView(PlayerModel pm, MainMenuModel mm, GameOverMenuModel gm, MapView mv, EnemyView ev, PlayerView pv,
            BulletView bv, MainMenuView mmv, GameOverMenuView gov, HPBarView hpv, PlayerControl pc, BulletControl bc,
            MainMenuControl mc, GameOverMenuControl gc, DialogueBoxView dv, DialogueBoxControl dc) {
        // Model
        this.playerModel = pm;
        this.mainMenuModel = mm;
        this.gameOverMenuModel = gm;
        // View
        this.mapView = mv;
        this.enemyView = ev;
        this.playerView = pv;
        this.bulletView = bv;
        this.mainMenuView = mmv;
        this.gameOverMenuView = gov;
        this.hpBarView = hpv;
        this.dialogueBoxView = dv;
        this.isPerspectiveMoving = false;

        // GameView内でまとめてキー登録をする
        this.addKeyListener(bc);
        this.addKeyListener(pc);
        this.addKeyListener(mc);
        this.addKeyListener(gc);
        this.addKeyListener(dc);
        this.setFocusable(true);
        // this.requestFocusInWindow(); // 起動時に自動でキー入力を受け付ける魔法の呪文らしい
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 画面全体を黒で塗りつぶす
        super.paintComponent(g);
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);

        Graphics2D g2d = (Graphics2D) g;

        if (GameState.getCurrentState() == GameState.State.PLAYING) { // ゲームプレイ中の描画
            int logicWidth;
            int logicHeight;

            int playerDrawX;
            int playerDrawY;

            int modelLeft;
            int modelTop;

            int offsetX;
            int offsetY;


            // --- スケーリングの設定 ---
            double scale = 2.0; // 2.0倍に拡大
            // ドット絵をクッキリさせる設定
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            // 描画領域の中心を基準にスケーリングするために一旦保存
            AffineTransform oldTransform = g2d.getTransform();

            // 画面全体を拡大（これ以降の座標指定は「拡大前の数値」でOKになる）
            g2d.scale(scale, scale);

            // --- 座標計算 (拡大を考慮した画面サイズで計算) ---
            // 画面が拡大された分、表示できる論理的な幅は WINDOW_WIDTH / scale になる
            logicWidth = (int) (ConstSet.WINDOW_WIDTH / scale);
            logicHeight = (int) (ConstSet.WINDOW_HEIGHT / scale);

            playerDrawX = (logicWidth - ConstSet.PLAYER_SIZE) / 2;
            playerDrawY = (logicHeight - ConstSet.PLAYER_SIZE) / 2;

            modelLeft = playerModel.getPlayerX() - (ConstSet.PLAYER_SIZE / 2);
            modelTop = playerModel.getPlayerY() - (ConstSet.PLAYER_SIZE / 2);
            if (!isPerspectiveMoving) {
                offsetX = playerDrawX - modelLeft;
                offsetY = playerDrawY - modelTop;
                // --- 描画実行 ---
                mapView.drawMap(g2d, offsetX, offsetY, this);
                enemyView.drawEnemies(g2d, offsetX, offsetY);
                playerView.drawPlayer(g2d, playerDrawX, playerDrawY);
                bulletView.drawBullet(g2d, offsetX, offsetY);
            } else {
                int moving = perspectiveMoving();
                offsetX = playerDrawX - modelLeft + moving;
                offsetY = playerDrawY - modelTop + moving;
                // --- 描画実行 ---
                mapView.drawMap(g2d, offsetX, offsetY, this);
                enemyView.drawEnemies(g2d, offsetX, offsetY);
                playerView.drawPlayer(g2d, playerDrawX + moving, playerDrawY + moving);
                bulletView.drawBullet(g2d, offsetX, offsetY);
            }
            // 元に戻す（UIなどは拡大しない場合）
            g2d.setTransform(oldTransform);

            // UI描画時の拡大の影響を受けない. 視点移動の影響も受けない.
            hpBarView.drawHPBar(g2d, 20, 30, playerModel.getPlayerHP(), playerModel.getMaxHP());

            // 会話ボックスはUIとして最前面に描画
            dialogueBoxView.drawDialogueBox(g2d);

        } else if (GameState.getCurrentState() == GameState.State.MENU) { // メインメニュー画面の描画
            mainMenuView.drawMainMenu(g);
        } else if (GameState.getCurrentState() == GameState.State.GAME_OVER) { // ゲームオーバー画面の描画
            gameOverMenuView.drawGameOverMenu(g);
        }
    }

    public boolean isPerspectiveMoving() { // 視点が動いている状態ならtrueを返す.
        return this.isPerspectiveMoving;
    }

    public void startPerspectiveMoving() {
        isPerspectiveMoving = true;
    }

    public void finishPerspectiveMoving() {
        isPerspectiveMoving = false;
    }

    private int perspectiveMoving() { // offsetへの補正値を返す.
        if (DialogueSet.dialogueState == DialogueSet.DialogueState.MOVING_PERSPECTIVE_TO_WORKING) {
            timer++;
            if (timer < 60) {
                return timer * 3;
            } else if (60 <= timer && timer < 120) {
                return 60 * 3;
            } else if (120 <= timer && timer < 180) {
                return 60 * 3 - 3 * (timer - 120);
            } else {
                timer = 0;
                DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_PROLOGUE_DIALOGUE;
                finishPerspectiveMoving();
            }
        }
        return 0;
    }
}

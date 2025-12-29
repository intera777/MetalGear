package view;

import GameConfig.*;
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
    private MapView mapView;
    private PlayerView playerView;
    private EnemyView enemyView;
    private BulletView bulletView;
    private GameOverMenuView gameOverMenuView;

    public GameView(PlayerModel pm, GameOverMenuModel gm, MapView mv, EnemyView ev, PlayerView pv,
            BulletView bv, GameOverMenuView gv, PlayerControl pc, BulletControl bc,
            GameOverMenuControl gc) {
        this.playerModel = pm;
        this.gameOverMenuModel = gm;
        this.mapView = mv;
        this.enemyView = ev;
        this.playerView = pv;
        this.bulletView = bv;
        this.gameOverMenuView = gv;

        // GameView内でまとめてキー登録をする
        this.addKeyListener(bc);
        this.addKeyListener(pc);
        this.addKeyListener(gc);
        this.setFocusable(true);
        // this.requestFocusInWindow(); // 起動時に自動でキー入力を受け付ける魔法の呪文らしい
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (GameState.getCurrentState() == GameState.PLAYING) {
            // --- スケーリングの設定 ---
            double scale = 1.5; // 1.5倍に拡大
            // ドット絵をクッキリさせる設定
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
            // 描画領域の中心を基準にスケーリングするために一旦保存
            AffineTransform oldTransform = g2d.getTransform();
        
            // 画面全体を拡大（これ以降の座標指定は「拡大前の数値」でOKになる）
            g2d.scale(scale, scale);

            // --- 座標計算 (拡大を考慮した画面サイズで計算) ---
            // 画面が拡大された分、表示できる論理的な幅は WINDOW_WIDTH / scale になる
            int logicWidth = (int)(ConstSet.WINDOW_WIDTH / scale);
            int logicHeight = (int)(ConstSet.WINDOW_HEIGHT / scale);

            int playerDrawX = (logicWidth - ConstSet.PLAYER_SIZE) / 2;
            int playerDrawY = (logicHeight - ConstSet.PLAYER_SIZE) / 2;

            int modelLeft = playerModel.getPlayerX() - (ConstSet.PLAYER_SIZE / 2);
            int modelTop = playerModel.getPlayerY() - (ConstSet.PLAYER_SIZE / 2);

            int offsetX = playerDrawX - modelLeft;
            int offsetY = playerDrawY - modelTop;

            // --- 描画実行 ---
            mapView.drawMap(g2d, offsetX, offsetY);
            enemyView.drawEnemies(g2d, offsetX, offsetY);
            playerView.drawPlayer(g2d, playerDrawX, playerDrawY);
            bulletView.drawBullet(g2d, offsetX, offsetY);

            // 元に戻す（UIなどは拡大しない場合）
            g2d.setTransform(oldTransform);

        } else if (GameState.getCurrentState() == GameState.GAME_OVER) {
            gameOverMenuView.drawGameOverMenu(g);
        }
    }
}

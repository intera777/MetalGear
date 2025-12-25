package view;

import GameConfig.*;
import control.*;

import model.*;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GameView extends JPanel {
    private PlayerModel playerModel; // 使ってないけど一応保持しておく
    private GameOverMenuModel gameOverMenuModel; // なんか必要らしい
    private MapView mapView;
    private PlayerView playerView;
    private EnemyView enemyView;
    private BulletView bulletView;
    private GameOverMenuView gameOverMenuView;

    public GameView(PlayerModel pm, GameOverMenuModel gm, MapView mv, EnemyView ev, 
            PlayerView pv, BulletView bv, GameOverMenuView gv, PlayerControl pc,
            BulletControl bc, GameOverMenuControl gc) {
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
        super.paintComponent(g); // 背景の初期化

        // プレーヤーが中央に固定されるような座標
        int playerDrawX = (ConstSet.WINDOW_WIDTH - ConstSet.PLAYER_SIZE) / 2;
        int playerDrawY = (ConstSet.WINDOW_HEIGHT - ConstSet.PLAYER_SIZE) / 2;

        // プレイヤーのモデル上の「左上」座標を出す
        int modelLeft = playerModel.getPlayerX() - (ConstSet.PLAYER_SIZE / 2);
        int modelTop = playerModel.getPlayerY() - (ConstSet.PLAYER_SIZE / 2);

        // カメラのずれ(px) offset を計算.
        int offsetX = playerDrawX - modelLeft;
        int offsetY = playerDrawY - modelTop;

        // カメラのずれを一括して反映させる
        mapView.drawMap(g, offsetX, offsetY); // マップの描画
        enemyView.drawEnemies(g, offsetX, offsetY); // 敵の描画
        playerView.drawPlayer(g, playerDrawX, playerDrawY); // プレイヤーの描画
        bulletView.drawBullet(g, offsetX, offsetY); // 弾の描画
        gameOverMenuView.drawGameOverMenu(g); // ゲームオーバー画面の描画
    }
}

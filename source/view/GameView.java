package view;

import GameConfig.*;
import model.*;
import control.*;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GameView extends JPanel {
    private PlayerModel playerModel; // 使ってないけど一応保持しておく
    private MapView mapView;
    private PlayerView playerView;
    private BulletView bulletView;

    public GameView(PlayerModel pm, MapView mv, PlayerView pv, BulletView bv, PlayerControl pc, BulletControl bc) {
        this.playerModel = pm;
        this.mapView = mv;
        this.playerView = pv;
        this.bulletView = bv;

        // GameView内でまとめてキー登録をする
        this.addKeyListener(bc);
        this.addKeyListener(pc);
        this.setFocusable(true);
        // this.requestFocusInWindow(); // 起動時に自動でキー入力を受け付ける魔法の呪文らしい
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 背景の初期化

        // プレーヤーが中央に固定されるような座標
        int playerDrawX = (ConstSet.WINDOW_WIDTH - ConstSet.PLAYER_SIZE) / 2;
        int playerDrawY = (ConstSet.WINDOW_HEIGHT  - ConstSet.PLAYER_SIZE) / 2;

        // プレイヤーのモデル上の「左上」座標を出す
        int modelLeft = playerModel.getPlayerX() - (ConstSet.PLAYER_SIZE / 2);
        int modelTop  = playerModel.getPlayerY() - (ConstSet.PLAYER_SIZE / 2);

        // カメラのずれ(px) offset を計算.
        int offsetX = playerDrawX - modelLeft;
        int offsetY = playerDrawY - modelTop;

        // カメラのずれを一括して反映させる
        mapView.drawMap(g, offsetX, offsetY); // マップの描画
        playerView.drawPlayer(g, playerDrawX, playerDrawY); // プレイヤーの描画
        bulletView.drawBullet(g, offsetX, offsetY); // 弾の描画
    }
}
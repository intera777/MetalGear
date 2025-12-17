package view;

import model.*;
import control.*;

import javax.swing.JPanel;
import java.awt.Graphics;

public class GameView extends JPanel {
    private PlayerModel playerModel; // 使ってないけど一応保持しておく
    private PlayerView playerView;
    private BulletView bulletView;

    public GameView(PlayerModel pm, PlayerView pv, BulletView bv, PlayerControl pc, BulletControl bc) {
        this.playerModel = pm;
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
        playerView.drawPlayer(g); // プレイヤーの描画
        bulletView.drawBullet(g); // 弾の描画
    }
}
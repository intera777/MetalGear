package view;

import model.PlayerModel;
import control.PlayerControl;

import javax.swing.JPanel;
import java.awt.Graphics;

public class PlayerView extends JPanel {
    private PlayerModel model;

    public PlayerView(PlayerModel m, PlayerControl c) {
        this.model = m;

        this.addKeyListener(c);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Modelからデータを取得して描画
        g.fillRect(model.getPlayerX(), model.getPlayerY(), 30, 30);
        // g.fillRect(100,530,30,30); //描画範囲テスト用
    }
}

package view;

import model.*;

import java.awt.Graphics;

public class PlayerView { // extends JPanel は消去した. どうやらパネルが重なっちゃうのは不適切らしいため.
    private PlayerModel model;

    public PlayerView(PlayerModel m) {
        this.model = m;
    }

    // プレイヤーを描画するメソッド
    protected void drawPlayer(Graphics g) {
        g.fillRect(model.getPlayerX(), model.getPlayerY(), 30, 30);
        // g.fillRect(100,530,30,30); //描画範囲テスト用
    }
}

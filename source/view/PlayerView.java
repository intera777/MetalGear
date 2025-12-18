package view;

import GameConfig.*;
import model.*;

import java.awt.Graphics;

public class PlayerView { // extends JPanel は消去した. どうやらパネルが重なっちゃうのは不適切らしいため.
    private PlayerModel model;

    public PlayerView(PlayerModel m) {
        this.model = m;
    }

    // プレイヤーを画面中央に描画するメソッド
    protected void drawPlayer(Graphics g) {
        int drawX = ConstSet.WINDOW_WIDTH / 2 - (30 / 2);
        int drawY = ConstSet.WINDOW_HEIGHT / 2 - (30 / 2);

        g.fillRect(drawX, drawY, 30, 30);
        // g.fillRect(100,530,30,30); //描画範囲テスト用
    }
}

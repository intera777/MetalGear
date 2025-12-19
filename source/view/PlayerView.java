package view;

import GameConfig.*;
import model.*;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerView { // extends JPanel は消去した. どうやらパネルが重なっちゃうのは不適切らしいため.
    private PlayerModel model;

    public PlayerView(PlayerModel m) {
        this.model = m;
    }

    // プレイヤーを画面中央に描画するメソッド
    protected void drawPlayer(Graphics g, int drawX, int drawY) {
        g.setColor(Color.GREEN); // プレーヤーの色を緑色にした
        g.fillRect(drawX, drawY, ConstSet.PLAYER_SIZE, ConstSet.PLAYER_SIZE);
        // g.fillRect(100,530,30,30); //描画範囲テスト用
    }
}

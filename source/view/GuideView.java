package view;

import model.*;
import GameConfig.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GuideView {

    public void drawGuide(Graphics g, GuideModel model) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("MS ゴシック", Font.PLAIN, 16));
        g.drawString("Hキーを押すと操作方法が表示されます", 10, 20);

        if (model.getGuideDisplay()) {
            // 背景を少し暗くする
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);

            g.setColor(Color.WHITE);
            
            g.setFont(new Font("MS ゴシック", Font.BOLD, 32));
            g.drawString("操作方法", 100, 150);

            g.setFont(new Font("MS ゴシック", Font.PLAIN, 24));
            g.drawString("Wキー: 前に移動", 100, 220);
            g.drawString("Aキー: 左に移動", 100, 270);
            g.drawString("Sキー: 後ろに移動", 100, 320);
            g.drawString("Dキー: 右に移動", 100, 370);
            g.drawString("Jキー: 弾発射", 100, 420);
        }
    }
}

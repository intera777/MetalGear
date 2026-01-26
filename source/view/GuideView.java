package view;

import model.*;
import GameConfig.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GuideView {

    public void drawGuide(Graphics g, GuideModel model) {

        if (model.getGuideDisplay()) {
            // 背景を少し暗くする
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);

            g.setColor(Color.WHITE);
            
            g.setFont(new Font("MS ゴシック", Font.BOLD, 20));
            g.drawString("操作方法", 20, 90);

            g.setFont(new Font("MS ゴシック", Font.PLAIN, 14));
            g.drawString("Wキー: 前に移動", 20, 130);
            g.drawString("Aキー: 左に移動", 20, 160);
            g.drawString("Sキー: 後ろに移動", 20, 190);
            g.drawString("Dキー: 右に移動", 20, 220);
            g.drawString("Jキー: 弾発射", 20, 250);
        }
    }
}

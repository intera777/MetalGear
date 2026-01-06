package view;

import GameConfig.*;
import model.*;

import java.awt.*;

public class GameOverMenuView {
    private GameOverMenuModel gameOverMenuModel;

    // フォントの設定
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 48); // タイトルのフォント
    private final Font menuFont = new Font("SansSerif", Font.PLAIN, 32); // メニューのフォント

    // 色の設定
    private final Color titleColor = Color.RED; // タイトルの色
    private final Color normalColor = Color.WHITE; // 通常の色
    private final Color selectionColor = Color.YELLOW; // 選択されるときの色

    public GameOverMenuView(GameOverMenuModel gm) {
        this.gameOverMenuModel = gm;
    }

    // ゲームオーバー画面を描画するメソッド
    public void drawGameOverMenu(Graphics g) {
        // 背景を少し暗くする. 黒色の透明度 a を 150
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);

        // 「GAME OVER」タイトルの描画
        g.setFont(titleFont);
        g.setColor(titleColor);
        String title = "GAME OVER";
        int titleX = getCenterX(g, title, ConstSet.WINDOW_WIDTH);
        g.drawString(title, titleX, ConstSet.WINDOW_HEIGHT / 3);

        // メニュー項目の描画
        g.setFont(menuFont);
        GameOverMenuModel.MenuItem[] items = gameOverMenuModel.getMenuItems();
        int selectedIndex = gameOverMenuModel.getSelectedIndex(); // 今何番目の項目を選択しているかを取得

        for (int i = 0; i < items.length; i++) {
            String text = getMenuText(items[i]);

            // 選択中の項目は色を変える、矢印をつける. 選択されてないものは白色
            if (i == selectedIndex) {
                g.setColor(selectionColor);
                text = "> " + text + " <";
            } else {
                g.setColor(normalColor);
            }

            int itemX = getCenterX(g, text, ConstSet.WINDOW_WIDTH);
            // タイトルの下から順に並べる (選択項目の間隔が 50px)
            g.drawString(text, itemX, (ConstSet.WINDOW_HEIGHT / 2) + (i * 50));
        }
    }

    // gameState によって, 表示テキストを変える

    private String getMenuText(GameOverMenuModel.MenuItem item) {
        switch (item) {
            case RESTART_GAME:
                return "もう一度プレイする";
            case FINISH_GAME:
                return "ゲームをやめる";
            default:
                return "UNKNOWN";
        }
    }

    // テキストを中央揃えにするための x 座標を計算
    private int getCenterX(Graphics g, String text, int screenWidth) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        return (screenWidth - textWidth) / 2;
    }
}

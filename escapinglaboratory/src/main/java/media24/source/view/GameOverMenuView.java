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
    private final Color rankColor = new Color(200, 0, 0); // 評価を表示するときの色

    public GameOverMenuView(GameOverMenuModel gm) {
        this.gameOverMenuModel = gm;
    }

    // ゲームオーバー画面を描画するメソッド
    public void drawGameOverMenu(Graphics g) {
        // 背景を黒で塗りつぶす
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);

        // 「評価：不可」を画面中央より少し上にドカンと配置
        g.setFont(new Font("MS Gothic", Font.BOLD, 120)); // 少しサイズをアップ
        g.setColor(new Color(220, 0, 0)); // 鮮烈な赤
    
        String rankText = "評価：不可";
        int rankX = getCenterX(g, rankText, ConstSet.WINDOW_WIDTH);
        // 画面の 1/3 くらいの高さに配置
        g.drawString(rankText, rankX, ConstSet.WINDOW_HEIGHT / 3 + 50);

        // メニュー項目の描画
        g.setFont(menuFont);
        GameOverMenuModel.MenuItem[] items = gameOverMenuModel.getMenuItems();
        int selectedIndex = gameOverMenuModel.getSelectedIndex();

        for (int i = 0; i < items.length; i++) {
            String text = getMenuText(items[i]);
            if (i == selectedIndex) {
                g.setColor(selectionColor);
                text = "> " + text + " <";
            } else {
                g.setColor(normalColor);
            }

            int itemX = getCenterX(g, text, ConstSet.WINDOW_WIDTH);
            // 「不可」から少し離れた位置（画面半分より下）に配置
            g.drawString(text, itemX, (ConstSet.WINDOW_HEIGHT / 2 + 50) + (i * 60));
        }
    }

    // gameState によって, 表示テキストを変える

    private String getMenuText(GameOverMenuModel.MenuItem item) {
        switch (item) {
            case RESTART_GAME:
                return "もう一度脱出を試みる";
            case FINISH_GAME:
                return "諦めて謎の棒をぐるぐる回す";
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

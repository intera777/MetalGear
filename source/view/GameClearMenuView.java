package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameClearMenuView {
    private GameClearMenuModel gameClearModel;

    // フォントの設定
    private final Font menuFont = new Font("SansSerif", Font.PLAIN, 32);  // メニューのフォント

    // 色の設定
    private final Color normalColor = Color.WHITE;     // 通常の色
    private final Color selectionColor = Color.YELLOW; // 選択中の色

    // 背景画像の変数（必要に応じてConstSetにパスを追加してください）
    private Image backgroundImage;

    public GameClearMenuView(GameClearMenuModel model) {
        this.gameClearModel = model;
        loadImages();
    }

    private void loadImages() {
        try {
            // 背景画像がある場合は読み込み（存在しない場合は黒背景で代用）
            File imgFile = new File(ConstSet.IMG_PATH_GAMECLEAR_BG);
            if (imgFile.exists()) {
                backgroundImage = ImageIO.read(imgFile);
            }
        } catch (IOException e) {
            System.err.println("GameClear背景の読み込みに失敗しました");
        }
    }

    public void drawGameClearMenu(Graphics g) {
        // 1. 背景の描画
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT, null);
        } else {
            g.setColor(new Color(0, 0, 0, 150)); // 少し透過させた黒（ゲーム画面の上に重ねる想定）
            g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        }

        // 3. メニュー項目の描画
        g.setFont(menuFont);
        GameClearMenuModel.MenuItem[] items = gameClearModel.getMenuItems();
        int selectedIndex = gameClearModel.getSelectedIndex();

        for (int i = 0; i < items.length; i++) {
            String text = getMenuText(items[i]);

            if (i == selectedIndex) {
                g.setColor(selectionColor);
                text = "> " + text + " <";
            } else {
                g.setColor(normalColor);
            }

            int itemX = getCenterX(g, text, ConstSet.WINDOW_WIDTH);
            // タイトルの少し下から項目を並べる
            g.drawString(text, itemX, (ConstSet.WINDOW_HEIGHT / 2 + 60) + (i * 60));
        }
    }

    // 列挙型に応じた日本語テキストを返す
    private String getMenuText(GameClearMenuModel.MenuItem item) {
        switch (item) {
            case RESTART_GAME:
                return "もう一度プレイする";
            case FINISH_GAME:
                return "ゲームを終了する";
            default:
                return "Unknown";
        }
    }

    // テキストを中央揃えにするための x 座標を計算
    private int getCenterX(Graphics g, String text, int screenWidth) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        return (screenWidth - textWidth) / 2;
    }
}
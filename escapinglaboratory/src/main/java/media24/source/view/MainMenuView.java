package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MainMenuView {
    private MainMenuModel mainMenuModel;

    // フォントの設定
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 48); // タイトルのフォント
    private final Font menuFont = new Font("SansSerif", Font.PLAIN, 32); // メニューのフォント

    // 色の設定
    private final Color titleColor = Color.CYAN; // タイトルの色
    private final Color normalColor = Color.WHITE; // 通常の色
    private final Color selectionColor = Color.YELLOW; // 選択されるときの色

    // 背景画像の変数
    private Image backgroundImage;

    public MainMenuView(MainMenuModel mmv) {
        this.mainMenuModel = mmv;
        loadImages();
    }

    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource(ConstSet.IMG_PATH_MAINMENU_BG));
        } catch (IOException e) {
            // 読み込み失敗時のデバック用
            System.err.println("読み込み失敗");
            System.err.println("探した場所: " + ConstSet.IMG_PATH_MAINMENU_BG);
            e.printStackTrace();
        }
    }

    public void drawMainMenu(Graphics g) {
        // 背景の描画
        if (backgroundImage != null) { // 画像がある場合は画像を描画
            g.drawImage(backgroundImage, 0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT, null);
        } else { // 画像がない場合は黒背景を描画
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        }

        // メニュー項目の描画
        g.setFont(menuFont);
        ConstSet.MainMenuItems[] items = mainMenuModel.getMenuItems();
        int selectedIndex = mainMenuModel.getSelectedIndex(); // 今何番目の項目を選択しているかを取得

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
            g.drawString(text, itemX, (ConstSet.WINDOW_HEIGHT / 2 + 60) + (i * 50)); // 画面半分の少し下に項目を表示
        }
    }

    // gameState によって, 表示テキストを変える
    public String getMenuText(ConstSet.MainMenuItems item) {
        switch (item) {
            case START_GAME:
                return "ゲームを始める";
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

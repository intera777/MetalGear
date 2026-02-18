package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameClearMenuView {
    private GameClearMenuModel gameClearModel;

    // フォントの設定
    private final Font menuFont = new Font("SansSerif", Font.PLAIN, 32); // メニューのフォント

    // 色の設定
    private final Color normalColor = Color.WHITE; // 通常の色
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
            java.net.URL imgUrl = getClass().getResource(ConstSet.IMG_PATH_GAMECLEAR_BG);
            if (imgUrl != null) {
                backgroundImage = ImageIO.read(imgUrl);
            }
        } catch (IOException e) {
            System.err.println("GameClear背景の読み込みに失敗しました");
        }
    }

    public void drawGameClearMenu(Graphics g) {
        GameClearMenuModel.Phase currentPhase = gameClearModel.getCurrentPhase();

        // 背景の描画
        if (currentPhase == GameClearMenuModel.Phase.BACKGROUND_ONLY) {
            // フェーズ1: クリア背景画像（MISSION COMPLETEなど）を全画面表示
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT,
                        null);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
            }
        } else {
            // フェーズ2: 真っ黒な背景に移行（リザルト画面）
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        }

        // スコアとランクの描画 (フェーズ2以上で表示)
        if (currentPhase == GameClearMenuModel.Phase.SCORE_DISPLAY
                || currentPhase == GameClearMenuModel.Phase.MENU_DISPLAY) {

            drawResultStats(g);
        }

        // メニュー項目の描画 (フェーズ3のみ) ---
        if (currentPhase == GameClearMenuModel.Phase.MENU_DISPLAY) {
            drawMenuItems(g);
        }
    }

    /**
     * スコアとランクを表示するメソッド
     */
    private void drawResultStats(Graphics g) {
        String rankText = gameClearModel.getFinalRank();
        String scoreText = "FINAL SCORE: " + gameClearModel.getFinalScore();

        // スコアの描画
        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        int scoreX = getCenterX(g, scoreText, ConstSet.WINDOW_WIDTH);
        g.drawString(scoreText, scoreX, ConstSet.WINDOW_HEIGHT / 2 - 120);

        // ランク（評価）の描画
        g.setFont(new Font("MS Gothic", Font.BOLD, 120));

        // ランクに応じた色分け（メタルギア風）
        switch (rankText) {
            case "秀":
                g.setColor(new Color(255, 215, 0));
                break; // 金色
            case "優":
                g.setColor(new Color(192, 192, 192));
                break; // 銀色
            case "良":
                g.setColor(Color.CYAN);
                break;
            default:
                g.setColor(Color.WHITE);
                break;
        }

        String fullRankText = "評価: " + rankText;
        int rankX = getCenterX(g, fullRankText, ConstSet.WINDOW_WIDTH);
        g.drawString(fullRankText, rankX, ConstSet.WINDOW_HEIGHT / 2 + 20);
    }

    /**
     * 既存のメニュー項目描画ロジックを独立させたメソッド
     */
    private void drawMenuItems(Graphics g) {
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
            g.drawString(text, itemX, (ConstSet.WINDOW_HEIGHT / 2 + 150) + (i * 60));
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

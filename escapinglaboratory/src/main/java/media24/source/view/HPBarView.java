package view;

import java.awt.*;

public class HPBarView {
    private static final int BAR_WIDTH = 150;
    private static final int BAR_HEIGHT = 12;
    private static final Color COLOR_BG = new Color(50, 50, 50);      // 暗いグレー
    private static final Color COLOR_HP = new Color(0, 255, 100);     // 体力に余裕のある時の緑
    private static final Color COLOR_MEDIUM = new Color(255, 200, 0); // 注意時の黄色
    private static final Color COLOR_DANGER = new Color(255, 50, 50); // ピンチ時の赤
    private static final Color COLOR_BORDER = Color.WHITE;

    // HPバーを描画する
    public void drawHPBar(Graphics2D g2d, int x, int y, int currentHp, int maxHp) {
        // HPの割合を計算 (0.0 ~ 1.0)
        double hpRatio = Math.max(0, Math.min(1.0, (double) currentHp / maxHp));
        int currentBarWidth = (int)(BAR_WIDTH * hpRatio);

        // 背景を描画
        g2d.setColor(COLOR_BG);
        g2d.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        // HPバーを描画（HPが低いときは色を変える）
        if (hpRatio < 0.3) {
            g2d.setColor(COLOR_DANGER);
        } else if (hpRatio <= 0.5) {
            g2d.setColor(COLOR_MEDIUM);
        } else {
            g2d.setColor(COLOR_HP);
        }
        g2d.fillRect(x, y, currentBarWidth, BAR_HEIGHT);

        // 外枠を描画
        g2d.setColor(COLOR_BORDER);
        g2d.setStroke(new BasicStroke(2)); // 線の太さ
        g2d.drawRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        // HPの数値を横に表示
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("LIFE", x, y - 5);
    }
}
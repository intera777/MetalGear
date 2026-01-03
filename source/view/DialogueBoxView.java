package view;

import model.DialogueBoxesModel;
import model.DialogueBoxModel;
import GameConfig.ConstSet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class DialogueBoxView {
    private DialogueBoxesModel model;

    public DialogueBoxView(DialogueBoxesModel model) {
        this.model = model;
    }

    public void drawDialogueBox(Graphics g) {
        if (!model.isVisible())
            return;

        DialogueBoxModel current = model.getCurrentDialogue();
        if (current == null)
            return;

        // ウィンドウ下部に黒い半透明のボックスを描画
        int height = 150;
        int y = ConstSet.WINDOW_HEIGHT - height;
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, y, ConstSet.WINDOW_WIDTH, height);

        // 枠線
        g.setColor(Color.WHITE);
        g.drawRect(10, y + 10, ConstSet.WINDOW_WIDTH - 20, height - 20);

        // 名前とセリフの描画
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString(current.getName(), 30, y + 50);

        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g.drawString(current.getDialogue(), 30, y + 90);
    }
}

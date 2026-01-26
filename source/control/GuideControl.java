package control;

import model.GuideModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GuideControl implements KeyListener {
    private GuideModel model;

    public GuideControl(GuideModel model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Hキーが押されたとき
        if (e.getKeyCode() == KeyEvent.VK_H) {
            // 押されている間はガイドを表示状態(true)にする
            model.setGuideDisplay(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Hキーが離されたとき
        if (e.getKeyCode() == KeyEvent.VK_H) {
            // 指を離したらガイドを非表示(false)にする
            model.setGuideDisplay(false);
        }
    }
}
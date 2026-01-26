package control;

import model.GuideModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GuideControl implements KeyListener {
    private GuideModel model;
    
    // キーの「押しっぱなし」を判定するためのフラグ
    private boolean isHPressed = false;

    public GuideControl(GuideModel model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Hキーが押されたとき
        if (e.getKeyCode() == KeyEvent.VK_H) {
            // まだ「押しっぱなし」状態でない場合のみ実行
            if (!isHPressed) {
                // Modelの表示状態を切り替える（ONならOFFへ、OFFならONへ）
                model.toggleVisibility(); 
                isHPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Hキーが離されたらフラグを解除
        if (e.getKeyCode() == KeyEvent.VK_H) {
            isHPressed = false;
        }
    }
}
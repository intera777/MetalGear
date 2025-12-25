package control;

import model.GameOverMenuModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameOverMenuControl implements KeyListener {
    private GameOverMenuModel model;
    // キーの「押しっぱなし」を判定するためのフラグ
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isEnterPressed = false;

    // 外部（Mainクラスなど）から「決定キーが押された瞬間」を知るためのフラグ
    public boolean isEnterTriggered = false;

    public GameOverMenuControl(GameOverMenuModel model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // --- ↑キーが押された時 ---
        if (code == KeyEvent.VK_UP) {
            if (!isUpPressed) {
                model.moveSelectionUp();
                isUpPressed = true;
            }
        }

        // --- ↓キーが押された時 ---
        if (code == KeyEvent.VK_DOWN) {
            if (!isDownPressed) {
                model.moveSelectionDown();
                isDownPressed = true;
            }
        }

        // --- Enterキーが押された時 ---
        if (code == KeyEvent.VK_ENTER) {
            if (!isEnterPressed) {
                model.enterPressed();
                isEnterTriggered = true;
                isEnterPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // キーが離されたらフラグを解除し、次のキーが新しく押された時の出力を受け付けられるようにする
        if (code == KeyEvent.VK_UP) {
            isUpPressed = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            isDownPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            isEnterPressed = false;
            isEnterTriggered = false;
        }
    }

    public boolean getEnterTriggered() {
        return isEnterTriggered;
    }
}

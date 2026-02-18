package control;

import model.DialogueBoxesModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DialogueBoxControl implements KeyListener {
    private DialogueBoxesModel model;
    private boolean isEnterPressed = false;

    public DialogueBoxControl(DialogueBoxesModel model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isEnterPressed) {
                if (model.isVisible()) {
                    model.next();
                }
                isEnterPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            isEnterPressed = false;
        }
    }
}

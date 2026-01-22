package model;

import GameConfig.DialogueSet;
import GameConfig.GameState;
import view.*;

public class DialogueBoxesModel {

    private DialogueBoxModel[] dialogues;
    private int currentIndex = -1;
    private boolean isVisible = false;
    private GameView gameview;

    public DialogueBoxesModel() {

    }

    public void setGameView(GameView gv) {
        gameview = gv;
    }

    public void setDialogues(DialogueSet.Dialogue[] newDialogues) {
        dialogues = new DialogueBoxModel[newDialogues.length];
        for (int i = 0; i < newDialogues.length; i++) {
            DialogueSet.Dialogue d = newDialogues[i];
            dialogues[i] = new DialogueBoxModel(d.name(), d.text());
        }
        currentIndex = 0;
        isVisible = true;
    }

    public void next() {
        if (!isVisible)
            return;
        currentIndex++;
        if (currentIndex >= dialogues.length) {
            isVisible = false;
            currentIndex = -1;

            // 会話セットの表示が完了したタイミングで、ゲームの進行状況を次の段階へ進めます。
            // このロジックは、会話イベントのシーケンスに強く依存しています。
            switch (DialogueSet.dialogueState) {
                case PROLOGUE:
                    DialogueSet.dialogueState =
                            DialogueSet.DialogueState.MOVING_PERSPECTIVE_TO_WORKING;
                    gameview.startPerspectiveMoving();
                    break;
                case AFTER_PROLOGUE_DIALOGUE:
                    DialogueSet.dialogueState =
                            DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION;
                    break;
                case AFTER_SCRIPTED_MOVE:
                    DialogueSet.dialogueState = DialogueSet.DialogueState.MAIN_GAMEPLAY;
                    break;
                case GUARDSMAN:
                    GameState.setCurrentState(GameState.State.GAME_OVER);
                    DialogueSet.dialogueState = DialogueSet.DialogueState.GAME_OVER;
                    break;
                default:
                    break; // その他の状態では何もしない
            }
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public DialogueBoxModel getCurrentDialogue() {
        if (isVisible && currentIndex >= 0 && currentIndex < dialogues.length) {
            return dialogues[currentIndex];
        }
        return null;
    }
}

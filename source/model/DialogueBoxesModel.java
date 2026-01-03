package model;

import GameConfig.DialogueSet;

public class DialogueBoxesModel {

    private DialogueBoxModel[] dialogues;
    private int currentIndex = -1;
    private boolean isVisible = false;

    public void setDialogues(String[] names, String[] texts) {
        if (names.length != texts.length)
            return;

        dialogues = new DialogueBoxModel[names.length];
        for (int i = 0; i < names.length; i++) {
            dialogues[i] = new DialogueBoxModel(names[i], texts[i]);
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

            DialogueSet.dialogue_count++;
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

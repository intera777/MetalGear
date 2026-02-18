package model;

import GameConfig.*;

public class DialogueBoxModel {
    private String name; // 誰が発言しているか.
    private String dialogue; // 発言の内容.

    public DialogueBoxModel(String name, String dialogue) {
        this.name = name;
        this.dialogue = dialogue;
    }

    public String getName() {
        return name;
    }

    public String getDialogue() {
        return dialogue;
    }
}

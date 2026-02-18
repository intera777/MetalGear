package GameConfig;

public final class DialogueSet {

    private DialogueSet() {}

    public record Dialogue(String name, String text) {
    }
    /**
     * ゲームの会話イベントの進行状況を示します。
     * <ul>
     * <li>{@code PROLOGUE}: プロローグ開始時 (旧 dialogue_count = 0)</li>
     * <li>{@code AFTER_PROLOGUE_DIALOGUE}: プロローグ会話終了後 (旧 dialogue_count = 1)</li>
     * <li>{@code AWAITING_SCRIPTED_MOVE_COMPLETION}: スクリプト移動完了待ち (旧 dialogue_count = 2)</li>
     * <li>{@code AFTER_SCRIPTED_MOVE}: スクリプト移動完了後 (旧 dialogue_count = 3)</li>
     * <li>{@code MAIN_GAMEPLAY}: メインゲームプレイ開始 (旧 dialogue_count = 4)</li>
     * </ul>
     */
    //@formatter:off
    public enum DialogueState {
        PROLOGUE,
        MOVING_PERSPECTIVE_TO_WORKING,
        AFTER_PROLOGUE_DIALOGUE,
        AWAITING_SCRIPTED_MOVE_COMPLETION,
        AFTER_SCRIPTED_MOVE,
        MAIN_GAMEPLAY,
        REACHED_B2F,
        REACHED_B1F,
        REACHED_1F,
        GUARDSMAN,
        GAME_OVER
    }

    //各階に既に到達したかどうかのフラグ.既に到達していたらtrue. Dialogueを一回のみ表示するため.
    public static boolean isReachedYetB2F=false;
    public static boolean isReachedYetB1F=false;
    public static boolean isReachedYet1F=false;

    public static void initdialogueflag(){
        isReachedYetB2F=false;
        isReachedYetB1F=false;
        isReachedYet1F=false;
    }

    public static DialogueState dialogueState = DialogueState.PROLOGUE;

   

    public final static Dialogue[] DIALOGUE_SET_0 = {
        new Dialogue("", "ここは名門大学,D大学"),
        new Dialogue("", "ここでは世界に通用する学生の育成の名のもと"),
        new Dialogue("", "常軌を逸する授業,基礎科学実験Aが行われていた..."),
        new Dialogue("", "この科目の単位を落とした学生は地下に幽閉され,"),
        new Dialogue("", "研究者の監視のもと強制的な学習をさせられることになる"),
        new Dialogue("", "主人公も単位を落とし,地下で日夜勉強に励んでいたのだが..."),
        new Dialogue("", "ある日地下で恐ろしい光景を目にしてしまう"),
        new Dialogue("", "それは著しく成績の悪い学生たちが"),
        new Dialogue("", "強制的に労働させられている姿であった"),
    };

    public final static Dialogue[] DIALOGUE_SET_1 = {
        new Dialogue("プレイヤー", "なんで...ここでこんなことが..."),
        new Dialogue("プレイヤー", "うわああああああ!"),
    };

    public final static Dialogue[] DIALOGUE_SET_2 = {
        new Dialogue("プレイヤー", "一刻も早く...  ここから脱出しないと... "),
        new Dialogue("プレイヤー", "地上へ行って... 絶対に生きて帰るんだ"),
        new Dialogue("プレイヤー", "でも,もし教授たちに見つかったらどうしよう..."),
        new Dialogue("プレイヤー", " この銃で気絶させればその場しのぎにはなるけど... 限界があるし"),
        new Dialogue("プレイヤー", "弾は足りるかな... 途中で補給できればいいんだけど")
    };

    public final static Dialogue[] DIALOGUE_SET_B2F={
        new Dialogue("プレイヤー","とりあえず収容所からは抜けられたけど,問題はここからだ"),
    };

    public final static Dialogue[] DIALOGUE_SET_1F={
        new Dialogue("プレイヤー","あと少しで外だ... でも警備員に見つかったら終わりだ..."),
        new Dialogue("プレイヤー", "不審な動きをしなければ大丈夫なはず"),
        new Dialogue("プレイヤー", "慎重に,慎重にいこう")
    };

    public final static Dialogue[] DIALOGUE_GUARDSMAN={
        new Dialogue("警備員", "学生証,見せてくれる？"),
        new Dialogue("プレイヤー","......"),
        new Dialogue("警備員","……その銃は,何かな？"),
        new Dialogue("プレイヤー","......"),
        new Dialogue("警備員","君,脱獄者だよね.")
    };
    //@formatter:on

}

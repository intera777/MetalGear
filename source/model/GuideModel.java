package model;

public class GuideModel {
    // trueなら表示,falseのときは非表示.
    private boolean isGuideDisplay = false;

    public GuideModel() {
        isGuideDisplay = false;
    }

    // 表示されるかどうかのフラグを返す.
    public boolean getGuideDisplay() {
        return isGuideDisplay;
    }

    // フラグをセット.
    public void setGuideDisplay(boolean b) {
        isGuideDisplay = b;
    }
}

package model;

import GameConfig.*;

public class MainMenuModel {

      // 項目を格納する配列.
      private final ConstSet.MainMenuItems[] gamestate =
                  {ConstSet.MainMenuItems.START_GAME, ConstSet.MainMenuItems.FINISH_GAME};

      // 今上から何番目の項目を選択しているか.
      private int selectedIndex = 0;

      public MainMenuModel() {}

      // 上の項目へ移動(一番上から移動した場合は一番下へ).
      public void moveSelectionUp() {
            selectedIndex = (selectedIndex - 1 + gamestate.length) % gamestate.length;
      }

      // 下の項目へ移動(一番下から移動した場合は一番上へ).
      public void moveSelectionDown() {
            selectedIndex = (selectedIndex + 1) % gamestate.length;
      }

      // 選択したゲームの状態を取得するメソッド.
      public ConstSet.MainMenuItems getSelectedItem() {
            return gamestate[selectedIndex];
      }

      // 項目のリスト自体を取得.
      public ConstSet.MainMenuItems[] getMenuItems() {
            return gamestate;
      }

      // 今何番目の項目を選択しているかを取得.
      public int getSelectedIndex() {
            return selectedIndex;
      }

      // ↓キーが押された時呼び出される.
      public void downArrowPressed() {
            moveSelectionDown();
      }

      // ↑キーが押された時呼び出される.
      public void upArrowPressed() {
            moveSelectionUp();
      }

      // エンターキーが押された時呼び出される.
      public void enterPressed() {
            if (getSelectedItem() == ConstSet.MainMenuItems.START_GAME) {
                  GameState.setCurrentState(GameState.State.PLAYING);
            } else if (getSelectedItem() == ConstSet.MainMenuItems.FINISH_GAME) {
                  System.exit(0);
            }

      }
}

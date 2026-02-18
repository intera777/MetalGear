package model;

import GameConfig.*;

public class GameOverMenuModel {

      public enum MenuItem {
            RESTART_GAME, FINISH_GAME
      }

      // 項目を格納する配列.
      private final MenuItem[] menuItems = {MenuItem.RESTART_GAME, MenuItem.FINISH_GAME};

      // 今上から何番目の項目を選択しているか.
      private int selectedIndex = 0;

      public GameOverMenuModel() {}

      // 上の項目へ移動(一番上から移動した場合は一番下へ).
      public void moveSelectionUp() {
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
      }

      // 下の項目へ移動(一番下から移動した場合は一番上へ).
      public void moveSelectionDown() {
            selectedIndex = (selectedIndex + 1) % menuItems.length;
      }

      // 選択したゲームの状態を取得するメソッド.

      public MenuItem getSelectedItem() {
            return menuItems[selectedIndex];
      }

      // 項目のリスト自体を取得.

      public MenuItem[] getMenuItems() {
            return menuItems;
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
            if (getSelectedItem() == MenuItem.RESTART_GAME) {
                  GameState.setCurrentState(GameState.State.PLAYING);
            } else if (getSelectedItem() == MenuItem.FINISH_GAME) {
                  System.exit(0);
            }

      }
}

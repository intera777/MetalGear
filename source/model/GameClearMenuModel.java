package model;

import GameConfig.*;

public class GameClearMenuModel {

      // 演出のフェーズ定義
      public enum Phase {
            BACKGROUND_ONLY, // ステージクリア画像のみ
            SCORE_DISPLAY,   // 暗転してスコアとランクを表示
            MENU_DISPLAY     // 最後に選択メニューが出現
      }

      public enum MenuItem {
            RESTART_GAME, FINISH_GAME
      }

      // 項目を格納する配列.
      private final MenuItem[] menuItems = {MenuItem.RESTART_GAME, MenuItem.FINISH_GAME};

      // 今上から何番目の項目を選択しているか.
      private int selectedIndex = 0;

      private Phase currentPhase = Phase.BACKGROUND_ONLY;
      private String finalRank = "";
      private int finalScore = 0;

      public GameClearMenuModel() {}

      // 上の項目へ移動(一番上から移動した場合は一番下へ).
      public void moveSelectionUp() {
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
      }

      // 下の項目へ移動(一番下から移動した場合は一番上へ).
      public void moveSelectionDown() {
            selectedIndex = (selectedIndex + 1) % menuItems.length;
      }

      // ゲッター
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

      public Phase getCurrentPhase() {
            return currentPhase;
      }

      public String getFinalRank() {
            return finalRank;
      }

      public int getFinalScore() {
            return finalScore;
      }

      // セッター
      public void setFinalResult(int score) {
            this.finalScore = score;
            if (score >= 9000) {
                  this.finalRank = "秀";
            } else if (score >= 7000) {
                  this.finalRank = "優";
            } else if (score >= 4000) {
                  this.finalRank = "良";
            } else {
                  this.finalRank = "可";
            }
      }

      // フェーズの切り
      public void setCurrentPhase(Phase phase) {
            this.currentPhase = phase;
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

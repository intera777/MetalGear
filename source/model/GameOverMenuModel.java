package model;

public class GameOverMenuModel {
      /**
       * メニューの選択項目を定義するenum
       */
      public enum MenuItem {
            RETRY("リトライ"), TO_TITLE("タイトルへ");

            private final String label;

            MenuItem(String label) {
                  this.label = label;
            }

            @Override
            public String toString() {
                  return this.label;
            }
      }

      private final MenuItem[] menuItems = MenuItem.values();
      private int selectedIndex = 0;

      public GameOverMenuModel() {}

      public void moveSelectionUp() {
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
      }

      public void moveSelectionDown() {
            selectedIndex = (selectedIndex + 1) % menuItems.length;
      }

      public MenuItem getSelectedItem() {
            return menuItems[selectedIndex];
      }

      public MenuItem[] getMenuItems() {
            return menuItems;
      }

      public int getSelectedIndex() {
            return selectedIndex;
      }
}

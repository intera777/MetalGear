package GameConfig;

public class GameState {
      public final static int MENU = 0;
      public final static int PLAYING = 1;
      public final static int GAME_OVER = 2;

      public static int currentState;

      public GameState(int state) {
            currentState = state;
      }

      // ゲームの状態を取得するメソッド
      public static int getGameState() {
            return currentState;
      }

      // ゲームの状態を設定するメソッド
      public static void setGameState(int state) {
            currentState = state;
      }

      public static int getCurrentState() {
            return currentState;
      }
}

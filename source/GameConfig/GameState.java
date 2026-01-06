package GameConfig;

public class GameState {

      public enum State {
            MENU, PLAYING, GAME_OVER
      }

      public static State currentState = State.GAME_OVER;

      private GameState() {

      }

      // ゲームの状態を取得するメソッド

      public static State getCurrentState() {
            return currentState;
      }

      // ゲームの状態を設定するメソッド

      public static void setCurrentState(State state) {
            currentState = state;
      }
}

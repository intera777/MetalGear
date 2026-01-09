package model;

import GameConfig.*;

import java.util.Arrays;

public class PlayerModel {
    // PlayerXとPlayerYはどちらもプレイヤーの画像の中心の座標.
    private int playerX = -100; // 初期位置
    private int playerY = -100; // 初期位置
    private int playerDirection = ConstSet.RIGHT; // プレイヤーが向いている方向.0123の順で右上左下.
    private int playerHP = 10;// プレイヤーの体力の初期値.
    private int maxHP = 10; // プレイヤーの体力の最大値.

    // アニメーション用
    private int animationFrame = 0; // 0:静止, 1:足1, 2:足2
    private int animationCounter = 0; // コマを切り替えるためのタイマー代わり
    private int[] animSequence = {0, 1, 0, 2}; // 歩行パターンの順序
    private int animIndex = 0; // animSequence の添字

    // キーの状態管理フラグ
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    // スクリプト移動用の変数
    private boolean isScripted = false;
    private int[] scriptX;
    private int[] scriptY;
    private int scriptIndex = 0;

    // Viewのタイマーから定期的に呼ばれるメソッド
    public void updatePlayerPosition(MapModel mm) {
        // スクリプト移動中ならそちらを優先
        if (isScripted) {
            updateScriptedPosition(mm);
            return;
        }

        // 移動中かどうかを判定
        boolean isMoving = isUpPressed || isDownPressed || isLeftPressed || isRightPressed;

        if (isMoving) { // プレーヤーが動いているとき
            // フラグを見て座標を更新
            if (isUpPressed || isDownPressed) {
                if (isUpPressed) {
                    playerY -= ConstSet.PLAYER_SPEED;
                    playerDirection = ConstSet.UP;
                    while (isObstacleExist(mm)) { // 障害物と重ならなくなるまで座標を戻す.
                        playerY += 1;
                    }
                }
                if (isDownPressed) {
                    playerY += ConstSet.PLAYER_SPEED;
                    playerDirection = ConstSet.DOWN;
                    while (isObstacleExist(mm)) { // 障害物と重ならなくなるまで座標を戻す.
                        playerY -= 1;
                    }
                }
            } else if (isLeftPressed || isRightPressed) {
                if (isLeftPressed) {
                    playerX -= ConstSet.PLAYER_SPEED;
                    playerDirection = ConstSet.LEFT;
                    while (isObstacleExist(mm)) { // 障害物と重ならなくなるまで座標を戻す.
                        playerX += 1;
                    }
                }
                if (isRightPressed) {
                    playerX += ConstSet.PLAYER_SPEED;
                    playerDirection = ConstSet.RIGHT;
                    while (isObstacleExist(mm)) { // 障害物と重ならなくなるまで座標を戻す.
                        playerX -= 1;
                    }
                }
            }

            // アニメーション更新
            animationCounter++;
            if (animationCounter >= 10) { // 10回updateが呼ばれるごとに1コマ進める
                // 配列のインデックスを 0 -> 1 -> 2 -> 3 -> 0... と回す
                animIndex = (animIndex + 1) % animSequence.length;
                animationFrame = animSequence[animIndex];
                animationCounter = 0;
            }

        } else { // プレーヤーが止まっているとき
            animationFrame = 0;
            animIndex = 0;
            animationCounter = 0;
        }


        if (playerX < ConstSet.TILE_SIZE / 2)
            playerX = ConstSet.TILE_SIZE / 2;
        if (playerY < ConstSet.TILE_SIZE / 2)
            playerY = ConstSet.TILE_SIZE / 2;
        if (playerX > mm.getMap()[0].length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2)
            playerX = mm.getMap()[0].length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2;
        if (playerY > mm.getMap().length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2)
            playerY = mm.getMap().length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2;

    }

    // フラグをセットするメソッド
    public void setUp(boolean pressed) {
        this.isUpPressed = pressed;
    }

    public void setDown(boolean pressed) {
        this.isDownPressed = pressed;
    }

    public void setLeft(boolean pressed) {
        this.isLeftPressed = pressed;
    }

    public void setRight(boolean pressed) {
        this.isRightPressed = pressed;
    }

    // プレイヤーのX座標を取得する.
    public int getPlayerX() {
        return playerX;
    }

    // プレイヤーのY座標を取得する.
    public int getPlayerY() {
        return playerY;
    }

    // プレイヤーの向いている方向を取得する.
    public int getPlayerDirection() {
        return playerDirection;
    }

    // プレーヤーのアニメーションの番号を取得する.
    public int getAnimationFrame() {
        return animationFrame;
    }

    // プレイヤーのHPを取得するメソッド.
    public int getPlayerHP() {
        return playerHP;
    }

    // プレイヤーの最大HPを取得するメソッド.
    public int getMaxHP() {
        return maxHP;
    }

    // プレイヤーの座標を直接セットする.
    public void playerPositionSet(int tx, int ty) {
        playerX = tx;
        playerY = ty;
    }

    // プレイヤーと障害物が重なっていないか判定するメソッド.
    public boolean isObstacleExist(MapModel mm) {
        int halfSize = ConstSet.PLAYER_SIZE / 2;
        int[][] corners = {{playerX + halfSize - 1, playerY + halfSize - 1}, // 右下
                {playerX - halfSize, playerY - halfSize}, // 左上
                {playerX - halfSize, playerY + halfSize - 1}, // 左下
                {playerX + halfSize - 1, playerY - halfSize} // 右上
        };

        // いずれかの隅が障害物タイルに一致するかどうかを判定します.
        for (int[] point : corners) {
            int x = point[0];
            int y = point[1];
            int tile = mm.getTile(x, y);

            for (int obstacle : MapData.OBSTACLES) {
                if (tile == obstacle) {
                    return true;
                }
            }

            if (tile == MapData.HALF_CLEAR_OBSTACLE) {
                // 上半分は通行不可. タイルのY座標の開始位置を計算し、そこから半分の高さ未満なら衝突とみなす
                int tileTopY = (y / ConstSet.TILE_SIZE) * ConstSet.TILE_SIZE;
                if (y < tileTopY + ConstSet.TILE_SIZE / 2) {
                    return true;
                }
            }
        }
        return false;
    }

    // プレイヤーのHPを減少させる.
    public void decreaseHP(int damage) {
        this.playerHP -= damage;
        if (this.playerHP < 0) {
            this.playerHP = 0;
        }
    }

    /**
     * プレイヤーが死亡したかどうかを返します。
     * 
     * @return HPが0以下ならtrue
     */
    public boolean isDead() {
        return this.playerHP <= 0;
    }

    // ゲームリスタート時にプレイヤーのステータスを初期化するメソッド.
    public void resetStatus() {
        this.playerHP = maxHP; // HPを初期値に戻す
        // 他にリセットすべき値があればここに追加
    }

    // スクリプト移動を開始するメソッド.
    // 通過点(x, y)の配列を渡すと, 順番に移動する.
    public void startScriptedMovement(int[] x, int[] y) {
        this.scriptX = x;
        this.scriptY = y;
        this.scriptIndex = 0;
        this.isScripted = true;

        // 手動操作のフラグをクリア
        this.isUpPressed = false;
        this.isDownPressed = false;
        this.isLeftPressed = false;
        this.isRightPressed = false;
    }

    public boolean isScripted() {
        return isScripted;
    }

    // スクリプトによる移動処理を行うメソッド.
    private void updateScriptedPosition(MapModel mm) {
        if (scriptX == null || scriptIndex >= scriptX.length) {
            isScripted = false;
            // スクリプト移動が完了したことを示すために状態を更新します。
            if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION) {
                DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE;
            }
            return;
        }

        int targetX = scriptX[scriptIndex];
        int targetY = scriptY[scriptIndex];

        // 移動方向の決定 (簡易的)
        int dx = targetX - playerX;
        int dy = targetY - playerY;
        if (Math.abs(dx) > Math.abs(dy)) {
            playerDirection = (dx > 0) ? ConstSet.RIGHT : ConstSet.LEFT;
        } else if (dy != 0) {
            playerDirection = (dy > 0) ? ConstSet.DOWN : ConstSet.UP;
        }

        // 座標更新
        if (playerX < targetX)
            playerX = Math.min(playerX + ConstSet.PLAYER_SPEED, targetX);
        else if (playerX > targetX)
            playerX = Math.max(playerX - ConstSet.PLAYER_SPEED, targetX);

        if (playerY < targetY)
            playerY = Math.min(playerY + ConstSet.PLAYER_SPEED, targetY);
        else if (playerY > targetY)
            playerY = Math.max(playerY - ConstSet.PLAYER_SPEED, targetY);

        // 移動したか判定
        if (playerX != targetX || playerY != targetY) {
            // アニメーション更新
            animationCounter++;
            if (animationCounter >= 10) {
                animIndex = (animIndex + 1) % animSequence.length;
                animationFrame = animSequence[animIndex];
                animationCounter = 0;
            }
        } else {
            // 目標地点に到達したら次のポイントへ
            scriptIndex++;
            if (scriptIndex >= scriptX.length) {
                isScripted = false;
                animationFrame = 0; // 停止

                // スクリプト移動が完了したことを示すために状態を更新します。
                if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION) {
                    DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE;
                }
            }
        }

        if (mm.getPlayerTile() > 100) {
            isScripted = false;
            // スクリプト移動が完了したことを示すために状態を更新します。
            if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION) {
                DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE;
            }
        }
    }
}

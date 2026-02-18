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
    private int ammo = ConstSet.INITIAL_PLAYER_AMMO; // 初期弾数
    private int score = 10000; // 初期スコア

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

    private boolean inputEnabled = true;

    // --- 変更: 銃声タイマーを削除し、無敵タイマーを追加 ---
    private int invincibleTimer = 0; // ダメージ後の無敵時間用

    // Viewのタイマーから定期的に呼ばれるメソッド
    public void updatePlayerPosition(MapModel mm) {
        // --- 修正: 無敵タイマーを更新 ---
        if (invincibleTimer > 0) {
            invincibleTimer--;
        }

        // スクリプト移動中ならそちらを優先
        if (isScripted) {
            updateScriptedPosition(mm);
            return;
        }

        if (!inputEnabled) {
            animationFrame = 0;
            animIndex = 0;
            animationCounter = 0;
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
            if (animationCounter >= 8) { // 8回updateが呼ばれるごとに1コマ進める
                animIndex = (animIndex + 1) % animSequence.length;
                animationFrame = animSequence[animIndex];
                animationCounter = 0;
            }

        } else { // プレーヤーが止まっているとき
            animationFrame = 0;
            animIndex = 0;
            animationCounter = 0;
        }

        // 画面端の補正
        if (playerX < ConstSet.TILE_SIZE / 2)
            playerX = ConstSet.TILE_SIZE / 2;
        if (playerY < ConstSet.TILE_SIZE / 2)
            playerY = ConstSet.TILE_SIZE / 2;
        if (playerX > mm.getMap()[0].length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2)
            playerX = mm.getMap()[0].length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2;
        if (playerY > mm.getMap().length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2)
            playerY = mm.getMap().length * ConstSet.TILE_SIZE - ConstSet.TILE_SIZE / 2;

    }

    // --- 銃声(shootTimer)関連のメソッドは「概念消去」のため削除 ---

    // フラグをセットするメソッド
    public void setUp(boolean pressed) { this.isUpPressed = pressed; }
    public void setDown(boolean pressed) { this.isDownPressed = pressed; }
    public void setLeft(boolean pressed) { this.isLeftPressed = pressed; }
    public void setRight(boolean pressed) { this.isRightPressed = pressed; }
    public void setInputEnabled(boolean enabled) { this.inputEnabled = enabled; }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int getPlayerDirection() { return playerDirection; }
    public int getAnimationFrame() { return animationFrame; }
    public int getPlayerHP() { return playerHP; }
    public int getMaxHP() { return maxHP; }
    public int getAmmo() { return ammo; }
    public int getScore() { return score; }

    // --- 追加: 無敵状態かどうかを返すメソッド ---
    public boolean isInvincible() { return invincibleTimer > 0; }

    public void playerPositionSet(int tx, int ty) {
        playerX = tx;
        playerY = ty;
    }

    public boolean isObstacleExist(MapModel mm) {
        int halfSize = ConstSet.PLAYER_SIZE / 2;
        int[][] corners = {{playerX + halfSize - 1, playerY + halfSize - 1}, 
                           {playerX - halfSize, playerY - halfSize}, 
                           {playerX - halfSize, playerY + halfSize - 1}, 
                           {playerX + halfSize - 1, playerY - halfSize}};

        for (int[] point : corners) {
            int x = point[0];
            int y = point[1];
            int tile = mm.getTile(x, y);

            for (int obstacle : MapData.OBSTACLES) {
                if (tile == obstacle) return true;
            }

            if (tile == MapData.HALF_CLEAR_OBSTACLE || tile == MapData.WALL_UNIT || tile == MapData.WALL_UNIT_1F ||
                tile == MapData.ALARM || tile == MapData.BOX) {
                int tileTopY = (y / ConstSet.TILE_SIZE) * ConstSet.TILE_SIZE;
                if (y < tileTopY + ConstSet.TILE_SIZE / 2) return true;
            }
        }
        return false;
    }

    public void decreaseHP(int damage) {
        // --- 修正: 無敵時間中でないときだけダメージを受ける ---
        if (invincibleTimer <= 0) {
            this.playerHP -= damage;
            penaltyForDamage(damage); // ダメージを受けたらスコア減点
            if (this.playerHP < 0) this.playerHP = 0;
            this.invincibleTimer = 15; // 15フレーム(0.5秒)の無敵時間を設定
        }
    }

    // 弾薬を取得したときに弾数を増やす.
    public void addAmmo(int amount) { 
        // 現在の弾数 + 追加分 が MAX_PLAYER_AMMO を超えないようにする
        this.ammo = Math.min(this.ammo + amount, ConstSet.MAX_PLAYER_AMMO);
    }

    // 弾を撃てるか判定
    public boolean useAmmo() {
        if (this.ammo > 0) {
            this.ammo--;
            return true; // 発射許可
        }
        return false; // 弾切れ
    }

    // 発見されたときの減点
    public void penaltyForAlert() {
        score -= 1000;
        if (score < 0) score = 0; // スコアの最小は0
    }

    // ダメージを受けた時の減点
    public void penaltyForDamage(int damage) {
        score -= (damage * 100); // 1ダメージにつき100点減点
        if (score < 0) score = 0;
    }

    public void penaltyForKill() {
        this.score -= 500; // 1人につき500点減点
        if (this.score < 0) this.score = 0;
    }

    public boolean isDead() { return this.playerHP <= 0; }

    public void resetStatus() { 
        this.playerHP = maxHP; 
        this.ammo = ConstSet.INITIAL_PLAYER_AMMO;
        this.invincibleTimer = 0; // リセット時は無敵も解除
        this.score = 10000;
    }

    public void startScriptedMovement(int[] x, int[] y) {
        this.scriptX = x;
        this.scriptY = y;
        this.scriptIndex = 0;
        this.isScripted = true;
        this.isUpPressed = this.isDownPressed = this.isLeftPressed = this.isRightPressed = false;
    }

    public boolean isScripted() { return isScripted; }

    private void updateScriptedPosition(MapModel mm) {
        if (scriptX == null || scriptIndex >= scriptX.length) {
            isScripted = false;
            if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION) {
                DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE;
            }
            return;
        }

        int targetX = scriptX[scriptIndex];
        int targetY = scriptY[scriptIndex];

        int dx = targetX - playerX;
        int dy = targetY - playerY;
        if (Math.abs(dx) > Math.abs(dy)) {
            playerDirection = (dx > 0) ? ConstSet.RIGHT : ConstSet.LEFT;
        } else if (dy != 0) {
            playerDirection = (dy > 0) ? ConstSet.DOWN : ConstSet.UP;
        }

        if (playerX < targetX) playerX = Math.min(playerX + ConstSet.PLAYER_SPEED, targetX);
        else if (playerX > targetX) playerX = Math.max(playerX - ConstSet.PLAYER_SPEED, targetX);

        if (playerY < targetY) playerY = Math.min(playerY + ConstSet.PLAYER_SPEED, targetY);
        else if (playerY > targetY) playerY = Math.max(playerY - ConstSet.PLAYER_SPEED, targetY);

        if (playerX != targetX || playerY != targetY) {
            animationCounter++;
            if (animationCounter >= 10) {
                animIndex = (animIndex + 1) % animSequence.length;
                animationFrame = animSequence[animIndex];
                animationCounter = 0;
            }
        } else {
            scriptIndex++;
            if (scriptIndex >= scriptX.length) {
                isScripted = false;
                animationFrame = 0;
                if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION) {
                    DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE;
                }
            }
        }

        if (mm.getPlayerTile() > 100) {
            isScripted = false;
            if (DialogueSet.dialogueState == DialogueSet.DialogueState.AWAITING_SCRIPTED_MOVE_COMPLETION) {
                DialogueSet.dialogueState = DialogueSet.DialogueState.AFTER_SCRIPTED_MOVE;
            }
        }
    }
}
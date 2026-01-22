package model;

import java.util.*;
import GameConfig.*;

public class GuardsmanModel {
      private int guardsmanX;
      private int guardsmanY;
      private int guardsmancondition; // 敵の状態.
      private int guardsmandirection; // 敵が向いている方向.0123の順で右上左下.
      private int player_track = 0; // 1のときはプレイヤーを追尾.

      private int direction_timer = 0; // 方向を変える用のタイマー.

      // アニメーションに用いる変数
      private int animationTick = 0; // アニメーションの経過時間
      private int animationFrame = 0; // 0, 1, 2 のいずれか

      public GuardsmanModel(int startX, int startY) {
            this.guardsmanX = startX;
            this.guardsmanY = startY;
      }


      public int getguardsmanX() { // 敵のX座標を取得.
            return guardsmanX;
      }

      public int getguardsmanY() { // 敵のY座標を取得.
            return guardsmanY;
      }

      public int getguardsmanCondition() {// 敵の状態を取得.
            return guardsmancondition;
      }

      public int getguardsmanDirection() {// 敵の向きを取得.
            return guardsmandirection;
      }

      public void setguardsmanIndex(int x, int y) { // 敵のX座標とY座標を設定.
            this.guardsmanX = x;
            this.guardsmanY = y;
      }

      public void setEmenyCondition(int condition) {// 敵の状態を設定.
            this.guardsmancondition = condition;
      }

      public void setguardsmanDirection(int direction) {// 敵の向きを設定.
            this.guardsmandirection = direction;
      }

      // プレイヤーを追尾するかどうかの変数の値を書き換える.
      public void changePlayerTrack(int state) {
            this.player_track = state;
      }

      public int getPlayerTrack() {
            return this.player_track;
      }

      private void moveTowardsPlayer(MapModel mm, PlayerModel pm) {
            int startX = this.guardsmanX / ConstSet.TILE_SIZE;
            int startY = this.guardsmanY / ConstSet.TILE_SIZE;
            int targetX = pm.getPlayerX() / ConstSet.TILE_SIZE;
            int targetY = pm.getPlayerY() / ConstSet.TILE_SIZE;

            int nextTile = getNextStep(startX, startY, targetX, targetY, mm.getMap());

            if (nextTile != -1) {
                  int w = mm.getMap()[0].length;
                  int nextX = nextTile % w;
                  int nextY = nextTile / w;

                  int targetPixelX = nextX * ConstSet.TILE_SIZE + ConstSet.TILE_SIZE / 2;
                  int targetPixelY = nextY * ConstSet.TILE_SIZE + ConstSet.TILE_SIZE / 2;

                  if (guardsmanX < targetPixelX) {
                        guardsmanX = Math.min(guardsmanX + ConstSet.GUARDSMAN_SPEED, targetPixelX);
                        setguardsmanDirection(ConstSet.RIGHT);
                  } else if (guardsmanX > targetPixelX) {
                        guardsmanX = Math.max(guardsmanX - ConstSet.GUARDSMAN_SPEED, targetPixelX);
                        setguardsmanDirection(ConstSet.LEFT);
                  } else if (guardsmanY < targetPixelY) {
                        guardsmanY = Math.min(guardsmanY + ConstSet.GUARDSMAN_SPEED, targetPixelY);
                        setguardsmanDirection(ConstSet.DOWN);
                  } else if (guardsmanY > targetPixelY) {
                        guardsmanY = Math.max(guardsmanY - ConstSet.GUARDSMAN_SPEED, targetPixelY);
                        setguardsmanDirection(ConstSet.UP);
                  }
            }
      }

      private int getNextStep(int startX, int startY, int targetX, int targetY, int[][] map) {
            int h = map.length;
            int w = map[0].length;
            int start = startY * w + startX;
            int target = targetY * w + targetX;

            if (start == target)
                  return start;

            Queue<Integer> queue = new LinkedList<>();
            queue.add(start);
            Map<Integer, Integer> cameFrom = new HashMap<>();
            cameFrom.put(start, null);

            int[] dx = {0, 0, -1, 1};
            int[] dy = {-1, 1, 0, 0};

            while (!queue.isEmpty()) {
                  int curr = queue.poll();
                  if (curr == target)
                        break;

                  int cx = curr % w;
                  int cy = curr / w;

                  for (int i = 0; i < 4; i++) {
                        int nx = cx + dx[i];
                        int ny = cy + dy[i];
                        if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                              int next = ny * w + nx;
                              if (!cameFrom.containsKey(next)) {
                                    boolean isObstacle = false;
                                    for (int obs : MapData.OBSTACLES) {
                                          if (map[ny][nx] == obs) {
                                                isObstacle = true;
                                                break;
                                          }
                                    }
                                    if (!isObstacle) {
                                          queue.add(next);
                                          cameFrom.put(next, curr);
                                    }
                              }
                        }
                  }
            }
            if (!cameFrom.containsKey(target))
                  return -1;
            int curr = target;
            while (cameFrom.get(curr) != null && cameFrom.get(curr) != start) {
                  curr = cameFrom.get(curr);
            }
            return curr;
      }

      public int getAnimationFrame() { // ゲッター
            return animationFrame;
      }

      private void updateAnimation() {
            animationTick++;
            // 10フレームごとに1コマ進める
            if (animationTick % 10 == 0) {
                  animationFrame = (animationFrame + 1) % 3; // 0, 1, 2 を繰り返す
            }
      }

      public void updateGuardsmanPosition(MapModel mm, PlayerModel pm, BulletsModel bm) {
            if (player_track == 1) {
                  moveTowardsPlayer(mm, pm);
                  updateAnimation();
            } else {
                  checkPlayerInSight(pm);
                  if (player_track == 0) {
                        if (direction_timer++ == 30) {
                              guardsmandirection = (guardsmandirection + 1) % 4;
                              direction_timer = 0;
                        }
                  }
            }
      }

      private void checkPlayerInSight(PlayerModel pm) {
            int playerX = pm.getPlayerX();
            int playerY = pm.getPlayerY();
            int sightDistance = ConstSet.TILE_SIZE * 10;

            boolean isFound = false;

            switch (guardsmandirection) {
                  case ConstSet.RIGHT:
                        if (playerX > guardsmanX
                                    && Math.abs(playerY - guardsmanY) < ConstSet.SIGHTRANGE
                                    && playerX - guardsmanX < sightDistance) {
                              isFound = true;
                        }
                        break;
                  case ConstSet.UP:
                        if (playerY < guardsmanY
                                    && Math.abs(playerX - guardsmanX) < ConstSet.SIGHTRANGE
                                    && guardsmanY - playerY < sightDistance) {
                              isFound = true;
                        }
                        break;
                  case ConstSet.LEFT:
                        if (playerX < guardsmanX
                                    && Math.abs(playerY - guardsmanY) < ConstSet.SIGHTRANGE
                                    && guardsmanX - playerX < sightDistance) {
                              isFound = true;
                        }
                        break;
                  case ConstSet.DOWN:
                        if (playerY > guardsmanY
                                    && Math.abs(playerX - guardsmanX) < ConstSet.SIGHTRANGE
                                    && playerY - guardsmanY < sightDistance) {
                              isFound = true;
                        }
                        break;
            }

            if (isFound) {
                  player_track = 1;
            }
      }

}

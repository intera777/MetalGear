package view;

import GameConfig.*;
import model.*;

import java.awt.Graphics;
import java.awt.color.*;

public class MapView {
    private MapModel mapModel;
    private PlayerModel playerModel;

    public MapView(MapModel mm, PlayerModel pm) {
        this.mapModel = mm;
        this.playerModel = pm;
    }

    public void drawMap(Graphics g) {
        // 現在のマップデータを取得
        int[][] map = mapModel.getMap();

        // プレイヤーのピクセルにおける座標
        int playerPixX = playerModel.getPlayerX();
        int playerPixY = playerModel.getPlayerY();

        // 画面中央からプレイヤーの位置を引いて、マップの描画開始点を決める
        // プレイヤー位置をウインドウの真ん中に固定するため、「背景をどれだけずらすか」という値が offset
        int offsetX = (ConstSet.WINDOW_WIDTH / 2) - playerPixX;
        int offsetY = (ConstSet.WINDOW_HEIGHT / 2) - playerPixY;

        // 二次元配列をループで回して描画. 二次元配列の座標を(x, y)とする.
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                // タイルの種類を取得
                int tileType = map[y][x];

                // 描画位置の計算 (絶対座標にoffsetを加算することで, プレイヤー中心の画面が描画できる)
                int drawX = x * ConstSet.TILE_SIZE + offsetX;
                int drawY = y * ConstSet.TILE_SIZE + offsetY;

                // 画面外のタイルは描画しない
                if (drawX + ConstSet.TILE_SIZE < 0 || drawX > ConstSet.WINDOW_WIDTH
                        || drawY + ConstSet.TILE_SIZE < 0 || drawY > ConstSet.WINDOW_HEIGHT) {
                    continue;
                }

                // タイルの描画
                if (tileType == MapData.STONEFLOOR) {
                    // 床の画像を描画
                    g.setColor(java.awt.Color.DARK_GRAY);
                    g.fillRect(drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE);
                } else if (tileType == MapData.STONEWALL) {
                    // 壁の画像を描画
                    g.setColor(java.awt.Color.LIGHT_GRAY);
                    g.fillRect(drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE);
                } else if (tileType > 100) {
                    // 遷移ポイント（101以上）の描画
                    g.setColor(java.awt.Color.CYAN);
                    g.fillRect(drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE);
                }
            }
        }
    }
}

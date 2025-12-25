package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MapView {
    private MapModel mapModel;
    private PlayerModel playerModel;

    // 画像保持用の変数
    private Image floorImage;
    private Image wallImage;
    private Image bedImage;
    private Image verticalStairImage;
    private Image portalImage;

    public MapView(MapModel mm, PlayerModel pm) {
        this.mapModel = mm;
        this.playerModel = pm;
        loadImages(); // コンストラクタ内で画像読み取り
    }

    private void loadImages() {
        try {
            floorImage = ImageIO.read(new File(ConstSet.IMG_PATH_FLOOR));
            wallImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL));
            bedImage = ImageIO.read(new File(ConstSet.IMG_PATH_BED));
            verticalStairImage = ImageIO.read(new File(ConstSet.IMG_PATH_VERTICAL_STAIR));
            
            // 遷移ポイントの画像は未定
            // portalImage = ImageIO.read(new File(ConstSet.IMG_PATH_PORTAL));

        } catch (IOException e) {
            // 読み込み失敗時のデバック用
            System.err.println("読み込み失敗");
            System.err.println("探した場所: " + new File(ConstSet.IMG_PATH_FLOOR).getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void drawMap(Graphics g, int offsetX, int offsetY) {
        // 現在のマップデータを取得
        int[][] map = mapModel.getMap();

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

                // タイルの種類に応じて画像を描画
                Image imgToDraw = null;

                // タイルの描画
                if (tileType == MapData.STONEFLOOR) {
                    // 床の画像を描画
                    imgToDraw = floorImage;
                } else if (tileType == MapData.STONEWALL) {
                    // 壁の画像を描画
                    imgToDraw = wallImage;
                } else if (tileType == MapData.BED) {
                    imgToDraw = bedImage;
                } else if (tileType == MapData.VERTIVAL_STAIR) {
                    imgToDraw = verticalStairImage;
                } else if (tileType > 100) {
                    // 遷移ポイント（101以上）の描画
                    // 遷移ポイントの画像は未定
                    g.setColor(Color.CYAN);
                    g.fillRect(drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE);
                }

                // 画像が存在すれば描画
                // ベッドサイズが異なるため, サイズ調整のための分岐
                if (imgToDraw != null && imgToDraw != bedImage) {
                    g.drawImage(imgToDraw, drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE, null);
                } else if (imgToDraw == bedImage) { // 2タイル×1タイル
                    g.drawImage(imgToDraw, drawX, drawY, ConstSet.TILE_SIZE, 2 * ConstSet.TILE_SIZE, null);
                }

            }
        }
    }
}

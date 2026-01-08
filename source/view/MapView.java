package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import javax.imageio.ImageIO;

public class MapView {
    private MapModel mapModel;
    private PlayerModel playerModel;

    // MobViewのインスタンスを作成
    private MobView mobView = new MobView();

    // 画像保持用の変数
    private Image floorImage;
    private Image wallTopNorthImage;
    private Image wallNorthImage;
    private Image wallTopSouthImage;
    private Image wallTopEastImage;
    private Image wallTopWestImage;
    private Image bedImage;
    private Image verticalStairImage;
    private Image cornerNorthWestImage;
    private Image cornerNorthEastImage;
    private Image cornerSouthEastImage;
    private Image cornerSouthWestimage;
    private Image container1T2Image;
    private Image container2T2Image;

    public MapView(MapModel mm, PlayerModel pm) {
        this.mapModel = mm;
        this.playerModel = pm;
        loadImages(); // コンストラクタ内で画像読み取り
    }

    private void loadImages() {
        try {
            floorImage = ImageIO.read(new File(ConstSet.IMG_PATH_FLOOR));
            wallTopNorthImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_TOP_NORTH));
            wallNorthImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_NORTH));
            wallTopSouthImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_TOP_SOUTH));
            wallTopEastImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_TOP_EAST));
            wallTopWestImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_TOP_WEST));
            cornerNorthWestImage = ImageIO.read(new File(ConstSet.IMG_PATH_CORNER_NORTH_WEST));
            cornerNorthEastImage = ImageIO.read(new File(ConstSet.IMG_PATH_CORNER_NORTH_EAST));
            cornerSouthEastImage = ImageIO.read(new File(ConstSet.IMG_PATH_CORNER_SOUTH_EAST));
            cornerSouthWestimage = ImageIO.read(new File(ConstSet.IMG_PATH_CORNER_SOUTH_WEST));
            bedImage = ImageIO.read(new File(ConstSet.IMG_PATH_BED));
            verticalStairImage = ImageIO.read(new File(ConstSet.IMG_PATH_VERTICAL_STAIR));
            container1T2Image = ImageIO.read(new File(ConstSet.IMG_PATH_CONTAINER1T2));
            container2T2Image = ImageIO.read(new File(ConstSet.IMG_PATH_CONTAINER2T2));

        } catch (IOException e) {
            // 読み込み失敗時のデバック用
            System.err.println("読み込み失敗");
            System.err.println("探した場所: " + new File(ConstSet.IMG_PATH_FLOOR).getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void drawMap(Graphics g, int offsetX, int offsetY, ImageObserver observer) {
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

                // 画面外のタイルは描画しない. 正確には, 画面外から4マス分外れたところまで描画している
                int buffer = ConstSet.TILE_SIZE;
                if (drawX + ConstSet.TILE_SIZE < - 4 * buffer || drawX > ConstSet.WINDOW_WIDTH + 4 * buffer
                        || drawY + ConstSet.TILE_SIZE < -4 * buffer || drawY > ConstSet.WINDOW_HEIGHT + 4 * buffer) {
                    continue;
                }

                // タイルの種類に応じて画像を描画
                Image imgToDraw = null;

                // タイルの描画
                if (tileType == MapData.STONEFLOOR) { // 床
                    imgToDraw = floorImage;
                } else if (tileType == MapData.WALL_TOP_NORTH) { // 壁
                    imgToDraw = wallTopNorthImage;
                } else if (tileType == MapData.WALL_NORTH) {
                    imgToDraw = wallNorthImage;
                } else if (tileType == MapData.WALL_TOP_SOUTH) {
                    imgToDraw = wallTopSouthImage;
                } else if (tileType == MapData.WALL_TOP_EAST) {
                    imgToDraw = wallTopEastImage;
                } else if (tileType == MapData.WALL_TOP_WEST) {
                    imgToDraw = wallTopWestImage;
                } else if (tileType == MapData.CORNER_NORTH_WEST) { // 壁の角
                    imgToDraw = cornerNorthWestImage;
                } else if (tileType == MapData.CORNER_NORTH_EAST) {
                    imgToDraw = cornerNorthEastImage;
                } else if (tileType == MapData.CORNER_SOUTH_EAST) {
                    imgToDraw = cornerSouthEastImage;
                } else if (tileType == MapData.CORNER_SOUTH_WEST) {
                    imgToDraw = cornerSouthWestimage;
                } else if (tileType > 100) { // 遷移点 → 何も描画しない
                    imgToDraw = null;
                } else if (tileType == MapData.BED) { // オブジェクト
                    imgToDraw = bedImage;
                } else if (tileType == MapData.VERTICAL_STAIR) {
                    imgToDraw = verticalStairImage;
                } else if (tileType == MapData.CONTAINER_1T2) {
                    imgToDraw = container1T2Image;
                } else if (tileType == MapData.CONTAINER_2T2) {
                    imgToDraw = container2T2Image;
                } else if (tileType == MapData.SPIN_MOB) {
                    int mobSize = ConstSet.TILE_SIZE * 4;
                    mobView.drawMob(g, drawX, drawY, mobSize, mobSize, observer);
                    continue; // Mobは既に描画しているので, 以下の描画処理はスキップ                
                }

                // 画像が存在すれば描画
                // ベッドサイズが異なるため, サイズ調整のための分岐
                if (imgToDraw != null && imgToDraw != wallNorthImage && imgToDraw != bedImage && imgToDraw != verticalStairImage &&
                     imgToDraw != container1T2Image && imgToDraw != container2T2Image) { // 1×1マス
                    g.drawImage(imgToDraw, drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE, null);
                } else if (imgToDraw == wallNorthImage) { // 2×2マス
                    g.drawImage(imgToDraw, drawX, drawY, 2 * ConstSet.TILE_SIZE, 2 * ConstSet.TILE_SIZE, null);
                } else if (imgToDraw == bedImage) { // 1×2マス
                    g.drawImage(imgToDraw, drawX, drawY, ConstSet.TILE_SIZE, 2 * ConstSet.TILE_SIZE, null);
                } else if (imgToDraw == verticalStairImage) { // 3×6マス
                    g.drawImage(imgToDraw, drawX, drawY, 3 * ConstSet.TILE_SIZE, 6 * ConstSet.TILE_SIZE, null);
                } else if (imgToDraw == container1T2Image) { // 1×2マス
                    g.drawImage(imgToDraw, drawX, drawY, ConstSet.TILE_SIZE, 2 * ConstSet.TILE_SIZE, null);
                } else if (imgToDraw == container2T2Image) { // 2×2マス
                    g.drawImage(imgToDraw, drawX, drawY, 2 * ConstSet.TILE_SIZE, 2 * ConstSet.TILE_SIZE, null);
                }

            }
        }
    }
}

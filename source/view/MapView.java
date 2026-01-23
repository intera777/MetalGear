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

    private MobView mobView = new MobView();

    // 画像保持用の変数
    private Image floorImage;
    private Image wallUnitImage;
    private Image wallUpImage;
    private Image bedImage;
    private Image tableLeftImage;
    private Image tableMiddleImage;
    private Image tableRightImage;
    private Image verticalStairImage;
    private Image stairGoDownImage;
    private Image container1T2Image;
    private Image container2T2Image;
    private Image largeDoorImage;
    private Image prisonDoorOpenedImage;
    private Image prisonDoorClosedImage;
    private Image alarmImage;
    private Image shelfImage;
    private Image bigshelfImage;
    private Image counterImage;
    private Image elevatorImage; // スペル修正
    private Image floor1FImage;
    private Image wallUnit1FImage;
    private Image wallUp1FImage;
    private Image plantImage;
    private Image boxImage;
    private Image pictureImage;
    private Image booksImage;

    public MapView(MapModel mm, PlayerModel pm) {
        this.mapModel = mm;
        this.playerModel = pm;
        loadImages();
    }

    private void loadImages() {
        try {
            // 基本タイル
            floorImage = ImageIO.read(new File(ConstSet.IMG_PATH_FLOOR));
            wallUnitImage = ImageIO.read(new File(ConstSet.IMG_PATH__WALL_UNIT));
            wallUpImage = ImageIO.read(new File(ConstSet.IMG_PATH__WALL_UP));

            // オブジェクト
            bedImage = ImageIO.read(new File(ConstSet.IMG_PATH_BED));
            tableLeftImage = ImageIO.read(new File(ConstSet.IMG_PATH_TABLE_LEFT));
            tableMiddleImage = ImageIO.read(new File(ConstSet.IMG_PATH_TABLE_MIDDLE));
            tableRightImage = ImageIO.read(new File(ConstSet.IMG_PATH_TABLE_RIGHT));
            verticalStairImage = ImageIO.read(new File(ConstSet.IMG_PATH_VERTICAL_STAIR));
            stairGoDownImage = ImageIO.read(new File(ConstSet.IMG_PATH_STAIR_GODOWN));
            container1T2Image = ImageIO.read(new File(ConstSet.IMG_PATH_CONTAINER1T2));
            container2T2Image = ImageIO.read(new File(ConstSet.IMG_PATH_CONTAINER2T2));
            largeDoorImage = ImageIO.read(new File(ConstSet.IMG_PATH_LARGEDOOR));
            prisonDoorOpenedImage = ImageIO.read(new File(ConstSet.IMG_PATH_PRISONDOOR_OPENED));
            prisonDoorClosedImage = ImageIO.read(new File(ConstSet.IMG_PATH_PRISONDOOR_CLOSED));
            
            // 1F・追加オブジェクト
            alarmImage = ImageIO.read(new File(ConstSet.IMG_PATH_ALARM));
            shelfImage = ImageIO.read(new File(ConstSet.IMG_PATH_SHELF));
            bigshelfImage = ImageIO.read(new File(ConstSet.IMG_PATH_BIGSHELF));
            counterImage = ImageIO.read(new File(ConstSet.IMG_PATH_COUNTER));
            elevatorImage = ImageIO.read(new File(ConstSet.IMG_PATH_ELAVATOR)); // 変数名はelavatorImageでもOK
            floor1FImage = ImageIO.read(new File(ConstSet.IMG_PATH_FLOOR1F));
            wallUnit1FImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_UNIT_1F));
            wallUp1FImage = ImageIO.read(new File(ConstSet.IMG_PATH_WALL_UP_1F));
            plantImage = ImageIO.read(new File(ConstSet.IMG_PATH_PLANT));
            boxImage = ImageIO.read(new File(ConstSet.IMG_PATH_BOX));
            pictureImage = ImageIO.read(new File(ConstSet.IMG_PATH_PICTURE));
            booksImage = ImageIO.read(new File(ConstSet.IMG_PATH_BOOKS));

        } catch (IOException e) {
            System.err.println("読み込み失敗");
            System.err.println("探した場所: " + new File(ConstSet.IMG_PATH_FLOOR).getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void drawMap(Graphics g, int offsetX, int offsetY, ImageObserver observer) {
        int[][] map = mapModel.getMap();
        int buffer = ConstSet.TILE_SIZE;

        // 背景一括描画（1F床の出し分け対応）
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                int tileType = map[y][x];
                if (tileType == MapData.CLEAR_OBSTACLE) continue;

                int drawX = x * ConstSet.TILE_SIZE + offsetX;
                int drawY = y * ConstSet.TILE_SIZE + offsetY;

                if (drawX + ConstSet.TILE_SIZE < - 6 * buffer || drawX > ConstSet.WINDOW_WIDTH + 6 * buffer ||
                    drawY + ConstSet.TILE_SIZE < -6 * buffer || drawY > ConstSet.WINDOW_HEIGHT + 6 * buffer) continue;

                Image bg = (tileType == MapData.FLOOR_1F || tileType == MapData.WALL_UNIT_1F) ? floor1FImage : floorImage;
                g.drawImage(bg, drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE, null);
            }
        }

        // オブジェクト描画
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                int tileType = map[y][x];
                int drawX = x * ConstSet.TILE_SIZE + offsetX;
                int drawY = y * ConstSet.TILE_SIZE + offsetY;

                if (drawX + ConstSet.TILE_SIZE < - 6 * buffer || drawX > ConstSet.WINDOW_WIDTH + 6 * buffer ||
                    drawY + ConstSet.TILE_SIZE < -6 * buffer || drawY > ConstSet.WINDOW_HEIGHT + 6 * buffer) continue;

                Image imgToDraw = null;

                // --- 画像特定セクション ---
                if (tileType == MapData.WALL_UNIT) imgToDraw = wallUnitImage;
                else if (tileType == MapData.WALL_UP) imgToDraw = wallUpImage;
                else if (tileType == MapData.WALL_UNIT_1F) imgToDraw = wallUnit1FImage;
                else if (tileType == MapData.BED) imgToDraw = bedImage;
                else if (tileType == MapData.VERTICAL_STAIR) imgToDraw = verticalStairImage;
                else if (tileType == MapData.STAIRS_GODOWN) imgToDraw = stairGoDownImage;
                else if (tileType == MapData.CONTAINER_1T2) imgToDraw = container1T2Image;
                else if (tileType == MapData.CONTAINER_2T2) imgToDraw = container2T2Image;
                else if (tileType == MapData.LARGE_DOOR) imgToDraw = largeDoorImage;
                else if (tileType == MapData.PRISONDOOR_OPENED) imgToDraw = prisonDoorOpenedImage;
                else if (tileType == MapData.PRISONDOOR_CLOSED) imgToDraw = prisonDoorClosedImage;
                else if (tileType == MapData.ALARM) imgToDraw = alarmImage;
                else if (tileType == MapData.SHELF) imgToDraw = shelfImage;
                else if (tileType == MapData.BIGSHELF) imgToDraw = bigshelfImage;
                else if (tileType == MapData.COUNTER) imgToDraw = counterImage;
                else if (tileType == MapData.ELAVATOR) imgToDraw = elevatorImage;
                else if (tileType == MapData.TABLE_LEFT) imgToDraw = tableLeftImage;
                else if (tileType == MapData.TABLE_MIDDLE) imgToDraw = tableMiddleImage;
                else if (tileType == MapData.TABLE_RIGHT) imgToDraw = tableRightImage;
                else if  (tileType == MapData.WALL_UP_1F) imgToDraw = wallUp1FImage;
                else if (tileType == MapData.PLANT) imgToDraw = plantImage;
                else if (tileType == MapData.BOX) imgToDraw = boxImage;
                else if (tileType == MapData.PICTURE) imgToDraw = pictureImage;
                else if (tileType == MapData.BOOKS) imgToDraw = booksImage;

                else if (tileType == MapData.SPIN_MOB) {
                    int mobSize = ConstSet.TILE_SIZE * 4;
                    mobView.drawMob(g, drawX, drawY, mobSize, mobSize, observer);
                    continue;
                }

                if (imgToDraw == null) continue;

                // --- サイズ調整セクション ---
                int tw = ConstSet.TILE_SIZE;
                
                if (tileType == MapData.LARGE_DOOR) { // 5x2
                    g.drawImage(imgToDraw, drawX, drawY, 5 * tw, 2 * tw, null);
                } else if (tileType == MapData.VERTICAL_STAIR) { // 3x6
                    g.drawImage(imgToDraw, drawX, drawY, 3 * tw, 6 * tw, null);
                } else if (tileType == MapData.STAIRS_GODOWN || tileType == MapData.BIGSHELF || 
                           tileType == MapData.COUNTER || tileType == MapData.ELAVATOR) { // 3x2
                    g.drawImage(imgToDraw, drawX, drawY, 3 * tw, 2 * tw, null);
                } else if (tileType == MapData.CONTAINER_2T2 || tileType == MapData.PRISONDOOR_OPENED || 
                           tileType == MapData.PRISONDOOR_CLOSED || tileType == MapData.SHELF) { // 2x2
                    g.drawImage(imgToDraw, drawX, drawY, 2 * tw, 2 * tw, null);
                } else if (tileType == MapData.BED || tileType == MapData.CONTAINER_1T2 || tileType == MapData.PLANT) { // 1x2
                    g.drawImage(imgToDraw, drawX, drawY, tw, 2 * tw, null);
                } else if (tileType == MapData.PICTURE) { // 2x1
                    g.drawImage(imgToDraw, drawX, drawY, 2 * tw, tw, null);
                } else if (tileType == MapData.BOOKS) { // 3x4
                    g.drawImage(imgToDraw, drawX, drawY, 3 * tw, 4 * tw, null);
                } else { // 1x1
                    g.drawImage(imgToDraw, drawX, drawY, tw, tw, null);
                }
            }
        }
    }
}
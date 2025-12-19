import control.*;
import model.*;
import view.*;
import GameConfig.*;

import javax.swing.*;


public class Metalgear extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("MetalGear");
        frame.setSize(ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Playerクラス関連のオブジェクトの生成.
        PlayerModel playermodel = new PlayerModel();
        PlayerControl playercontrol = new PlayerControl(playermodel);
        PlayerView playerview = new PlayerView(playermodel);

        // Bulletクラス関連のオブジェクトの生成.
        BulletsModel bulletsmodel = new BulletsModel(playermodel);
        BulletControl bulletcontrol = new BulletControl(bulletsmodel);
        BulletView bulletview = new BulletView(bulletsmodel);

        // Mapクラス関連のオブジェクトを生成.
        MapModel mapmodel = new MapModel(playermodel);
        MapView mapview = new MapView(mapmodel, playermodel);

        GameView gameview = new GameView(playermodel, mapview, playerview, bulletview,
                playercontrol, bulletcontrol);
        frame.add(gameview);

        frame.setVisible(true);

        final int FPS = 30; // フレームレート.

        playermodel.playerPositionSet(4 * ConstSet.TILE_SIZE, 4 * ConstSet.TILE_SIZE);
        mapmodel.setCurrentMap(MapData.MAPA0);
        // ゲームループ本体.
        while (true) {
            playermodel.updatePlayerPosition();
            bulletsmodel.updateBulletsPosition();
            mapmodel.updateMap(playermodel);
            gameview.repaint();
            try {
                // 約0.033秒停止.
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                // 停止中に割り込まれた時の処理.
                e.printStackTrace();
            }
        }

    }
}

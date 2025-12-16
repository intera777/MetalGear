import control.*;
import model.*;
import view.*;
import GameConfig.ConstSet;

import javax.swing.*;


public class Metalgear extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("MetalGear");
        frame.setSize(ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // レイアウトマネージャの無効化.
        JLayeredPane layeredPane = new JLayeredPane();
        frame.setContentPane(layeredPane);

        // Playerクラス関連のオブジェクトの生成.
        PlayerModel playermodel = new PlayerModel();
        PlayerControl playercontrol = new PlayerControl(playermodel);
        PlayerView playerview = new PlayerView(playermodel, playercontrol);

        // Bulletクラス関連のオブジェクトの生成.
        BulletsModel bulletsmodel = new BulletsModel(playermodel);
        BulletControl bulletcontrol = new BulletControl(bulletsmodel);
        BulletView bulletview = new BulletView(bulletsmodel.getBullets(), bulletcontrol);

        playerview.setBounds(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);
        bulletview.setBounds(0, 0, ConstSet.WINDOW_WIDTH, ConstSet.WINDOW_HEIGHT);

        playerview.setOpaque(true);
        bulletview.setOpaque(false);// playerviewの上に重ねるので透明にする.

        layeredPane.add(playerview, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(bulletview, JLayeredPane.PALETTE_LAYER);

        frame.addKeyListener(bulletcontrol);
        frame.addKeyListener(playercontrol);

        frame.setVisible(true);
        frame.requestFocusInWindow();

        final int FPS = 30; // フレームレート.

        // ゲームループ本体.
        while (true) {
            playermodel.updatePlayerPosition();
            bulletsmodel.updateBulletsPosition();
            frame.repaint();
            playerview.repaint();
            bulletview.repaint();
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

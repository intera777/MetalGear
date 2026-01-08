package view;

import GameConfig.*;

import java.awt.*;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class MobView {
    private static final Image SPIN_MOB_GIF = new ImageIcon(ConstSet.IMG_PATH_SPIN_MOB).getImage();

    public MobView() {}

    // GameView が コマ変化を認識するために observer を引数に取る
    public void drawMob(Graphics g, int x, int y, int width, int height, ImageObserver observer) {
        g.drawImage(SPIN_MOB_GIF, x, y, width, height, observer);
    }
}
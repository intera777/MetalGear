package view;

import model.PlayerModel;
import control.PlayerControl;

import javax.swing.JPanel;
import javax.swing.Timer; // タイマー用
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.Observable;

public class PlayerView extends JPanel implements Observer {
    private PlayerModel model;
    private Timer timer;

    public PlayerView(PlayerModel m, PlayerControl c) {
        this.model = m;
        model.addObserver(this);
        
        this.addKeyListener(c); 
        this.setFocusable(true);

        // タイマーの設定（約30FPS = 33ミリ秒ごとに更新）
        // Timerのイベントで model.updatePosition() を呼び出す
        timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updatePosition();
            }
        });
        timer.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint(); // 再描画
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Modelからデータを取得して描画
        g.fillRect(model.getPlayerX(), model.getPlayerY(), 30, 30);
    }
}
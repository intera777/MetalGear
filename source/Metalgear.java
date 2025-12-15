import control.PlayerControl;
import model.PlayerModel;
import view.PlayerView;

import javax.swing.*;

public class Metalgear extends JFrame{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("MetalGear");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // 1. まずModelを作る
        PlayerModel playermodel = new PlayerModel();
        
        // 2. 次にControllerを作る（Modelを操作するため渡す
        PlayerControl playercontrol = new PlayerControl(playermodel);
        // 3. 最後にViewを作る（Modelを表示し、Controllerに入力を流すため渡す)
        PlayerView playerview = new PlayerView(playermodel, playercontrol);
        
        frame.add(playerview);
        frame.setVisible(true);

        final int FPS = 30; //フレームレート.

        while(true){
            playermodel.updatePlayerPosition();
            playerview.repaint();
            try {
                //約0.033秒停止.
                Thread.sleep(1000/30);
            } catch (InterruptedException e) {
                //停止中に割り込まれた時の処理.
                e.printStackTrace();
            }
        }

    }
}
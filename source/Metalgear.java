import control.PlayerControl;
import model.PlayerModel;
import view.PlayerView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Metalgear extends JFrame{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("MetalGear");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // 1. まずModelを作る
        PlayerModel model = new PlayerModel();
        
        // 2. 次にControllerを作る（Modelを操作するため渡す
        PlayerControl control = new PlayerControl(model);
        // 3. 最後にViewを作る（Modelを表示し、Controllerに入力を流すため渡す)
        PlayerView view = new PlayerView(model, control);
        
        frame.add(view);


        frame.setVisible(true);
    }
}
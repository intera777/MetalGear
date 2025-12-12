package control;

import model.PlayerModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BulletControl implements KeyListener{
      private BulletModel model;

      public BulletControl(BulletModel model){
            this.model=model;
      }

      @Override
      public void KeyTyped(KeyEvent e){}

      @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code)
        
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
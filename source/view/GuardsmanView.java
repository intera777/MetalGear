package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GuardsmanView {
    private GuardsmenModel guardsmenModel;
    private BufferedImage[][] sprites; // スプライトを保持する変数

    public GuardsmanView(GuardsmenModel gmm) {
        this.guardsmenModel = gmm;
        loadGuardsmanSprites();
    }

    
    // 敵のスプライトを分割して読み込む
    private void loadGuardsmanSprites() {
        try {
            BufferedImage sheet = ImageIO.read(new File(ConstSet.IMG_PATH_GUARDSMAN));
            
            // 4行（右・上・左・下）× 3コマで初期化
            sprites = new BufferedImage[4][3];

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 3; col++) {
                    sprites[row][col] = sheet.getSubimage(
                        col * ConstSet.ENEMY_SIZE,
                        row * ConstSet.ENEMY_SIZE,
                        ConstSet.ENEMY_SIZE,
                        ConstSet.ENEMY_SIZE
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("敵のスプライトの読み込みに失敗しました。");
            e.printStackTrace();
        }
    }

    public void drawGuardsmen(Graphics g, int offsetX, int offsetY) {
        for (GuardsmanModel guardsman : guardsmenModel.getguardsmen()) {
            if (guardsman == null) continue;
            renderGuardsman(g, guardsman, offsetX, offsetY);
        }
    }

    public void renderGuardsman(Graphics g, GuardsmanModel guardsman, int offsetX, int offsetY) {
        int drawX = guardsman.getguardsmanX() + offsetX - ConstSet.ENEMY_SIZE / 2;
        int drawY = guardsman.getguardsmanY() + offsetY - ConstSet.ENEMY_SIZE / 2;
        
        int row = guardsman.getguardsmanDirection(); // 0:右, 1:上, 2:左, 3:下
        int col = guardsman.getAnimationFrame(); // 0, 1, 2

        // 画像の描画
        g.drawImage(sprites[row][col], drawX, drawY, ConstSet.ENEMY_SIZE, ConstSet.ENEMY_SIZE, null);
    }
}

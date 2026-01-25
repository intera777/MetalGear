package view;

import GameConfig.*;
import model.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ItemView {
    private ItemsModel itemsModel;
    private BufferedImage[] itemSprites; // アイテムの種類ごとに画像を保持

    public ItemView(ItemsModel im) {
        this.itemsModel = im;
        loadItemSprites();
    }

    // アイテムの画像を読み込む
    private void loadItemSprites() {
        try {
            // 現状1種類でも、将来的に横に並んだスプライトシートから読み込める構成
            BufferedImage sheet = ImageIO.read(new File("../resources/weapon/ammo.png"));
            itemSprites = new BufferedImage[1]; // とりあえず要素数1
            itemSprites[0] = sheet; 
            
            /* 複数種類にする場合は以下のように分割
            itemSprites[0] = sheet.getSubimage(0, 0, ITEM_SIZE, ITEM_SIZE);
            itemSprites[1] = sheet.getSubimage(ITEM_SIZE, 0, ITEM_SIZE, ITEM_SIZE);
            */
        } catch (IOException e) {
            System.err.println("アイテム画像の読み込みに失敗しました。");
        }
    }

    public void drawItems(Graphics g, int offsetX, int offsetY) {
        for (ItemModel item : itemsModel.getItems()) {
            int drawX = item.getItemX() + offsetX - ConstSet.TILE_SIZE / 2;
            int drawY = item.getItemY() + offsetY - ConstSet.TILE_SIZE / 2;
            
            int type = item.getItemType();
            if (type >= 0 && type < itemSprites.length && itemSprites[type] != null) {
                g.drawImage(itemSprites[type], drawX, drawY, ConstSet.TILE_SIZE, ConstSet.TILE_SIZE, null);
            }
        }
    }
}
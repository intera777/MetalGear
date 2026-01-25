package model;

import java.util.*;
import GameConfig.*;

public class ItemsModel {
    private ArrayList<ItemModel> items;

    public ItemsModel() {
        this.items = new ArrayList<>();
    }

    // --- ItemsModel.java ---

public boolean updateItems(PlayerModel pm) {
    boolean pickedUpAny = false; // 拾ったかどうかのフラグ
    Iterator<ItemModel> it = items.iterator();
    while (it.hasNext()) {
            ItemModel item = it.next();
        
            double dist = Math.sqrt(Math.pow(pm.getPlayerX() - item.getItemX(), 2) 
                        + Math.pow(pm.getPlayerY() - item.getItemY(), 2));
        
            if (dist < ConstSet.TILE_SIZE / 2) {
                pm.addAmmo(ConstSet.AMMO_REFILL_AMOUNT);
                it.remove();
                pickedUpAny = true; // 拾った
            }
        }
        return pickedUpAny; // 結果を返す
    }

    public void setItemsForMap(int[][] map) {
        items.clear();
        int HS = ConstSet.TILE_SIZE / 2;
        int TS = ConstSet.TILE_SIZE;
        if (map == MapData.MAPA0) {
            // type 0 (弾薬) として追加
            items.add(new ItemModel(TS * 4 + HS, TS * 3 + HS, 0));
        } else if (map == MapData.MAPA1) {
            items.add(new ItemModel(ConstSet.TILE_SIZE * 4 + HS, ConstSet.TILE_SIZE * 4 + HS, 0));
        }
    }

    public ArrayList<ItemModel> getItems() { return items; }
}
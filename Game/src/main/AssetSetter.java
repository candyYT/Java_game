package main;

import object.OBJ_Gear;

public class AssetSetter {
    gamepanel gp;
    public AssetSetter (gamepanel gp){
        this.gp = gp;
    }
    public void setObject(){
        gp.obj[0]= new OBJ_Gear();
        gp.obj[0].worldX = 39*gp.TileSize;
        gp.obj[0].worldY = 25*gp.TileSize;

        gp.obj[1]= new OBJ_Gear();
        gp.obj[1].worldX = 40*gp.TileSize;
        gp.obj[1].worldY = 25*gp.TileSize;
    }
}

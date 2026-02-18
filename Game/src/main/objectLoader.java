package main;

import entity.Entity;
import monster.MON_CorruptedOne;
import object.OBJ_Gear;
import object.SuperObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class objectLoader {

    public static void loadObjects(gamepanel gp, String mapName) {

        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }
        for (int i = 0; i < gp.monster.length; i++) {
            gp.monster[i] = null;
        }

        String filePath = "/maps/objects_" + mapName;
        InputStream is = gamepanel.class.getResourceAsStream(filePath);
        if (is == null) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int objIndex = 0;
            int monsterIndex = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String type = parts[0].trim();
                int col = Integer.parseInt(parts[1].trim());
                int row = Integer.parseInt(parts[2].trim());

                int worldX = col * gp.TileSize;
                int worldY = row * gp.TileSize;

                switch (type) {
                    case "Gear":
                        if (objIndex < gp.obj.length) {
                            OBJ_Gear gear = new OBJ_Gear();
                            gear.worldX = worldX;
                            gear.worldY = worldY;
                            gp.obj[objIndex] = gear;
                            objIndex++;
                        }
                        break;

                    case "CorruptedOne":
                        if (monsterIndex < gp.monster.length) {
                            MON_CorruptedOne monster = new MON_CorruptedOne(gp);
                            monster.worldX = worldX;
                            monster.worldY = worldY;
                            gp.monster[monsterIndex] = monster;
                            monsterIndex++;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

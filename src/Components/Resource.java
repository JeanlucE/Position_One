package Components;

import WIP.DebugLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 09:41
 */
public enum Resource implements Serializable {

    player_NORTH("player_NORTH.png"),
    player_SOUTH("player_SOUTH.png"),
    player_WEST("player_WEST.png"),
    player_EAST("player_EAST.png"),
    enemy_DOWN("enemy.png"),
    wall01("wall_stone01.png"),
    floor01("floor_stone01.png"),
    weapon_melee_01_FLOOR("weapon_melee_01_FLOOR.png"),
    projectile_arrow_01_wooden_FLOOR_NORTH("projectile_arrow_01_wooden_FLOOR_NORTH.png"),
    projectile_arrow_01_wooden_FLOOR_SOUTH("projectile_arrow_01_wooden_FLOOR_SOUTH.png"),
    projectile_arrow_01_wooden_FLOOR_EAST("projectile_arrow_01_wooden_FLOOR_EAST.png"),
    projectile_arrow_01_wooden_FLOOR_WEST("projectile_arrow_01_wooden_FLOOR_WEST.png");

    private String imagePath = "./images/";
    private BufferedImage image;

    private Resource(String fileName) {
        try {
            image = ImageIO.read(new File(imagePath + fileName));
        } catch (FileNotFoundException e) {
            //TODO make a filler image for visual debugging
            DebugLog.write("File not found at " + imagePath + fileName);
        } catch (IOException e) {
            DebugLog.write("IOException at: " + imagePath + fileName);
            System.exit(-1);
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}

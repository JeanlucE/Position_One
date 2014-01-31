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
    player_NORTH_WALK01("player_NORTH_WALK01.png"),
    player_NORTH_WALK02("player_NORTH_WALK02.png"),
    player_NORTH_WALK03("player_NORTH_WALK03.png"),
    player_NORTH_WALK04("player_NORTH_WALK04.png"),
    player_NORTH_WALK05("player_NORTH_WALK05.png"),
    player_NORTH_WALK06("player_NORTH_WALK06.png"),
    player_NORTH_WALK07("player_NORTH_WALK07.png"),
    player_NORTH_WALK08("player_NORTH_WALK08.png"),
    player_NORTH_WALK09("player_NORTH_WALK09.png"),
    player_NORTH_WALK10("player_NORTH_WALK10.png"),
    player_SOUTH("player_SOUTH.png"),
    player_SOUTH_WALK01("player_SOUTH_WALK01.png"),
    player_SOUTH_WALK02("player_SOUTH_WALK02.png"),
    player_SOUTH_WALK03("player_SOUTH_WALK03.png"),
    player_SOUTH_WALK04("player_SOUTH_WALK04.png"),
    player_SOUTH_WALK05("player_SOUTH_WALK05.png"),
    player_SOUTH_WALK06("player_SOUTH_WALK06.png"),
    player_SOUTH_WALK07("player_SOUTH_WALK07.png"),
    player_SOUTH_WALK08("player_SOUTH_WALK08.png"),
    player_SOUTH_WALK09("player_SOUTH_WALK09.png"),
    player_SOUTH_WALK10("player_SOUTH_WALK10.png"),
    player_WEST("player_WEST.png"),
    player_WEST_WALK01("player_WEST_WALK01.png"),
    player_WEST_WALK02("player_WEST_WALK02.png"),
    player_WEST_WALK03("player_WEST_WALK03.png"),
    player_WEST_WALK04("player_WEST_WALK04.png"),
    player_WEST_WALK05("player_WEST_WALK05.png"),
    player_WEST_WALK06("player_WEST_WALK06.png"),
    player_WEST_WALK07("player_WEST_WALK07.png"),
    player_WEST_WALK08("player_WEST_WALK08.png"),
    player_WEST_WALK09("player_WEST_WALK09.png"),
    player_WEST_WALK10("player_WEST_WALK10.png"),
    player_EAST("player_EAST.png"),
    player_EAST_WALK01("player_EAST_WALK01.png"),
    player_EAST_WALK02("player_EAST_WALK02.png"),
    player_EAST_WALK03("player_EAST_WALK03.png"),
    player_EAST_WALK04("player_EAST_WALK04.png"),
    player_EAST_WALK05("player_EAST_WALK05.png"),
    player_EAST_WALK06("player_EAST_WALK06.png"),
    player_EAST_WALK07("player_EAST_WALK07.png"),
    player_EAST_WALK08("player_EAST_WALK08.png"),
    player_EAST_WALK09("player_EAST_WALK09.png"),
    player_EAST_WALK10("player_EAST_WALK10.png"),
    enemy_DOWN("enemy.png"),
    wall01("wall_stone01.png"),
    floor01("floor_stone01.png"),
    weapon_melee_01_FLOOR("weapon_melee_01_FLOOR.png"),
    projectile_arrow_01_wooden_FLOOR_NORTH("projectile_arrow_01_wooden_FLOOR_NORTH.png"),
    projectile_arrow_01_wooden_FLOOR_SOUTH("projectile_arrow_01_wooden_FLOOR_SOUTH.png"),
    projectile_arrow_01_wooden_FLOOR_EAST("projectile_arrow_01_wooden_FLOOR_EAST.png"),
    projectile_arrow_01_wooden_FLOOR_WEST("projectile_arrow_01_wooden_FLOOR_WEST.png"),
    projectile_fire_01_FLOOR_NORTH("projectile_fire_01_FLOOR_NORTH.png"),
    projectile_fire_01_FLOOR_SOUTH("projectile_fire_01_FLOOR_SOUTH.png"),
    projectile_fire_01_FLOOR_EAST("projectile_fire_01_FLOOR_EAST.png"),
    projectile_fire_01_FLOOR_WEST("projectile_fire_01_FLOOR_WEST.png");

    private final String imagePath = "./images/";
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

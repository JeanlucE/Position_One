package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.12.13
 * Time: 16:55
 */
public enum DynamicResource {
    //TODO add player and projectile images here
    PLAYER(new Resource[]{
            Resource.player_UP,
            Resource.player_RIGHT,
            Resource.player_DOWN,
            Resource.player_LEFT}),
    WOODENARROW(new Resource[]{
            Resource.projectile_arrow_01_wooden_FLOOR_NORTH,
            Resource.projectile_arrow_01_wooden_FLOOR_EAST,
            Resource.projectile_arrow_01_wooden_FLOOR_SOUTH,
            Resource.projectile_arrow_01_wooden_FLOOR_WEST});

    private Resource[] resources;

    DynamicResource(Resource[] resources) {
        this.resources = resources;
    }

    public BufferedImage getNorth() {
        return resources[0].getImage();
    }

    public BufferedImage getEast() {
        return resources[1].getImage();
    }

    public BufferedImage getSouth() {
        return resources[2].getImage();
    }

    public BufferedImage getWest() {
        return resources[3].getImage();
    }
}

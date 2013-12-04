package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.12.13
 * Time: 16:55
 */
public enum DynamicResource {
    PLAYER(new Resource[]{
            Resource.player_NORTH,
            Resource.player_EAST,
            Resource.player_SOUTH,
            Resource.player_WEST}),
    WOODENARROW(new Resource[]{
            Resource.projectile_arrow_01_wooden_FLOOR_NORTH,
            Resource.projectile_arrow_01_wooden_FLOOR_EAST,
            Resource.projectile_arrow_01_wooden_FLOOR_SOUTH,
            Resource.projectile_arrow_01_wooden_FLOOR_WEST}),
    ENEMY_CHUCHU(new Resource[]{
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
    });

    private Resource[] resources;

    private DynamicResource(Resource[] resources) {
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

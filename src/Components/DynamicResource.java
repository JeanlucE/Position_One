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
    WOODEN_ARROW(new Resource[]{
            Resource.projectile_arrow_01_wooden_FLOOR_NORTH,
            Resource.projectile_arrow_01_wooden_FLOOR_EAST,
            Resource.projectile_arrow_01_wooden_FLOOR_SOUTH,
            Resource.projectile_arrow_01_wooden_FLOOR_WEST}),
    ENEMY_CHUCHU(new Resource[]{
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
    }),
    FIRE_PROJECTILE(new Resource[]{
        Resource.projectile_fire_01_FLOOR_NORTH,
        Resource.projectile_fire_01_FLOOR_EAST,
        Resource.projectile_fire_01_FLOOR_SOUTH,
        Resource.projectile_fire_01_FLOOR_WEST,
    });

    private Resource[] resources;
    private int size;

    private DynamicResource(Resource[] resources) {
        this.resources = resources;
        size = resources.length;
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

    public BufferedImage getNotActivated(){
        return resources[0].getImage();
    }

    public BufferedImage getActivated(){
        return resources[1].getImage();
    }

    public int getSize() {
        return size;
    }
}

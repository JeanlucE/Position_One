package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.12.13
 * Time: 16:55
 */
public class DynamicResource {

    //TODO refine animation images for player or get other images
    public static final ActorGraphicsResource PLAYER = new ActorGraphicsResource(
            Resource.player_NORTH,
            Resource.player_EAST,
            Resource.player_SOUTH,
            Resource.player_WEST,
            new Animation(new Resource[]{Resource.player_NORTH_WALK01, Resource.player_NORTH_WALK02,
                    Resource.player_NORTH_WALK03, Resource.player_NORTH_WALK04, Resource.player_NORTH_WALK05,
                    Resource.player_NORTH_WALK06, Resource.player_NORTH_WALK07, Resource.player_NORTH_WALK08,
                    Resource.player_NORTH_WALK09, Resource.player_NORTH_WALK10}),
            new Animation(new Resource[]{Resource.player_EAST_WALK01, Resource.player_EAST_WALK02,
                    Resource.player_EAST_WALK03, Resource.player_EAST_WALK04, Resource.player_EAST_WALK05,
                    Resource.player_EAST_WALK06, Resource.player_EAST_WALK07, Resource.player_EAST_WALK08,
                    Resource.player_EAST_WALK09, Resource.player_EAST_WALK10}),
            new Animation(new Resource[]{Resource.player_SOUTH_WALK01, Resource.player_SOUTH_WALK02,
                    Resource.player_SOUTH_WALK03, Resource.player_SOUTH_WALK04, Resource.player_SOUTH_WALK05,
                    Resource.player_SOUTH_WALK06, Resource.player_SOUTH_WALK07, Resource.player_SOUTH_WALK08,
                    Resource.player_SOUTH_WALK09, Resource.player_SOUTH_WALK10}),
            new Animation(new Resource[]{Resource.player_WEST_WALK01, Resource.player_WEST_WALK02,
                    Resource.player_WEST_WALK03, Resource.player_WEST_WALK04, Resource.player_WEST_WALK05,
                    Resource.player_WEST_WALK06, Resource.player_WEST_WALK07, Resource.player_WEST_WALK08,
                    Resource.player_WEST_WALK09, Resource.player_WEST_WALK10}));
    public static final DynamicResource WOODEN_ARROW = new DynamicResource(new Resource[]{
            Resource.projectile_arrow_01_wooden_FLOOR_NORTH,
            Resource.projectile_arrow_01_wooden_FLOOR_EAST,
            Resource.projectile_arrow_01_wooden_FLOOR_SOUTH,
            Resource.projectile_arrow_01_wooden_FLOOR_WEST});
    public static final ActorGraphicsResource ENEMY_CHUCHU = new ActorGraphicsResource(
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            Resource.enemy_DOWN,
            null, null, null, null);
    public static final DynamicResource FIRE_PROJECTILE = new DynamicResource(new Resource[]{
            Resource.projectile_fire_01_FLOOR_NORTH,
            Resource.projectile_fire_01_FLOOR_EAST,
            Resource.projectile_fire_01_FLOOR_SOUTH,
            Resource.projectile_fire_01_FLOOR_WEST,
    });

    protected Resource[] resources;
    private int size;

    private DynamicResource(Resource[] resources) {
        this.resources = resources;
        size = resources.length;
    }

    protected DynamicResource() {
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

    public BufferedImage getNotActivated() {
        return resources[0].getImage();
    }

    public BufferedImage getActivated() {
        return resources[1].getImage();
    }

    public int getSize() {
        return size;
    }
}

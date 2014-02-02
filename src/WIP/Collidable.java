package WIP;

import Components.PhysicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 11.12.13
 * Time: 19:18
 */
public interface Collidable {

    PhysicsComponent getCollider();

    void setCollider(PhysicsComponent physicsComponent);
}

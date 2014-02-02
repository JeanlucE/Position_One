package Items;

import Components.PhysicsComponent;
import Components.ProjectileGraphicsComponent;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 27.11.13
 * Time: 23:26
 */
public class Ammunition extends Equipment implements Stackable {

    private int stack = 50;
    private PhysicsComponent physicsComponent;

    public Ammunition(String name, int level, ProjectileGraphicsComponent g, PhysicsComponent phys) {
        super(name, level, 0, EquipmentClass.RANGED, g);
        physicsComponent = phys;
    }

    @Override
    public boolean isWeapon() {
        return false;
    }

    @Override
    public boolean isArmour() {
        return false;
    }

    @Override
    public boolean isAmmunition() {
        return true;
    }

    public PhysicsComponent getCollider() {
        return physicsComponent;
    }

    @Override
    public void setStack(int stack) {
        this.stack = stack;
    }

    @Override
    public boolean removeFromStack(int remove) {
        if (stack - remove >= 0) {
            addToStack(-remove);
            return true;
        }
        return false;
    }

    public int getStack() {
        return stack;
    }

    public void addToStack(int amount) {
        stack += amount;
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public boolean equals(Ammunition a) {
        if (a == null) return false;
        if (this == a) return true;
        return (a.getName().equals(this.getName()));

    }

    public Projectile createProjectile(Transform origin, Weapon_Ranged weapon) {

        //Rotate collider when arrow is pointing east or west
        PhysicsComponent phys = getCollider().clone();
        if (origin.getDirection().ofAxisWestEast()) {
            phys.rotate90();
        }
        origin = origin.clone();
        return new Projectile(origin, new ProjectileGraphicsComponent(origin.getDirection(),
                ((ProjectileGraphicsComponent) getGraphic()).getResource()), phys, 10, 6,
                weapon.getBaseDamage());
    }
}

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
public class Arrow extends Equipment {

    protected int stack;
    private PhysicsComponent physicsComponent;

    public Arrow(String name, int level, ProjectileGraphicsComponent g, PhysicsComponent phys) {
        super(name, level, 0, EquipmentClass.RANGED, g);
        physicsComponent = phys;
    }

    public PhysicsComponent getCollider() {
        return physicsComponent;
    }

    public int getStack() {
        return stack;
    }

    public void addToStack(int amount) {
        stack += amount;
    }

    public int removeFromStack() {
        if (stack > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public boolean equals(Arrow p) {
        if (p == null) return false;
        if (this == p) return true;
        if (!p.getName().equals(this.getName())) return false;
        if (!(p.getLevel() == this.getLevel())) return false;
        if (!(p.getSkillRequirement() == this.getSkillRequirement())) return false;
        if (!(p.getType().equals(this.getType()))) return false;

        return true;
    }

    public Projectile createProjectile(Weapon_Ranged weapon, Transform origin) {
        stack--;
        Projectile p = new Projectile(origin, getGraphic(), getCollider(), 5, 6, weapon.getBaseDamage());
        return p;
    }

}

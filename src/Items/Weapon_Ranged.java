package Items;

import Components.ItemGraphicsComponent;
import WIP.DebugLog;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 03.11.13
 * Time: 13:53
 * <p/>
 * This is a class with which Ranged weapons can be created.
 * <p/>
 * Unique fields which are not in superclass are:
 * - RangedType
 * <p/>
 * Unique behaviour not inherited by superclass:
 * - Shooting projectiles like an arrow
 */
public class Weapon_Ranged extends Weapon {

    public Weapon_Ranged(String name, int level, int dexterityRequirement, int baseDamage, int baseRange,
                         float useTime, ItemGraphicsComponent g) {
        super(name, level, dexterityRequirement, baseDamage, baseRange, useTime, EquipmentClass.RANGED, g);
        DebugLog.write("New Ranged Weapon created: " + name);
    }

    protected void useThis() {
        Ammunition ammunition = ((Actors.Character) equipped).getAmmunition();
        if (ammunition == null || !ammunition.removeFromStack(1)) {
            //ammunition.destroy();
            DebugLog.write("No ammunition left");
            return;
        }
        ammunition.createProjectile(equipped.getTransform(), this);
    }

    public enum RangedType {
        ARROW, BOLT
    }
}

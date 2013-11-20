package Items;

import Components.GraphicsComponent;
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

    public void use() {
        //TODO implement this
    }

    //TODO RangedType
}

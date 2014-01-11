package Items;

import Actors.Actor;
import Components.ItemGraphicsComponent;
import WIP.DebugLog;
import WIP.Game;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 03.11.13
 * Time: 12:15
 * This is a class with which Magic weapons can be created.
 * <p/>
 * Unique fields which are not in superclass are:
 * none
 * <p/>
 * Unique behaviour not inherited by superclass:
 * - all melee weapons have a base range of 1 block
 */
public class Weapon_Melee extends Weapon {

    public Weapon_Melee(String name, int level, int strengthRequirement, int baseDamage, float useTime,
                        ItemGraphicsComponent g) {
        //All melee weapons have a range of 1 block
        super(name, level, strengthRequirement, baseDamage, 1, useTime, EquipmentClass.MELEE, g);
        DebugLog.write("New Melee Weapon created: " + name);

    }

    public void use() {
        Actor[] actors = Game.getInstance().getActors();
        for (Actor a : actors) {
            if (!a.equals(equipped)) {
                if (equipped.withinLinearRangeOf(a, getBaseRange()))
                    a.damage(getBaseDamage());
            }
        }
    }
}

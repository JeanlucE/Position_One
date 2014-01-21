package Items;

import Components.ItemGraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 21.01.14
 * Time: 10:08
 */
public class Shield extends Armour {
    public Shield(String name, int level, int skillRequirement, int defence, ItemGraphicsComponent g) {
        super(name, level, skillRequirement, EquipmentClass.MELEE, defence, ArmourType.SHIELD, g);
    }
}

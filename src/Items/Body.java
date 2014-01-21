package Items;

import Components.ItemGraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 21.01.14
 * Time: 10:06
 */
public class Body extends Armour {
    public Body(String name, int level, int skillRequirement, EquipmentClass armourClass, int defence,
                ItemGraphicsComponent g) {
        super(name, level, skillRequirement, armourClass, defence, ArmourType.BODY, g);
    }
}

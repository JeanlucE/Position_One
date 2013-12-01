package WIP;

import Items.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 01.12.13
 * Time: 15:37
 */
public class EquipmentManager {
    private Character parent;
    private EquipmentSlot<Armour> helmetSlot;
    private EquipmentSlot<Armour> bodySlot;
    private EquipmentSlot<Armour> legsSlot;
    private EquipmentSlot<Weapon> mainHandSlot;
    private EquipmentSlot<Armour> offHandSlot;

    public EquipmentManager(Character parent) {
        this.parent = parent;
        helmetSlot = new EquipmentSlot();
        bodySlot = new EquipmentSlot();
        legsSlot = new EquipmentSlot();
        mainHandSlot = new EquipmentSlot();
        offHandSlot = new EquipmentSlot();
    }

    public void equip(Equipment equipment) {
        //TODO if isOccupied throw the current previous equipment into the inventory
        if (canEquip(equipment)) {
            if (equipment instanceof Weapon) {
                mainHandSlot.equip((Weapon) equipment);
            } else {
                Armour armour = (Armour) equipment;
                if (armour.getArmourType().equals(Armour.ArmourType.SHIELD)) {
                    offHandSlot.equip(armour);
                } else if (armour.getArmourType().equals(Armour.ArmourType.HELMET)) {
                    helmetSlot.equip(armour);
                } else if (armour.getArmourType().equals(Armour.ArmourType.BODY)) {
                    bodySlot.equip(armour);
                } else if (armour.getArmourType().equals(Armour.ArmourType.LEGS)) {
                    legsSlot.equip(armour);
                }
            }
            DebugLog.write("Player " + parent.getName() + " has equipped: " + equipment.getName());
        } else {
            DebugLog.write("Player " + parent.getName() + " cannot equip: " + equipment.getName());
        }
    }

    //takes an equipmentslot that should be unequipped and when in the inventory the item should be put
    public void unequip(EquipmentSlot equipmentSlot, Inventory.InventorySlot inventorySlot) {
        if (!inventorySlot.isOccupied() && equipmentSlot.isOccupied()) {
            inventorySlot.addItem(equipmentSlot.getEquipment());
            equipmentSlot.equip(null);
        } else {
            System.out.println("Debug: Method Character.unequip called for an occupied inventory slot or an " +
                    "unoccupied equipment slot.");
        }
    }

    public boolean canEquip(Equipment equipment) {
        //Checks if Player is high enough level
        boolean metLevelReq = parent.getLevel() >= equipment.getLevel();

        //Checks if Player has a high enough skill in either strength, intelligence or dexterity
        Equipment.EquipmentClass equipmentClass = equipment.getType();
        boolean metSkillReq;
        if (equipmentClass.equals(Equipment.EquipmentClass.MELEE)) {
            metSkillReq = parent.STRENGTH.getLevel() >= equipment.getSkillRequirement();
        } else if (equipmentClass.equals(Equipment.EquipmentClass.MAGIC)) {
            metSkillReq = parent.INTELLIGENCE.getLevel() >= equipment.getSkillRequirement();
        } else {
            metSkillReq = parent.DEXTERITY.getLevel() >= equipment.getSkillRequirement();
        }


        return metLevelReq && metSkillReq;
    }

    public Armour getHelmet() {
        return helmetSlot.getEquipment();
    }

    public Armour getBody() {
        return bodySlot.getEquipment();
    }

    public Armour getLegs() {
        return legsSlot.getEquipment();
    }

    public Weapon getMainHand() {
        return mainHandSlot.getEquipment();
    }

    public Armour getOffHand() {
        return offHandSlot.getEquipment();
    }

    public String toString() {
        String equipment = "";
        equipment += "Helmet: " + helmetSlot.toString() + "\n";
        equipment += "Body: " + bodySlot.toString() + "\n";
        equipment += "Legs: " + legsSlot.toString() + "\n";
        equipment += "Main Hand: " + mainHandSlot.toString() + "\n";
        equipment += "Offhand: " + offHandSlot.toString();
        return equipment;
    }
}

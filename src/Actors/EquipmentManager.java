package Actors;

import Items.*;
import WIP.DebugLog;

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
    private EquipmentSlot<Ammunition> ammunitionSlot;

    /*
    Instantiates all equipment slots with what kind of equipment that particular slot can carry
    Main hand should always be a weapon
    Problems could arise here with Two-Handed and off-hand weapons
     */
    public EquipmentManager(Character parent) {
        this.parent = parent;
        helmetSlot = new EquipmentSlot<>();
        bodySlot = new EquipmentSlot<>();
        legsSlot = new EquipmentSlot<>();
        mainHandSlot = new EquipmentSlot<>();
        offHandSlot = new EquipmentSlot<>();
        ammunitionSlot = new EquipmentSlot<>();
    }

    /*
    Should only be called when equipment slot is not occupied
    This method equips the player with a piece of equipment.
    It first checks if the level and skill requirements of the equipment are met by the player.
    If the piece of equipment is a weapon it then assigns the weapon to the main hand slot.
    If the piece of equipment is an armour piece, it then check what armour type it is (Helmet, Body,
    Legs, Shield) and then assigns it to the appropriate equipment slot.
     */
    public void equip(Equipment equipment) {
        //TODO if isOccupied throw the current equipment into the inventory and equip the new Equipment
        if (canEquip(equipment)) {
            if (equipment.isWeapon()) {
                equip((Weapon) equipment);
            } else if (equipment.isArmour()) {
                equip((Armour) equipment);
            } else { //equipment.isAmmunition
                ammunitionSlot.equip((Ammunition) equipment);
            }
            DebugLog.write("Player " + parent.getName() + " has equipped: " + equipment.getName());
        } else {
            DebugLog.write("Player " + parent.getName() + " cannot equip: " + equipment.getName());
        }
    }

    private void equip(Weapon weapon) {
        mainHandSlot.equip(weapon);
        weapon.setEquipped(parent);
    }

    private void equip(Armour armour) {
        if (armour.isShield()) {
            offHandSlot.equip(armour);
        } else if (armour.isHelmet()) {
            helmetSlot.equip(armour);
        } else if (armour.isBody()) {
            bodySlot.equip(armour);
        } else if (armour.isLegs()) {
            legsSlot.equip(armour);
        }
    }

    //takes an equipmentslot that should be unequipped and when in the inventory the item should be put
    /*public void unequip(EquipmentSlot equipmentSlot, Inventory.InventorySlot inventorySlot) {
        if (!inventorySlot.isOccupied() && equipmentSlot.isOccupied()) {
            inventorySlot.addItem(equipmentSlot.getEquipment());
            equipmentSlot.equip(null);
        } else {
            System.out.println("Debug: Method Character.unequip called for an occupied inventory slot or an " +
                    "unoccupied equipment slot.");
        }
    }*/

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

    public Ammunition getAmmunition() {
        return ammunitionSlot.getEquipment();
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

package Items;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 05.11.13
 * Time: 23:22
 * <p/>
 * This class acts an a slot of equipment for the Character. It can hold equipment like a piece of armour or a
 * weapon. Once it is instantiated the type of equipment it can hold cannot be changed.
 */
public class EquipmentSlot {
    private Equipment equipment;
    private final Class equipmentType;

    public EquipmentSlot(Class equipmentType) {
        this.equipmentType = equipmentType;
    }

    //Should only be called when isOccupied returns true
    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public boolean isOccupied() {
        return equipment != null;
    }

    public String toString() {
        return (isOccupied()) ? (equipment.getName()) : ("");
    }
}

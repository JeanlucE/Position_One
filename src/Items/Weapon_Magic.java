package Items;

import Actors.Character;
import Components.DynamicResource;
import Components.ItemGraphicsComponent;
import Components.PhysicsComponent;
import Components.ProjectileGraphicsComponent;
import WIP.DebugLog;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 03.11.13
 * Time: 13:29
 * <p/>
 * This is a class with which Magic weapons can be created.
 * <p/>
 * Unique fields which are not in superclass are:
 * - Element
 * <p/>
 * Unique behaviour not inherited by superclass:
 * - Shooting projectiles of elements
 */
public class Weapon_Magic extends Weapon {

    private final Element element;

    public Weapon_Magic
            (String name, int level, int intelligenceRequirement, int baseDamage, int baseRange, float useTime,
             Element element, ItemGraphicsComponent g) {
        super(name, level, intelligenceRequirement, baseDamage, baseRange, useTime, EquipmentClass.MAGIC, g);
        this.element = element;
        DebugLog.write("New Magic Weapon created: " + name);
    }

    protected void useThis() {
        if (element.equals(Element.FIRE)) {

            //TODO Spell class: mana usage, equipped, collider, element, speed

            if (equipped instanceof Actors.Character && !((Character) equipped).useMana(5)) return;

            Transform origin = equipped.getTransform().clone();

            //Rotate collider when arrow is pointing east or west
            PhysicsComponent phys = new PhysicsComponent(30, 48);
            if (origin.getDirection().ofAxisWestEast()) {
                phys.rotate90();
            }
            new Projectile(origin, new ProjectileGraphicsComponent(origin.getDirection(),
                    DynamicResource.FIRE_PROJECTILE), phys, 5, getBaseRange(), getBaseDamage());
        }
    }

    public Element getElement() {
        return element;
    }

    public String toString() {
        return this.getName() + ":\n"
                + "Type: " + this.getClass().toString() + "\n"
                + "Level: " + this.getLevel() + "\n"
                + "Skill Requirement: " + this.getSkillRequirement() + "\n"
                + "Weapon Type: " + this.getType().toString() + "\n"
                + "Base Damage: " + this.getBaseDamage() + "\n"
                + "Base Range: " + this.getBaseRange() + "\n"
                + "Hits per Second: " + this.getUseTime() + "\n"
                + "Element: " + this.element.toString();
    }

    /*
    The element of a magic weapon only serves an aesthetic purpose for now.
     */
    public enum Element {
        FIRE("Fire"), WATER("Water"), LIGHTNING("Lightning");

        private final String name;

        private Element(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}

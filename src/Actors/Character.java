package Actors;

import Components.ActorGraphicsComponent;
import Components.DynamicResource;
import Components.InputComponent;
import Components.PhysicsComponent;
import Environment.Floor;
import Environment.WorldSpace;
import Items.*;
import WIP.*;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 30.09.13
 * Time: 16:00
 */
public class Character extends Actor {
    //region Skills
    public Skill STRENGTH;
    public Skill ENDURANCE;
    public Skill INTELLIGENCE;
    public Skill DEXTERITY;
    public Skill CHARISMA;
    public Skill PERCEPTION;
    public Skill LUCK;
    public Skill STEALTH;
    //endregion

    //region Stats
    private int level;
    private int experience;
    private int skillPoints;
    private int maxStamina, currentStamina;
    private int maxMana, currentMana;

    //Regen per second
    private float healthRegenRate = 0f;
    private float staminaRegenRate = 7.5f;
    private float manaRegenRate = 3.5f;

    //Drain per second
    private float staminaDrainRate = 10f;
    //endregion

    //Array of all skills
    private Skill[] skills;

    //Manages all the equipment of the player
    private final EquipmentManager equipment;

    //Manages the inventory of the player
    private final Inventory inventory;

    //The speed at which the player walks per frame
    private int walkSpeed = 3;
    //The speed at which the player sprints per frame
    private int sprintSpeed = 6;
    //true == player is sprinting, false == player is not sprinting
    private boolean sprinting = false;

    //Constructor for final build
    public Character(String name) {
        this(name, 0, 0, 0);
    }

    //Debug Purposes
    public Character(String name, int level, int experience, int skillPoints) {
        super(name,
                new Transform(),
                new ActorGraphicsComponent(DynamicResource.PLAYER, true),
                new PhysicsComponent(30, 39));
        ((ActorGraphicsComponent) this.getGraphic()).setParent(this);

        this.level = level;
        this.experience = experience;
        this.skillPoints = skillPoints;
        this.instantiateSkills();

        recalculateStats();
        currentHealth = maxHealth;
        currentMana = maxMana;
        maxStamina = 100;
        currentStamina = maxStamina;



        this.inventory = new Inventory();
        equipment = new EquipmentManager(this);

        DebugLog.write("New Character created: " + name);
    }

    public static int getXPOfLevel(int level) {
        return 20 * level * level * level;
    }

    /*
    Gets all public fields in Character.java and instatntiates each of the skills.
    Skill names are read the name of the public Field like so:
    "STRENGTH"'s name would be "Strength"
    It then adds all skills to an array for later purposes
     */
    private void instantiateSkills() {
        Field[] fields = this.getClass().getFields();
        skills = new Skill[fields.length];
        int i = 0;
        for (Field f : fields) {
            try {
                f.set(this, new Skill(getSkillName(f)));
                skills[i] = (Skill) f.get(this);
                i++;
            } catch (IllegalAccessException e) {
                System.out.println("Player skill creation failed!");
                e.printStackTrace();
            }
        }
    }

    private String getSkillName(Field f) {
        return f.getName().charAt(0) + f.getName().substring(1, f.getName().length()).toLowerCase();
    }
    //endregion

    public String[] getSkills() {
        String[] result = new String[skills.length];
        for (int i = 0; i < skills.length; i++) {
            result[i] = skills[i].toString();
        }
        return result;
    }

    public boolean levelUpSkill(String skill) {
        Skill s = null;
        for (Skill skillToSearch : skills) {
            if (skillToSearch.getName().equals(skill)) {
                s = skillToSearch;
                break;
            }
        }
        if (s != null) {
            s.setLevel(getLevel() + 1);
            recalculateStats();
            return true;
        } else {
            return false;
        }
    }

    private void recalculateStats() {
        //TODO damage bonuses
        setMaxHealth(100 + STRENGTH.getLevel() * 15);
        setCurrentHealth(maxHealth);
        setStaminaDrainRate(10f - ENDURANCE.getLevel() * 0.5f);
        setStaminaRegenRate(7.5f + ENDURANCE.getLevel() * 0.5f);
        setMaxMana(20 + INTELLIGENCE.getLevel() * 4);
        setCurrentMana(getMaxMana());
        setManaRegenRate(3.6f + INTELLIGENCE.getLevel() * 0.2f);

        staminaTimer = Math.round(1000 / staminaRegenRate);
        manaTimer = Math.round(1000 / manaRegenRate);
        staminaDrainRate = Math.round(1000 / staminaDrainRate);
    }

    protected void updateThis() {
        applyMovement();

        if (InputComponent.getInstance().isSpaceDown()) {
            attack();
        }

        if (InputComponent.getInstance().isQTyped()) {
            pickupNextItem();
        }
    }

    private void applyMovement() {
        int moveDistance;
        if (InputComponent.getInstance().isShiftDown()) {
            if (currentStamina >= 3) {
                moveDistance = sprintSpeed;
                sprinting = true;
            } else {
                moveDistance = walkSpeed;
                sprinting = false;
            }
        } else {
            moveDistance = walkSpeed;
            sprinting = false;
        }

        int XAxis = InputComponent.getInstance().getXAxis();
        int YAxis = InputComponent.getInstance().getYAxis();

        setXVel(XAxis * moveDistance);
        setYVel(YAxis * moveDistance);
    }

    private void attack() {
        Weapon weapon = equipment.getMainHand();
        if (weapon != null) {
            weapon.use();
        }
    }

    /**
     * Picks up an item nearest to the player
     */
    private void pickupNextItem() {
        WorldSpace nextAbsPos = Game.getInstance().getCurrentWorld().get(getTransform().getPosition());
        if (nextAbsPos != null && nextAbsPos.isFloor()) {
            Floor floor = (Floor) nextAbsPos;
            addItem(floor.hasDroppedItems() ? (floor.getTopItem()) : (null));
        }
    }

    private void addItem(Item item) {
        if (item == null) return;
        if (item.isStackable()) {
            if (equipment.hasAmmunitionEquipped() && item instanceof Ammunition && equip((Equipment) item)) {
                DebugLog.write("Ammunition stacked");
            } else
                inventory.add(item);
        } else {
            inventory.add(item);
        }
    }

    private int staminaRegen, manaRegen;
    private int staminaTimer, manaTimer;

    private void regenerate() {

        //Regens stamina
        if (currentStamina < maxStamina && !sprinting) {
            staminaRegen += Time.deltaTime();
            while (staminaRegen >= staminaTimer) {
                if (currentStamina + 1 <= maxStamina) {
                    currentStamina++;
                }
                staminaRegen -= staminaTimer;
            }
        }

        //Regens Mana
        if (currentMana < maxMana) {
            manaRegen += Time.deltaTime();
            while (manaRegen >= manaTimer) {
                if (currentMana + 1 <= maxMana)
                    currentMana++;
                manaRegen -= manaTimer;
            }
        }
    }

    //region Player Stats
    public int getMaxStamina() {
        return maxStamina;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public boolean useMana(int mana) {
        if (currentMana - mana >= 0) {
            currentMana -= mana;
            return true;
        } else {
            DebugLog.write("Not enough Mana!");
            return false;
        }
    }

    public int getCurrentStamina() {
        return currentStamina;
    }

    public void setCurrentStamina(int currentStamina) {
        this.currentStamina = currentStamina;
    }

    public float getHealthRegenRate() {
        return healthRegenRate;
    }

    public void setHealthRegenRate(float healthRegenRate) {
        this.healthRegenRate = healthRegenRate;
    }

    public float getStaminaRegenRate() {
        return staminaRegenRate;
    }

    public void setStaminaRegenRate(float staminaRegenRate) {
        this.staminaRegenRate = staminaRegenRate;
    }

    public float getManaRegenRate() {
        return manaRegenRate;
    }

    public void setManaRegenRate(float manaRegenRate) {
        this.manaRegenRate = manaRegenRate;
    }

    public float getStaminaDrainRate() {
        return staminaDrainRate;
    }

    public void setStaminaDrainRate(float staminaDrainRate) {
        this.staminaDrainRate = staminaDrainRate;
    }

    //endregion

    //region Level and Skill Methods
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        DebugLog.write("Player level set to: " + level, true);
    }

    public void addExperience(int experience) {
        this.experience += experience;
        DebugLog.write("Player gained experience: " + experience);
        while (getXPToNextLevel() <= 0) {
            levelUp();
        }

    }

    private void levelUp() {
        level++;
        skillPoints++;

        for (Skill s : skills) {
            s.setLevel(s.getLevel() + 1);
        }

        recalculateStats();

        DebugLog.write("Player " + getName() + " has leveled up.");
        DebugLog.write("Player " + getName() + " is now level " + level + ".");
    }

    /**
     * Returns how much xp is needed for this character to level up
     *
     * @return Returns how much xp is needed for this character to level up
     */
    public int getXPToNextLevel() {
        return Character.getXPOfLevel(level + 1) - this.experience;
    }

    public int getXPOfNextLevel() {
        return Character.getXPOfLevel(level + 1) - Character.getXPOfLevel(level);
    }

    public int getCurrentXPProgress() {
        return getXPOfNextLevel() - getXPToNextLevel();
    }

    /**
     * Returns how many skillpoint the character currently has
     *
     * @return Returns how many skillpoint the character currently has
     */
    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
        DebugLog.write("Player skill points set to: " + skillPoints, true);
    }

    //endregion

    //region Equipment Methods
    public Inventory getInventory() {
        return inventory;
    }

    public boolean equip(Equipment equipment) {
        return this.equipment.equip(equipment);
    }

    //TODO public void unequip()

    public Armour getHelmet() {
        return equipment.getHelmet();
    }

    public Armour getBody() {
        return equipment.getBody();
    }

    public Armour getLegs() {
        return equipment.getLegs();
    }

    public Weapon getMainHand() {
        return equipment.getMainHand();
    }

    public Armour getOffHand() {
        return equipment.getOffHand();
    }

    public Ammunition getAmmunition() {
        return equipment.getAmmunition();
    }

    public String printEquipment() {
        return equipment.toString();
    }
    //endregion

    @Override
    public Faction getFaction() {
        return Faction.PLAYER;
    }

    //For Debugging purposes
    public String toString() {
        return "Character: " + getName() + ", " + this.level + ", " + this.experience;
    }

    @Override
    protected void death() {
        DebugLog.write("Player " + getName() + " is dead.");
        this.getTransform().getPosition().set(new Vector(60, 60));
        this.currentHealth = maxHealth;
    }

    @Override
    protected int getDamageTimeout() {
        return 750;
    }

    public int getAttack() {
        return getMainHand() != null ? getMainHand().getBaseDamage() : 0;
    }

    public int getDefense() {
        int result = 0;
        result += (equipment.hasHelmetEquipped()) ? (getHelmet().getDefence()) : 0;
        result += (equipment.hasBodyEquipped()) ? (getBody().getDefence()) : 0;
        result += (equipment.hasLegsEquipped()) ? (getLegs().getDefence()) : 0;
        result += (equipment.hasOffHandEquipped()) ? (getOffHand().getDefence()) : 0;
        return result;
    }

    @Override
    protected int actualDamage(int damage) {
        int actualDamage = damage - getDefense();
        return (actualDamage > 1) ? (actualDamage) : 1;
    }

    public float getStaminaPercentage() {
        return currentStamina / (float) maxStamina;
    }

    public float getManaPercentage() {
        return currentMana / (float) maxMana;
    }

    @Override
    protected void animate() {
        super.animate();
        if (isMoving() && sprinting)
            setAnimationState(AnimationState.SPRINTING);
    }


    private int staminaDrainTimer = 0;

    private void drainStamina() {

        if (isSprinting()) {
            staminaDrainTimer += Time.deltaTime();
            while (staminaDrainTimer >= staminaDrainRate) {
                currentStamina--;
                staminaDrainTimer -= staminaDrainRate;
            }
        }
    }

    public boolean isSprinting() {
        return isMoving() && sprinting;
    }

    @Override
    protected void lateUpdate() {
        drainStamina();
        regenerate();
    }
}

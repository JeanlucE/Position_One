package Actors;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 31.01.14
 * Time: 19:02
 */
public enum AnimationState {
    IDLE, WALKING, SPRINTING;

    public boolean isIdle() {
        return this == IDLE;
    }

    public boolean isWalking() {
        return this == WALKING;
    }

    public boolean isSprinting() {
        return this == SPRINTING;
    }
}

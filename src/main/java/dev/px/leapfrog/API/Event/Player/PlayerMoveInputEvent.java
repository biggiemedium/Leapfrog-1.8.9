package dev.px.leapfrog.API.Event.Player;

public class PlayerMoveInputEvent {

    private float moveStrafe;
    private float moveForward;
    private boolean jump;
    private boolean sneak;
    private double multiplier;

    public PlayerMoveInputEvent(float moveStrafe, float moveForward, boolean jump, boolean sneak, double multiplier) {
        this.moveStrafe = moveStrafe;
        this.moveForward = moveForward;
        this.jump = jump;
        this.sneak = sneak;
        this.multiplier = multiplier;
    }

    public float getMoveStrafe() {
        return moveStrafe;
    }

    public void setMoveStrafe(float moveStrafe) {
        this.moveStrafe = moveStrafe;
    }

    public float getMoveForward() {
        return moveForward;
    }

    public void setMoveForward(float moveForward) {
        this.moveForward = moveForward;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isSneak() {
        return sneak;
    }

    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}

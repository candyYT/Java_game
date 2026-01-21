package main;

public class Dash {
    public boolean isDashing = false;
    public String dashDirection;
    public float dashProgress = 0F;
    public float dashDuration = 0.1F;
    public int dashDistance = 500;
    public int dashSpeed = 0;

    private Movement movement;

    public Dash(Movement movement) {
        this.movement = movement;
        calculateDashSpeed();
    }

    private void calculateDashSpeed() {
        int totalFrames = (int)(dashDuration * 60);
        dashSpeed = dashDistance / totalFrames;
    }

    public void startDash(char lastPressed) {
        if (!isDashing) {
            isDashing = true;
            dashProgress = 0;

            if (movement.right) {
                dashDirection = "right";
            } else if (movement.left) {
                dashDirection = "left";
            } else if (lastPressed == 'a'){
                dashDirection = "left" ;
            } else {dashDirection = "right";}
        }
    }
    public void update() {
        if (isDashing) {
            dashProgress += (float) 1 / 60;
            if (dashProgress >= dashDuration) {
                isDashing = false;
                dashProgress = 0;
            }
        }
    }
    public boolean shouldApplyDashMovement() {
        return isDashing && dashProgress < dashDuration;
    }
    public void stopDash() {
        isDashing = false;
        dashProgress = 0;
    }
}

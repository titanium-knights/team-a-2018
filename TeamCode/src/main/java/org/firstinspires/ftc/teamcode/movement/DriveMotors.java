package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Public interface representing the set of methods every single drive system is guaranteed to implement.
 * For example, both a regular two wheel drive and a mecanum drive implement these same methods.
 * This makes it useful for creating op modes that work for both systems.
 */
public interface DriveMotors {
    /**
     * Returns the underlying motors that this DriveMotors instance represents.
     * @return the underlying DcMotors represented by this instance
     */
    DcMotor[] getMotors();

    /**
     * Steers the drive in a given direction at a given power (i.e. without encoder).
     * @param power the power at which to drive the motors
     * @param turn the direction to steer the motors in (negative = left, 0 = straight, positive = right)
     */
    void steerWithPower(double power, double turn);

    /**
     * Steers the drive in a given direction at a given speed (i.e. with encoder).
     * @param speed the speed at which to drive the motors
     * @param turn the direction to steer the motors in (negative = left, 0 = straight, positive = right)
     */
    void steerWithSpeed(double speed, double turn);

    /**
     * Steers the drive forward or reverse at a given power (i.e. without encoder).
     * @param power the power at which to drive the motors (negative = reverse, positive = forward)
     */
    void forwardWithPower(double power);

    /**
     * Steers the drive forward or reverse at a given speed (i.e. with encoder).
     * @param speed the speed at which to drive the motors (negative = reverse, positive = forward)
     */
    void forwardWithSpeed(double speed);

    /**
     * Stops the drive. What could be simpler?
     */
    void stop();
}
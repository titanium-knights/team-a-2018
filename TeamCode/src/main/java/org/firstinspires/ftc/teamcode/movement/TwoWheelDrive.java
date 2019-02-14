package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Class representing a simple, run-of-the-mill two wheel drive.
 * We used to use this; now we are not.
 *
 * This was one of the earliest classes made this year; I apologize in advance for the poor coding in this class.
 * @author anli5005
 */
public class TwoWheelDrive implements DriveMotors {
    /**
     * The DcMotor mounted to the robot's left wheel.
     */
    public DcMotor leftDrive;

    /**
     * The DcMotor mounted to the robot's right wheel
     */
    public DcMotor rightDrive;

    /**
     * Number at which to multiply any powers passed in by.
     * Useful for reversing a TwoWheelDrive.
     */
    public double powerMultiplier = 1;

    /**
     * Constructs a TwoWheelDrive using two DcMotors.
     * @param leftDrive motor mounted to the left wheel
     * @param rightDrive motor mounted to the right wheel
     */
    public TwoWheelDrive(DcMotor leftDrive, DcMotor rightDrive) {
        this.leftDrive = leftDrive;
        this.rightDrive = rightDrive;
        this.setDirections(DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE);
    }

    /**
     * Calls setDirection on each of the DcMotors.
     * @param left the direction to set the left motor to
     * @param right the direction to set the right motor to
     */
    public void setDirections(DcMotor.Direction left, DcMotor.Direction right) {
        this.leftDrive.setDirection(left);
        this.rightDrive.setDirection(right);
    }

    /**
     * Sets the left motor to a given mode.
     * @param mode mode to set the left motor to
     */
    public void setLeftMode(DcMotor.RunMode mode) {
        if (this.leftDrive.getMode() != mode) {
            this.leftDrive.setMode(mode);
        }
    }

    /**
     * Sets the right motor to a given mode.
     * @param mode mode to set the right motor to
     */
    public void setRightMode(DcMotor.RunMode mode) {
        if (this.rightDrive.getMode() != mode) {
            this.rightDrive.setMode(mode);
        }
    }

    /**
     * Sets both motors to a given mode.
     * @param mode mode to set the motors to
     */
    public void setMotorMode(DcMotor.RunMode mode) {
        this.setLeftMode(mode);
        this.setRightMode(mode);
    }

    /**
     * Moves the left motor at a given power (i.e. without encoder).
     * @param power power to move at
     */
    public void setLeftPower(double power) {
        this.setLeftMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.leftDrive.setPower(power * powerMultiplier);
    }

    /**
     * Moves the right motor at a given power (i.e. without encoder).
     * @param power power to move at
     */
    public void setRightPower(double power) {
        this.setRightMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.rightDrive.setPower(power * powerMultiplier);
    }

    /**
     * Moves both motors at given powers (i.e. without encoder).
     * @param left power to move the left motor at
     * @param right power to move the right motor at
     */
    public void setPowers(double left, double right) {
        this.setLeftPower(left);
        this.setRightPower(right);
    }

    /**
     * Moves the left motor at a given speed (i.e. with encoder).
     * @param speed speed to move at
     */
    public void setLeftSpeed(double speed) {
        this.setLeftMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftDrive.setPower(speed * powerMultiplier);
    }

    /**
     * Moves the right motor at a given speed (i.e. with encoder).
     * @param speed speed to move at
     */
    public void setRightSpeed(double speed) {
        this.setRightMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightDrive.setPower(speed * powerMultiplier);
    }

    /**
     * Moves both motors at given speeds (i.e. with encoder).
     * @param left speed to move the left motor at
     * @param right speed to move the right motor at
     */
    public void setSpeeds(double left, double right) {
        this.setLeftSpeed(left);
        this.setRightSpeed(right);
    }

    public void steerWithPower(double power, double turn) {
        this.setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.leftDrive.setPower(power - turn);
        this.rightDrive.setPower(power + turn);
    }

    public void steerWithSpeed(double speed, double turn) {
        this.setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftDrive.setPower(speed - turn);
        this.rightDrive.setPower(speed + turn);
    }

    public void forwardWithPower(double power) {
        this.setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.leftDrive.setPower(power * powerMultiplier);
        this.rightDrive.setPower(power * powerMultiplier);
    }

    public void forwardWithSpeed(double speed) {
        this.setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftDrive.setPower(speed * powerMultiplier);
        this.rightDrive.setPower(speed * powerMultiplier);
    }

    @Deprecated public void moveFor(int left, int right, double leftPower, double rightPower) {
        this.setMotorMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.leftDrive.setTargetPosition(left + this.leftDrive.getCurrentPosition());
        this.leftDrive.setPower(leftPower);
        this.rightDrive.setTargetPosition(left + this.rightDrive.getCurrentPosition());
        this.rightDrive.setPower(rightPower);
    }

    @Deprecated public void moveFor(int amount, double power) {
        this.moveFor(amount, amount, power, power);
    }

    @Deprecated public boolean isBusy() {
        return this.leftDrive.isBusy() || this.rightDrive.isBusy();
    }

    @Deprecated public void waitUntilAvailable() {
        while (this.isBusy()) {}
    }

    /**
     * Stops and resets each of the motors and their encoders.
     */
    public void reset() {
        this.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void stop() {
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    public DcMotor[] getMotors() {
        return new DcMotor[]{leftDrive, rightDrive};
    }

    static String leftDriveName = "left_drive";
    static String rightDriveName = "right_drive";
    static double standardPowerMultiplier = 1;
    static DcMotor.Direction standardLeftDirection = DcMotor.Direction.REVERSE;
    static DcMotor.Direction standardRightDirection = DcMotor.Direction.FORWARD;

    /**
     * Given only a hardware map, constructs a ready-to-use two wheel drive with preset motors and parameters.
     * @param hardwareMap hardware map containing the motors installed on the robot
     * @return a "standard" two wheel drive
     */
    public static TwoWheelDrive standard(HardwareMap hardwareMap) {
        TwoWheelDrive result = new TwoWheelDrive(hardwareMap.get(DcMotor.class, leftDriveName), hardwareMap.get(DcMotor.class, rightDriveName));
        result.powerMultiplier = standardPowerMultiplier;
        result.setDirections(standardLeftDirection, standardRightDirection);
        return result;
    }
}

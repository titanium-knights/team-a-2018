package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TwoWheelDrive implements DriveMotors {
    public DcMotor leftDrive;
    public DcMotor rightDrive;

    public double powerMultiplier = 1;

    public TwoWheelDrive(DcMotor leftDrive, DcMotor rightDrive) {
        this.leftDrive = leftDrive;
        this.rightDrive = rightDrive;
        this.setDirections(DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE);
    }

    public void setDirections(DcMotor.Direction left, DcMotor.Direction right) {
        this.leftDrive.setDirection(left);
        this.rightDrive.setDirection(right);
    }

    public void setLeftMode(DcMotor.RunMode mode) {
        if (this.leftDrive.getMode() != mode) {
            this.leftDrive.setMode(mode);
        }
    }

    public void setRightMode(DcMotor.RunMode mode) {
        if (this.rightDrive.getMode() != mode) {
            this.rightDrive.setMode(mode);
        }
    }

    public void setMotorMode(DcMotor.RunMode mode) {
        this.setLeftMode(mode);
        this.setRightMode(mode);
    }

    public void setLeftPower(double power) {
        this.setLeftMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.leftDrive.setPower(power * powerMultiplier);
    }

    public void setRightPower(double power) {
        this.setRightMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.rightDrive.setPower(power * powerMultiplier);
    }

    public void setPowers(double left, double right) {
        this.setLeftPower(left);
        this.setRightPower(right);
    }

    public void setLeftSpeed(double speed) {
        this.setLeftMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftDrive.setPower(speed * powerMultiplier);
    }

    public void setRightSpeed(double speed) {
        this.setRightMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightDrive.setPower(speed * powerMultiplier);
    }

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

    public void backwardWithPower(double power) {
        this.setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.leftDrive.setPower(-(power * powerMultiplier));
        this.rightDrive.setPower(-(power * powerMultiplier));
    }

    public void backwardWithSpeed(double speed) {
        this.setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftDrive.setPower(-(speed * powerMultiplier));
        this.rightDrive.setPower(-(speed * powerMultiplier));
    }

    public void moveFor(int left, int right, double leftPower, double rightPower) {
        this.setMotorMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.leftDrive.setTargetPosition(left + this.leftDrive.getCurrentPosition());
        this.leftDrive.setPower(leftPower);
        this.rightDrive.setTargetPosition(left + this.rightDrive.getCurrentPosition());
        this.rightDrive.setPower(rightPower);
    }

    public void moveFor(int amount, double power) {
        this.moveFor(amount, amount, power, power);
    }

    public boolean isBusy() {
        return this.leftDrive.isBusy() || this.rightDrive.isBusy();
    }

    public void waitUntilAvailable() {
        while (this.isBusy()) {}
    }

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
    public static TwoWheelDrive standard(HardwareMap hardwareMap) {
        TwoWheelDrive result = new TwoWheelDrive(hardwareMap.get(DcMotor.class, leftDriveName), hardwareMap.get(DcMotor.class, rightDriveName));
        result.powerMultiplier = standardPowerMultiplier;
        result.setDirections(standardLeftDirection, standardRightDirection);
        return result;
    }
}

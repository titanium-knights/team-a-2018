package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveMotors {
    public DcMotor leftDrive;
    public DcMotor rightDrive;

    public double powerMultiplier = -1;
    public double defaultSpeed = 1;

    public DriveMotors(DcMotor leftDrive, DcMotor rightDrive) {
        this.leftDrive = leftDrive;
        this.rightDrive = rightDrive;
    }

    public void setLeftPower(double power) {
        this.leftDrive.setPower(power * powerMultiplier);
    }

    public void setRightDrive(double power) {
        this.rightDrive.setPower(power * powerMultiplier);
    }

    public void steer(double speed, double turn) {
        setLeftPower(speed - turn);
        setRightDrive(speed + turn);
    }

    public void forward() {
        this.forward(defaultSpeed);
    }

    public void forward(double speed) {
        setLeftPower(speed);
        setRightDrive(speed);
    }

    public void backward() {
        this.backward(defaultSpeed);
    }

    public void backward(double speed) {
        setLeftPower(-speed);
        setRightDrive(-speed);
    }

    public void stop() {
        setLeftPower(0);
        setRightDrive(0);
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface DriveMotors {
    DcMotor[] getMotors();
    void steerWithPower(double power, double turn);
    void steerWithSpeed(double speed, double turn);
    void forwardWithPower(double power);
    void forwardWithSpeed(double power);
    void stop();
}
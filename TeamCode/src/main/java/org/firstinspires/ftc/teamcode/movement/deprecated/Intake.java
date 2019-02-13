package org.firstinspires.ftc.teamcode.movement.deprecated;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Deprecated public class Intake {
    public DcMotor arm;
    public DcMotor extender;
    public Roller[] rollers;

    public boolean rollerExtended = false;

    public Intake(DcMotor arm, DcMotor extender, Roller[] rollers) {
        this.arm = arm;
        this.extender = extender;
        this.rollers = rollers;
    }

    public void moveToInitialPositions() {

    }

    public void setArmPower(double power) {
        arm.setPower(power);
    }

    public void stopArm() {
        setArmPower(0);
    }

    public void setExtenderPower(double power) {
        extender.setPower(power);
    }

    public void stopExtender() {
        setExtenderPower(0);
    }

    public void stopArmAndExtender() {
        stopArm();
        stopExtender();
    }

    public double armExtensionRate = 1;
    public double extenderExtensionRate = 0.5;

    public void setPower(double power) {
        setArmPower(power * armExtensionRate);
        setExtenderPower(power * extenderExtensionRate);
    }

    public void setRollerPosition(int index, int position) {
        Roller roller = rollers[index];
        roller.servo.setPosition(position);
        roller.state = Math.abs(position - roller.rollerExtendedPos) < Math.abs(position - roller.rollerRetractedPos) ? Roller.State.EXTENDED : Roller.State.RETRACTED;
    }

    public void setRollerPosition(int position) {
        for (int i = 0; i < rollers.length; i++) {
            setRollerPosition(i, position);
        }
    }

    public void extendRoller(int index) {
        Roller roller = rollers[index];
        roller.servo.setPosition(roller.rollerExtendedPos);
        roller.state = Roller.State.EXTENDED;
    }

    public void extendRollers() {
        for (int i = 0; i < rollers.length; i++) {
            extendRoller(i);
        }
    }

    public void retractRoller(int index) {
        Roller roller = rollers[index];
        roller.servo.setPosition(roller.rollerExtendedPos);
        roller.state = Roller.State.RETRACTED;
    }

    public void retractRollers() {
        for (int i = 0; i < rollers.length; i++) {
            retractRoller(i);
        }
    }

    public void toggleRoller(int index) {
        if (rollers[index].state == Roller.State.RETRACTED) {
            extendRoller(index);
        } else {
            retractRoller(index);
        }
    }

    public int rollersCount() {
        return rollers.length;
    }

    // MARK: Convenience methods for Jasper

    public void setShoulderPower(double power) {
        setArmPower(power);
    }

    public void setElbowPower(double power) {
        setExtenderPower(power);
    }

    public void setFingerPower(int index, int position) {
        setRollerPosition(index, position);
    }

    public void extendFinger(int index) {
        extendRoller(index);
    }

    public void retractFinger(int index) {
        retractRoller(index);
    }

    public void toggleFinger(int index) {
        toggleRoller(index);
    }

    // End convenience methods

    public static String armMotorName = "intake_arm_motor";
    public static String extenderMotorName = "intake_extender_motor";
    public static String rollerMotorName = "intake_roller1_servo";
    @NonNull
    public static Intake standard(HardwareMap hardwareMap) {
        Roller roller = new Roller(hardwareMap.get(Servo.class, rollerMotorName));
        Roller[] servos = {roller, roller};
        return new Intake(hardwareMap.get(DcMotor.class, armMotorName), hardwareMap.get(DcMotor.class, extenderMotorName), servos);
    }
}

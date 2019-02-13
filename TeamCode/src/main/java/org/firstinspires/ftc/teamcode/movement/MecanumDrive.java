package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class MecanumDrive implements DriveMotors {
    Motor[] motors;

    public static class Motor {
        String name = null;
        DcMotor motor;
        Vector2D vector;
        byte location = 0;

        public static class Vector2D {
            public final double x;
            public final double y;

            public Vector2D(double x, double y) {
                this.x = x;
                this.y = y;
            }

            public static double dotProduct(Vector2D a, Vector2D b) {
                return a.x * b.x + a.y * b.y;
            }

            public Vector2D rotatedBy(double angle) {
                double sinOf = Math.sin(angle);
                double cosOf = Math.cos(angle);
                return new MecanumDrive.Motor.Vector2D(x * cosOf - y * sinOf, x * sinOf + y * cosOf);
            }
        }

        public enum Location {
            FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT
        }

        public Motor(DcMotor motor, Vector2D vector, Location location) {
            this.motor = motor;
            this.vector = vector;

            if (location == Location.FRONT_LEFT) {
                this.location = 2;
            } else if (location == Location.FRONT_RIGHT) {
                this.location = 2 | 1;
            } else if (location == Location.BACK_LEFT) {
                this.location = 0;
            } else if (location == Location.BACK_RIGHT) {
                this.location = 1;
            }
        }
    }

    public MecanumDrive(Motor[] motors) {
        this.motors = motors;
    }

    public enum PowerBehavior {
        CLAMP, DIVIDE
    }

    public PowerBehavior powerBehavior = PowerBehavior.CLAMP;

    private double transformPower(double power) {
        return powerBehavior == PowerBehavior.CLAMP ? Range.clip(power, -1.0, 1.0) : power / 2;
    }

    public DcMotor[] getMotors() {
        DcMotor[] dcMotors = new DcMotor[motors.length];
        for (int i = 0; i < motors.length; i++) {
            dcMotors[i] = motors[i].motor;
        }
        return dcMotors;
    }

    public void setMotorMode(DcMotor.RunMode mode) {
        for (DcMotor motor: getMotors()) {
            motor.setMode(mode);
        }
    }

    public void move(double power, Motor.Vector2D vector, double turn) {
        for (Motor motor: motors) {
            if ((motor.location & 1) > 0) {
                motor.motor.setPower(Range.clip(transformPower(Motor.Vector2D.dotProduct(vector, motor.vector)) * power * 2 * (0.5 - turn), -1.0, 1.0));
            } else {
                motor.motor.setPower(Range.clip(transformPower(Motor.Vector2D.dotProduct(vector, motor.vector)) * power * 2 * (turn + 0.5), -1.0, 1.0));
            }
        }
    }

    public void forwardWithPower(double power) {
        setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        move(power, new Motor.Vector2D(0, 1), 0);
    }

    public void forwardWithSpeed(double power) {
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        move(power, new Motor.Vector2D(0, 1), 0);
    }

    public void strafeLeftWithPower(double power) {
        setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        move(power, new Motor.Vector2D(-1, 0), 0);
    }

    public void strafeLeftWithSpeed(double power) {
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        move(power, new Motor.Vector2D(-1, 0), 0);
    }

    public void strafeRightWithPower(double power) {
        setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        move(power, new Motor.Vector2D(1, 0), 0);
    }

    public void strafeRightWithSpeed(double power) {
        setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        move(power, new Motor.Vector2D(1, 0), 0);
    }

    public void steerWithPower(double power, double turn) {
        setMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        move(power, new Motor.Vector2D(1, 0), turn);
    }

    public void steerWithSpeed(double speed, double turn) {
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        move(speed, new Motor.Vector2D(1, 0), turn);
    }

    public void stop() {
        for (DcMotor motor: getMotors()) {
            motor.setPower(0);
        }
    }

    public void reset() {
        for (DcMotor motor: getMotors()) {
            motor.setPower(0);
        }

        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    static String[] standardMotorNames = {"mecanum_fl", "mecanum_fr", "mecanum_bl", "mecanum_br"};
    static Motor.Vector2D[] standardMotorVectors = {new Motor.Vector2D(1, 1), new Motor.Vector2D(-1, 1), new Motor.Vector2D(-1, 1), new Motor.Vector2D(1, 1)};
    static Motor.Location[] standardMotorLocations = {Motor.Location.FRONT_LEFT, Motor.Location.FRONT_RIGHT, Motor.Location.BACK_LEFT, Motor.Location.BACK_RIGHT};
    static DcMotor.Direction[] standardMotorDirections = {DcMotor.Direction.REVERSE, DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE, DcMotor.Direction.FORWARD};
    public static MecanumDrive standard(HardwareMap hardwareMap) {
        Motor[] motors = new Motor[standardMotorNames.length];
        for (int i = 0; i < motors.length; i++) {
            DcMotor motor = hardwareMap.get(DcMotor.class, standardMotorNames[i]);
            motor.setDirection(standardMotorDirections[i]);
            motors[i] = new Motor(motor, standardMotorVectors[i], standardMotorLocations[i]);
        }
        return new MecanumDrive(motors);
    }
}
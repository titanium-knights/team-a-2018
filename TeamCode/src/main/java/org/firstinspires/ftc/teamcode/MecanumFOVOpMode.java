package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "Mecanum FOV Op Mode", group = "Tests")
public class MecanumFOVOpMode extends OpMode {
    MecanumDrive drive;
    Gamepad gamepad;

    @Override public void init() {
        drive = MecanumDrive.standard(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override public void start() {
        gamepad = gamepad1;
    }

    public void loop() {
        MecanumDrive.Motor.Vector2D vector = new MecanumDrive.Motor.Vector2D(gamepad.left_stick_x, -gamepad.left_stick_y);
        double turn = gamepad.right_stick_x;

        drive.move(1, vector, turn);

        telemetry.addData("Status", "Driving");
        telemetry.addData("Forward Power", vector.y);
        telemetry.addData("Strafe", (vector.x == 0 ? "None " : (vector.x < 0 ? "Left " : "Right ")) + Math.abs(vector.x));
        telemetry.addData("Turn", (turn == 0 ? "None " : (turn < 0 ? "Left " : "Right ")) + Math.abs(turn));
    }
}

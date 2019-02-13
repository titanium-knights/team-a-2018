package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.movement.MecanumDrive;

@Autonomous(name = "Mecanum Tests", group = "Tests")
public class MecanumTestOpMode extends LinearOpMode {
    MecanumDrive drive;

    public void runOpMode() {
        MecanumDrive drive = MecanumDrive.standard(hardwareMap);
        drive.setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        drive.forwardWithPower(0.5);
        sleep(2000);

        drive.strafeLeftWithPower(0.5);
        sleep(2000);

        drive.strafeRightWithPower(0.5);
        sleep(2000);

        drive.move(0.5, new MecanumDrive.Motor.Vector2D(1, 1), 0);
        sleep(2000);

        drive.steerWithPower(0.5, 1);
        sleep(2000);

        requestOpModeStop();
    }
}

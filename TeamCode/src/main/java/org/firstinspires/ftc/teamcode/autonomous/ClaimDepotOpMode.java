package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.movement.DriveMotors;
import org.firstinspires.ftc.teamcode.movement.deprecated.Intake;
import org.firstinspires.ftc.teamcode.movement.MecanumDrive;

@Autonomous(name = "Claim Depot Mode", group = "* Main")
public class ClaimDepotOpMode extends LinearOpMode {
    DriveMotors driveMotors;
    Intake intake;

    @Override
    public void runOpMode() {
        driveMotors = MecanumDrive.standard(hardwareMap);
        intake = Intake.standard(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        telemetry.addData("Status", "Moving");
        telemetry.update();
        driveMotors.forwardWithPower(0.3);
        sleep(9000);
        driveMotors.stop();

        telemetry.addData("Status", "Raising arm");
        telemetry.update();
        intake.setArmPower(1);
        sleep(60);
        intake.stopArm();

        telemetry.addData("Status", "Complete");
        telemetry.update();
    }
}
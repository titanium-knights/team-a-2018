package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Claim Depot Mode", group = "* Main")
public class ClaimDepotOpMode extends LinearOpMode {
    DriveMotors driveMotors;
    Intake intake;

    @Override
    public void runOpMode() {
        driveMotors = DriveMotors.standard(hardwareMap);
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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Mineral Knocker", group = "Tests")
public class MineralKnockerOpMode extends LinearOpMode {
    DriveMotors driveMotors;
    MineralKnocker knocker;

    final double turnPower = 0.5;
    final double forwardPower = 0.5;

    @Override
    public void runOpMode() {
        driveMotors = DriveMotors.standard(hardwareMap);
        knocker = new MineralKnocker();
        knocker.mineralDetection = new MineralDetection(hardwareMap);
        knocker.mineralDetection.init();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        knocker.mineralDetection.activate();

        telemetry.addData("Status", "Measuring");

        int lastCount = -1;
        while (knocker.countMeasurements() < 10) {
            if (knocker.countMeasurements() != lastCount) {
                lastCount = knocker.countMeasurements();
                telemetry.addData("Measurements Taken", knocker.countMeasurements());
                telemetry.update();
            }
            knocker.measure();
        }

        telemetry.addData("Status", "Turning");

        int targetPos = (int)knocker.getTurnAmount();
        telemetry.addData("Target Position", targetPos);
        telemetry.addData("Average Position", knocker.averageMeasurements());
        telemetry.update();
        driveMotors.moveFor(targetPos, -targetPos, turnPower, turnPower);
        driveMotors.waitUntilAvailable();

        telemetry.addData("Status", "Moving Forward");
        telemetry.update();

        driveMotors.moveFor(360, forwardPower);
        driveMotors.waitUntilAvailable();

        telemetry.addData("Status", "Complete");
        telemetry.update();

        requestOpModeStop();

        while (opModeIsActive()) {}

        knocker.mineralDetection.shutdown();
    }
}

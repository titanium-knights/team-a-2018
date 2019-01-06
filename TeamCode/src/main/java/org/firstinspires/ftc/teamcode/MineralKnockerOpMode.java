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

    private double wheelCircumference = 4 * Math.PI;
    private double ticksPerRotation = 1100;

    private double distanceToMinerals = 9;
    private double distanceToMoveForward = 5;

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

        telemetry.addData("Status", "Moving to minerals");
        driveMotors.moveFor((int)((distanceToMinerals / wheelCircumference) * ticksPerRotation), forwardPower);
        driveMotors.waitUntilAvailable();

        telemetry.addData("Status", "Measuring");

        int lastCount = -1;
        while (knocker.countMeasurements() < 10) {
            if (knocker.countMeasurements() != lastCount) {
                lastCount = knocker.countMeasurements();
                telemetry.addData("Measurements Taken", knocker.countMeasurements());
                telemetry.addData("Average Position", knocker.averageMeasurements());
                telemetry.update();
            }
            knocker.measure();
        }

        telemetry.addData("Status", "Turning");

        int targetPos = (int)knocker.getTurnAmount();
        telemetry.addData("Average Position", knocker.averageMeasurements());
        telemetry.addData("Will move", targetPos);
        telemetry.update();
        driveMotors.moveFor(targetPos, -targetPos, turnPower, turnPower);
        driveMotors.waitUntilAvailable();

        telemetry.addData("Status", "Knocking");
        telemetry.update();

        driveMotors.moveFor((int)((distanceToMoveForward / wheelCircumference) * ticksPerRotation), forwardPower);
        driveMotors.waitUntilAvailable();

        telemetry.addData("Status", "Complete");
        telemetry.update();

        requestOpModeStop();

        while (opModeIsActive()) {}

        knocker.mineralDetection.shutdown();
    }
}

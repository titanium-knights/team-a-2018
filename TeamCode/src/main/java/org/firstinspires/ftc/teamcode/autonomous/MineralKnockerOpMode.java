package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.movement.DriveMotors;
import org.firstinspires.ftc.teamcode.movement.TwoWheelDrive;
import org.firstinspires.ftc.teamcode.sensing.MineralDetection;
import org.firstinspires.ftc.teamcode.sensing.MineralKnocker;

@Autonomous(name = "Mineral Knocker", group = "Tests")
public class MineralKnockerOpMode extends LinearOpMode {
    DriveMotors driveMotors;
    MineralKnocker knocker;

    final static double turnPower = 0.5;
    final double instanceTurnPower = 0.5;
    final static double forwardPower = 0.5;
    final double instanceForwardPower = forwardPower;

    private long moveTime = 9000;

    @Override
    public void runOpMode() {
        driveMotors = TwoWheelDrive.standard(hardwareMap);
        knocker = new MineralKnocker();
        knocker.mineralDetection = new MineralDetection(hardwareMap);
        knocker.mineralDetection.init();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        knocker.mineralDetection.activate();

        driveMotors.forwardWithPower(0.3);
        sleep(100);
        driveMotors.stop();

        telemetry.addData("Status", "Measuring");

        int lastCount = -1;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 10000) {
            if (knocker.countMeasurements() != lastCount) {
                lastCount = knocker.countMeasurements();
                telemetry.addData("Measurements Taken", knocker.countMeasurements());
                telemetry.addData("Average Position", knocker.averageMeasurements());
                telemetry.update();
            }
            knocker.measure();

            if (!opModeIsActive()) {
                knocker.mineralDetection.shutdown();
                return;
            }
        }

        telemetry.addData("Status", "Turning");

        int targetPos = knocker.countMeasurements() < 1 ? 0 : (int)knocker.getTurnAmount();
        telemetry.addData("Average Position", knocker.averageMeasurements());
        telemetry.addData("Will move", targetPos);
        telemetry.update();
        driveMotors.steerWithPower(instanceTurnPower, (targetPos >= 0 ? 1 : -1));
        sleep(Math.abs(targetPos));
        driveMotors.stop();

        telemetry.addData("Status", "Moving forward");
        telemetry.addData("Moved", targetPos);
        telemetry.update();

        driveMotors.forwardWithPower(instanceForwardPower);
        sleep(moveTime);
        driveMotors.stop();

        telemetry.addData("Status", "Complete");
        telemetry.update();

        while (opModeIsActive()) {}

        knocker.mineralDetection.shutdown();
    }
}

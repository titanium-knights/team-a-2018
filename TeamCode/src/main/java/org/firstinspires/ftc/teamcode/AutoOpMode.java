package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcontroller.external.samples.BasicOpMode_Linear;

@Autonomous(name = "Main Autonomous OpMode", group = "* Main")
@Disabled
public class AutoOpMode extends BasicOpMode_Linear {
    DriveMotors driveMotors;
    Intake intake;
    MineralKnocker knocker;
    Vision vision;

    private void updateLocationTelemetry(Vision.Location location) {
        telemetry.addData("Location", String.format("(%.1f, %.1f, %.1f)", location.x, location.y, location.z));
        telemetry.addData("Orientation", String.format("Roll %.1f, Pitch %.1f, Heading %.1f;", location.roll, location.pitch, location.heading));
        telemetry.addData("Visible Target", location.visibleTarget == null ? "None" : location.visibleTarget);
        telemetry.update();
    }

    @Override public void runOpMode() {
        driveMotors = DriveMotors.standard(hardwareMap);
        intake = Intake.standard(hardwareMap);
        intake.moveToInitialPositions();

        vision = new Vision(hardwareMap);
        vision.init();

        knocker = new MineralKnocker();
        knocker.mineralDetection = vision;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        vision.activate();

        telemetry.addData("Status", "Getting location");
        telemetry.update();

        Vision.Location location = null;
        while (location == null || location.isStale) {
            location = vision.getLocation();
        }

        while (opModeIsActive()) {
            updateLocationTelemetry(location);
            location = vision.getLocation();
        }
    }
}

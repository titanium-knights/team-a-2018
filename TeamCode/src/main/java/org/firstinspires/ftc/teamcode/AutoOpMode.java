package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.BasicOpMode_Linear;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.ref.WeakReference;

abstract class AutoOpModeStage {
    WeakReference<AutoOpMode> autoOpMode;

    DriveMotors driveMotors() {
        return autoOpMode.get().driveMotors;
    }
    Intake intake() {
        return autoOpMode.get().intake;
    }
    Vision vision() {
        return autoOpMode.get().vision;
    }
    HardwareMap hardwareMap() {
        return autoOpMode.get().hardwareMap;
    }
    Telemetry telemetry() {
        return autoOpMode.get().telemetry;
    }
    void sleep(long milliseconds) {
        autoOpMode.get().sleep(milliseconds);
    }

    public AutoOpModeStage(AutoOpMode auto) {
        autoOpMode = new WeakReference<>(auto);
    }

    abstract String getName();
    abstract void runStage();

    boolean shouldRunStage(ElapsedTime elapsedTime) {
        return true;
    }
}

class KnockMineralStage extends AutoOpModeStage {
    MineralKnocker knocker;
    double forwardPower = MineralKnockerOpMode.forwardPower;
    double turnPower = MineralKnockerOpMode.turnPower;
    long moveTime = 1500;

    public KnockMineralStage(AutoOpMode auto) {
        super(auto);
    }

    String getName() {
        return "Mineral Knocker";
    }

    void runStage() {
        knocker = new MineralKnocker();
        knocker.mineralDetection = vision();

        int lastCount = -1;
        double startTime = autoOpMode.get().elapsedTime.milliseconds();
        while (lastCount < 10 || System.currentTimeMillis() - startTime < 10000) {
            if (knocker.countMeasurements() != lastCount) {
                lastCount = knocker.countMeasurements();
                telemetry().addData("Measurements Taken", knocker.countMeasurements());
                telemetry().addData("Average Position", knocker.averageMeasurements());
                telemetry().update();
            }
            knocker.measure();
        }

        telemetry().addData("Status", "Turning");

        int targetPos = knocker.countMeasurements() < 1 ? 0 : (int)knocker.getTurnAmount();
        telemetry().addData("Average Position", knocker.averageMeasurements());
        telemetry().addData("Will move", targetPos);
        telemetry().update();
        double adjustedPower = turnPower * (targetPos >= 0 ? 1 : -1);
        driveMotors().setSpeeds(adjustedPower, -adjustedPower);
        sleep(Math.abs(targetPos));
        driveMotors().stop();

        telemetry().addData("Status", "Moving forward");
        telemetry().addData("Moved", targetPos);
        telemetry().update();

        driveMotors().forwardWithPower(forwardPower);
        sleep(moveTime);
        driveMotors().stop();
    }
}

class DriveToDepotFromCraterStage extends AutoOpModeStage {
    public DriveToDepotFromCraterStage(AutoOpMode auto) {
        super(auto);
    }

    String getName() {
        return "Drive to Depot From Crater";
    }

    void runStage() {
        sleep(1000);
    }
}

class DriveToDepotStage extends AutoOpModeStage {
    public DriveToDepotStage(AutoOpMode auto) {
        super(auto);
    }

    String getName() {
        return "Drive to Depot";
    }

    void runStage() {
        sleep(1000);
    }
}

class ClaimDepotStage extends AutoOpModeStage {
    public ClaimDepotStage(AutoOpMode auto) {
        super(auto);
    }

    String getName() {
        return "Claim Depot";
    }

    void runStage() {
        sleep(1000);
    }
}

class ParkStage extends AutoOpModeStage {
    public ParkStage(AutoOpMode auto) {
        super(auto);
    }

    String getName() {
        return "Park";
    }

    void runStage() {
        sleep(1000);
    }
}

@Autonomous(name = "Main Autonomous OpMode", group = "* Main")
public class AutoOpMode extends BasicOpMode_Linear {
    DriveMotors driveMotors;
    Intake intake;
    Vision vision;
    AutoOpModePath pathChosen;
    AutoOpModeStage[] stages;
    ElapsedTime elapsedTime;

    enum AutoOpModePath {
        START_AT_CRATER, START_AT_DEPOT
    }

    private void updateLocationTelemetry(Vision.Location location) {
        telemetry.addData("Location", String.format("(%.1f, %.1f, %.1f)", location.x, location.y, location.z));
        telemetry.addData("Orientation", String.format("Roll %.1f, Pitch %.1f, Heading %.1f;", location.roll, location.pitch, location.heading));
        telemetry.addData("Visible Target", location.visibleTarget == null ? "None" : location.visibleTarget);
        telemetry.update();
    }

    private void updatePathTelemetry() {
        if (pathChosen == AutoOpModePath.START_AT_CRATER) {
            telemetry.addData("Path Chosen", "Starting at crater, knocking, claiming, parking");
        } else {
            telemetry.addData("Path Chosen", "Starting at depot, knocking, claiming, parking");
        }
    }

    @Override public void runOpMode() {
        elapsedTime = new ElapsedTime();

        driveMotors = DriveMotors.standard(hardwareMap);
        intake = Intake.standard(hardwareMap);
        intake.moveToInitialPositions();

        vision = new Vision(hardwareMap);
        vision.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        elapsedTime.reset();

        vision.activate();

        telemetry.addData("Status", "Getting location");
        telemetry.update();

        Vision.Location location = null;
        while (location == null || location.isStale) {
            location = vision.getLocation();
            if (!opModeIsActive()) {
                return;
            }
        }

        updateLocationTelemetry(location);
        pathChosen = AutoOpModePath.START_AT_CRATER;

        if (pathChosen == AutoOpModePath.START_AT_CRATER) {
            stages = new AutoOpModeStage[]{
                    new KnockMineralStage(this),
                    new DriveToDepotFromCraterStage(this),
                    new ClaimDepotStage(this),
                    new ParkStage(this)
            };
        } else {
            stages = new AutoOpModeStage[]{
                    new KnockMineralStage(this),
                    new DriveToDepotStage(this),
                    new ClaimDepotStage(this),
                    new ParkStage(this)
            };
        }

        telemetry.addData("Status", "Running");

        for (int i = 0; i < stages.length; i++) {
            AutoOpModeStage stage = stages[i];
            if (!stage.shouldRunStage(elapsedTime) || !opModeIsActive()) {
                telemetry.addData("Status", "Stopped Prematurely");
                telemetry.addData("Stage", String.format("%d/%d - %s", i + 1, stages.length, stage.getName()));
                updatePathTelemetry();
                telemetry.update();
                break;
            }

            telemetry.addData("Stage", String.format("%d/%d - %s", i + 1, stages.length, stage.getName()));
            updatePathTelemetry();
            telemetry.update();
            stage.runStage();

            if (i == stages.length - 1) {
                telemetry.addData("Status", "Complete");
                updatePathTelemetry();
                telemetry.update();
            }
        }

        while (opModeIsActive()) {}

        vision.shutdown();
    }
}

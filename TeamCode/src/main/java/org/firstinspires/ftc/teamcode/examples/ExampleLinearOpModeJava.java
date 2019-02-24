package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.sensing.Gyro;
import org.firstinspires.ftc.teamcode.sensing.IMUGyro;
import org.firstinspires.ftc.teamcode.sensing.Vision;

/**
 * A bare-bones linear op mode in Java.
 * Runs by calling a method once and is slightly simpler to implement than an iterative op mode.
 * Good for Autonomous op modes.
 */
@Autonomous(name = "NAME HERE") // TODO: Add a name and group
@Disabled // TODO: Delete this line to enable your op mode
public class ExampleLinearOpModeJava extends LinearOpMode {
    @Override public void runOpMode() {
        MecanumDrive drive = MecanumDrive.standard(hardwareMap); // Comment this line if you, for some reason, don't need to use the drive motors

        // Uncomment the next three lines if you need to use the gyroscope
        // Gyro gyro = IMUGyro.standard(hardwareMap);
        // gyro.initialize();
        // gyro.calibrate();

        // Uncomment the next two lines if you need to use the vision system
        // Vision vision = new Vision(hardwareMap);
        // vision.init();

        // Uncomment the next line if you have a servo
        // Servo myServo = hardwareMap.get(Servo.class, "myServo");

        // TODO: Place any code that should run when INIT is pressed here.

        telemetry.addData("Status", "Initialized");
        telemetry.update(); // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        waitForStart();

        // Uncomment the next line if you need to use the vision system
        // vision.activate();

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        // TIPS:
        // To access the gamepads, use gamepad1 or gamepad2.
        // See the MecanumDrive docs to learn how to drive the robot.
        //
        // If you want to stop the op mode once you're done, call requestOpModeStop().

        // Uncomment the next line if you need to use the vision system
        // vision.shutdown();
    }
}

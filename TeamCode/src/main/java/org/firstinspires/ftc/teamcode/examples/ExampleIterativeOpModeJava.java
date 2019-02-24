package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.movement.MecanumDrive;
import org.firstinspires.ftc.teamcode.sensing.Gyro;
import org.firstinspires.ftc.teamcode.sensing.IMUGyro;
import org.firstinspires.ftc.teamcode.sensing.Vision;

@TeleOp(name = "NAME HERE") // TODO: Add a group name
@Disabled // TODO: Delete this line to enable your op mode
public class ExampleIterativeOpModeJava extends OpMode {
    private MecanumDrive drive;
    private Gyro gyro;
    private Vision vision;

    // To add a servo, use this code, and uncomment the corresponding line in init:
    // private Servo myServo;

    @Override public void init() {
        drive = MecanumDrive.standard(hardwareMap); // Comment this line if you, for some reason, don't need to use the drive motors

        // Uncomment the next three lines if you need to use the gyroscope
        // gyro = IMUGyro.standard(hardwareMap);
        // gyro.initialize();
        // gyro.calibrate();

        // Uncomment the next two lines if you need to use the vision system
        // vision = new Vision(hardwareMap);
        // vision.init();

        // Uncomment the next line if you have a servo
        // myServo = hardwareMap.get(Servo.class, "myServo");

        /** Place any code that should run when INIT is pressed here. **/
    }

    @Override public void init_loop() {
        // If the gyro is done calibrating, let the drivers know.
        if (gyro.isCalibrated()) {
            telemetry.addData("Gyro", "Calibrated");
        }

        /** Place any code that should repeatedly run after INIT is pressed but before the op mode starts here. **/
    }

    @Override public void start() {
        // Uncomment the next line if you need to use the vision system
        // vision.activate();

        /** Place any code that should run once when the op mode starts here. **/
    }

    @Override public void loop() {
        /** Place any code that should repeatedly run here. **/

        // TIPS:
        // To access the gamepads, use gamepad1 or gamepad2.
        // See the MecanumDrive docs to learn how to drive the robot.
        //
        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }

    @Override public void stop() {
        // Uncomment the next line if you need to use the vision system
        // vision.activate();

        /** Place any code that should run once the op mode stops here. **/
    }
}

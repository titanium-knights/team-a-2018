package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensing.Gyro
import org.firstinspires.ftc.teamcode.sensing.IMUGyro
import org.firstinspires.ftc.teamcode.sensing.Vision

/**
 * A bare-bones linear op mode.
 * Runs by calling a method once and is slightly simpler to implement than an iterative op mode.
 * Good for Autonomous op modes.
 */
@Autonomous(name = "NAME HERE") // TODO: Add a good name
@Disabled // TODO: Delete this line to enable your op mode
class ExampleLinearOpMode: LinearOpMode() {
    val drive: MecanumDrive by lazy { MecanumDrive.standard(hardwareMap) }
    val gyro: Gyro by lazy { IMUGyro.standard(hardwareMap) }
    val vision: Vision by lazy { Vision(hardwareMap) }

    override fun runOpMode() {
        drive // Comment this line if you, for some reason, don't need to use the drive motors

        // Uncomment the next few lines if you need to use the gyroscope
        // gyro.initialize()
        // gyro.calibrate()
        // while (!gyro.isCalibrated && !isStopRequested) { idle() } // Wait for the gyro to be calibrated.

        // Uncomment the next few lines if you need to use the vision system
        // vision.init()

        // To add a servo, use this code:
        // val myServo = hardwareMap.get(Servo::class.java, "myServo")

        // TODO: Place any code that should run when INIT is pressed here.

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        // TIPS:
        // To access the gamepads, use gamepad1 or gamepad2.
        // See the MecanumDrive docs to learn how to drive the robot.
        //
        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
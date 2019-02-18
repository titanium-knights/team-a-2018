package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.max


/**
 * Tests REV Core Hex Motor on Intake Roller
 */
@TeleOp(name = "Servo Lifting Test", group = "Tests") // TODO: Add a good name
class ServoTestOpMode: LinearOpMode() {

    var tilter: Servo? = null

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        tilter = hardwareMap.get(Servo::class.java, "elevator_tilter")

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            val power = max(0.0, gamepad1.left_stick_y.toDouble())*180
            tilter!!.position = power
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
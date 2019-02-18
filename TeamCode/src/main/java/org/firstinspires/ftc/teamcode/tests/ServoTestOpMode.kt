package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.max
import kotlin.math.min


/**
 * Tests REV Core Hex Motor on Intake Roller
 */
@TeleOp(name = "Servo Lifting Test", group = "Tests") // TODO: Add a good name
class ServoTestOpMode: LinearOpMode() {

    var tilter: Servo? = null
    var elevatorMotor: DcMotor? = null
    var currPos = 1.0

    val maximumElevatorHeight = 950

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        tilter = hardwareMap.get(Servo::class.java, "elevator_tilter")
        elevatorMotor = hardwareMap.get(DcMotor::class.java, "elevator_extender")

        elevatorMotor!!.mode = DcMotor.RunMode.RUN_USING_ENCODER

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            var power2 = gamepad1.right_stick_y.toDouble()*-0.5
            if (elevatorMotor!!.currentPosition <= 0) power2 = max(0.0, power2)
            else if (elevatorMotor!!.currentPosition >= maximumElevatorHeight) power2 = min(0.0, power2)
            elevatorMotor!!.power = power2
            if (currPos < 1.0 && !gamepad1.left_bumper) currPos+=0.01
            else if (gamepad1.left_bumper) currPos = 40.0/180.0
            tilter!!.position = currPos
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
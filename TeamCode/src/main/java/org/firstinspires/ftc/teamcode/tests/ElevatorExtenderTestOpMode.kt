package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Tests REV Core Hex Motor on LinearMotion Extender
 */
@TeleOp(name = "LinearMotion Test", group = "Tests") // TODO: Add a good name
@Disabled
class ElevatorExtenderTestOpMode: LinearOpMode() {

    var extenderMotor: DcMotor? = null

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        extenderMotor = hardwareMap.get(DcMotor::class.java, "elevator_extender")

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            val power = gamepad1.left_stick_y.toDouble()*-0.5
            extenderMotor!!.power = power
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
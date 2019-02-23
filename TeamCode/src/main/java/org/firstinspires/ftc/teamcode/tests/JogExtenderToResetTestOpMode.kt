package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Tests REV Core Hex Motor on OldIntake Roller
 */
@TeleOp(name = "Reset Extender Test", group = "Tests") // TODO: Add a good name
@Disabled
class JogExtenderToResetTestOpMode: LinearOpMode() {

    var extenderMotor: DcMotor? = null

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        extenderMotor = hardwareMap.get(DcMotor::class.java, "intake_extender")

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            extenderMotor!!.power = if (gamepad1.left_bumper) -1.0 else 0.0
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Tests REV Core Hex Motor on Intake Roller
 */
@TeleOp(name = "Intake Roller Test", group = "Tests") // TODO: Add a good name
class IntakeRollerTestOpMode: LinearOpMode() {

    var rollerMotor: DcMotor? = null

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        rollerMotor = hardwareMap.get(DcMotor::class.java, "intake_roller")

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            val power = gamepad1.left_stick_y.toDouble()
            rollerMotor!!.power = power
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
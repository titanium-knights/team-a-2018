package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Tests REV Core Hex Motor on both Intake Extender and Elevator
 */
@TeleOp(name = "Combined Test", group = "Tests") // TODO: Add a good name
class IntakeElevatorTestOpMode: LinearOpMode() {

    var rollerMotor: DcMotor? = null
    var elevatorMotor: DcMotor? = null

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        rollerMotor = hardwareMap.get(DcMotor::class.java, "intake_extender")
        elevatorMotor = hardwareMap.get(DcMotor::class.java, "elevator_extender")

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            val power = gamepad1.left_stick_y.toDouble()
            val power2 = gamepad1.right_stick_y.toDouble()*-0.5
            rollerMotor!!.power = power
            elevatorMotor!!.power = power2
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
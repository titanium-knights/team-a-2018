package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.max
import kotlin.math.min

/**
 * Tests REV Core Hex Motor on both Intake Extender and Elevator
 */
@TeleOp(name = "Combined Test", group = "Tests") // TODO: Add a good name
class IntakeElevatorTestOpMode: LinearOpMode() {

    val maximumElevatorHeight = 950
    val maximumIntakeExtension = 3250

    var extenderMotor: DcMotor? = null
    var elevatorMotor: DcMotor? = null
    var tilterMotor: DcMotor? = null
    var rollerMotor: DcMotor? = null

    override fun runOpMode() {

        // TODO: Place any code that should run when INIT is pressed here

        telemetry.addData("Status", "Initialized")
        telemetry.update() // NOTE: In a LinearOpMode, you will need to call update() whenever you modify telemetry data.

        extenderMotor = hardwareMap.get(DcMotor::class.java, "intake_extender")
        elevatorMotor = hardwareMap.get(DcMotor::class.java, "elevator_extender")
        tilterMotor = hardwareMap.get(DcMotor::class.java, "intake_tilter")
        rollerMotor = hardwareMap.get(DcMotor::class.java, "intake_roller")

        elevatorMotor!!.mode = DcMotor.RunMode.RUN_USING_ENCODER
        tilterMotor!!.mode = DcMotor.RunMode.RUN_USING_ENCODER

        waitForStart()

        // TODO: Place any code that should run once the op mode starts here.
        // Use opModeIsActive() to check whether the robot should stop.

        while (opModeIsActive()) {
            telemetry.addData("Elevator Pos", elevatorMotor!!.currentPosition)
            telemetry.addData("Intake Extender Pos", extenderMotor!!.currentPosition)
            telemetry.update()
            val power = gamepad1.left_stick_y.toDouble()
            var power2 = gamepad1.right_stick_y.toDouble()*-0.5
            if (elevatorMotor!!.currentPosition <= 0) power2 = max(0.0, power2)
            else if (elevatorMotor!!.currentPosition >= maximumElevatorHeight) power2 = min(0.0, power2)
            // TODO: The tilter needs better control and lower power
            tilterMotor!!.power = power*0.5
            elevatorMotor!!.power = power2
            if (gamepad1.left_bumper && extenderMotor!!.currentPosition >= 50) extenderMotor!!.power = -1.0
            else if (gamepad1.right_bumper && extenderMotor!!.currentPosition <= maximumIntakeExtension) extenderMotor!!.power = 1.0
            else extenderMotor!!.power = 0.0
            if (gamepad1.left_trigger == 1.0f) rollerMotor!!.power = 1.0
            else if (gamepad1.right_trigger == 1.0f) rollerMotor!!.power = -1.0
            else rollerMotor!!.power = 0.0
        }

        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }
}
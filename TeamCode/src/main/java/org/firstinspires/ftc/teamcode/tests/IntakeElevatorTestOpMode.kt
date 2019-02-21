package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.movement.ElevatorExtake
import org.firstinspires.ftc.teamcode.movement.ElevatorIntake

/**
 * Tests REV Core Hex Motor on both OldIntake Extender and LinearMotion
 */
@TeleOp(name = "Combined Test", group = "Tests") // TODO: Add a good name
class IntakeElevatorTestOpMode: OpMode() {
    val intake: ElevatorIntake by lazy { ElevatorIntake.standard(hardwareMap) }
    val extake: ElevatorExtake by lazy { ElevatorExtake.standard(hardwareMap) }

    override fun init() {
        intake.binMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        extake

        telemetry.addData("Status", "Initialized")
    }

    override fun loop() {
        telemetry.addData("Linear Motion Pos", extake.currentPosition)
        telemetry.addData("Intake Extender Pos", intake.currentPosition)

        intake.moveBin(gamepad1.left_stick_y.toDouble())
        extake.moveIfAble(gamepad1.right_stick_y.toDouble() * -0.5)
        intake.moveIfAble(when {
            gamepad1.left_bumper -> -1.0
            gamepad1.right_bumper -> 1.0
            else -> 0.0
        })
        intake.moveRoller(when {
            gamepad1.left_trigger == 1.0f -> -1.0
            gamepad1.right_trigger == 1.0f -> 1.0
            else -> 0.0
        })
    }
}
package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Mecanum Absolute Positioning", group = "Tests")
class AbsolutePositioningOpMode: OpMode() {
    var mecanumDrive: MecanumDrive? = null
    var gyro: Gyro? = null
    var baseAngle = 0.0

    val driveGamepad get() = gamepad1

    override fun init() {
        mecanumDrive = MecanumDrive.standard(hardwareMap)

        gyro = IMUGyro.standard(hardwareMap)
        gyro!!.initialize()
        gyro!!.calibrate()
    }

    override fun init_loop() {
        if (gyro!!.isCalibrated) {
            telemetry.addData("Status", "Initialized")
        }
    }

    override fun start() {
        telemetry.addData("Status", "Started")
    }

    override fun loop() {
        val angle = gyro!!.getAbsoluteAngle()

        if (gamepad1.start) {
            baseAngle = angle
        }

        val transformedAngle = (angle - baseAngle) % 180
        val radians = Math.toRadians(transformedAngle)

        val vector = MecanumDrive.Motor.Vector2D(gamepad1.left_stick_x.toDouble(), (-gamepad1.left_stick_y).toDouble())
        val rotated = vector.rotatedBy(radians)
        val turn = driveGamepad.right_stick_x.toDouble()
        mecanumDrive!!.move(1.0, rotated, turn)

        telemetry.addData("Base Angle", baseAngle)
        telemetry.addData("Current Angle", angle)
        telemetry.addData("Transformed Angle", transformedAngle)
        telemetry.addData("Input X", vector.x)
        telemetry.addData("Input Y", vector.y)
        telemetry.addData("Output X", rotated.x)
        telemetry.addData("Output Y", rotated.y)
        telemetry.addData("Turn", turn)
    }
}
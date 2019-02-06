package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "Gyro Test", group = "Tests")
class GyroTestOpMode: LinearOpMode() {
    fun doIdle() {
        sleep(50)
        idle()
    }

    override fun runOpMode() {
        val gyro: Gyro = IMUGyro.standard(hardwareMap)

        gyro.calibrate()
        while (!isStopRequested && !gyro.isCalibrated) {
            doIdle()
        }

        telemetry.addData("Status", "Initialized")
        telemetry.update()
        waitForStart()

        telemetry.addData("Status", "Measuring")
        telemetry.update()

        while (opModeIsActive()) {
            telemetry.addData("Angle", gyro.getAbsoluteAngle())
            telemetry.update()
        }
    }
}
package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.sensing.Gyro
import org.firstinspires.ftc.teamcode.sensing.IMUGyro

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
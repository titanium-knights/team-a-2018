package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference

interface Gyro {
    fun initialize()
    fun calibrate()
    val isCalibrated: Boolean
    fun getAbsoluteAngle(): Double
}

class IMUGyro(val imu: BNO055IMU, val parameters: BNO055IMU.Parameters): Gyro {
    override fun initialize() {
        imu.initialize(parameters)
    }

    override fun calibrate() {}
    override val isCalibrated get() = imu.isGyroCalibrated

    var angleModifier = -1.0

    override fun getAbsoluteAngle(): Double {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle.toDouble() * angleModifier
    }

    companion object Factory {
        val IMU_NAME = "imu"
        val IMU_PARAMETERS: BNO055IMU.Parameters get() {
            val parameters = BNO055IMU.Parameters()
            parameters.mode = BNO055IMU.SensorMode.IMU
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
            parameters.loggingEnabled = false
            return parameters
        }

        @JvmStatic fun standard(hardwareMap: HardwareMap): IMUGyro {
            return IMUGyro(hardwareMap[BNO055IMU::class.java, IMU_NAME], IMU_PARAMETERS)
        }
    }
}
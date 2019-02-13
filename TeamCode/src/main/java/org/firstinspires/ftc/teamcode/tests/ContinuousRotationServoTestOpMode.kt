package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo

@TeleOp(name = "Continuous Rotation Servo Test", group = "Tests")
class ContinuousRotationServoTestOpMode: OpMode() {
    var crServo: CRServo? = null

    override fun init() {
        crServo = hardwareMap.get(CRServo::class.java, "crServo")
    }

    override fun loop() {
        crServo!!.power = gamepad2.right_stick_y.toDouble()
    }
}
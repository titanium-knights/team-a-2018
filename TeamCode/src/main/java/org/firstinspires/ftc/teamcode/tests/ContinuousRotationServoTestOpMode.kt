package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo

@TeleOp(name = "Continuous Rotation Servo Test", group = "Tests")
@Disabled
class ContinuousRotationServoTestOpMode: OpMode() {
    var crServo: CRServo? = null

    override fun init() {
        crServo = hardwareMap.get(CRServo::class.java, "crServo")
    }

    override fun loop() {
        crServo!!.power = if (gamepad2.right_stick_y > 0.5) 1.0 else (if (gamepad2.right_stick_y < 0.5) -1.0 else 0.0)
    }
}
package org.firstinspires.ftc.teamcode.utils

import com.qualcomm.robotcore.hardware.DcMotor

fun DcMotor.stopAndResetEncoder() {
    val prevMode = mode
    mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    mode = prevMode
}
package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.sensing.Vision

@Autonomous(name = "Vision Location Test Op Mode", group = "Tests")
class VisionLocationTestOpMode: OpMode() {
    val vision: Vision by lazy { Vision(hardwareMap) }

    override fun init() {
        vision.init()
        telemetry.addData("Status", "Initialized")
    }

    override fun start() {
        vision.activate()
    }

    override fun loop() {
        val location: Vision.Location? = vision.location
        if (location != null) {
            telemetry.addData("X", location.x)
            telemetry.addData("Y", location.y)
            telemetry.addData("Heading", location.heading)
            telemetry.addData("Target", location.visibleTarget)
            telemetry.addData("Is Stale", if (location.isStale) "Yes" else "No")
        }
    }

    override fun stop() {
        vision.shutdown()
    }
}
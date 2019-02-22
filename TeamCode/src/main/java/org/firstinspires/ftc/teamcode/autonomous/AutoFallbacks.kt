package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Autonomous Op Mode - Depot Side", group = "Fallbacks")
class AutoOpModeDepot: AutoOpMode() {
    override fun runOpMode() {
        setup()
        states.add(GatherVisionData(false))
        addDepotStages()
        waitForStart()
        run()
    }
}

@Autonomous(name = "Autonomous Op Mode - Crater Side", group = "Fallbacks")
class AutoOpModeCrater: AutoOpMode() {
    override fun runOpMode() {
        setup()
        states.add(GatherVisionData(false))
        addCraterStages()
        waitForStart()
        run()
    }
}
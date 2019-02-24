package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

/** Autonomous op mode to use in case there is a chance of collision with the other team's robot. **/
@Autonomous(name = "Autonomous Op Mode - Fallback on Crater Side", group = "Fallbacks")
class AutoOpModeFallbackOnCraterSide: AutoOpMode() {
    override fun addCraterStates() {
        states.add(KnockMineralDepotSide())
    }
}

/** Autonomous op mode to use in case the robot cannot determine its position on the depot side. **/
@Autonomous(name = "Autonomous Op Mode - Depot Side", group = "Fallbacks")
class AutoOpModeDepot: AutoOpMode() {
    override fun runOpMode() {
        setup()
        states.add(GatherVisionData(false))
        addDepotStates()
        waitForStart()
        run()
    }
}

/** Autonomous op mode to use in case the robot cannot determine its position on the crater side. **/
@Autonomous(name = "Autonomous Op Mode - Crater Side", group = "Fallbacks")
class AutoOpModeCrater: AutoOpMode() {
    override fun runOpMode() {
        setup()
        states.add(GatherVisionData(false))
        addCraterStates()
        waitForStart()
        run()
    }
}

/** Autonomous op mode to use in case the normal one completely borks. **/
@Autonomous(name = "Autonomous Op Mode - Fallback", group = "Fallbacks")
class AutoOpModeFallback: AutoOpMode() {
    override fun runOpMode() {
        setup()
        states.add(GatherVisionData(false))
        addFallbackStates()
        waitForStart()
        run()
    }
}
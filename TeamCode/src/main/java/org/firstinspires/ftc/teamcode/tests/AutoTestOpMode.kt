package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.AutoOpMode

@Autonomous(name = "Autonomous Op Mode - Test", group = "Fallbacks")
class AutoTestOpMode: AutoOpMode() {
    override fun runOpMode() {
        setup()
        states.clear()
        states.add(GatherVisionData(false))
        states.add(MoveFromLander())
        states.add(KnockMineralCraterSide())
        states.add(TravelToDepot())
        states.add(ClaimDepot())
        states.add(ParkAtCrater())
        waitForStart()
        run()
    }
}
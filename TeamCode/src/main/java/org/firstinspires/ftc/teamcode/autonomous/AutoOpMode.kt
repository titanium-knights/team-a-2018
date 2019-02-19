package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensing.Gyro
import org.firstinspires.ftc.teamcode.sensing.IMUGyro
import org.firstinspires.ftc.teamcode.sensing.Vision
import java.io.File
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

/**
 * The autonomous op mode.
 *
 * The letters A-M and numbers 1-3 found in the documentation for the quick tuning variables correspond to the letters and numbers in the autonomous plan diagram.
 * **/
@Autonomous(name = "Autonomous Op Mode", group = "* Main")
class AutoOpMode: LinearOpMode() {
    // Quick tuning variables

    /**
     * Standard power used when moving the robot.
     * All millisecond values are multiplied by this value.
     * **/
    val standardPower = 0.5

    /** Power at which the robot should turn. **/
    val turnPower = 0.3

    /** How long the robot should move away from the lander, in power-adjusted milliseconds. (A) **/
    val moveFromLanderTime = 700

    /** How long the robot should obtain vision data for, in power-adjusted milliseconds. (1) **/
    val visionTime = 2000

    /** How long the robot should move horizontally towards a mineral, in milliseconds. (M) **/
    val moveToMineralTime = 2000

    /** How long the robot should move to knock a mineral on the depot side, in milliseconds. (B) **/
    val knockMineralDepotSideTime = 2000

    /** How long the robot should move to knock a mineral on the crater side, in milliseconds. (C) **/
    val knockMineralCraterSideTime = 1000

    /** How long the robot should move from the center mineral towards the side of the field before traveling to the depot, in milliseconds. (D) **/
    val moveToSideCraterSideTime = 1000

    /** Angle to which the robot should turn in order to claim the depot when started from the crater side, in degrees. (3) **/
    val claimDepotAngle = -90.0

    /** Power at which the robot moves along the side of the field towards the depot. (V) **/
    val moveToDepotCraterSidePower = standardPower

    /** Threshold at which the robot must stop while moving to the depot from the crater side, in the vision coordinate system. (V) **/
    val moveToDepotCraterSideThreshold = 0.0

    /** How long the robot should move towards the depot when started from the crater side, in milliseconds. (E) **/
    val moveToDepotCraterSideTime = 1000

    // NO LONGER USED.
    // /** How long the robot should move towards the depot when started from the depot side, in milliseconds. (F) **/
    // val moveToDepotDepotSideTime = 1000

    /** Power at which the robot should extend and retract the intake, in milliseconds. (2) **/
    val extendIntakePower = 1000

    /** How long the robot should extend the intake, in milliseconds. (2) **/
    val extendIntakeTime = 1000

    /** Power at which the robot should rotate the intake, in milliseconds. (2) **/
    val rotateIntakePower = 0.5

    /** How long the robot should rotate the intake, in milliseconds. (2) **/
    val rotateIntakeTime = 1000

    /** How long the robot should move towards the side of the field after claiming the depot. (G) **/
    val moveToSideTime = 1000

    /** How long the robot should move towards the crater, in milliseconds. (H) **/
    val parkAtCraterTime = 1000

    // END Quick tuning variables

    /**
     * Position of the gold mineral.
     *
     * -1 is left, 0 is center, and 1 is right.
     */
    private var mineralPos = 0

    /** Abstract class representing a single state/step in the AutoOpMode. **/
    abstract inner class State {
        abstract val name: String
        abstract fun run(prev: State?, next: State?)
    }

    /** Work-in-progress stage in which the robot lands. **/
    inner class Land: State() {
        override val name = "Land"

        override fun run(prev: State?, next: State?) {
            TODO("No landing mechanism")
        }
    }

    /** Stage in which the robot moves away from the lander. (A) **/
    inner class MoveFromLander: State() {
        override val name = "Move from lander"

        override fun run(prev: State?, next: State?) {
            drive.forwardWithPower(standardPower)
            sleep(adjustByPower(moveFromLanderTime))
            drive.stop()
        }
    }

    /** Stage in which the robot gathers vision data. (1) **/
    inner class GatherVisionData(val shouldFindLocation: Boolean = true): State() {
        override val name = "Gather vision data"

        override fun run(prev: State?, next: State?) {
            var atDepot = true

            var location: Vision.Location? = null
            var goldMineralPosSum: Double = 0.0
            var measurementsTaken = 0

            val startTime = elapsedTime.milliseconds()

            while (elapsedTime.milliseconds() - startTime < visionTime) {
                if (shouldFindLocation) {
                    location = vision.location
                }

                val measurements = vision.updatedRecognitions
                if (measurements != null) {
                    measurements.filter { it.label == Vision.LABEL_GOLD_MINERAL }?.maxBy { it.height }?.let {
                        measurementsTaken++
                        goldMineralPosSum += it.left.toDouble()
                    }
                }

                sleep(50)
            }

            if (shouldFindLocation && location != null) {
                if ((location.heading > 0 && location.heading < 90) || (location.heading > 180 && location.heading < 270)) {
                    atDepot = false
                }
            }

            if (measurementsTaken > 0) {
                val goldMineralX = goldMineralPosSum / measurementsTaken
                mineralPos = when {
                    goldMineralX > 0.7 -> 1
                    goldMineralX < 0.3 -> -1
                    else -> 0
                }
            }

            if (shouldFindLocation) {
                if (atDepot) addDepotStages() else addCraterStages()
            }
        }
    }

    /** Stage in which the robot knocks the mineral from the depot side. **/
    inner class KnockMineralDepotSide: State() {
        override val name = "Knock mineral"

        override fun run(prev: State?, next: State?) {
            if (mineralPos != 0) {
                drive.strafeRightWithPower(mineralPos * standardPower)
                sleep(adjustByPower(moveToMineralTime))
                drive.stop()
            }

            drive.forwardWithPower(standardPower)
            sleep(adjustByPower(knockMineralDepotSideTime))
            drive.stop()

            if (mineralPos != 0) {
                drive.strafeRightWithPower(-mineralPos * standardPower)
                sleep(adjustByPower(moveToMineralTime))
                drive.stop()
            }
        }
    }

    /** Stage in which the robot knocks the mineral and travels to the crater from the depot side. **/
    inner class KnockMineralCraterSide: State() {
        override val name = "Knock mineral and travel to depot"

        override fun run(prev: State?, next: State?) {
            if (mineralPos != 0) {
                drive.strafeRightWithPower(mineralPos * standardPower)
                sleep(adjustByPower(moveToMineralTime))
                drive.stop()
            }

            drive.forwardWithPower(standardPower)
            sleep(adjustByPower(knockMineralCraterSideTime))
            drive.stop()

            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(knockMineralCraterSideTime))
            drive.stop()

            drive.strafeLeftWithPower(standardPower)
            sleep(adjustByPower(moveToSideCraterSideTime + moveToMineralTime * mineralPos))
            drive.stop()

            drive.steerWithPower(turnPower, -1.0)
            while (gyro.getAbsoluteAngle() > claimDepotAngle) {
                idle()
            }
            drive.stop()

            drive.move(moveToDepotCraterSidePower, MecanumDrive.Motor.Vector2D(1.0, 1.0), 0.0)
            var location: Vision.Location?
            do {
                location = vision.location
                sleep(50)
            } while (location == null || (sign(location.x) != -sign(location.y) || abs(location.y) < moveToDepotCraterSideThreshold))
            drive.stop()

            drive.strafeLeftWithPower(standardPower)
            sleep(adjustByPower(moveToDepotCraterSideTime))
            drive.stop()
        }
    }

    inner class ClaimDepot: State() {
        override val name = "Claim depot"

        override fun run(prev: State?, next: State?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    inner class ParkAtCrater: State() {
        override val name = "Park at crater"

        override fun run(prev: State?, next: State?) {
            drive.move(standardPower, MecanumDrive.Motor.Vector2D(1.0, 1.0), 0.0)
            sleep(adjustByPower(moveToSideTime))
            drive.stop()

            drive.move(standardPower, MecanumDrive.Motor.Vector2D(1.0, -1.0), 0.0)
            sleep(adjustByPower(parkAtCraterTime))
            drive.stop()
        }
    }

    /** Adjust a number of milliseconds for power. **/
    private fun adjustByPower(ms: Int): Long {
        return (ms * standardPower).toLong()
    }

    val drive: MecanumDrive by lazy { MecanumDrive.standard(hardwareMap) }
    val gyro: Gyro by lazy { IMUGyro.standard(hardwareMap) }
    val vision: Vision by lazy { Vision(hardwareMap) }

    private val elapsedTime = ElapsedTime()

    private val states = LinkedList<State>()

    private fun setup() {
        drive
        gyro.initialize()
        gyro.calibrate()
        vision.init()

        states.clear()
        // states.add(Land())
        states.add(MoveFromLander())

        while (opModeIsActive() && !gyro.isCalibrated) {
            sleep(50)
        }

        telemetry.addData("Status", "Initialized")
        telemetry.update()
    }

    protected fun addDepotStages() {
        states.addAll(arrayOf(
                KnockMineralDepotSide(),
                ClaimDepot(),
                ParkAtCrater()
        ))
    }

    protected fun addCraterStages() {
        states.addAll(arrayOf(
                KnockMineralCraterSide(),
                ClaimDepot(),
                ParkAtCrater()
        ))
    }

    protected fun run() {
        elapsedTime.reset()

        var previousState: State? = null
        while (opModeIsActive() && states.peek() != null) {
            val state = states.remove()

            telemetry.addData("Status", state.name)
            telemetry.addData("States Left", states.size)
            telemetry.update()

            state.run(previousState, states.peek())

            previousState = state
        }

        requestOpModeStop()
    }

    override fun runOpMode() {
        setup()
        states.add(GatherVisionData())
        waitForStart()
        run()
    }
}
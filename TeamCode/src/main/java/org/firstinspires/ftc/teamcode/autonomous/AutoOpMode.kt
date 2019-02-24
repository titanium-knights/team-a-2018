package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.movement.ElevatorExtake
import org.firstinspires.ftc.teamcode.movement.ElevatorIntake
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensing.Gyro
import org.firstinspires.ftc.teamcode.sensing.IMUGyro
import org.firstinspires.ftc.teamcode.sensing.Vision
import org.firstinspires.ftc.teamcode.utils.stopAndResetEncoder
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

/**
 * The autonomous op mode.
 *
 * The letters A-M and numbers 1-3 found in the documentation for the quick tuning variables correspond to the letters and numbers in the autonomous plan diagram.
 * **/
// @Autonomous(name = "Autonomous Op Mode", group = "* Main")
open class AutoOpMode: LinearOpMode() {
    // Quick tuning variables

    /**
     * Standard power used when moving the robot.
     * All millisecond values are multiplied by this value.
     * **/
    val standardPower = 0.6

    /** Power at which the robot should turn. **/
    val turnPower = 0.3

    /** How long the robot should move away from the lander, in power-adjusted milliseconds. (A) **/
    val moveFromLanderTime = 1800

    /** How long the robot should obtain vision data for, in milliseconds. (1) **/
    val visionTime = 2000

    /** How long the robot should move horizontally towards a mineral when started from the depot side, in milliseconds. (M) **/
    val moveToMineralDepotSideTime = 2700

    /** How long the robot should move horizontally towards a mineral when started from the depot side, in milliseconds. (M) **/
    val moveToMineralCraterSideTime = 2700

    /** How long the robot should move to knock a mineral on the depot side, in milliseconds. (B) **/
    val knockMineralDepotSideTime = 2500

    /** How long the robot should move after knocking a mineral on the depot side, in milliseconds. (C) **/
    val postKnockMineralDepotSideTime = 2100

    /** How long the robot should move to knock a mineral on the crater side, in milliseconds. (C) **/
    val knockMineralCraterSideTime = 800

    /** How long the robot should move after knocking a mineral on the crater side, in milliseconds. (C) **/
    val knockMineralReturnCraterSideTime = 1150

    /** How long the robot should move from the center mineral towards the side of the field before traveling to the depot, in milliseconds. (D) **/
    val moveToSideCraterSideTime = 6600

    /** Angle to which the robot should turn in order to claim the depot when started from the crater side, in degrees. (3) **/
    val claimDepotAngle = 45.0

    /** How long the robot exits the depot for when started from the depot side. **/
    val exitDepotTime = 1500

    /** Power at which the robot moves along the side of the field towards the depot. (V) **/
    val moveToDepotCraterSidePower = standardPower

    /** Threshold at which the robot must stop while moving to the depot from the crater side, in the vision coordinate system. (V) **/
    val moveToDepotCraterSideThreshold = 0.0

    /** Power at which the robot should rotate the intake, in milliseconds. (2) **/
    val rotateIntakePower = 0.3

    /** Point to which the robot should rotate the intake. (2) **/
    val rotateIntakePos = 1000

    /** How long the robot should pause after extending the intake, in milliseconds. (2) **/
    val claimDepotPauseTime = 1000

    /** How long the robot should move towards the side of the field after claiming the depot. (G) **/
    val moveToSideTime = 1000

    /** How long the robot should move towards the crater, in milliseconds. (H) **/
    val parkAtCraterDepotSideTime = 9000
    val parkAtCraterCraterSideTime = 6600

    // END Quick tuning variables

    /**
     * Position of the gold mineral.
     *
     * -1 is left, 0 is center, and 1 is right.
     */
    private var mineralPos = -1

    /** Abstract class representing a single state/step in the AutoOpMode. **/
    abstract inner class State {
        /** The name of the State, displayed on the driver station. **/
        abstract val name: String

        /**
         * Runs the code associated with a state.
         * @param prev previous state; null if none
         * @param next next state; null if none known
         */
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
            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(moveFromLanderTime))
            drive.stop()
        }
    }

    /** Stage in which the robot gathers vision data. (1) **/
    inner class GatherVisionData(val shouldFindLocation: Boolean = true): State() {
        override val name = "Gather vision data"

        override fun run(prev: State?, next: State?) {
            var atDepot: Boolean? = null

            var location: Vision.Location? = null
            var goldMineralPosSum = 0.0
            var measurementsTaken = 0

            val startTime = elapsedTime.milliseconds()

            while (elapsedTime.milliseconds() - startTime < visionTime) {
                if (shouldFindLocation) {
                    location = vision.location
                    if (location != null) {
                        telemetry.addData("Location X", location.x)
                        telemetry.addData("Location Y", location.y)
                        telemetry.addData("Location Heading", location.heading)
                        telemetry.addData("Location Target", location.visibleTarget)
                    }
                }

                val measurements = vision.updatedRecognitions
                if (measurements != null) {
                    measurements.filter {
                        it.label == Vision.LABEL_GOLD_MINERAL
                    }.maxBy { it.height }?.let {
                        telemetry.addData("Measurements Taken", measurementsTaken)
                        telemetry.addData("Gold Mineral X", it.left)
                        telemetry.addData("Gold Mineral Confidence", it.confidence)
                        measurementsTaken++
                        goldMineralPosSum += it.left.toDouble()
                    }
                }

                telemetry.update()
                sleep(50)
            }

            if (shouldFindLocation && location != null) {
                atDepot = !((location.heading > 0 && location.heading < 90) || (location.heading > 180 && location.heading < 270))
            }

            mineralPos = when (measurementsTaken > 0) {
                true -> if (goldMineralPosSum / measurementsTaken > 500) 1 else 0
                else -> -1
            }

            if (shouldFindLocation) {
                if (atDepot == null) {
                    addFallbackStates()
                } else {
                    if (atDepot) addDepotStates() else addCraterStates()
                }
            }
        }
    }

    /** Stage in which the robot knocks the mineral from the depot side. **/
    inner class KnockMineralDepotSide: State() {
        override val name = "Knock mineral (depot)"

        override fun run(prev: State?, next: State?) {
            if (mineralPos != 0) {
                drive.move(-mineralPos * standardPower, MecanumDrive.Motor.Vector2D(1.0, 0.2), 0.0)
                sleep(adjustByPower(moveToMineralDepotSideTime))
                drive.stop()
            }

            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(knockMineralDepotSideTime))
            drive.stop()
        }
    }

    /** Stage in which the robot enters the depot from the depot side. **/
    inner class EnterDepot: State() {
        override val name = "Enter depot"

        override fun run(prev: State?, next: State?) {
            if (mineralPos != 0) {
                drive.move(mineralPos * standardPower, MecanumDrive.Motor.Vector2D(1.0, 0.2), 0.0)
                sleep(adjustByPower(moveToMineralDepotSideTime))
                drive.stop()
            }

            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(postKnockMineralDepotSideTime))
            drive.stop()
        }
    }

    /** Stage in which the robot exits the depot. **/
    inner class ExitDepot: State() {
        override val name = "Exit depot"

        override fun run(prev: State?, next: State?) {
            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(exitDepotTime))
            drive.stop()
        }
    }

    /** Stage in which the robot knocks the mineral and travels to the crater from the depot side. **/
    inner class KnockMineralCraterSide: State() {
        override val name = "Knock mineral (crater)"

        override fun run(prev: State?, next: State?) {
            if (mineralPos != 0) {
                drive.move(-mineralPos * standardPower, MecanumDrive.Motor.Vector2D(1.0, 0.2), 0.0)
                sleep(adjustByPower(moveToMineralCraterSideTime))
                drive.stop()
            }

            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(knockMineralCraterSideTime))
            drive.stop()

            drive.forwardWithPower(standardPower)
            sleep(adjustByPower(knockMineralReturnCraterSideTime))
            drive.stop()
        }
    }

    /** Stage in which the robot moves to the side to prepare to travel to the depot. **/
    inner class MoveToSideCraterSide: State() {
        override val name = "Move to side"

        override fun run(prev: State?, next: State?) {
            drive.move(-standardPower, MecanumDrive.Motor.Vector2D(-1.0, 0.2), 0.0)
            sleep(adjustByPower(moveToSideCraterSideTime + moveToMineralCraterSideTime * mineralPos))
            drive.stop()
        }
    }

    /** Stage in which the robot uses its gyro to turn towards the depot when started from the crater. **/
    inner class TurnTowardsDepotCraterSide: State() {
        override val name = "Turn towards depot"

        override fun run(prev: State?, next: State?) {
            drive.steerWithPower(turnPower, 1.0)
            while (gyro.getAbsoluteAngle() <= claimDepotAngle - 4.0 && opModeIsActive()) {
                telemetry.addData("Gyro", gyro.getAbsoluteAngle())
                telemetry.update()
                idle()
            }
            drive.stop()

        }
    }

    /** Stage in which the robot uses its gyro to turn towards the depot when started from the depot. **/
    inner class TurnTowardsDepotDepotSide: State() {
        override val name = "Turn towards depot (depot)"

        override fun run(prev: State?, next: State?) {
            drive.steerWithPower(turnPower, -1.0)
            while (gyro.getAbsoluteAngle() >= -180 + claimDepotAngle + 9.0 && opModeIsActive()) {
                telemetry.addData("Gyro", gyro.getAbsoluteAngle())
                telemetry.update()
                idle()
            }
            drive.stop()
        }
    }

    /** Stage in which the robot travels to the depot. **/
    inner class TravelToDepot: State() {
        override val name = "Travel to depot"

        override fun run(prev: State?, next: State?) {
            // drive.forwardWithPower(moveToDepotCraterSidePower)
            // sleep(1500)
            /* var location: Vision.Location?
            do {
                location = vision.location
                sleep(50)
            } while (opModeIsActive() && (location == null || (sign(location.x) != -sign(location.y) || abs(location.y) < moveToDepotCraterSideThreshold))) */
            // drive.stop()

            intake.motor.mode = DcMotor.RunMode.RUN_TO_POSITION
            intake.motor.targetPosition = intake.range!!.endInclusive
            intake.move(1.0)
            while (intake.motor.isBusy && opModeIsActive()) {
                idle()
            }
            intake.stop()
        }
    }

    /** Stage in which the robot uses its intake to claim the depot. (2) **/
    inner class ClaimDepot: State() {
        override val name = "Claim depot"

        override fun run(prev: State?, next: State?) {
            if (prev is TravelToDepot) {
                intake.move(1.0)
                sleep(300)
                intake.stop()
            }

            val mode = intake.binMotor.mode

            intake.binMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
            intake.binMotor.targetPosition = rotateIntakePos
            intake.moveBin(rotateIntakePower)
            while (intake.binMotor.isBusy && opModeIsActive()) {
                idle()
            }
            intake.stopBin()

            intake.moveRoller(1.0)
            sleep(claimDepotPauseTime.toLong())
            intake.stopRoller()

            intake.binMotor.targetPosition = 0
            intake.moveBin(rotateIntakePower)
            while (intake.binMotor.isBusy && opModeIsActive()) {
                idle()
            }
            intake.stopBin()
            intake.binMotor.mode = mode

            if (prev is TravelToDepot) {
                intake.motor.targetPosition = intake.range!!.start
                intake.move(1.0)
                while (intake.motor.isBusy && opModeIsActive()) {
                    idle()
                }
                intake.stop()
                intake.motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            }
        }
    }

    /** Stage in which the robot travels to and parks at the crater when started from the crater side. **/
    inner class ParkAtCraterDepotSide: State() {
        override val name = "Park at crater (depot side)"

        override fun run(prev: State?, next: State?) {
            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(parkAtCraterDepotSideTime / 2))
            drive.stop()

            val startAngle = gyro.getAbsoluteAngle()
            val target = if (startAngle == 0.0) 180.0 else sign(startAngle) * (abs(startAngle) - 180)
            val targetRange = (target-2.0)..(target+2.0)
            drive.steerWithPower(turnPower, -1.0)
            while (!targetRange.contains(gyro.getAbsoluteAngle()) && opModeIsActive()) {
                telemetry.addData("Gyro", gyro.getAbsoluteAngle())
                telemetry.update()
                idle()
            }
            drive.stop()

            drive.forwardWithPower(standardPower)
            sleep(adjustByPower(parkAtCraterDepotSideTime / 2))
            drive.stop()
        }
    }

    inner class ParkAtCraterCraterSide: State() {
        override val name = "Park at crater (crater side)"

        override fun run(prev: State?, next: State?) {
            drive.forwardWithPower(-standardPower)
            sleep(adjustByPower(parkAtCraterCraterSideTime / 2))
            drive.stop()

            val startAngle = gyro.getAbsoluteAngle()
            val target = if (startAngle == 0.0) 180.0 else sign(startAngle) * (abs(startAngle) - 180)
            val targetRange = (target-2.0)..(target+2.0)
            drive.steerWithPower(turnPower, -1.0)
            while (!targetRange.contains(gyro.getAbsoluteAngle()) && opModeIsActive()) {
                telemetry.addData("Gyro", gyro.getAbsoluteAngle())
                telemetry.update()
                idle()
            }
            drive.stop()

            drive.forwardWithPower(standardPower)
            sleep(adjustByPower(parkAtCraterCraterSideTime / 2))
            drive.stop()
        }
    }

    /** Adjust a number of milliseconds for power. **/
    private fun adjustByPower(ms: Int): Long {
        return (ms * abs(standardPower)).toLong()
    }

    val drive: MecanumDrive by lazy { MecanumDrive.standard(hardwareMap) }
    val gyro: Gyro by lazy { IMUGyro.standard(hardwareMap) }
    val vision: Vision by lazy { Vision(hardwareMap) }
    val intake: ElevatorIntake by lazy { ElevatorIntake.standard(hardwareMap) }
    val extake: ElevatorExtake by lazy { ElevatorExtake.standard(hardwareMap) }

    private val elapsedTime = ElapsedTime()

    protected val states = LinkedList<State>()

    protected fun setup() {
        /* val motors = mutableListOf(intake.motor, intake.binMotor, intake.rollerMotor, extake.motor).apply {
            addAll(drive.motors)
        }
        motors.forEach {
            it.stopAndResetEncoder()
        } */

        drive
        intake
        extake

        gyro.initialize()
        gyro.calibrate()
        vision.init()

        states.clear()

        while (opModeIsActive() && !gyro.isCalibrated) {
            sleep(50)
        }

        telemetry.addData("Status", "Initialized")
        telemetry.update()
    }

    /** Adds states to be used in case a location cannot be determined. **/
    protected open fun addFallbackStates() {
        states.addAll(arrayOf(
                MoveFromLander(),
                KnockMineralDepotSide()
        ))
    }

    /** Adds states to be used when the robot starts on the depot side. **/
    protected open fun addDepotStates() {
        states.addAll(arrayOf(
                MoveFromLander(),
                KnockMineralDepotSide(),
                EnterDepot(),
                TurnTowardsDepotDepotSide(),
                ExitDepot(),
                ClaimDepot(),
                ParkAtCraterDepotSide()
        ))
    }

    /** Adds states to be used when the robot starts on the crater side. **/
    protected open fun addCraterStates() {
        states.addAll(arrayOf(
                MoveFromLander(),
                KnockMineralCraterSide(),
                MoveToSideCraterSide(),
                TurnTowardsDepotCraterSide(),
                TravelToDepot(),
                ClaimDepot(),
                ParkAtCraterCraterSide()
        ))
    }

    protected fun run() {
        elapsedTime.reset()
        vision.activate()

        var previousState: State? = null
        while (opModeIsActive() && states.peek() != null) {
            val state = states.remove()

            telemetry.addData("Status", state.name)
            telemetry.addData("# of States", states.size)
            telemetry.update()

            state.run(previousState, states.peek())

            previousState = state
        }

        vision.shutdown()
        requestOpModeStop()
    }

    override fun runOpMode() {
        setup()
        states.add(GatherVisionData())
        waitForStart()
        run()
    }
}
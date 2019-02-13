package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensing.Gyro
import org.firstinspires.ftc.teamcode.sensing.IMUGyro
import org.firstinspires.ftc.teamcode.sensing.Vision
import java.util.*

// General autonomous op mode.
@Autonomous(name = "Autonomous Op Mode", group = "* Main")
class AutoOpMode: LinearOpMode() {
    abstract inner class State {
        abstract val name: String
        abstract fun run(prev: State?, next: State?)
    }

    inner class Land: State() {
        override val name = "Land"

        override fun run(prev: State?, next: State?) {
            TODO("No landing mechanism")
        }
    }

    inner class MoveFromLander: State() {
        override val name = "Move from lander"

        override fun run(prev: State?, next: State?) {
            drive.forwardWithPower(0.3)
            sleep(700)
        }
    }

    inner class DeterminePosition: State() {
        override val name = "Determine position"

        override fun run(prev: State?, next: State?) {
            var atBase = false

            var location: Vision.Location? = null
            val startTime = elapsedTime.milliseconds()

            while (location?.isStale != true && elapsedTime.milliseconds() - startTime < 2000) {
                location = vision.location
                sleep(50)
            }

            if (location != null) {
                if ((location.heading > 0 && location.heading < 90) || (location.heading > 180 && location.heading < 270)) {
                    atBase = true
                }
            }

            if (atBase) {

            } else {

            }
        }
    }

    inner class KnockMineral: State() {
        override val name = "Knock mineral"

        override fun run(prev: State?, next: State?) {

        }
    }

    inner class ClaimDepot: State() {
        override val name = "Claim depot"

        override fun run(prev: State?, next: State?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private var _drive: MecanumDrive? = null
    private val drive get() = _drive!!

    private var _gyro: Gyro? = null
    private val gyro get() = _gyro!!

    private var _vision: Vision? = null
    private val vision get() = _vision!!

    private val elapsedTime = ElapsedTime()

    private val states = LinkedList<State>()

    private fun setup() {
        _drive = MecanumDrive.standard(hardwareMap)
        _vision = Vision(hardwareMap)
        _gyro = IMUGyro.standard(hardwareMap)

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

    override fun runOpMode() {
        setup()
        waitForStart()
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
}
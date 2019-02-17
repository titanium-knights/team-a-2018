package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.movement.MecanumDrive
import org.firstinspires.ftc.teamcode.sensing.Gyro
import org.firstinspires.ftc.teamcode.sensing.IMUGyro
import org.firstinspires.ftc.teamcode.sensing.Vision

/**
 * A bare-bones iterative op mode.
 * Runs by repeatedly calling loop over and over again.
 * Good for Tele-Op op modes.
 */
@TeleOp(name = "NAME HERE") // TODO: Add a good name
@Disabled // TODO: Delete this line to enable your op mode
class ExampleIterativeOpMode: OpMode() {
    val drive: MecanumDrive by lazy { MecanumDrive.standard(hardwareMap) }
    val gyro: Gyro by lazy { IMUGyro.standard(hardwareMap) }
    val vision: Vision by lazy { Vision(hardwareMap) }

    // To add a servo, use this code, and uncomment the corresponding line in init:
    // val myServo: Servo by lazy { hardwareMap.get(Servo::class.java, "myServo") }

    override fun init() {
        drive // Comment this line if you, for some reason, don't need to use the drive motors

        // Uncomment the next two lines if you need to use the gyroscope
        // gyro.initialize()
        // gyro.calibrate()

        // Uncomment the next line if you need to use the vision system
        // vision.init()

        // Uncomment the next line if you have a servo
        // myServo

        /** Place any code that should run when INIT is pressed here. **/
    }

    override fun init_loop() {
        // If the gyro is done calibrating, let the drivers know.
        if (gyro.isCalibrated) {
            telemetry.addData("Gyro", "Calibrated")
        }

        /** Place any code that should repeatedly run after INIT is pressed but before the op mode starts here. **/
    }

    override fun start() {
        /** Place any code that should run once when the op mode starts here. **/
        drive.forwardWithPower(0.1)
    }

    override fun loop() {
        /** Place any code that should repeatedly run here. **/

        // TIPS:
        // To access the gamepads, use gamepad1 or gamepad2.
        // See the MecanumDrive docs to
        //
        // If you want to stop the op mode once you're done, call requestOpModeStop().
    }

    override fun stop() {
        /** Place any code that should run once the op mode stops here. **/
    }
}
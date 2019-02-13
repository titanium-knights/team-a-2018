package org.firstinspires.ftc.teamcode.teleop.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.BasicOpMode_Iterative;
import org.firstinspires.ftc.teamcode.movement.deprecated.Intake;
import org.firstinspires.ftc.teamcode.movement.TwoWheelDrive;
import org.firstinspires.ftc.teamcode.movement.deprecated.Roller;

@TeleOp(name = "Main TeleOp Mode", group = "* Main")
@Disabled
@Deprecated public class MainOpMode extends BasicOpMode_Iterative {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private TwoWheelDrive driveMotors;
    private Intake intake;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        driveMotors = TwoWheelDrive.standard(hardwareMap);
        intake = Intake.standard(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Controls", "Left Stick - Arm; Right Stick - Extender; LB/RB - Toggle Roller Positions");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    private boolean leftBumperActive = false;
    private boolean rightBumperActive = false;

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        telemetry.addData("Status", "Running");
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (gamepad1.left_bumper || gamepad1.right_bumper) {
            double drive = -gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            driveMotors.setPowers(Range.clip(drive + turn, -1.0, 1.0), Range.clip(drive - turn, -1.0, 1.0));
        } else {
            driveMotors.setPowers(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        }

        intake.setArmPower(gamepad2.left_stick_y);
        intake.setExtenderPower(gamepad2.left_stick_y * 0.7);

        if (gamepad2.left_bumper) {
            if (!leftBumperActive) {
                intake.toggleRoller(0);
            }
            leftBumperActive = true;
        } else {
            leftBumperActive = false;
        }

        if (gamepad2.right_bumper) {
            if (!rightBumperActive) {
                intake.toggleRoller(1);
            }
            rightBumperActive = true;
        } else {
            rightBumperActive = false;
        }

        telemetry.addData("Left Drive Power", driveMotors.leftDrive.getPower());
        telemetry.addData("Right Drive Power", driveMotors.rightDrive.getPower());
        telemetry.addData("=== : INTAKE", "===");
        telemetry.addData("Arm Position", intake.arm.getCurrentPosition());
        telemetry.addData("Arm Power", intake.arm.getPower());
        telemetry.addData("Extender Position", intake.extender.getCurrentPosition());
        telemetry.addData("Extender Power", intake.extender.getPower());
        telemetry.addData("=== : ROLLERS", "===");
        telemetry.addData("Roller State", intake.rollers[0].state == Roller.State.RETRACTED ? "Retracted" : "Extended");
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}

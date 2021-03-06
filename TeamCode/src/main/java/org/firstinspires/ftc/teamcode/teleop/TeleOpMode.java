package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.movement.ElevatorExtake;
import org.firstinspires.ftc.teamcode.movement.ElevatorIntake;
import org.firstinspires.ftc.teamcode.movement.MecanumDrive;

@TeleOp(name = "Main TeleOp Mode", group = "* Main")
public class TeleOpMode extends OpMode {
    private MecanumDrive drive;
    private ElevatorIntake intake;
    private ElevatorExtake extake;

    private final int intakeInPos = 0;
    private final int intakeMidPos = 480;
    private final int intakeOutPos = 1000;

    private final double extakeInPos = 0.95;
    private final double extakeOutPos = 0.05;

    private void resetEncoder(DcMotor motor) {
        DcMotor.RunMode mode = motor.getMode();
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(mode);
    }

    @Override public void init() {
        drive = MecanumDrive.standard(hardwareMap);
        intake = ElevatorIntake.standard(hardwareMap);
        extake = ElevatorExtake.standard(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    @Override public void loop() {
        // Strafe the Mecanum Drive using the left stick X as long as it is clearly to the side.
        double x = gamepad1.left_stick_x;
        if (x > -0.2 && x < 0.2) {
            x = 0.0;
        }

        // Move the Mecanum Drive forward using the left stick Y.
        double y = -gamepad1.left_stick_y;

        // Turn the Mecanum Drive using the right stick X.
        double turn = gamepad1.right_stick_x;

        // Drive the Mecanum Drive.
        MecanumDrive.Motor.Vector2D vector = new MecanumDrive.Motor.Vector2D(x, y);
        drive.move(1, vector, turn, MecanumDrive.TurnBehavior.ADDSUBTRACT);

        // Retract or extend the intake (front elevator) if LT or RT are pressed.
        // If the A button is pressed, extend/retract without obeying software limits.
        if (gamepad2.left_trigger == 1.0f) {
            if (gamepad2.a) {
                intake.move(-0.2);
            } else {
                intake.moveIfAble(-1.0);
            }
        } else if (gamepad2.right_trigger == 1.0f) {
            if (gamepad2.a) {
                intake.move(0.2);
            } else {
                intake.moveIfAble(1.0);
            }
        } else {
            intake.stop();
        }

        // Manually move the intake if the left stick is used.
        if (Math.abs(gamepad2.left_stick_y) > 0.1) {
            intake.getBinMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            intake.moveBin(gamepad2.left_stick_y < 0 ? -0.4 : 0.4);
        } else {
            // Stop the intake if it is not being moved.
            if (!intake.getBinMotor().isBusy() && (intake.getBinMotor().getMode() != DcMotor.RunMode.RUN_TO_POSITION || intake.getBinMotor().getTargetPosition() != intakeMidPos)) {
                intake.stopBin();
            }

            // Move the intake bin out if B is pressed.
            // Move it to an intermediate position if Y is pressed.
            // Move it in if X is pressed.
            if (gamepad2.b) {
                intake.getBinMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                intake.getBinMotor().setTargetPosition(intakeOutPos);
                intake.moveBin(0.3);
            } else if (gamepad2.y) {
                intake.getBinMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                intake.getBinMotor().setTargetPosition(intakeMidPos);
                intake.moveBin(0.3);
            } else if (gamepad2.x) {
                if (gamepad2.a || intake.getCurrentPosition() < 1000) {
                    intake.getBinMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    intake.getBinMotor().setTargetPosition(intakeInPos);
                    intake.moveBin(0.3);
                }
            }
        }

        // Retract or extend the extake (back elevator) if LT or RT are pressed.
        // If the A button is pressed, extend/retract without obeying software limits.
        if (gamepad2.left_bumper) {
            if (gamepad2.a) {
                extake.move(-0.2);
            } else {
                extake.moveIfAble(-1.0);
            }
        } else if (gamepad2.right_bumper) {
            if (gamepad2.a) {
                extake.move(0.2);
            } else {
                extake.moveIfAble(1.0);
            }
        } else {
            extake.stop();
        }

        // Move the extake bin out if UP on the D-Pad is pressed.
        // Move it in if DOWN is pressed.
        if (gamepad2.dpad_up) {
            if (gamepad2.a || extake.getCurrentPosition() > 450) {
                extake.moveBin(extakeOutPos);
            }
        } else if (gamepad2.dpad_down) {
            extake.moveBin(extakeInPos);
        }

        // Move the rollers backward if LEFT on the D-Pad is pressed; move them forward if RIGHT is pressed.
        if (gamepad2.dpad_left) {
            intake.moveRoller(-1.0);
        } else if (gamepad2.dpad_right) {
            intake.moveRoller(1.0);
        } else {
            intake.stopRoller();
        }

        // Resets encoders if X is held.
        if (gamepad1.x) {
            if (gamepad1.a) {
                resetEncoder(intake.getBinMotor());
            }
            if (gamepad1.b) {
                resetEncoder(intake.getMotor());
            }
            if (gamepad1.y) {
                resetEncoder(extake.getMotor());
            }
        }

        // Update telemetry data.
        telemetry.addData("Status", "Driving");
        telemetry.addData("Forward Power", y);
        telemetry.addData("Strafe", (x == 0 ? "None " : (x < 0 ? "Left " : "Right ")) + Math.abs(x));
        telemetry.addData("Turn", (turn == 0 ? "None " : (turn < 0 ? "Left " : "Right ")) + Math.abs(turn));
        telemetry.addData("Intake Extender Pos", intake.getCurrentPosition());
        telemetry.addData("Intake Bin Pos", intake.getBinMotor().getCurrentPosition());
        telemetry.addData("Extake Extender Pos", extake.getCurrentPosition());
        telemetry.addData("Extake Bin Pos", extake.getBinPosition());
    }
}

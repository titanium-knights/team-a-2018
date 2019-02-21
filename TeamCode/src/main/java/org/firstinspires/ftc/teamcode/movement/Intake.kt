package org.firstinspires.ftc.teamcode.movement

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.utils.Factory

/**
 * Interface representing the set of methods every intake is guaranteed to implement. It is currently empty.
 *
 * An intake takes in minerals for the robot.
 */
interface Intake

/**
 * Intake comprised of an elevator that extends and retracts a rotatable bin and roller.
 */
class ElevatorIntake(
        /**
         * The extender motor.
         */
        extenderMotor: DcMotor,

        /**
         * The range of allowable motion for the extender.
         * @see LinearMotion.range
         */
        extenderRange: IntRange? = null,

        /**
         * Power multiplier for the extender.
         * @see LinearMotion.powerMultiplier
         */
        extenderPowerMultiplier: Double = 1.0,

        /**
         * The bin motor, used for tilting the bin.
         */
        val binMotor: DcMotor,

        /**
         * The roller motor.
         */
        val rollerMotor: DcMotor
): LinearMotion(extenderMotor, extenderRange, extenderPowerMultiplier), Intake {
    /**
     * Moves the bin motor at a given power.
     * @param power the power to move the bin motor at
     */
    fun moveBin(power: Double) {
        binMotor.power = power
    }

    /**
     * Stops the bin motor.
     */
    fun stopBin() = moveBin(0.0)

    /**
     * Moves the roller motor at a given power.
     * @param power the power to move the bin motor at
     */
    fun moveRoller(power: Double) {
        rollerMotor.power = power
    }

    /**
     * Stops the roller motor.
     */
    fun stopRoller() = moveRoller(0.0)

    /**
     * Stops all moving parts.
     */
    fun stopAll() {
        stop()
        stopBin()
        stopRoller()
    }

    companion object: Factory<ElevatorIntake> {
        /** @inheritDoc **/
        @JvmStatic override fun standard(hardwareMap: HardwareMap): ElevatorIntake {
            TODO("not implemented")
        }
    }
}
package org.firstinspires.ftc.teamcode.movement

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.utils.Factory

/**
 * Interface representing the set of methods every extake is guaranteed to implement. It is currently empty.
 *
 * An extake deposits minerals into the lander.
 */
interface Extake

/**
 * Extake comprised of an elevator that extends and retracts a rotatable bin.
 */
class ElevatorExtake(
        /**
         * The elevator motor.
         */
        elevatorMotor: DcMotor,

        /**
         * The range of allowable motion for the elevator.
         * @see LinearMotion.range
         */
        elevatorRange: IntRange? = null,

        /**
         * Power multiplier for the extender.
         * @see LinearMotion.powerMultiplier
         */
        elevatorPowerMultiplier: Double = 1.0,

        /**
         * The bin servo, used for tilting the bin.
         */
        val binMotor: Servo
): LinearMotion(elevatorMotor, elevatorRange, elevatorPowerMultiplier), Intake {
    /**
     * Moves the bin motor at a given power.
     * @param power the power to move the bin motor at
     */
    fun moveBin(position: Double) {
        binMotor.position
    }

    /**
     * Stops the bin motor.
     */
    fun stopBin() = moveBin(binPosition)

    /**
     * The current bin position.
     */
    val binPosition get() = binMotor.position

    /**
     * Stops all moving parts.
     */
    fun stopAll() {
        stop()
        stopBin()
    }

    companion object: Factory<ElevatorExtake> {
        /** @inheritDoc **/
        @JvmStatic override fun standard(hardwareMap: HardwareMap) = ElevatorExtake(
                hardwareMap.get(DcMotor::class.java, "elevator_extender"),
                0..950,
                1.0,
                hardwareMap.get(Servo::class.java, "elevator_tilter")
        )
    }
}
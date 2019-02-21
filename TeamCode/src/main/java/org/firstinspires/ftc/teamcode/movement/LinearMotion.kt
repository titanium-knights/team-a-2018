package org.firstinspires.ftc.teamcode.movement

import com.qualcomm.robotcore.hardware.DcMotor

/**
 * A linear motion system driven by a DcMotor.
 */
open class LinearMotion(
        /**
         * The motor controlled by the linear motion system.
         */
        val motor: DcMotor,

        /**
         * The range of movement that this system supports.
         */
        val range: IntRange? = null,

        /**
         * Constant to multiply powers by.
         */
        val powerMultiplier: Double = 1.0
) {
    init {
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    /**
     * Checks whether the linear motion system is allowed to move at a given power.
     *
     * @param power the power at which to check whether the linear motion system should move; null to simply check whether the system is in a valid position
     * @param at the position at which to check whether the linear motion system should move; null to use the system's current position
     * @return whether it is safe for the linear motion system to move
     */
    fun canMove(power: Double? = null, at: Int? = null): Boolean {
        val adjusted = power?.let { it * powerMultiplier }

        if (range == null) {
            return true
        }

        val pos = at ?: motor.currentPosition
        return when {
            adjusted == null -> range.contains(pos)
            adjusted > 0 -> pos <= range.endInclusive
            adjusted < 0 -> pos >= range.start
            else -> true
        }
    }

    /**
     * Moves the linear motion system at a given power.
     * @param power the power at which to move the linear motion system
     */
    fun move(power: Double) = moveRaw(power * powerMultiplier)

    /**
     * Stops the linear motion system.
     */
    fun stop() = moveRaw(0.0)

    /**
     * The current position of the linear motion system.
     */
    val currentPosition get() = motor.currentPosition

    /**
     * Moves the linear motion system at a given power if it is allowed to; stops it if it is not.
     * @param power the power at which to move the linear motion system
     * @return whether the linear motion system was allowed to move
     * @see LinearMotion.canMove
     */
    fun moveIfAble(power: Double) = when (canMove(power)) {
        true -> {
            move(power)
            true
        }
        false -> {
            stop()
            false
        }
    }

    /**
     * Move the linear motion system at a power, bypassing powerMultiplier.
     * @param power the power at which to move the linear motion system
     */
    fun moveRaw(power: Double) {
        motor.power = power
    }
}
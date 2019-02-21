package org.firstinspires.ftc.teamcode.utils

import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Interface representing a factory that creates a standard Hardware instance.
 * Most hardware classes will have companion objects implementing this interface.
 *
 * @see Factory.standard
 * @param Hardware the hardware class created by this Factory
 */
interface Factory<Hardware> {
    /**
     * Creates a standard instance of a hardware class pre-configured for the Titanium Knights Team A robot.
     *
     * @param hardwareMap the hardware map to use when configuring the instance
     * @return a standard pre-configured instance of a hardware class
     */
    fun standard(hardwareMap: HardwareMap): Hardware
}
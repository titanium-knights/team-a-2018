package org.firstinspires.ftc.teamcode.sensing;

import com.qualcomm.robotcore.hardware.Servo;

@Deprecated public class Roller {
    public Servo servo;
    public int rollerRetractedPos = 0;
    public int rollerExtendedPos = 180;
    public State state = State.UNKNOWN;

    public Roller(Servo servo) {
        this.servo = servo;
    }

    public enum State {
        UNKNOWN, RETRACTED, EXTENDED
    }
}
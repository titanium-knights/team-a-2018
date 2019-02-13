# Titanium Knights Team A 2018-2019 TeamCode Module

Welcome!

This module, TeamCode, contains the code for the team's robot controller app.

## List of Packages
This module contains numerous packages, each containing a different set of classes with various functions.

Packages containing OpModes are:

| Package | Function
| ---:|:---
| autonomous | Contains op modes for the Autonomous portion of the competition
| teleop | Contains op modes for the tele-operated portion of the competition
| tests | Contains op modes for testing various functions on the robot. These should never be used on the field.

Packages containing utility classes, or helpers meant to facilitate programming, are as follows:

| Package | Function
| ---:|:---
| movement | Includes utility classes that drive the robot and move its intake
| sensing | Includes classes that assist in using sensors such as the gyroscope and vision

## Adding your own Op Modes
Adding your own Op Modes is as simple as creating a new class in the `org.firstinspires.ftc.teamcode` directory. Make sure your class inherits from either `OpMode` or `LinearOpMode`, and add the `@TeleOp` or `@Autonomous` decorator.

If this all seems a little confusing, go to **FtcRobotController** > **java** > **org.firstinspires.ftc.robotcontroller** > **external.samples** if you want to see what creating an op mode is like.

## A Note on Languages
This project uses both Java and Kotlin (Java, but better). Feel free to use either.
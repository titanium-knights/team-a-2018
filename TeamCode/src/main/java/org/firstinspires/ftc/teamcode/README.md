# Titanium Knights Team A 2018-2019 TeamCode Module

Welcome!

This module, TeamCode, contains the code for the team's robot controller app.

Sample code from FIRST can be found in `FtcRobotController/src/main/java/external.samples`.

**IMPORTANT:** Use of vision features requires a Vuforia authentication key. Please browse to `sensing.VuforiaAuthKey` to learn how to set one up.

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
| utils | Includes miscellaneous utility classes

## Adding your own Op Modes
To add an op mode, create a new file in the `autonomous`, `teleop`, or `test` package, then copy and paste the code in `ExampleIterativeOpMode` or `ExampleLinearOpMode` into a new Kotlin file.

## A Note on Languages
This project uses both Java and Kotlin (Java, but better). Feel free to use either.
/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.sensing.MineralDetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Mineral Detection Test", group = "Tests")
public class MineralDetectionTest extends OpMode {
    private MineralDetection manager;

    @Override
    public void init() {
        manager = new MineralDetection(hardwareMap);
        manager.init();
    }

    @Override
    public void start() {
        manager.activate();
    }

    @Override
    public void loop() {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = manager.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // if (updatedRecognitions.size() == 3) {
                ArrayList<Float> xValues = new ArrayList<>();
                ArrayList<Float> heightValues = new ArrayList<>();
                ArrayList<String> recognitions = new ArrayList<>();
                ArrayList<Float> confidenceValues = new ArrayList<>();
                for (Recognition recognition : updatedRecognitions) {
                    xValues.add(recognition.getLeft());
                    if (recognition.getLabel().equals(MineralDetection.LABEL_GOLD_MINERAL)) {
                        recognitions.add("Gold");
                    } else {
                        recognitions.add("Silver");
                    }
                    heightValues.add(recognition.getHeight());
                    confidenceValues.add(recognition.getConfidence());
                }
                Collections.sort(xValues);
                telemetry.addData("X Values", xValues.toString());
                telemetry.addData("Recognitions", recognitions.toString());
                telemetry.addData("Heights", heightValues.toString());
                telemetry.addData("Confidences", confidenceValues.toString());
            // }
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        manager.shutdown();
    }
}

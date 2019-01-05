package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MineralKnocker {
    public MineralDetection mineralDetection;

    public double measurementModifier = -0.5;
    public double turnAmountRate = 0.36;

    public int rollingAverage = 10;

    public ArrayList<Double> goldMeasurements = new ArrayList<>();

    public void addMeasurement(double measurement) {
        goldMeasurements.add(measurement);
    }

    public void measure() {
        List<Recognition> recognitions = mineralDetection.getUpdatedRecognitions();
        if (recognitions != null) {
            for (Recognition recognition: recognitions) {
                if (recognition.getLabel().equals(MineralDetection.LABEL_GOLD_MINERAL)) {
                    addMeasurement(recognition.getLeft());
                    break;
                }
            }
        }
    }

    public int countMeasurements() {
        return goldMeasurements.size();
    }

    public void clearMeasurements() {
        goldMeasurements.clear();
    }

    public double averageMeasurements() {
        int size = goldMeasurements.size();
        if (size < 1) {
            return 0;
        }

        int n = rollingAverage > 0 ? Math.min(size, rollingAverage) : size;

        List<Double> subList = goldMeasurements.subList(size - n, size);
        double sum = 0;
        for (Double measurement: subList) {
            sum += measurement;
        }

        return sum / n;
    }

    public double getTurnAmount() {
        return turnAmountRate * (averageMeasurements() + measurementModifier);
    }
}

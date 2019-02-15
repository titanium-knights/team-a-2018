package org.firstinspires.ftc.teamcode.sensing;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.sensing.MineralDetection;

import java.util.ArrayList;
import java.util.List;

/**
 * This helper class was intended to assist in the creation of mineral knocking op modes.
 * Since every use case is different, this class has been deprecated.
 * See MineralKnockerOpMode for an example of how it was used.
 *
 * Do not use this class.
 */
@Deprecated public class MineralKnocker {
    public MineralDetection mineralDetection;

    public double measurementModifier = -750;
    public double turnAmountRate = 0.6;

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

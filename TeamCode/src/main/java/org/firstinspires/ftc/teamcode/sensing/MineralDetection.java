package org.firstinspires.ftc.teamcode.sensing;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * Wrapper class for Vuforia and TFOD.
 * Improves code reusability.
 * Unlike most other classes, you usually do not call standard; instead, you use its constructor.
 *
 */
public class MineralDetection {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;
    public VuforiaLocalizer.CameraDirection cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    public HardwareMap hardwareMap;

    /**
     * Initializer that takes a hardware map as an argument.
     */
    public MineralDetection(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    /**
     * Convenience method to initialize Vuforia and TensorFlow at the same time.
     * Usually called during the init phase of an op mode.
     */
    public void init() {
        this.initVuforia();
        this.initTfod();
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VuforiaAuthKey.getAuthKey(hardwareMap.appContext);
        parameters.cameraDirection = this.cameraDirection;

        // Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    /**
     * Activate TFOD.
     * This method is typically called in an OpMode's start function.
     */
    public void activate() {
        tfod.activate();
    }

    /**
     * Shut down TFOD.
     * This method is typically called in an OpMode's stop function.
     */
    public void shutdown() {
        tfod.shutdown();
    }

    /**
     * Asks TFOD for updated recognitions.
     * @return a list of recognitions that may be null
     */
    public List<Recognition> getUpdatedRecognitions() {
        return tfod.getUpdatedRecognitions();
    }
}

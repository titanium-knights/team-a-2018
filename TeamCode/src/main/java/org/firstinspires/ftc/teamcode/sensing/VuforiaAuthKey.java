package org.firstinspires.ftc.teamcode.sensing;

import android.content.Context;

import org.firstinspires.ftc.teamcode.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class for retrieving the Vuforia authentication key used to power MineralDetection and Vision.
 * This class is automatically called by MineralDetection and Vision.
 *
 * For security reasons, we do not include the Vuforia auth key in the repository. As such, you will have to add one yourself.
 * Browse to TeamCode/src/main/res/raw, and make a new file called "vuforiaauthkey.txt". Paste your Vuforia authentication key into there.
 * TITANIUM KNIGHTS MEMBERS: To get the authentication key, contact Anthony Li or Edward Li.
 */
public class VuforiaAuthKey {
    /**
     * Gets the Vuforia authentication key.
     * @param context Android application context (HardwareMap.appContext)
     * @return the Vuforia auth key, or "auth key error" if one cannot be found
     */
    public static String getAuthKey(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.vuforiaauthkey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String key = reader.readLine().replaceAll("\n", "");
            reader.close();

            return key;
        } catch (IOException e) {
            return "Vuforia auth key error";
        }
    }
}

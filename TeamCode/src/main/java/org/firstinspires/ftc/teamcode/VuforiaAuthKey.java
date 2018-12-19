package org.firstinspires.ftc.teamcode;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VuforiaAuthKey {
    public static String getAuthKey(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.vuforiaauthkey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String key = reader.readLine().replaceAll("\n", "");
            reader.close();

            return key;
        } catch (IOException e) {
            return "YOUR VUFORIA AUTH KEY HERE";
        }
    }
}

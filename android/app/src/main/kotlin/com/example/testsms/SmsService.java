package com.example.testsms;


import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SmsService extends IntentService {
    private static final String TAG = "SmsService";

    public SmsService() {
        super("SmsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String sender = intent.getStringExtra("sender");
        String message = intent.getStringExtra("message");

        Log.d(TAG, "Handling intent in SmsService...");
        Log.d(TAG, "Sender: " + sender);
        Log.d(TAG, "Message: " + message);

        try {
            URL url = new URL("http://192.168.150.149:8000/api/messages");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"sender\": \"%s\", \"content\": \"%s\"}", sender, message);
            Log.d(TAG, "JSON Input String: " + jsonInputString);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "POST Response Code :: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error sending message", e);
        }
    }
}

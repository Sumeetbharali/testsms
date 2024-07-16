package com.example.testsms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.DONUT) {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    }
                    String sender = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.DONUT) {
                        sender = smsMessage.getDisplayOriginatingAddress();
                    }
                    String messageBody = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.DONUT) {
                        messageBody = smsMessage.getMessageBody();
                    }
                    Log.d(TAG, "Received SMS from: " + sender + " with message: " + messageBody);

                    Intent serviceIntent = new Intent(context, SmsService.class);
                    serviceIntent.putExtra("sender", sender);
                    serviceIntent.putExtra("message", messageBody);
                    Log.d(TAG, "Starting SmsService...");
                    context.startService(serviceIntent);
                }
            } else {
                Log.d(TAG, "No PDUs found");
            }
        } else {
            Log.d(TAG, "No bundle found");
        }
    }
}

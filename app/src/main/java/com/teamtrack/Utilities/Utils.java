package com.teamtrack.Utilities;

import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import com.teamtrack.R;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by GIRIN on 05-Jun-18.
 */

public class Utils {

    public static boolean hasInternet(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }

        return true;
    }


    public static boolean isBetweenNineAndFive() {

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);

        if (hours >= 9 && hours <= 17) {
            return true;
        }

        return true;
    }

    public static void showNotification(Context context, String text) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification!") // title for notification
                .setContentText(text) // message for notification
                .setAutoCancel(true); // clear notification after click
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.notify(generateRandom(), mBuilder.build());
        }
    }

    private static int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}

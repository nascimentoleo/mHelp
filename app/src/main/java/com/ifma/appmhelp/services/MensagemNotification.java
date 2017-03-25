package com.ifma.appmhelp.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.lib.ImageLib;


public class MensagemNotification {

    private static final String NOTIFICATION_TAG = "Message";

    public static void notify(final Context context, String ticker, String title, String text, String bigText, PendingIntent intent) {
        Resources res = context.getResources();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification);
        icon = ImageLib.getCircleBitmap(icon);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(icon)
                .setTicker(ticker)
                .setNumber(1)
                .setContentIntent(intent)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(bigText)
                .setBigContentTitle(title)
                .setSummaryText(text))
                .setAutoCancel(true);
        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}

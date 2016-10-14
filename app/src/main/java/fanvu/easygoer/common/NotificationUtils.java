package fanvu.easygoer.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import fanvu.easygoer.Constant;
import fanvu.easygoer.Controller;
import fanvu.easygoer.activity.ChatActivity;
import fanvu.easygoer.gcm.R;

/**
 * Created by framgia on 14/10/2016.
 */
public class NotificationUtils {
    public static final String NOTIFICATION_ID = "notificationNumber";

    public static void pushNotification(Context context, String title, String content) {
        SharedPreferences prefs = context.getSharedPreferences(Constant.SHARE_PREFERENCE,
            Context.MODE_PRIVATE);
        int notificationNumber = prefs.getInt(NOTIFICATION_ID, 0);
        Intent intent = new Intent(context, ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
            notificationNumber, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_drive_eta_white_24dp)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationNumber, builder.build());
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt(NOTIFICATION_ID, notificationNumber);
        editor.apply();
    }

    public static void clearNotification() {
        NotificationManager notificationManager = (NotificationManager)
            Controller.getInstanceContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}

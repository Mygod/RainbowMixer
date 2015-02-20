package tk.mygod.rainbowmixer.display;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import tk.mygod.rainbowmixer.app.R;
import tk.mygod.rainbowmixer.display.ColorDisplay;

/**
 * @author Mygod
 */
public final class NotificationLights extends ColorDisplay {
    private Notification notification;
    private NotificationManager manager;

    public NotificationLights(Context context) {
        requiresMainThread = true;
        Notification.Builder builder = new Notification.Builder(context).setLights(0, 1, 0)
                .setContentTitle(context.getResources().getText(R.string.app_name))
                .setSmallIcon(android.R.drawable.stat_notify_voicemail)
                .setDeleteIntent(PendingIntent.getBroadcast(context, 0, new Intent().setAction
                        ("tk.mygod.rainbowmixer.app.action.STOP"), 0));
        if (Build.VERSION.SDK_INT >= 16) {
            builder.setPriority(Notification.PRIORITY_LOW);
            notification = builder.build();         // pre-build to reduce GC
        } else notification = builder.getNotification();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(context, context.getString(R.string.start_toast), Toast.LENGTH_LONG).show();
    }

    @Override
    public void run() {
        if (stopped) manager.cancel(0);
        else {
            notification.ledARGB = color == 0xFF000000 ? 0xFF010101 : color;    // prevent showing charging LED
            manager.notify(0, notification);
        }
    }
}

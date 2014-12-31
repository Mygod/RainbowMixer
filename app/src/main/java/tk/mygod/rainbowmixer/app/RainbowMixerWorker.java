package tk.mygod.rainbowmixer.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;

/**
 * @author Mygod
 */
public final class RainbowMixerWorker extends Thread {
    private class NotificationPusher implements Runnable {
        @Override
        public void run() {
            manager.notify(0, notification);
        }
    }

    private final Handler handler = new Handler();
    private Notification notification;
    private NotificationManager manager;
    private NotificationPusher pusher = new NotificationPusher();

    public RainbowMixerWorker(Context context) {
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
    }

    @Override
    public void run() {
        int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        if (bufferSize < 1470) bufferSize = 1470;   // No higher than 60fps
        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        int size = bufferSize >> 1;
        if (size < 131072) size = 131072;           // always prepare enough buffer
        short[] buffer = new short[size];
        record.startRecording();
        int count = 0, k;
        double h = 0;
        while (!isInterrupted() && (k = record.read(buffer, count, size - count)) >= 0) try {
            count += k;
            if (count < 1470) Thread.sleep(15); else {
                short min = buffer[0], max = min;
                for (int i = 1; i < count; ++i) {
                    short j = buffer[i];
                    if (j < min) min = j;
                    else if (j > max) max = j;
                }
                count = 0;
                double b = (max - min) / 65535.0;
                h += 0.001 * (1 - Math.sqrt(b * (2 - b)));
                if (h > 1) h -= 1;
                int argb = Hsl2Argb(h, 1, b / 2);
                notification.ledARGB = argb == 0xFF000000 ? 0xFF000001 : argb;  // prevent showing charging LED
                if (!isInterrupted()) handler.post(pusher);
            }
        } catch (InterruptedException ignore) {
            break;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                manager.cancel(0);
            }
        });
        record.stop();
        record.release();
    }

    private static int Hsl2Argb(double h, double s, double l) {
        double r = l, g = l, b = l, v = (l <= 0.5) ? (l * (1.0 + s)) : (l + s - l * s);
        if (v > 0) {
            double m = l + l - v, sv = (v - m) / v;
            h *= 6.0;
            int sextant = (int) h;
            double fract = h - sextant, vsf = v * sv * fract, mid1 = m + vsf, mid2 = v - vsf;
            switch (sextant) {
                case 0:
                    r = v;
                    g = mid1;
                    b = m;
                    break;
                case 1:
                    r = mid2;
                    g = v;
                    b = m;
                    break;
                case 2:
                    r = m;
                    g = v;
                    b = mid1;
                    break;
                case 3:
                    r = m;
                    g = mid2;
                    b = v;
                    break;
                case 4:
                    r = mid1;
                    g = m;
                    b = v;
                    break;
                case 5:
                    r = v;
                    g = m;
                    b = mid2;
                    break;
            }
        }
        return 0xff000000 | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255);
    }
}

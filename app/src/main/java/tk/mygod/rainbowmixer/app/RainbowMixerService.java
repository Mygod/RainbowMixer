package tk.mygod.rainbowmixer.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * @author Mygod
 */
public final class RainbowMixerService extends Service {
    private PowerManager.WakeLock wakeLock;
    private RainbowMixerWorker worker;

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RainbowMixerWakelockTag");
        wakeLock.acquire();
        (worker = new RainbowMixerWorker(getApplicationContext())).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        worker.interrupt();
        wakeLock.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

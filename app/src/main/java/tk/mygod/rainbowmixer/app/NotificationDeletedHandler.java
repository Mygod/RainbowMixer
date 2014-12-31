package tk.mygod.rainbowmixer.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Mygod
 */
public class NotificationDeletedHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("tk.mygod.rainbowmixer.app.action.STOP".equals(intent.getAction()))
            context.stopService(new Intent(context, RainbowMixerService.class));
    }
}

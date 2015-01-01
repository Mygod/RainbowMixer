package tk.mygod.rainbowmixer.app;

import android.os.Handler;

/**
 * The abstract class for displaying color to user interface.
 * @author Mygod
 */
public abstract class ColorDisplay implements Runnable {
    protected boolean stopped, requiresMainThread;
    protected int color;

    public final void updateColor(int argb, Handler handler) {
        color = argb;
        if (requiresMainThread) handler.post(this); else run();
    }

    public final void stop(Handler handler) {
        stopped = true;
        if (requiresMainThread) handler.post(this); else run();
    }
}

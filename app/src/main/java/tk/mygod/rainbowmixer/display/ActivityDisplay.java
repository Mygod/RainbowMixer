package tk.mygod.rainbowmixer.display;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;
import tk.mygod.rainbowmixer.app.R;

/**
 * @author Mygod
 */
public class ActivityDisplay extends ColorDisplay {
    private Activity instance;

    public ActivityDisplay(Activity activity) {
        instance = activity;
        requiresMainThread = true;
    }

    @Override
    public void run() {
        if (stopped) return;
        Window window = instance.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(color));
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
        instance.findViewById(R.id.toolbar).setBackgroundColor(color);
    }
}

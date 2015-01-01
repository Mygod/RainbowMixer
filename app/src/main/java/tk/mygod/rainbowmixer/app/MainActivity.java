package tk.mygod.rainbowmixer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, RainbowMixerService.class));
        finish();   // todo: get a better way
    }
}

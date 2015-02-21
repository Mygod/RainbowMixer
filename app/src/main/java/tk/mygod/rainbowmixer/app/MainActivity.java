package tk.mygod.rainbowmixer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public final class MainActivity extends Activity {
    static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(R.string.app_name);
        instance = this;
        startService(new Intent(this, RainbowMixerService.class));
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, RainbowMixerService.class));
    }
}

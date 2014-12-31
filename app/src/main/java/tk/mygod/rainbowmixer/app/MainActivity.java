package tk.mygod.rainbowmixer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, RainbowMixerService.class));
        Toast.makeText(this, getString(R.string.start_toast), Toast.LENGTH_LONG).show();
        finish();   // todo: get a better way
    }
}

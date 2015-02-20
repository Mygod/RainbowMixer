package tk.mygod.rainbowmixer.app;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import tk.mygod.rainbowmixer.display.ColorDisplay;
import tk.mygod.rainbowmixer.display.NotificationLights;
import tk.mygod.rainbowmixer.generate.IColorGenerate;
import tk.mygod.rainbowmixer.generate.RainbowBoomBox;

/**
 * @author Mygod
 */
public final class RainbowMixerWorker extends Thread {
    private final Handler handler = new Handler();
    private ColorDisplay colorDisplay;
    private IColorGenerate colorGenerate = new RainbowBoomBox(0, 0.001);

    public RainbowMixerWorker(Context context) {
        colorDisplay = new NotificationLights(context); // new LedLights();
    }

    @Override
    public void run() {
        int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        if ((bufferSize & 1) > 0) throw new RuntimeException("Buffer size is stupid!");
        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        int size = bufferSize >> 1;
        size = size < 4096 ? 4096 : 1 << 32 - Integer.numberOfLeadingZeros(size);   // always prepare enough buffer
        short[] buffer = new short[size];
        record.startRecording();
        int count = 0, k;
        while (!isInterrupted() && (k = record.read(buffer, count, size - count)) >= 0) try {
            count += k;
            if (count < 1837) Thread.sleep(41); else {
                colorDisplay.updateColor(colorGenerate.getArgb(buffer, count), handler);
                count = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        colorDisplay.stop(handler);
        record.stop();
        record.release();
    }
}

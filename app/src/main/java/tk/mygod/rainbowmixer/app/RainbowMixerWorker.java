package tk.mygod.rainbowmixer.app;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

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
        int paddedSize = 1 << 32 - Integer.numberOfLeadingZeros(size);
        short[] buffer = new short[paddedSize];
        record.startRecording();
        int count;
        while (!isInterrupted() && (count = record.read(buffer, 0, size)) >= 0) try {
            if (count > 0) colorDisplay.updateColor(colorGenerate.getArgb(buffer, count), handler);
            else Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        colorDisplay.stop(handler);
        record.stop();
        record.release();
    }
}

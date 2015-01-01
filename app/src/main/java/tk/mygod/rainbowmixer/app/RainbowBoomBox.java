package tk.mygod.rainbowmixer.app;

/**
 * Generate color using HSL where the loudness of the wave determines dS/dt and L.
 * @author Mygod
 */
public final class RainbowBoomBox implements IColorGenerate {
    private double hueRate, hue;

    public RainbowBoomBox(double startHue, double rateHue) {
        hue = startHue;
        hueRate = rateHue;
    }

    @Override
    public int getArgb(short[] wave, int count) {
        short min = wave[0], max = min;
        for (int i = 1; i < count; ++i) {
            short j = wave[i];
            if (j < min) min = j;
            else if (j > max) max = j;
        }
        double b = (max - min) / 65535.0;
        hue += hueRate * (1 - Math.sqrt(b * (2 - b)));
        if (hue > 1) hue -= 1;
        return Color.fromHsl(hue, 1, b / 2).toArgb();
    }
}

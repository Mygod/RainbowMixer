package tk.mygod.rainbowmixer.generate;

import tk.mygod.rainbowmixer.Color;

/**
 * All things considered, I should probably try using exponential transform which should be better.
 * Test result: still pretty terrible
 * @author Mygod
 */
public class RainbowExponentialTransformer extends FftGenerate {
    @Override
    public int getArgb(int n) {
        Color color = new Color(), full = new Color();
        double max = Math.log(n);
        for (int i = 1; i < n; ++i) {
            double length = getLength(i), hue = Math.log(i) / max;
            color.add(Color.fromHsl(hue, 1, 0.5 * length));
            full.add(Color.fromHsl(hue, 1, 0.5));
        }
        color.dividedBy(full);
        return color.toArgb();
    }
}

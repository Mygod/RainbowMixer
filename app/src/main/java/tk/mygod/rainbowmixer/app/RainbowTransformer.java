package tk.mygod.rainbowmixer.app;

/**
 * This transformer transforms sound waves based on their frequencies to a hue rainbow which should be a little better?
 * Test result: Apparently not.
 * @author Mygod
 */
public class RainbowTransformer extends FftGenerate {
    @Override
    public int getArgb(int n) {
        Color color = new Color(), full = new Color();
        double dn = (double) n;
        for (int i = 1; i < n; ++i) {
            double length = getLength(i), hue = (i - 1) / dn;
            color.add(Color.fromHsl(hue, 1, 0.5 * length));
            full.add(Color.fromHsl(hue, 1, 0.5));
        }
        color.dividedBy(full);
        return color.toArgb();
    }
}

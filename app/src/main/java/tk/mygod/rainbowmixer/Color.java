package tk.mygod.rainbowmixer;

/**
 * A class for color. DUH!
 * @author Mygod
 */
public final class Color {
    public Color() { }
    public Color(double r, double g, double b) {
        R = r;
        G = g;
        B = b;
    }

    public static Color fromHsl(double h, double s, double l) {
        Color c = new Color(l, l, l);
        double v = (l <= 0.5) ? (l * (1.0 + s)) : (l + s - l * s);
        if (v > 0) {
            double m = l + l - v, sv = (v - m) / v;
            h *= 6.0;
            int sextant = (int) h;
            double fract = h - sextant, vsf = v * sv * fract, mid1 = m + vsf, mid2 = v - vsf;
            switch (sextant) {
                case 0:
                    c.R = v;
                    c.G = mid1;
                    c.B = m;
                    break;
                case 1:
                    c.R = mid2;
                    c.G = v;
                    c.B = m;
                    break;
                case 2:
                    c.R = m;
                    c.G = v;
                    c.B = mid1;
                    break;
                case 3:
                    c.R = m;
                    c.G = mid2;
                    c.B = v;
                    break;
                case 4:
                    c.R = mid1;
                    c.G = m;
                    c.B = v;
                    break;
                case 5:
                    c.R = v;
                    c.G = m;
                    c.B = mid2;
                    break;
            }
        }
        return c;
    }

    public void add(Color color) {
        R += color.R;
        G += color.G;
        B += color.B;
    }

    public void dividedBy(Color color) {
        R /= color.R;
        G /= color.G;
        B /= color.B;
    }

    public double R, G, B;

    public int toArgb() {
        return 0xff000000 | (int) (R * 255) << 16 | (int) (G * 255) << 8 | (int) (B * 255);
    }
}

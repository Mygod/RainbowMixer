package tk.mygod.rainbowmixer.generate;

import tk.mygod.rainbowmixer.Color;
import tk.mygod.rainbowmixer.generate.FftGenerate;

/**
 * This transformer transforms audible sound waves into visible light waves with some magic. :-P
 * I've tried my best to make it as linear as possible, but it still looks like crap. :-(
 * @author Mygod
 */
public final class WaveTransformer extends FftGenerate {
    private static final int[]
        lConeLog = new int[] {
            32186, 28202, 24660, 21688, 19178, 17371, 16029, 15136, 14290, 13513, 12842, 12414, 12010, 11606, 10974,
            10062, 9200, 8475, 7803, 7166, 6535, 5730, 4837, 3929, 3061, 2279, 1633, 1178, 830, 571, 330, 187, 128, 50,
            19, 1, 15, 86, 225, 325, 491, 727, 1026, 1380, 1823, 2346, 2943, 3603, 4421, 5327, 6273, 7262, 8407, 9658,
            10966, 12327, 13739, 15208, 16736, 18328, 19992, 21596, 23200, 24819, 26490, 28165, 29801, 31432, 33032,
            34625, 36223, 37767, 39316, 40841, 42344, 43840, 45308, 46783, 48211, 49631, 51040, 52441, 53815, 55168,
            56530, 57867, 59172, 60473, 61760
    }, mConeLog = new int[] {
            32908, 28809, 25120, 22013, 19346, 17218, 15535, 14235, 13033, 11900, 10980, 10342, 9794, 9319, 8632, 7734,
            6928, 6301, 5747, 5235, 4738, 4078, 3337, 2569, 1843, 1209, 699, 389, 191, 81, 4, 36, 163, 295, 514, 769,
            1115, 1562, 2143, 2753, 3443, 4264, 5198, 6247, 7390, 8610, 9915, 11294, 12721, 14205, 15748, 17370, 18900,
            20523, 22220, 23923, 25559, 27194, 28843, 30519, 32234, 33874, 35484, 37103, 38757, 40389, 41981, 43559,
            45101, 46634, 48153, 49607, 51068, 52505, 53916, 55326, 56715, 58107, 59447, 60778, 62097, 63405, 64673,
            65919, 67176, 68409, 69619, 70837, 72058
    }, sConeLog = new int[] {
            19660, 15744, 12037, 8743, 6002, 3915, 2427, 1542, 838, 373, 22, 69, 278, 782, 1217, 1540, 2164, 3185, 4446,
            5777, 7189, 8439, 9644, 11092, 12783, 14350, 16054, 17873, 19787, 21750, 23806, 25903, 28031, 30189, 32336,
            34479, 36607, 38713, 40790, 42831, 44832, 46787, 48694, 50550, 52351, 54098
    };
    private static final double[] rCo, gCo, bCo;

    static {
        int ll = lConeLog.length, ml = mConeLog.length, sl = sConeLog.length;
        rCo = new double[ll];
        for (int i = 0; i < ll; ++i) rCo[i] = Math.pow(10, (1 - lConeLog[i]) / 10000.0);
        gCo = new double[ll];
        for (int i = 0; i < ml; ++i) gCo[i] = Math.pow(10, (4 - mConeLog[i]) / 10000.0);
        bCo = new double[ll];
        for (int i = 0; i < sl; ++i) bCo[i] = Math.pow(10, (22 - sConeLog[i]) / 10000.0);
    }

    @Override
    public int getArgb(int n) {
        double r = 0, g = 0, b = 0;
        for (int i = 1; i < n; ++i) {
            double length = getLength(i), l = length * getCoefficient(rCo, i, n);
            if (l > r) r = l;
            l = length * getCoefficient(gCo, i, n);
            if (l > g) g = l;
            l = length * getCoefficient(bCo, i, n);
            if (l > b) b = l;
        }
        return new Color(r, g, b).toArgb();
    }

    private double getCoefficient(double[] lookup, int i, int n) {
        /*
            Max Period = n / 44100
            Period = n / 44100i
                   ~ 0.04644 / i s
            Frequency ~ 21.5332i Hz
            0.0001s ~ 10000Hz ~ 390nm ~ 0 (index)
            0.01s ~ 100Hz ~ 830nm ~ 88 (index)
            index = (period - 0.0001) * 88 / 0.0099
                  ~ period / 0.001125
         */
        double j = n / 49.6125 / i;
        if (j <= -1 || j >= 89) return 0;
        if (j <= 0) return lookup[0] * (j + 1);
        int k = (int) j, k1 = k + 1;
        return lookup[k] * (k1 - j) + lookup[k1] * (j - k);
    }
}

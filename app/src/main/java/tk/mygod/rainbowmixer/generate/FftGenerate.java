package tk.mygod.rainbowmixer.generate;

/**
 * @author Mygod
 */
public abstract class FftGenerate implements IColorGenerate {
    protected double[] x, y;
    private double[] sin, cos;

    public abstract int getArgb(int n);

    /***************************************************************
     * fft.c
     * Douglas L. Jones
     * University of Illinois at Urbana-Champaign
     * January 19, 1992
     * http://cnx.rice.edu/content/m12016/latest/
     *
     *   fft: in-place radix-2 DIT DFT of a complex input
     *
     *   input:
     * n: length of FFT: must be a power of two
     * m: n = 2**m
     *   input/output
     * x: double array of length n with real part of data
     * y: double array of length n with imag part of data
     *
     *   Permission to copy and use this program is granted
     *   as long as this header is included.
     ****************************************************************/
    @Override
    public final int getArgb(short[] wave, int count) {
        int m = 32 - Integer.numberOfLeadingZeros(count), n = 1 << m, n2 = n >> 1;
        if (y == null || n != y.length) {   // create a new buffer, ideally, this will be executed only once
            x = new double[n];
            y = new double[n];
            sin = new double[n2];
            cos = new double[n2];
            for (int i = 0; i < n2; ++i) {
                cos[i] = Math.cos(2 * Math.PI * i / n);
                sin[i] = Math.sin(2 * Math.PI * i / n);
            }
        }
        for (int i = 0; i < n; ++i) {
            x[i] = wave[i] / 32768.0;
            y[i] = 0;
        }
        int j = 0, up = n - 1;
        for (int i = 1; i < up; ++i) {
            int n1 = n2;
            while (j >= n1) {
                j -= n1;
                n1 >>= 1;
            }
            j += n1;
            if (i < j) {
                double t = x[i];
                x[i] = x[j];
                x[j] = t;
            }
        }
        // FFT
        n2 = 1;
        for (int i = 0; i < m; ++i) {
            int n1 = n2;
            n2 += n2;
            int a = 0;
            for (j = 0; j < n1; ++j) {
                double c = cos[a], s = sin[a];
                a += 1 << m - i - 1;
                for (int k = j; k < n; k += n2) {
                    int k1 = k + n1;
                    double t1 = c * x[k1] - s * y[k1], t2 = s * x[k1] + c * y[k1];
                    x[k1] = x[k] - t1;
                    y[k1] = y[k] - t2;
                    x[k] += t1;
                    y[k] += t2;
                }
            }
        }
        return getArgb(n);
    }

    protected final double getLength(int i) {
        double xi = x[i], yi = y[i];
        return Math.sqrt(xi * xi + yi * yi);
    }
}

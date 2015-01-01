package tk.mygod.rainbowmixer.app;

/**
 * Interface for generating color out of waves.
 * @author Mygod
 */
public interface IColorGenerate {
    public int getArgb(short[] wave, int count);
}

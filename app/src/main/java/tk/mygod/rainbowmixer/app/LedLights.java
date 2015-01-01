package tk.mygod.rainbowmixer.app;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author Mygod
 */
public final class LedLights extends ColorDisplay {
    private DataOutputStream os;
    private String red, green, blue, white;

    public LedLights() {
        try {
            os = new DataOutputStream(Runtime.getRuntime().exec("su").getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (test("red", "green", "blue")) return;
        if (test("led_r", "led_g", "led_b")) return;
        if (test("lv5219lg:rgb1:red", "lv5219lg:rgb1:green", "lv5219lg:rgb1:blue")) return;
        red = null;
        green = null;
        blue = null;
        if (test("white")) return;
        white = null;
    }

    private boolean test(String w) {
        return new File(white = "/sys/class/leds/" + w + "/brightness").isFile();
    }
    private boolean test(String r, String g, String b) {
        return new File(red = "/sys/class/leds/" + r + "/brightness").isFile() &&
                new File(green = "/sys/class/leds/" + g + "/brightness").isFile() &&
                new File(blue = "/sys/class/leds/" + b + "/brightness").isFile();
    }

    @Override
    public void run() {
        if (stopped) {
            try {
                os.writeBytes("exit\n");
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            if (white != null) os.writeBytes(String.format("echo %d > %s\n", (77 * (color >> 16 & 0xFF) +
                    150 * (color >> 8 & 0xFF) + 29 * (color & 0xFF)) >> 8, white)); // help I'm color blind 8(
            else os.writeBytes(String.format("echo %d > %s\necho %d > %s\necho %d > %s\n",
                    color >> 16 & 0xFF, red, color >> 8 & 0xFF, green, color & 0xFF, blue));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

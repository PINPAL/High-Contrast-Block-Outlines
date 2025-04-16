package io.pinpal.blockoutlines.util;

public class ConfigColor {
    private final Float red;
    private final Float green;
    private final Float blue;
    private final Float alpha;

    public ConfigColor(int red, int green, int blue, int alpha) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
        this.alpha = alpha / 255.0f;
    }

    public final Float getRed() {
        return red;
    }

    public final Float getGreen() {
        return green;
    }

    public final Float getBlue() {
        return blue;
    }

    public final Float getAlpha() {
        return alpha;
    }
}

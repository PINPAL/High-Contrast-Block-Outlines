package io.pinpal.blockoutlines.util;

public class ConfigColor {
    private Float red;
    private Float green;
    private Float blue;
    private Float alpha;

    public ConfigColor() {
        this.red = 0.0f;
        this.green = 0.0f;
        this.blue = 0.0f;
        this.alpha = 1.0f;
    }

    public ConfigColor(int red, int green, int blue, int alpha) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
        this.alpha = alpha / 255.0f;
    }

    public Float getRed() {
        return red;
    }

    public Float getGreen() {
        return green;
    }

    public Float getBlue() {
        return blue;
    }

    public Float getAlpha() {
        return alpha;
    }
}

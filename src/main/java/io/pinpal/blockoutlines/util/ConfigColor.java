package io.pinpal.blockoutlines.util;

public class ConfigColor {
    private Float red;
    private Float green;
    private Float blue;
    private Float alpha;

    private float intToFloat(int value) {
        return value / 255.0f;
    }

    private int floatToInt(float value) {
        return Math.round(value * 255);
    }

    public ConfigColor(int red, int green, int blue, int alpha) {
        this.red = intToFloat(red);
        this.green = intToFloat(green);
        this.blue = intToFloat(blue);
        this.alpha = intToFloat(alpha);
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

    public final int getRedInt() { return floatToInt(red); }

    public final int getGreenInt() { return floatToInt(green);}

    public final int getBlueInt() { return floatToInt(blue);}

    public final int getAlphaInt() { return floatToInt(alpha);}

    public final void setRed(int red) {
        this.red = intToFloat(red);
    }

    public final void setGreen(int green) {
        this.green = intToFloat(green);
    }

    public final void setBlue(int blue) {
        this.blue = intToFloat(blue);
    }

    public final void setAlpha(int alpha) {
        this.alpha = intToFloat(alpha);
    }

    public int getARGB32() {
        return (floatToInt(alpha) << 24) | (floatToInt(red) << 16) | (floatToInt(green) << 8) | floatToInt(blue);
    }
}

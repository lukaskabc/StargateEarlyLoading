package cz.lukaskabc.minecraft.mod_loader.loading.stargate_early_loading.stargate.variant;

public class Color {
    private float red = 0f;
    private float green = 0f;
    private float blue = 0f;
    private float alpha = 0f;

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color() {
    }

    private int norm(float f) {
        return (int) (f * 255);
    }

    public int packedColor() {
        return ((norm(alpha) & 0xff) << 24) | ((norm(blue) & 0xff) << 16) | ((norm(green) & 0xff) << 8) | (norm(red) & 0xff);
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}

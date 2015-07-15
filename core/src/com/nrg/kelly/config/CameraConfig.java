package com.nrg.kelly.config;

public class CameraConfig {

    private int worldToScreenScale = 64;
    private float viewportWidth;
    private float viewportHeight;


    public int getWorldToScreenScale() {
        return worldToScreenScale;
    }

    public void setWorldToScreenScale(int worldToScreenRatio) {
        this.worldToScreenScale = worldToScreenRatio;
    }

    public void setViewportWidth(float viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public float getViewportWidth() {
        return viewportWidth;
    }

    public float getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(float viewportHeight) {
        this.viewportHeight = viewportHeight;
    }
}

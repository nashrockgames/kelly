package com.nrg.kelly.config;

public class CameraConfig {

    private int worldToScreenScale = 64;

    public int getWorldToScreenScale() {
        return worldToScreenScale;
    }

    public void setWorldToScreenScale(int worldToScreenRatio) {
        this.worldToScreenScale = worldToScreenRatio;
    }
}

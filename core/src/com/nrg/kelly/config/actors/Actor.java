package com.nrg.kelly.config.actors;

/**
 * Created by Andrew on 3/05/2015.
 */
public abstract class Actor {

    private int x = 0;
    private int y = 0;
    private float height = 2.0f;
    private float width = 50.0f;
    private float density = 0.0f;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

}

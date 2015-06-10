package com.nrg.kelly.config.actors;

public class AtlasConfig {

    private String name;
    private String atlas;
    private ImageOffset imageOffset;
    private ImageScale imageScale;

    public AtlasConfig(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAtlas() {
        return atlas;
    }

    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    public ImageOffset getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(ImageOffset imageOffset) {
        this.imageOffset = imageOffset;
    }

    public ImageScale getImageScale() {
        return imageScale;
    }

    public void setImageScale(ImageScale imageScale) {
        this.imageScale = imageScale;
    }
}

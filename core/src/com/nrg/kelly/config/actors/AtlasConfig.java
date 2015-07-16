package com.nrg.kelly.config.actors;

public class AtlasConfig {

    private String name;
    private String atlas;
    private ImageOffsetConfig imageOffset;
    private ImageScaleConfig imageScale;

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

    public ImageOffsetConfig getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(ImageOffsetConfig imageOffset) {
        this.imageOffset = imageOffset;
    }

    public ImageScaleConfig getImageScale() {
        return imageScale;
    }

    public void setImageScale(ImageScaleConfig imageScale) {
        this.imageScale = imageScale;
    }
}

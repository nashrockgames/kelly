package com.nrg;

public enum COMMAND {
    CLEAN("CLEAN"),
    SPLIT("SPLIT"),
    PACK("PACK");
    private String id;
    COMMAND(String id){
        this.id = id;
    }
    @Override
    public String toString(){
        return this.id;
    }
}

package com.nrg;

public enum COMMAND {
    SPLIT("SPLIT");
    private String id;
    COMMAND(String id){
        this.id = id;
    }
    @Override
    public String toString(){
        return this.id;
    }
}

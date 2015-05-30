package com.nrg;

import java.io.IOException;

import static com.nrg.COMMAND.*;

public class ImageTools {


    public static void main(String[] args){

        if(args.length != 0){
            final COMMAND command = valueOf(args[0].toUpperCase());
            switch(command){
                case SPLIT:
                    ImageSplitter imageSplitter = new ImageSplitter();
                    try {
                        imageSplitter.split(args);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

    }



}

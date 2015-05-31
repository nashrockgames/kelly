package com.nrg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.nrg.COMMAND.*;

public class ImageTools {


    public static void main(String[] args){

        if(args.length != 0){
            final COMMAND command = valueOf(args[0].toUpperCase());
            switch(command){
                case SPLIT:
                    ImageSplitter imageSplitter = new ImageSplitter();

                    if(args.length == 2){
                        final File file = new File(args[1]);
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = br.readLine()) != null) {
                                split(line.split(" "),imageSplitter);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else {
                        split(args, imageSplitter);
                    }
            }
        }

    }

    private static void split(String[] args, ImageSplitter imageSplitter) {
        try {
            imageSplitter.split(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

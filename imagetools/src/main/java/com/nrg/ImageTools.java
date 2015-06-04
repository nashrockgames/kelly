package com.nrg;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.nrg.COMMAND.*;

public class ImageTools {


    public static void main(String[] args){

        if(args.length == 1) {
            final File file = new File(args[0]);
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.startsWith("#"))
                        continue;
                    final String[] split = line.split(" ");
                    if(split.length > 1)
                    run(split);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           run(args);
        }

    }

    private static void run(String[] args) {
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
                    break;
                case CLEAN:
                    ImageCleaner imageCleaner = new ImageCleaner();
                    imageCleaner.clean(args[1]);
                case PACK:
                    try {
                        String[] packerArgs = new String[args.length-1];
                        System.arraycopy(args,1,packerArgs,0,args.length-1);
                        TexturePacker.main(packerArgs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }

        }
    }


}

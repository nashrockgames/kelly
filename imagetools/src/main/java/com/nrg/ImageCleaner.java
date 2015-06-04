package com.nrg;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Andrew on 4/06/2015.
 */
public class ImageCleaner {

    public ImageCleaner(){

    }

    public void clean(String path){

        final File dir = new File(path);
        final File[] files = dir.listFiles();
        for(File file : files){
            System.out.println("Removing " + file.getAbsolutePath());
            file.delete();
        }

    }

}

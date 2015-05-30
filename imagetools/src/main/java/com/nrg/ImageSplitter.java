package com.nrg;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSplitter {

    public void split(String[] args) throws IOException {
        /**
         * split ../images *.png 64 128 ../out
         * **/
        if(args.length == 6){
            final String dirArg = args[1];
            final String wildcardArg = args[2];
            final File dir = new File(dirArg);
            final FileFilter fileFilter = new WildcardFileFilter(wildcardArg);
            final int width = Integer.parseInt(args[3]);
            final int height = Integer.parseInt(args[4]);
            final String outDir = args[5];
            File[] files = dir.listFiles(fileFilter);
            createImages(height, width, outDir, files);
        }

    }

    private void createImages(int height, int width, String outDir, File[] files) throws IOException {
        for (File f : files) {
            createImage(height, width, outDir, f);
        }
    }

    private void createImage(int height, int width, String outDir, File f) throws IOException {
        final BufferedImage image = ImageIO.read(f);
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        final int imageWidthCount = imageWidth / width;
        final int imageHeightCount = imageHeight / height;
        for(int i=0;i<imageHeightCount;i++){
            for(int j=0; j < imageWidthCount; j++ ){
                int x = j * width;
                int y = i * height;
                final BufferedImage subImage = image.getSubimage(x, y, width, height);
                final String fileName = f.getName();
                final String[] split = fileName.split("\\.");
                final String ext = split[1];
                final int index = i > 0 ? i * j : j;
                final String name = split[0] + "_" + index + "." +  ext;
                final String pathName = outDir + "/" + name;
                System.out.println("Creating image " + pathName);
                ImageIO.write(subImage, ext, new File(pathName));
                subImage.flush();
            }
        }
    }

}

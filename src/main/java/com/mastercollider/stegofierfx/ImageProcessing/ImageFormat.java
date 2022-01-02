package com.mastercollider.stegofierfx.ImageProcessing;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public enum ImageFormat {
    NOT_DEFINED,PNG, BMP,JPEG;

    public static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    public static ImageFormat getImageFormat(int ImageFormat)
    {
        if (ImageFormat == 1){return PNG;}
        else if(ImageFormat == 2){return BMP;}
        else if(ImageFormat == 3){return JPEG;}
        else {return  NOT_DEFINED;}
    }

    public static ImageFormat getImageFormat(String ImageFormatInString)
    {
        if(ImageFormatInString.equals("PNG")||ImageFormatInString.equals("png")){ return PNG; }
        else if(ImageFormatInString.equals("BMP")||ImageFormatInString.equals("bmp")){ return BMP; }
        else if(ImageFormatInString.equals("JPEG")||ImageFormatInString.equals("jpeg")){ return JPEG; }
        else { return NOT_DEFINED; }
    }

    public static ImageFormat doDetect(@NotNull File imagefile) throws IOException {

        ImageInputStream iis = ImageIO.createImageInputStream(imagefile);
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
        if (!imageReaders.hasNext()) {
            throw new RuntimeException("No readers found!");
        }
        ImageReader reader = imageReaders.next();
        return getImageFormat(reader.getFormatName());

    }
    public static ImageFormat doDetect(@NotNull String FileName) throws IOException {
        return doDetect(new File(FileName));
    }

}

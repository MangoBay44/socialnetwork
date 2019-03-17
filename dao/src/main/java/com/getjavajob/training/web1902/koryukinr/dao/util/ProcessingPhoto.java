package com.getjavajob.training.web1902.koryukinr.dao.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ProcessingPhoto {
    private static final String PATH_TO_PHOTO = "C:\\Users\\MLFFF\\Documents\\STUDY\\Home\\social_network\\";

    public ByteArrayInputStream createPhoto(byte[] photo) {
        if (photo != null) {
            return new ByteArrayInputStream(photo);
        }

        File file = new File(PATH_TO_PHOTO + "anonymous.jpg");
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(file);
            WritableRaster raster = bufferedImage.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            return new ByteArrayInputStream(data.getData());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

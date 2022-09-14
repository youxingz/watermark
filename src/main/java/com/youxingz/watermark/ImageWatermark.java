package com.youxingz.watermark;

import com.youxingz.watermark.builder.Config;
import com.youxingz.watermark.builder.ImageWatermarkRender;
import com.youxingz.watermark.builder.TextWatermarkBuilder;
import com.youxingz.watermark.exception.WatermarkException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.UUID;

public class ImageWatermark implements Watermark {
    private ImageWatermarkRender imageWatermarkBuilder;
    private TextWatermarkBuilder textWatermarkBuilder;

    @Override
    public void make(InputStream src, OutputStream target, String text, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.text);
        if (textWatermarkBuilder == null)
            textWatermarkBuilder = new TextWatermarkBuilder();
        ImageInputStream input = ImageIO.createImageInputStream(src);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
        while (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(input);
            BufferedImage srcImage = reader.read(0);  // Read the same image as ImageIO.read
            int width = srcImage.getWidth();
            int height = srcImage.getHeight();
            String format = reader.getFormatName(); // Get the format name for use later
            // render target
            BufferedImage watermark = textWatermarkBuilder.render(width, height, text, config, srcImage);
            // write image
            ImageIO.write(watermark, format, target);
        }
    }

    @Override
    public void make(InputStream src, OutputStream target, BufferedImage image, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.image);
        if (imageWatermarkBuilder == null)
            imageWatermarkBuilder = new ImageWatermarkRender();
        // ...
        ImageInputStream input = ImageIO.createImageInputStream(src);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
        // ...
        while (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(input);
            BufferedImage srcImage = reader.read(0);  // Read the same image as ImageIO.read
            String format = reader.getFormatName(); // Get the format name for use later
            int width = srcImage.getWidth();
            int height = srcImage.getHeight();
            // render target
            BufferedImage watermark = imageWatermarkBuilder.render(width, height, image, config, srcImage);
            // write image
            ImageIO.write(watermark, format, target);
        }
    }

    public static void main(String[] args) throws IOException, WatermarkException {
        String tempDir = "/Users/neo/Desktop/uploads";
        Config config = Config.buildTextConfig(true, null, -45, 3, 1.6f, 0.2f);
        new ImageWatermark().make(new FileInputStream(tempDir + "/test.jpg"), new FileOutputStream(tempDir + "/" + UUID.randomUUID().toString() + ".png"), "测试文字：ABC", config);
    }

    public static void main2(String[] args) throws IOException, WatermarkException {
        String tempDir = "/Users/neo/Desktop/uploads";
        Config config = Config.buildImageConfig(false, 100, 100, null, null, -45, 3, 1.6f, 0.2f);
        BufferedImage image = ImageIO.read(new FileInputStream(tempDir + "/test2.jpg"));
        new ImageWatermark().make(new FileInputStream(tempDir + "/test.jpg"), new FileOutputStream(tempDir + "/" + UUID.randomUUID().toString() + ".png"), image, config);
    }
}

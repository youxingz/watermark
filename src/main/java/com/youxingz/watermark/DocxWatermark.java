package com.youxingz.watermark;

import com.youxingz.watermark.builder.Config;
import com.youxingz.watermark.exception.WatermarkException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DocxWatermark implements Watermark {
    @Override
    public void make(InputStream src, OutputStream target, String text, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.text);
        // read input-stream and write result into target...
    }

    @Override
    public void make(InputStream src, OutputStream target, BufferedImage image, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.image);
        // read input-stream and write result into target...
    }
}

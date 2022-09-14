package com.youxingz.watermark;

import com.youxingz.watermark.builder.Config;
import com.youxingz.watermark.exception.WatermarkException;

import java.awt.image.BufferedImage;
import java.io.*;

public interface Watermark {
    /**
     * 文字水印
     *
     * @param src  impl: image, pdf, docx
     * @param text
     * @param target
     * @param config
     * @return
     */
    public void make(InputStream src, OutputStream target, String text, Config config) throws IOException, WatermarkException;

    /**
     * 图片水印
     *
     * @param src
     * @param target
     * @param image
     * @param config
     * @throws IOException
     */
    public void make(InputStream src, OutputStream target, BufferedImage image, Config config) throws IOException, WatermarkException;

    default void make(String inputFileName, String targetFileName, String watermark, Config config) throws IOException, WatermarkException {
        make(new FileInputStream(inputFileName), new FileOutputStream(targetFileName), watermark, config);
    }

    default void make(String inputFileName, String targetFileName, BufferedImage watermark, Config config) throws IOException, WatermarkException {
        make(new FileInputStream(inputFileName), new FileOutputStream(targetFileName), watermark, config);
    }

    default void assertConfigType(Config config, Config.Type type) throws WatermarkException {
        if (config == null) throw new WatermarkException("Config can not be null");
        if (!type.equals(config.getType())) throw new WatermarkException("Config type should be " + type.name());
    }
}

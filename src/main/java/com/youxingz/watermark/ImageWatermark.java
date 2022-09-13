package com.youxingz.watermark;

import com.youxingz.watermark.builder.Config;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.UUID;

public class ImageWatermark implements Watermark {
    private static final String tempDir = "/Users/neo/Desktop/uploads";

    public static void main(String[] args) throws IOException, WatermarkException {
        Config config = Config.buildTextConfig(null, 0, 2, 0.2f);
        config.setMode(Config.Mode.single);
        String filename = tempDir + "/" + UUID.randomUUID().toString() + ".png";
        new ImageWatermark().make(new FileInputStream(tempDir + "/test.jpg"), new FileOutputStream(filename), "测试文字：ABC", config);
    }

    @Override
    public void make(InputStream src, OutputStream target, String text, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.text);
        ImageInputStream input = ImageIO.createImageInputStream(src);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
        while (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(input);
            BufferedImage image = reader.read(0);  // Read the same image as ImageIO.read
            String format = reader.getFormatName(); // Get the format name for use later
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage watermark = null;
            if (Config.Mode.single.equals(config.getMode())) {
                Config.Position position = config.getPosition();
                Config.Margin margin = config.getMargin();
                Config.FontStyle fontStyle = config.getFontStyle();
                if (position == null) position = Config.Position.bottom_right;
                if (margin == null)
                    margin = new Config.Margin((int) (height * .02), (int) (width * .04), (int) (width * .02), (int) (height * .04));
                if (fontStyle == null) fontStyle = new Config.FontStyle("宋体", (height + width) / 60, Color.GRAY);
                watermark = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = watermark.createGraphics();
                // origin content
                graphics.drawImage(image, 0, 0, width, height, null);
                // add watermark
                graphics.setColor(fontStyle.getColor());
                graphics.setFont(new Font(fontStyle.getFontName(), Font.PLAIN, fontStyle.getFontSize()));
                // position?
                float textWidth = text.length() * fontStyle.getFontSize();
                float textHeight = fontStyle.getFontSize();
                switch (position) {
                    case top_left -> graphics.drawString(text, margin.getMarginLeft(), margin.getMarginTop());
                    case top_center -> graphics.drawString(text, (width - textWidth) / 2, margin.getMarginTop());
                    case top_right -> graphics.drawString(text, width - textWidth - margin.getMarginRight(), margin.getMarginTop());
                    case middle_left -> graphics.drawString(text, margin.getMarginLeft(), (height - textHeight) / 2);
                    case middle_center -> graphics.drawString(text, (width - textWidth) / 2, (height - textHeight) / 2);
                    case middle_right -> graphics.drawString(text, width - textWidth - margin.getMarginRight(), (height - textHeight) / 2);
                    case bottom_left -> graphics.drawString(text, margin.getMarginLeft(), height - textHeight - margin.getMarginBottom());
                    case bottom_center -> graphics.drawString(text, (width - textWidth) / 2, height - textHeight - margin.getMarginBottom());
                    case bottom_right -> graphics.drawString(text, width - textWidth - margin.getMarginRight(), height - textHeight - margin.getMarginBottom());
                }
                graphics.dispose();
            } else { // default: fit-full-page

            }
            // rebuild image
            ImageIO.write(watermark, format, target);
        }
    }

    @Override
    public void make(InputStream inputStream, OutputStream outputStream, BufferedImage image, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.image);
    }

    private BufferedImage addFullPageWatermark(BufferedImage srcImage, int width, int height, String text) {
        BufferedImage watermark = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = watermark.createGraphics();
        graphics.drawImage(srcImage, 0, 0, width, height, null);
        // add watermark
        int fontSize = (height + width) / 60;
        graphics.setColor(Color.gray);
        graphics.setFont(new Font("宋体", Font.PLAIN, fontSize));
        graphics.rotate(Math.toRadians(-38), width / 2, height / 2);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, .2F));
        for (int i = (int) (-height * 1.5); i < height * 2; i += fontSize * 3) {
            for (int j = 0; j < width; j += fontSize * text.length() * 1.6) {
                graphics.drawString(text, j, i);
            }
        }
        graphics.dispose();
        return watermark;
    }

    private BufferedImage addFooterWatermark(BufferedImage srcImage, int width, int height, String text) {
        BufferedImage watermark = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = watermark.createGraphics();
        graphics.drawImage(srcImage, 0, 0, width, height, null);
        // add watermark
        graphics.setColor(Color.gray);
        graphics.setFont(new Font("宋体", Font.PLAIN, (height + width) / 60));
        graphics.drawString(text, (int) (width * .02), (int) (height * .96));
        graphics.dispose();
        return watermark;
    }

    private String tempFilename() {
        return UUID.randomUUID().toString();
    }
}

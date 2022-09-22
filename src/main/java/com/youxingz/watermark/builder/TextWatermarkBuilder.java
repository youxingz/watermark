package com.youxingz.watermark.builder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextWatermarkBuilder {

    public BufferedImage render(int pageWidth, int pageHeight, String text, Config config) {
        return render(pageWidth, pageHeight, text, config, null);
    }

    public BufferedImage render(int pageWidth, int pageHeight, String text, Config config, BufferedImage background) {
        // fix config, assign invalid value!
        config.fixConfig(pageWidth, pageHeight);
        float theta = config.getTheta();
        float alpha = config.getAlpha();
        Config.Margin margin = config.getMargin();
        Config.FontStyle fontStyle = config.getFontStyle();
        // init?
        // create watermark image
        BufferedImage watermark = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = watermark.createGraphics();
        // origin content
        if (background != null)
            graphics.drawImage(background, 0, 0, pageWidth, pageHeight, null);
        // prepare watermark attrs.
        graphics.setColor(fontStyle.getColor());
        graphics.setFont(new Font(fontStyle.getFontName(), Font.PLAIN, fontStyle.getFontSize()));
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // use SRC_OVER instead of SRC_ATOP
        float textWidth = text.length() * fontStyle.getFontSize();
        float textHeight = fontStyle.getFontSize();
        if (Config.Mode.single.equals(config.getMode())) {
            // add watermark
            // position?
            Config.Position position = config.getPosition();
            if (position == null) position = Config.Position.bottom_right;
            switch (position) {
                case top_left -> graphics.drawString(text, margin.getLeft(), margin.getTop());
                case top_center -> graphics.drawString(text, (pageWidth - textWidth) / 2, margin.getTop());
                case top_right -> graphics.drawString(text, pageWidth - textWidth - margin.getRight(), margin.getTop());
                case middle_left -> graphics.drawString(text, margin.getLeft(), (pageHeight - textHeight) / 2);
                case middle_center ->
                        graphics.drawString(text, (pageWidth - textWidth) / 2, (pageHeight - textHeight) / 2);
                case middle_right ->
                        graphics.drawString(text, pageWidth - textWidth - margin.getRight(), (pageHeight - textHeight) / 2);
                case bottom_left ->
                        graphics.drawString(text, margin.getLeft(), pageHeight - textHeight - margin.getBottom());
                case bottom_center ->
                        graphics.drawString(text, (pageWidth - textWidth) / 2, pageHeight - textHeight - margin.getBottom());
                case bottom_right ->
                        graphics.drawString(text, pageWidth - textWidth - margin.getRight(), pageHeight - textHeight - margin.getBottom());
            }
            graphics.dispose();
        } else { // default: fit-page
            // rotate: only support for fit-page mode
            graphics.rotate(Math.toRadians(theta), pageWidth / 2.0, pageHeight / 2.0);
            // add watermark
            float dense = config.getDense();
            float space = config.getSpace();
            for (int y = -pageHeight * 2; y < pageHeight * 3; y += textHeight * dense) {
                for (int x = -pageWidth * 2; x < pageWidth * 3; x += textWidth * space) {
                    graphics.drawString(text, x + margin.getLeft(), y + margin.getTop());
                }
            }
            graphics.dispose();
        }
        return watermark;
    }

    public static void main(String[] args) throws IOException {
        Config config = Config.buildTextConfig(true, null, -45, 3, 1.6f, 0.8f);
//        BufferedImage image = new TextWatermarkBuilder().render(800, 1000, "测试文字：ABC", config);
        BufferedImage background = ImageIO.read(new File("/Users/neo/Desktop/uploads/test.jpg"));
        BufferedImage image = new TextWatermarkBuilder().render(800, 1000, "测试文字：ABC", config, background);
        FileOutputStream out = new FileOutputStream("/Users/neo/Desktop/uploads/out1.png");
        ImageIO.write(image, "png", out);
        out.close();
    }
}

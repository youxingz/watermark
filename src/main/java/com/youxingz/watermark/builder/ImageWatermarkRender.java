package com.youxingz.watermark.builder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWatermarkRender {
    public BufferedImage render(int pageWidth, int pageHeight, BufferedImage watermarkImage, Config config) {
        return render(pageWidth, pageHeight, watermarkImage, config, null);
    }

    public BufferedImage render(int pageWidth, int pageHeight, BufferedImage watermarkImage, Config config, BufferedImage background) {
        int imageWidth = watermarkImage.getWidth();
        int imageHeight = watermarkImage.getHeight();
        // fix config, assign invalid value!
        config.fixConfig(pageWidth, pageHeight, imageWidth, imageHeight);
        imageWidth = (int) config.getWidth();
        imageHeight = (int) config.getHeight();
//        System.out.println(config);
        float theta = config.getTheta();
        float alpha = config.getAlpha();
        Config.Margin margin = config.getMargin();
        // init?
        // create watermark image
        BufferedImage watermark = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = watermark.createGraphics();
        // origin content
        if (background != null)
            graphics.drawImage(background, 0, 0, pageWidth, pageHeight, null);
        // prepare watermark attrs.
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        if (Config.Mode.single.equals(config.getMode())) {
            // add watermark
            // position?
            Config.Position position = config.getPosition();
            switch (position) {
                case top_left ->
                        graphics.drawImage(watermarkImage, (int) margin.getLeft(), (int) margin.getTop(), imageWidth, imageHeight, null);
                case top_center ->
                        graphics.drawImage(watermarkImage, (pageWidth - imageWidth) / 2, (int) margin.getTop(), imageWidth, imageHeight, null);
                case top_right ->
                        graphics.drawImage(watermarkImage, (int) (pageWidth - imageWidth - margin.getRight()), (int) margin.getTop(), imageWidth, imageHeight, null);
                case middle_left ->
                        graphics.drawImage(watermarkImage, (int) margin.getLeft(), (pageHeight - imageHeight) / 2, imageWidth, imageHeight, null);
                case middle_center ->
                        graphics.drawImage(watermarkImage, (pageWidth - imageWidth) / 2, (pageHeight - imageHeight) / 2, imageWidth, imageHeight, null);
                case middle_right ->
                        graphics.drawImage(watermarkImage, (int) (pageWidth - imageWidth - margin.getRight()), (pageHeight - imageHeight) / 2, imageWidth, imageHeight, null);
                case bottom_left ->
                        graphics.drawImage(watermarkImage, (int) margin.getLeft(), (int) (pageHeight - imageHeight - margin.getBottom()), imageWidth, imageHeight, null);
                case bottom_center ->
                        graphics.drawImage(watermarkImage, (pageWidth - imageWidth) / 2, (int) (pageHeight - imageHeight - margin.getBottom()), imageWidth, imageHeight, null);
                case bottom_right ->
                        graphics.drawImage(watermarkImage, (int) (pageWidth - imageWidth - margin.getRight()), (int) (pageHeight - imageHeight - margin.getBottom()), imageWidth, imageHeight, null);
            }
            graphics.dispose();
        } else { // default: fit-page
            // rotate: only support for fit-page mode
            graphics.rotate(Math.toRadians(theta), pageWidth / 2.0, pageHeight / 2.0);
            // add watermark
            float dense = config.getDense();
            float space = config.getSpace();
            imageWidth += margin.getLeft() + margin.getRight();
            imageHeight += margin.getTop() + margin.getBottom();
            for (int y = -pageHeight * 2; y < pageHeight * 3; y += imageWidth * dense) {
                for (int x = -pageWidth * 2; x < pageWidth * 3; x += imageHeight * space) {
                    graphics.drawImage(watermarkImage, null, (int) (x + margin.getLeft()), (int) (y + margin.getTop()));
                }
            }
            graphics.dispose();
        }
        return watermark;
    }
}

package com.youxingz.watermark;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.UUID;

public class PdfWatermark {
    private static final String tempDir = "/Users/neo/Desktop/uploads";

    public static void main(String[] args) throws IOException {
        new PdfWatermark().make(new FileInputStream(tempDir + "/deligne.pdf"), "测试文字：ABC");
    }

    public OutputStream make(InputStream file, String text) throws IOException {
        PDDocument document = PDDocument.load(file);
        Iterator<PDPage> iterator = document.getPages().iterator();
        while (iterator.hasNext()) {
            PDPage page = iterator.next();
            PDRectangle rectangle = page.getMediaBox();
            float width = rectangle.getWidth();
            float height = rectangle.getHeight();
            PDImageXObject image = PDImageXObject.createFromByteArray(document, createFullPageWatermark((int) width, (int) height, text), "anything");

            PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, false);
//            contents.setFont(PDType1Font.HELVETICA, 20);
            contents.drawImage(image, 0, 0);
            contents.close();
        }
        String filename = tempDir + "/" + tempFilename() + ".pdf";
        FileOutputStream out = new FileOutputStream(filename);
        document.save(out);
        out.flush();
        out.close();
        return out;
    }

    private byte[] createFullPageWatermark(int width, int height, String text) throws IOException {
        BufferedImage watermark = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = watermark.createGraphics();
        // add watermark
        int fontSize = (height + width) / 60;
        graphics.setColor(Color.gray);
        graphics.setFont(new Font("宋体", Font.PLAIN, fontSize));
        graphics.rotate(Math.toRadians(-38), width / 2, height / 2);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2F));
        for (int i = (int) (-height * 1.5); i < height * 2; i += fontSize * 3) {
            for (int j = 0; j < width; j += fontSize * text.length() * 1.6) {
                graphics.drawString(text, j, i);
            }
        }
        graphics.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(watermark, "png", out);
        return out.toByteArray();
    }

    private byte[] createFooterWatermark(int width, int height, String text) throws IOException {
        BufferedImage watermark = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = watermark.createGraphics();
        // add watermark
        graphics.setColor(Color.gray);
        graphics.setFont(new Font("宋体", Font.PLAIN, (height + width) / 60));
        graphics.drawString(text, (int) (width * .02), (int) (height * .96));
        graphics.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(watermark, "png", out);
        return out.toByteArray();
    }

    private String tempFilename() {
        return UUID.randomUUID().toString();
    }
}

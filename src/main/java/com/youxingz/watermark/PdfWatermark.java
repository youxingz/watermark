package com.youxingz.watermark;

import com.youxingz.watermark.builder.Config;
import com.youxingz.watermark.builder.ImageWatermarkRender;
import com.youxingz.watermark.builder.TextWatermarkBuilder;
import com.youxingz.watermark.exception.WatermarkException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.UUID;

public class PdfWatermark implements Watermark {
    private ImageWatermarkRender imageWatermarkBuilder;
    private TextWatermarkBuilder textWatermarkBuilder;


    @Override
    public void make(InputStream src, OutputStream target, String text, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.text);
        if (textWatermarkBuilder == null)
            textWatermarkBuilder = new TextWatermarkBuilder();
        PDDocument document = PDDocument.load(src);
        Iterator<PDPage> iterator = document.getPages().iterator();
        while (iterator.hasNext()) {
            PDPage page = iterator.next();
            PDRectangle rectangle = page.getMediaBox();
            float width = rectangle.getWidth();
            float height = rectangle.getHeight();
            BufferedImage watermark = textWatermarkBuilder.render((int) width, (int) height, text, config);
            byte[] raw = toByteArray(watermark);
//            System.out.println(Arrays.toString(raw));
            PDImageXObject imageObject = PDImageXObject.createFromByteArray(document, raw, "anything");

            PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, false);
//            contents.setFont(PDType1Font.HELVETICA, 20);
            contents.drawImage(imageObject, 0, 0);
            contents.close();
        }
        document.save(target);
        target.flush();
//        target.close();
    }

    @Override
    public void make(InputStream src, OutputStream target, BufferedImage image, Config config) throws IOException, WatermarkException {
        assertConfigType(config, Config.Type.image);
        if (imageWatermarkBuilder == null)
            imageWatermarkBuilder = new ImageWatermarkRender();
        PDDocument document = PDDocument.load(src);
        Iterator<PDPage> iterator = document.getPages().iterator();
        while (iterator.hasNext()) {
            PDPage page = iterator.next();
            PDRectangle rectangle = page.getMediaBox();
            float width = rectangle.getWidth();
            float height = rectangle.getHeight();

            BufferedImage watermark = imageWatermarkBuilder.render((int) width, (int) height, image, config);
            byte[] raw = toByteArray(watermark);
            PDImageXObject imageObject = PDImageXObject.createFromByteArray(document, raw, "anything");

            PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, false);
            contents.drawImage(imageObject, 0, 0);
            contents.close();
        }
        document.save(target);
        target.flush();
//        target.close();
    }

    private byte[] toByteArray(BufferedImage watermark) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(watermark, "png", out);
        return out.toByteArray();
    }

    public static void main(String[] args) throws IOException, WatermarkException {
        String tempDir = "/Users/neo/Desktop/uploads";
        Config config = Config.buildTextConfig(true, null, -45, 3, 1.6f, 0.2f);
        new PdfWatermark().make(new FileInputStream(tempDir + "/deligne.pdf"), new FileOutputStream(tempDir + "/" + UUID.randomUUID().toString() + ".pdf"), "测试文字：ABC", config);
    }

    public static void main2(String[] args) throws IOException, WatermarkException {
        String tempDir = "/Users/neo/Desktop/uploads";
        Config config = Config.buildImageConfig(true, 100, 100, null, null, 0, 3, 1.6f, 0.2f);
        BufferedImage image = ImageIO.read(new FileInputStream(tempDir + "/test2.jpg"));
        new PdfWatermark().make(new FileInputStream(tempDir + "/deligne.pdf"), new FileOutputStream(tempDir + "/" + UUID.randomUUID().toString() + ".pdf"), image, config);
    }
}

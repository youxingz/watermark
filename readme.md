### Watermark in Java

Easy to use, supports a variety of custom styles.

Support file format:

- pdf (detail: [pdfbox](https://pdfbox.apache.org/))
- image (`BufferedImage`, e.g. `png`, `jpg`, `bmp`, ...)

Support watermark:

- text (`String`)
- image (`BufferedImage`)

Support styles:

- `font`: any font you installed
- `margin`: a box (text or image) margins `top`, `bottom`, `left`, `right`
- `position`: the position of watermark, e.g. `top-left`, `bottom-right`
- `theta`: rotation of watermark in degree, e.g. `-45`
- `alpha`: transparency of watermark, the range is `0-1`
- `mode`: support `fit-page`(repeat over the entire page), and `single`(only render once at the specified `position`)
- `dense` and `space`: if `mode=fit-page`, it changes the density of watermarks of the page

### Examples

Text watermark:

```java
public class Example {

    public static void main(String[] args) throws IOException, WatermarkException {
        Watermark watermark = new PdfWatermark();
        String watermarkStr = "Hello World";
        Config config = Config.buildTextConfig(true, new Config.FontStyle("Roboto", 32, Color.RED), -45, 3, 1.6f, 0.2f);
        watermark.make(new FileInputStream("test.pdf"), new FileOutputStream("target.pdf"), watermarkStr, config);
    }
}
```

Image watermark:

```java
public class Example {

    public static void main(String[] args) throws IOException, WatermarkException {
        Watermark watermark = new PdfWatermark();
        BufferedImage watermarkIcon = ImageIO.read(new File("image.png"));
        Config config = Config.buildTextConfig(true, null, -45, 3, 1.6f, 0.2f);
        watermark.make(new FileInputStream("test.pdf"), new FileOutputStream("target.pdf"), watermarkIcon, config);
    }
}
```

### Custom Implementation

You can implement interface `Watermark` to support more files of different formats.

Example:

```java
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
```

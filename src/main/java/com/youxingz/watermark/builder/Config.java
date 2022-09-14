package com.youxingz.watermark.builder;

import java.awt.*;

public class Config {
    private Type type;
    // if type == image ::
    private float width;
    private float height;
    // if type == text ::
    private FontStyle fontStyle;

    private Margin margin; // default: Margin(0, 0, 0, 0);
    private Position position = Position.bottom_right;
    private float theta; // in degree, e.g. 45
    private float dense = 3;
    private float space = 1.6F; // 间距
    private float alpha = 1.0f;
    private Mode mode;

    public enum Mode {
        fit_page, // 全屏覆盖
        single, // 只添加单个水印
    }

    public enum Type {
        image,
        text,
    }

    public enum Position {
        top_left,
        top_center,
        top_right,
        middle_left,
        middle_center,
        middle_right,
        bottom_left,
        bottom_center,
        bottom_right,
    }

    private Config() {
    }

    private Config(Type type, Mode mode, int width, int height, Position position, Margin margin, float theta, float dense, float space, float alpha, FontStyle fontStyle) {
        this.type = type;
        this.mode = mode;
        this.width = width;
        this.height = height;
        this.position = position;
        this.theta = theta;
        this.dense = dense;
        this.space = space;
        this.fontStyle = fontStyle;
        this.margin = margin;
        this.alpha = alpha;
    }

    public static Config buildTextConfig(boolean fitFullPage, FontStyle fontStyle, float theta, float dense, float space, float alpha) {
        return buildTextConfig(fitFullPage, fontStyle, null, null, theta, dense, space, alpha);
    }

    public static Config buildImageConfig(boolean fitFullPage, Position position, Margin margin, float theta, float dense, float space, float alpha) {
        return buildImageConfig(fitFullPage, -1, -1, position, margin, theta, dense, space, alpha);
    }

    public static Config buildTextConfig(boolean fitFullPage, FontStyle fontStyle, Position position, Margin margin, float theta, float dense, float space, float alpha) {
        return new Config(Type.text, fitFullPage ? Mode.fit_page : Mode.single, 0, 0, position, margin, theta, dense, space, alpha, fontStyle);
    }

    public static Config buildImageConfig(boolean fitFullPage, int width, int height, Position position, Margin margin, float theta, float dense, float space, float alpha) {
        return new Config(Type.image, fitFullPage ? Mode.fit_page : Mode.single, width, height, position, margin, theta, dense, space, alpha, null);
    }

    public static class FontStyle {
        private String fontName;
        private int fontSize;
        private Color color = Color.BLACK;

        public FontStyle(String fontName, int fontSize, Color color) {
            this.fontName = fontName;
            this.fontSize = fontSize;
            this.color = color;
        }

        public String getFontName() {
            return fontName;
        }

        public void setFontName(String fontName) {
            this.fontName = fontName;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "FontStyle{" +
                    "fontName='" + fontName + '\'' +
                    ", fontSize=" + fontSize +
                    ", color=" + color +
                    '}';
        }
    }

    public static class Margin {
        private float left;
        private float right;
        private float top;
        private float bottom;

        public Margin(float marginTop, float marginRight, float marginLeft, float marginBottom) {
            this.left = marginLeft;
            this.right = marginRight;
            this.top = marginTop;
            this.bottom = marginBottom;
        }

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        @Override
        public String toString() {
            return "Margin{" +
                    "left=" + left +
                    ", right=" + right +
                    ", top=" + top +
                    ", bottom=" + bottom +
                    '}';
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public FontStyle getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public float getDense() {
        return dense;
    }

    public void setDense(float dense) {
        this.dense = dense;
    }

    public float getSpace() {
        return space;
    }

    public void setSpace(float space) {
        this.space = space;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Config fixConfig(int pageWidth, int pageHeight) {
        if (alpha > 1 || alpha < 0) alpha = 1;
        if (margin == null)
//            margin = new Config.Margin(0, 0, 0, 0);
            margin = new Config.Margin((int) (pageHeight * .02), (int) (pageWidth * .04), (int) (pageWidth * .02), (int) (pageHeight * .04));
        if (fontStyle == null) fontStyle = new Config.FontStyle("宋体", (pageHeight + pageWidth) / 60, Color.GRAY);
        if (position == null) position = Config.Position.bottom_right;
        if (dense <= 0) dense = 3.6f; // default
        if (space <= 0) space = 1.2f;
        return this;
    }

    public Config fixConfig(int pageWidth, int pageHeight, int boxWidth, int boxHeight) {
//        System.out.println("page: width=" + pageWidth + ", height=" + pageHeight);
        this.fixConfig(pageWidth, pageHeight);
        if (width <= 0) // else: already set by user
            this.width = boxWidth;
        if (height <= 0) // else: already set by user
            this.height = boxHeight;
//        System.out.println(this);
        return this;
    }

    @Override
    public String toString() {
        return "Config{" +
                "mode=" + mode +
                ", type=" + type +
                ", width=" + width +
                ", height=" + height +
                ", fontStyle=" + fontStyle +
                ", margin=" + margin +
                ", position=" + position +
                ", theta=" + theta +
                ", dense=" + dense +
                ", space=" + space +
                ", alpha=" + alpha +
                '}';
    }
}

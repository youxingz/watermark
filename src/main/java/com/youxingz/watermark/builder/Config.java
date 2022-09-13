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
    private float dense;
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

    private Config(Type type, Mode mode, int width, int height, Position position, Margin margin, float theta, float dense, float alpha, FontStyle fontStyle) {
        this.type = type;
        this.mode = mode;
        this.width = width;
        this.height = height;
        this.position = position;
        this.theta = theta;
        this.dense = dense;
        this.fontStyle = fontStyle;
        this.margin = margin;
        this.alpha = alpha;
    }

    public static Config buildTextConfig(FontStyle fontStyle, float theta, float dense, float alpha) {
        return buildTextConfig(fontStyle, null, null, true, theta, dense, alpha);
    }

    public static Config buildTextConfig(FontStyle fontStyle, Position position, Margin margin, boolean fitFullPage, float theta, float dense, float alpha) {
        return new Config(Type.text, fitFullPage ? Mode.fit_page : Mode.single, 0, 0, position, margin, theta, dense, alpha, fontStyle);
    }

    public static Config buildImageConfig(int width, int height, Position position, Margin margin, boolean fitFullPage, float theta, float dense, float alpha) {
        return new Config(Type.image, fitFullPage ? Mode.fit_page : Mode.single, width, height, position, margin, theta, dense, alpha, null);
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
    }

    public static class Margin {
        private float marginLeft;
        private float marginRight;
        private float marginTop;
        private float marginBottom;

        public Margin(float marginTop, float marginRight, float marginLeft, float marginBottom) {
            this.marginLeft = marginLeft;
            this.marginRight = marginRight;
            this.marginTop = marginTop;
            this.marginBottom = marginBottom;
        }

        public float getMarginLeft() {
            return marginLeft;
        }

        public void setMarginLeft(float marginLeft) {
            this.marginLeft = marginLeft;
        }

        public float getMarginRight() {
            return marginRight;
        }

        public void setMarginRight(float marginRight) {
            this.marginRight = marginRight;
        }

        public float getMarginTop() {
            return marginTop;
        }

        public void setMarginTop(float marginTop) {
            this.marginTop = marginTop;
        }

        public float getMarginBottom() {
            return marginBottom;
        }

        public void setMarginBottom(float marginBottom) {
            this.marginBottom = marginBottom;
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
}

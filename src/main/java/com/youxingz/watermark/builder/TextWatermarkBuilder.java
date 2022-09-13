package com.youxingz.watermark.builder;

public class TextWatermarkBuilder {
    public byte[] build(String text, Config config) {
        return text.getBytes();
    }
}

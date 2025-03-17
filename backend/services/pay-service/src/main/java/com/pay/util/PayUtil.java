package com.pay.util;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.ByteArrayOutputStream;
import java.util.Base64;


public class PayUtil {

    private final static QrConfig QR_CONFIG = new QrConfig(500, 500);
    static {
        QR_CONFIG.setErrorCorrection(ErrorCorrectionLevel.H);
    }

    public static String urlToQrcode(String url) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String targetType = "jpg";
        QrCodeUtil.generate(url, QR_CONFIG, targetType, baos);
        String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        return "data:" + targetType + ";base64," + base64;
    }
}
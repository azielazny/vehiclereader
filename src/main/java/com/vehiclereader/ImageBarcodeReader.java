package com.vehiclereader;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public class ImageBarcodeReader {

    private BufferedImage aztecFile;
    private String charset = "UTF-8"; // or "ISO-8859-1"
    private Map<EncodeHintType, ErrorCorrectionLevel> hintMap = (Map<EncodeHintType, ErrorCorrectionLevel>) new HashMap<>().put(EncodeHintType.AZTEC_LAYERS, ErrorCorrectionLevel.L);

    public static void main(String[] args) throws IOException, NotFoundException {
        String filePath = "Y5Vt7.png";
//        String uriFile = "https://i.stack.imgur.com/Y5Vt7.png";

        ImageBarcodeReader imageBarcodeReader = new ImageBarcodeReader();
        imageBarcodeReader.readAztecCodeFromFile(filePath);
        System.out.println("Data read from Aztec by filepath: "
                + imageBarcodeReader.readAztecCode());

//        imageBarcodeReader.readAztecCodeFromUri(uriFile);
//        System.out.println("Data read from Aztec by uri: "
//                + imageBarcodeReader.readAztecCode());
    }

    public void readAztecCodeFromFile(String filePath) throws IOException {
        this.aztecFile = ImageIO.read(new FileInputStream(filePath));
    }

    public void readAztecCodeFromUri(String uriFile) throws IOException {
        this.aztecFile = ImageIO.read(new URL(uriFile));
    }

    public String readAztecCode()
            throws IOException, NotFoundException {

        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<>(
                DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.FALSE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
//        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(getAztecFile())));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, tmpHintsMap);
        return qrCodeResult.getText();

    }

    public void setAztecFile(BufferedImage aztecFile) {
        this.aztecFile = aztecFile;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<EncodeHintType, ErrorCorrectionLevel> getHintMap() {
        return hintMap;
    }

    public void setHintMap(Map<EncodeHintType, ErrorCorrectionLevel> hintMap) {
        this.hintMap = hintMap;
    }

    public BufferedImage getAztecFile() {
        return aztecFile;
    }


    /* zXing stuff */

    private RGBLuminanceSource zxRGBSource;
    private Reader zxReader;
    private Result zxResult;
    private BinaryBitmap zxBinBmp;


}

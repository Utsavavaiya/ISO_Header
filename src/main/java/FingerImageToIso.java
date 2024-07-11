import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

@Component
public class FingerImageToIso {
    private static Logger logger = LoggerFactory.getLogger(FingerImageToIso.class);
    boolean isSlapFinger = false;
    boolean isSingleFinger = true;
    String modality = "finger unknown";
    String imgCompressionAlgo = "4";
    int imgWidth;
    int imgHeight;

    public byte[] getFingerHeaderWithImage(byte[] image) {
        try {
            BufferedImage img = readJP2Image(image);
            if (img != null) {
                imgWidth = img.getWidth();
                imgHeight = img.getHeight();
            } else {
                logger.error("Error: Unable to read the JP2 image.");
            }
        } catch (IOException e) {
            logger.error("Error" + e);
        }

        int totalHeaderSize = 46 + image.length;
        byte[] fingerArray = new byte[totalHeaderSize];
        int lengthCounter = 0;
        byte[] generalHeader = getFingerGeneralHeader(image.length);
        System.arraycopy(generalHeader, 0, fingerArray, lengthCounter, generalHeader.length);
        lengthCounter += generalHeader.length;
        byte[] imageHeader = getFingerImageHeader(image, isSlapFinger, isSingleFinger, modality);
        System.arraycopy(imageHeader, 0, fingerArray, lengthCounter, imageHeader.length);
        lengthCounter += imageHeader.length;
        return fingerArray;
    }

    private BufferedImage readJP2Image(byte[] image) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(image));
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG2000");
        if (!readers.hasNext()) {
            throw new IOException("No reader found for JPEG2000");
        }
        ImageReader reader = readers.next();
        reader.setInput(iis, true);
        return reader.read(0);
    }

    private byte[] getFingerGeneralHeader(int imageSize) {
        byte[] fingerGeneralHeaderArray = new byte[32];
        int lengthCounter = 0;
        String hexFIR = "46495200";
        byte[] formatIdentifier = new byte[4];
        formatIdentifier = DataUtil.getByteArray(hexFIR, 4);
        System.arraycopy(formatIdentifier, 0, fingerGeneralHeaderArray, 0, formatIdentifier.length);
        lengthCounter += formatIdentifier.length;
        String hexVersion = "30313000";
        byte[] versionNumber = new byte[4];
        versionNumber = DataUtil.getByteArray(hexVersion, 4);
        System.arraycopy(versionNumber, 0, fingerGeneralHeaderArray, lengthCounter, versionNumber.length);
        lengthCounter += versionNumber.length;
        int totalSize = 32 + 1 * (14 + imageSize);
        String recordLengthStr = Integer.toHexString(totalSize);
        byte[] recordLength = new byte[6];
        recordLength = DataUtil.getByteArray(recordLengthStr, 6);
        System.arraycopy(recordLength, 0, fingerGeneralHeaderArray, lengthCounter, recordLength.length);
        lengthCounter += recordLength.length;
        String hexCaptureDeviceId = "00";
        byte[] captureDeviceId = new byte[2];
        captureDeviceId = DataUtil.getByteArray(hexCaptureDeviceId, 2);
        System.arraycopy(captureDeviceId, 0, fingerGeneralHeaderArray, lengthCounter, captureDeviceId.length);
        lengthCounter += captureDeviceId.length;
        String hexImageAcquistionLevel = "1F";
        byte[] imageAcquisitionLevel = new byte[2];
        imageAcquisitionLevel = DataUtil.getByteArray(hexImageAcquistionLevel, 2);
        System.arraycopy(imageAcquisitionLevel, 0, fingerGeneralHeaderArray, lengthCounter, imageAcquisitionLevel.length);
        lengthCounter += imageAcquisitionLevel.length;
        String hexNumberOfFingers = "1";
        byte[] numberOfFingers = new byte[1];
        numberOfFingers = DataUtil.getByteArray(hexNumberOfFingers, 1);
        System.arraycopy(numberOfFingers, 0, fingerGeneralHeaderArray, lengthCounter, numberOfFingers.length);
        lengthCounter += numberOfFingers.length;
        String hexScaleunits = "02";
        byte[] scaleUnits = new byte[1];
        scaleUnits = DataUtil.getByteArray(hexScaleunits, 1);
        System.arraycopy(scaleUnits, 0, fingerGeneralHeaderArray, lengthCounter, scaleUnits.length);
        lengthCounter += scaleUnits.length;
        String hexHorizontalScanResolution = "C5";
        byte[] horizontalScanResolution = new byte[2];
        horizontalScanResolution = DataUtil.getByteArray(hexHorizontalScanResolution, 2);
        System.arraycopy(horizontalScanResolution, 0, fingerGeneralHeaderArray, lengthCounter, horizontalScanResolution.length);
        lengthCounter += horizontalScanResolution.length;
        String hexVerticalScanResolution = "C5";
        byte[] verticalScanResolution = new byte[2];
        verticalScanResolution = DataUtil.getByteArray(hexVerticalScanResolution, 2);
        System.arraycopy(verticalScanResolution, 0, fingerGeneralHeaderArray, lengthCounter, verticalScanResolution.length);
        lengthCounter += verticalScanResolution.length;
        String hexHorizontalImageResolution = "C5";
        byte[] horizontalImageResolution = new byte[2];
        horizontalImageResolution = DataUtil.getByteArray(hexHorizontalImageResolution, 2);
        System.arraycopy(horizontalImageResolution, 0, fingerGeneralHeaderArray, lengthCounter, horizontalImageResolution.length);
        lengthCounter += horizontalImageResolution.length;
        String hexVerticalImageResolution = "C5";
        byte[] verticalImageResolution = new byte[2];
        verticalImageResolution = DataUtil.getByteArray(hexVerticalImageResolution, 2);
        System.arraycopy(verticalImageResolution, 0, fingerGeneralHeaderArray, lengthCounter, verticalImageResolution.length);
        lengthCounter += verticalImageResolution.length;
        String hexPixelDepth = "8";
        byte[] pixelDepth = new byte[1];
        pixelDepth = DataUtil.getByteArray(hexPixelDepth, 1);
        System.arraycopy(pixelDepth, 0, fingerGeneralHeaderArray, lengthCounter, pixelDepth.length);
        lengthCounter += pixelDepth.length;
        String hexCompressionAlgorithm = imgCompressionAlgo;
        byte[] compressionAlgorithm = new byte[1];
        compressionAlgorithm = DataUtil.getByteArray(hexCompressionAlgorithm, 1);
        System.arraycopy(compressionAlgorithm, 0, fingerGeneralHeaderArray, lengthCounter, compressionAlgorithm.length);
        lengthCounter += compressionAlgorithm.length;
        String hexReserved = "00";
        byte[] reserved = new byte[2];
        reserved = DataUtil.getByteArray(hexReserved, 2);
        System.arraycopy(reserved, 0, fingerGeneralHeaderArray, lengthCounter, reserved.length);
        lengthCounter += reserved.length;
        return fingerGeneralHeaderArray;
    }

    private byte[] getFingerImageHeader(byte[] image, boolean isSlapFinger, boolean isSingleFinger, String modality) {
        int totalHeaderSize = 14 + image.length;
        byte[] fingerImageHeaderArray = new byte[totalHeaderSize];
        int lengthCounter = 0;
        int totalSize = 14 + image.length;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] fingerDataLength = new byte[4];
        fingerDataLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(fingerDataLength, 0, fingerImageHeaderArray, 0, fingerDataLength.length);
        lengthCounter += fingerDataLength.length;
        byte[] fingerOrPalm = new byte[1];
        if (isSlapFinger) {
            if (modality.equals("right pointer-finger middle-finger ring-finger little-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(13));
            } else if (modality.equals("left pointer-finger middle-finger ring-finger little-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(14));
            } else if (modality.equals("left right thumb")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(15));
            }
        } else if (isSingleFinger) {
            if (modality.equals("right thumb")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(1));
            } else if (modality.equals("right pointer-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(2));
            } else if (modality.equals("right middle-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(3));
            } else if (modality.equals("right ring-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(4));
            } else if (modality.equals("right little-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(5));
            }
            if (modality.equals("left thumb")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(6));
            } else if (modality.equals("left pointer-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(7));
            } else if (modality.equals("left middle-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(8));
            } else if (modality.equals("left ring-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(9));
            } else if (modality.equals("left little-finger")) {
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(10));
            }
            if (modality.equals("finger unknown"))
                fingerOrPalm = DataUtil.getFingerOrPalm(Integer.toHexString(0));
        }
        System.arraycopy(fingerOrPalm, 0, fingerImageHeaderArray, lengthCounter, fingerOrPalm.length);
        lengthCounter += fingerOrPalm.length;
        String hexViewCount = "1";
        byte[] viewCount = new byte[1];
        viewCount = DataUtil.getByteArray(hexViewCount, 1);
        System.arraycopy(viewCount, 0, fingerImageHeaderArray, lengthCounter, viewCount.length);
        lengthCounter += viewCount.length;
        String hexViewNumber = "1";
        byte[] viewNumber = new byte[1];
        viewNumber = DataUtil.getByteArray(hexViewNumber, 1);
        System.arraycopy(viewNumber, 0, fingerImageHeaderArray, lengthCounter, viewNumber.length);
        lengthCounter += viewNumber.length;
        String hexImageQuality = "50";
        byte[] imageQuality = new byte[1];
        imageQuality = DataUtil.getByteArray(hexImageQuality, 1);
        System.arraycopy(imageQuality, 0, fingerImageHeaderArray, lengthCounter, imageQuality.length);
        lengthCounter += imageQuality.length;
        String hexImpressionType = "0";
        byte[] impressionType = new byte[1];
        impressionType = DataUtil.getByteArray(hexImpressionType, 1);
        System.arraycopy(impressionType, 0, fingerImageHeaderArray, lengthCounter, impressionType.length);
        lengthCounter += impressionType.length;
        String hexHorizontalLength = Integer.toHexString(imgWidth);
        byte[] horizontalLength = new byte[2];
        horizontalLength = DataUtil.getByteArray(hexHorizontalLength, 2);
        System.arraycopy(horizontalLength, 0, fingerImageHeaderArray, lengthCounter, horizontalLength.length);
        lengthCounter += horizontalLength.length;
        String hexVerticalLength = Integer.toHexString(imgHeight);
        byte[] verticalLength = new byte[2];
        verticalLength = DataUtil.getByteArray(hexVerticalLength, 2);
        System.arraycopy(verticalLength, 0, fingerImageHeaderArray, lengthCounter, verticalLength.length);
        lengthCounter += verticalLength.length;
        String hexReserved = "0";
        byte[] reserved = new byte[1];
        reserved = DataUtil.getByteArray(hexReserved, 1);
        System.arraycopy(reserved, 0, fingerImageHeaderArray, lengthCounter, reserved.length);
        lengthCounter += reserved.length;
        System.arraycopy(image, 0, fingerImageHeaderArray, lengthCounter, image.length);
        lengthCounter += image.length;
        return fingerImageHeaderArray;
    }
}

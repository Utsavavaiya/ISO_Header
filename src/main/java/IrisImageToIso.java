// package in.gov.uidai.packetcompressor.utils.headeraddition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class IrisImageToIso {
    private static Logger logger = LoggerFactory.getLogger(IrisImageToIso.class);
    private int imgWidth = 0;

    private int imgHeight = 0;

    private String imgCompressionAlgo = "0101";

    public byte[] getIrisHeaderWithImage(byte[] image) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
            imgWidth = img.getWidth();
            imgHeight = img.getHeight();
        } catch (IOException e) {
            logger.error("Error"+e);
        }
        int totalSize = 59 + image.length;
        byte[] irisArray = new byte[totalSize];
        int lengthCounter = 0;
        byte[] recordHeader = getIrisRecordHeader(image.length);
        System.arraycopy(recordHeader, 0, irisArray, 0, recordHeader.length);
        lengthCounter += recordHeader.length;
        byte[] subTypeHeader = getIrisSubtypeHeader("01");
        System.arraycopy(subTypeHeader, 0, irisArray, lengthCounter, subTypeHeader.length);
        lengthCounter += subTypeHeader.length;
        byte[] imageHeader = getIrisImageHeader(image.length);
        System.arraycopy(imageHeader, 0, irisArray, lengthCounter, imageHeader.length);
        lengthCounter += imageHeader.length;
        System.arraycopy(image, 0, irisArray, lengthCounter, image.length);
        lengthCounter += image.length;
        return irisArray;
    }

    private byte[] getIrisRecordHeader(int imageSize) {
        byte[] irisRecordHeaderArray = new byte[45];
        int lengthCounter = 0;
        String hexFormatIdentifier = "49495200";
        byte[] formatIdentifier = new byte[4];
        formatIdentifier = DataUtil.getByteArray(hexFormatIdentifier, 4);
        System.arraycopy(formatIdentifier, 0, irisRecordHeaderArray, 0, formatIdentifier.length);
        lengthCounter += formatIdentifier.length;
        String hexFormatVersion = "30313000";
        byte[] formatVersion = new byte[4];
        formatVersion = DataUtil.getByteArray(hexFormatVersion, 4);
        System.arraycopy(formatVersion, 0, irisRecordHeaderArray, lengthCounter, formatVersion.length);
        lengthCounter += formatVersion.length;
        int totalSize = 59 + imageSize;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] recordLength = new byte[4];
        recordLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(recordLength, 0, irisRecordHeaderArray, lengthCounter, recordLength.length);
        lengthCounter += recordLength.length;
        String hexCaptureDeviceId = "0";
        byte[] captureDeviceId = new byte[2];
        captureDeviceId = DataUtil.getByteArray(hexCaptureDeviceId, 2);
        System.arraycopy(captureDeviceId, 0, irisRecordHeaderArray, lengthCounter, captureDeviceId.length);
        lengthCounter += captureDeviceId.length;
        String hexNoOfSubtypes = "01";
        byte[] noOfSubtypes = new byte[1];
        noOfSubtypes = DataUtil.getByteArray(hexNoOfSubtypes, 1);
        System.arraycopy(noOfSubtypes, 0, irisRecordHeaderArray, lengthCounter, noOfSubtypes.length);
        lengthCounter += noOfSubtypes.length;
        String hexRecordHeaderLength = "2D";
        byte[] recordHeaderLength = new byte[2];
        recordHeaderLength = DataUtil.getByteArray(hexRecordHeaderLength, 2);
        System.arraycopy(recordHeaderLength, 0, irisRecordHeaderArray, lengthCounter, recordHeaderLength.length);
        lengthCounter += recordHeaderLength.length;
        String hexImageProperty = "00";
        byte[] imageProperty = new byte[2];
        imageProperty = DataUtil.getByteArray(hexImageProperty, 2);
        System.arraycopy(imageProperty, 0, irisRecordHeaderArray, lengthCounter, imageProperty.length);
        lengthCounter += imageProperty.length;
        String hexIrisDiameter = "00";
        byte[] irisDiameter = new byte[2];
        irisDiameter = DataUtil.getByteArray(hexIrisDiameter, 2);
        System.arraycopy(irisDiameter, 0, irisRecordHeaderArray, lengthCounter, irisDiameter.length);
        lengthCounter += irisDiameter.length;
        String hexImageFormat = imgCompressionAlgo;
        byte[] imageFormat = new byte[2];
        imageFormat = DataUtil.getByteArray(hexImageFormat, 2);
        System.arraycopy(imageFormat, 0, irisRecordHeaderArray, lengthCounter, imageFormat.length);
        lengthCounter += imageFormat.length;
        String hexRawImageWidth = Integer.toHexString(imgWidth);
        byte[] rawImageWidth = new byte[2];
        rawImageWidth = DataUtil.getByteArray(hexRawImageWidth, 2);
        System.arraycopy(rawImageWidth, 0, irisRecordHeaderArray, lengthCounter, rawImageWidth.length);
        lengthCounter += rawImageWidth.length;
        String hexRawImageHeight = Integer.toHexString(imgHeight);
        byte[] rawImageHeight = new byte[2];
        rawImageHeight = DataUtil.getByteArray(hexRawImageHeight, 2);
        System.arraycopy(rawImageHeight, 0, irisRecordHeaderArray, lengthCounter, rawImageHeight.length);
        lengthCounter += rawImageHeight.length;
        String hexIntensityDepth = "8";
        byte[] intensityDepth = new byte[1];
        intensityDepth = DataUtil.getByteArray(hexIntensityDepth, 1);
        System.arraycopy(intensityDepth, 0, irisRecordHeaderArray, lengthCounter, intensityDepth.length);
        lengthCounter += intensityDepth.length;
        String hexImageTransformation = "0";
        byte[] imageTransformation = new byte[1];
        imageTransformation = DataUtil.getByteArray(hexImageTransformation, 1);
        System.arraycopy(imageTransformation, 0, irisRecordHeaderArray, lengthCounter, imageTransformation.length);
        lengthCounter += imageTransformation.length;
        String hexDeviceUniqueIdentifier = "0";
        byte[] deviceUniqueIdentifier = new byte[16];
        deviceUniqueIdentifier = DataUtil.getByteArray(hexDeviceUniqueIdentifier, 16);
        System.arraycopy(deviceUniqueIdentifier, 0, irisRecordHeaderArray, lengthCounter, deviceUniqueIdentifier.length);
        lengthCounter += deviceUniqueIdentifier.length;
        return irisRecordHeaderArray;
    }

    private byte[] getIrisSubtypeHeader(String subType) {
        byte[] subTypeHeaderArray = new byte[3];
        int lengthCounter = 0;
        byte[] biometricSubType = new byte[1];
        biometricSubType = getBiometricSubType(subType);
        System.arraycopy(biometricSubType, 0, subTypeHeaderArray, 0, biometricSubType.length);
        lengthCounter += biometricSubType.length;
        String hexNoOfImages = "1";
        byte[] noOfImages = new byte[2];
        noOfImages = DataUtil.getByteArray(hexNoOfImages, 2);
        System.arraycopy(noOfImages, 0, subTypeHeaderArray, lengthCounter, noOfImages.length);
        lengthCounter += noOfImages.length;
        return subTypeHeaderArray;
    }

    private byte[] getBiometricSubType(String hexSubType) {
        byte[] biometricSubType = new byte[1];
        biometricSubType = DataUtil.getByteArray(hexSubType, 1);
        return biometricSubType;
    }

    private byte[] getIrisImageHeader(int imageSize) {
        byte[] imageHeaderArray = new byte[11];
        int lengthCounter = 0;
        String hexImageNumber = "1";
        byte[] imageNumber = new byte[2];
        imageNumber = DataUtil.getByteArray(hexImageNumber, 2);
        System.arraycopy(imageNumber, 0, imageHeaderArray, 0, imageNumber.length);
        lengthCounter += imageNumber.length;
        String hexQuality = "0";
        byte[] quality = new byte[1];
        quality = DataUtil.getByteArray(hexQuality, 1);
        System.arraycopy(quality, 0, imageHeaderArray, lengthCounter, quality.length);
        lengthCounter += quality.length;
        String hexRotationAngle = "00";
        byte[] rotationAngle = new byte[2];
        rotationAngle = DataUtil.getByteArray(hexRotationAngle, 2);
        System.arraycopy(rotationAngle, 0, imageHeaderArray, lengthCounter, rotationAngle.length);
        lengthCounter += rotationAngle.length;
        String hexRotationuncertainty = "05";
        byte[] rotationUncertainty = new byte[2];
        rotationUncertainty = DataUtil.getByteArray(hexRotationuncertainty, 2);
        System.arraycopy(rotationUncertainty, 0, imageHeaderArray, lengthCounter, rotationUncertainty.length);
        lengthCounter += rotationUncertainty.length;
        int totalSize = imageSize;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] imageLength = new byte[4];
        imageLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(imageLength, 0, imageHeaderArray, lengthCounter, imageLength.length);
        lengthCounter += imageLength.length;
        return imageHeaderArray;
    }

    public byte[] getIrisHeaderWithImage(ImageDetail imageDetail, String subType) {
        int totalSize = 59 + (imageDetail.getData()).length;
        byte[] irisArray = new byte[totalSize];
        int lengthCounter = 0;
        byte[] recordHeader = getIrisRecordHeader(imageDetail);
        System.arraycopy(recordHeader, 0, irisArray, 0, recordHeader.length);
        lengthCounter += recordHeader.length;
        byte[] subTypeHeader = getIrisSubtypeHeader(subType);
        System.arraycopy(subTypeHeader, 0, irisArray, lengthCounter, subTypeHeader.length);
        lengthCounter += subTypeHeader.length;
        byte[] imageHeader = getIrisImageHeader(imageDetail);
        System.arraycopy(imageHeader, 0, irisArray, lengthCounter, imageHeader.length);
        lengthCounter += imageHeader.length;
        System.arraycopy(imageDetail.getData(), 0, irisArray, lengthCounter, (imageDetail.getData()).length);
        lengthCounter += (imageDetail.getData()).length;
        return irisArray;
    }

    private byte[] getIrisRecordHeader(ImageDetail imageDetail) {
        byte[] irisRecordHeaderArray = new byte[45];
        int lengthCounter = 0;
        String hexFormatIdentifier = "49495200";
        byte[] formatIdentifier = new byte[4];
        formatIdentifier = DataUtil.getByteArray(hexFormatIdentifier, 4);
        System.arraycopy(formatIdentifier, 0, irisRecordHeaderArray, 0, formatIdentifier.length);
        lengthCounter += formatIdentifier.length;
        String hexFormatVersion = "11100";
        byte[] formatVersion = new byte[4];
        formatVersion = DataUtil.getByteArray(hexFormatVersion, 4);
        System.arraycopy(formatVersion, 0, irisRecordHeaderArray, lengthCounter, formatVersion.length);
        lengthCounter += formatVersion.length;
        int totalSize = 59 + (imageDetail.getData()).length;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] recordLength = new byte[4];
        recordLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(recordLength, 0, irisRecordHeaderArray, lengthCounter, recordLength.length);
        lengthCounter += recordLength.length;
        String hexCaptureDeviceId = "0";
        byte[] captureDeviceId = new byte[2];
        captureDeviceId = DataUtil.getByteArray(hexCaptureDeviceId, 2);
        System.arraycopy(captureDeviceId, 0, irisRecordHeaderArray, lengthCounter, captureDeviceId.length);
        lengthCounter += captureDeviceId.length;
        String hexNoOfSubtypes = "01";
        byte[] noOfSubtypes = new byte[1];
        noOfSubtypes = DataUtil.getByteArray(hexNoOfSubtypes, 1);
        System.arraycopy(noOfSubtypes, 0, irisRecordHeaderArray, lengthCounter, noOfSubtypes.length);
        lengthCounter += noOfSubtypes.length;
        String hexRecordHeaderLength = "2D";
        byte[] recordHeaderLength = new byte[2];
        recordHeaderLength = DataUtil.getByteArray(hexRecordHeaderLength, 2);
        System.arraycopy(recordHeaderLength, 0, irisRecordHeaderArray, lengthCounter, recordHeaderLength.length);
        lengthCounter += recordHeaderLength.length;
        String hexImageProperty = "00";
        byte[] imageProperty = new byte[2];
        imageProperty = DataUtil.getByteArray(hexImageProperty, 2);
        System.arraycopy(imageProperty, 0, irisRecordHeaderArray, lengthCounter, imageProperty.length);
        lengthCounter += imageProperty.length;
        String hexIrisDiameter = "00";
        byte[] irisDiameter = new byte[2];
        irisDiameter = DataUtil.getByteArray(hexIrisDiameter, 2);
        System.arraycopy(irisDiameter, 0, irisRecordHeaderArray, lengthCounter, irisDiameter.length);
        lengthCounter += irisDiameter.length;
        String hexImageFormat = "000E";
        byte[] imageFormat = new byte[2];
        imageFormat = DataUtil.getByteArray(hexImageFormat, 2);
        System.arraycopy(imageFormat, 0, irisRecordHeaderArray, lengthCounter, imageFormat.length);
        lengthCounter += imageFormat.length;
        String hexRawImageWidth = Integer.toHexString(imageDetail.getWidth());
        byte[] rawImageWidth = new byte[2];
        rawImageWidth = DataUtil.getByteArray(hexRawImageWidth, 2);
        System.arraycopy(rawImageWidth, 0, irisRecordHeaderArray, lengthCounter, rawImageWidth.length);
        lengthCounter += rawImageWidth.length;
        String hexRawImageHeight = Integer.toHexString(imageDetail.getHeight());
        byte[] rawImageHeight = new byte[2];
        rawImageHeight = DataUtil.getByteArray(hexRawImageHeight, 2);
        System.arraycopy(rawImageHeight, 0, irisRecordHeaderArray, lengthCounter, rawImageHeight.length);
        lengthCounter += rawImageHeight.length;
        String hexIntensityDepth = "8";
        byte[] intensityDepth = new byte[1];
        intensityDepth = DataUtil.getByteArray(hexIntensityDepth, 1);
        System.arraycopy(intensityDepth, 0, irisRecordHeaderArray, lengthCounter, intensityDepth.length);
        lengthCounter += intensityDepth.length;
        String hexImageTransformation = "0";
        byte[] imageTransformation = new byte[1];
        imageTransformation = DataUtil.getByteArray(hexImageTransformation, 1);
        System.arraycopy(imageTransformation, 0, irisRecordHeaderArray, lengthCounter, imageTransformation.length);
        lengthCounter += imageTransformation.length;
        String hexDeviceUniqueIdentifier = "0";
        byte[] deviceUniqueIdentifier = new byte[16];
        deviceUniqueIdentifier = DataUtil.getByteArray(hexDeviceUniqueIdentifier, 16);
        System.arraycopy(deviceUniqueIdentifier, 0, irisRecordHeaderArray, lengthCounter, deviceUniqueIdentifier.length);
        lengthCounter += deviceUniqueIdentifier.length;
        return irisRecordHeaderArray;
    }

    private byte[] getIrisImageHeader(ImageDetail imageDetail) {
        byte[] imageHeaderArray = new byte[11];
        int lengthCounter = 0;
        String hexImageNumber = "1";
        byte[] imageNumber = new byte[2];
        imageNumber = DataUtil.getByteArray(hexImageNumber, 2);
        System.arraycopy(imageNumber, 0, imageHeaderArray, 0, imageNumber.length);
        lengthCounter += imageNumber.length;
        String hexQuality = "0";
        byte[] quality = new byte[1];
        quality = DataUtil.getByteArray(hexQuality, 1);
        System.arraycopy(quality, 0, imageHeaderArray, lengthCounter, quality.length);
        lengthCounter += quality.length;
        String hexRotationAngle = "00";
        byte[] rotationAngle = new byte[2];
        rotationAngle = DataUtil.getByteArray(hexRotationAngle, 2);
        System.arraycopy(rotationAngle, 0, imageHeaderArray, lengthCounter, rotationAngle.length);
        lengthCounter += rotationAngle.length;
        String hexRotationuncertainty = "05";
        byte[] rotationUncertainty = new byte[2];
        rotationUncertainty = DataUtil.getByteArray(hexRotationuncertainty, 2);
        System.arraycopy(rotationUncertainty, 0, imageHeaderArray, lengthCounter, rotationUncertainty.length);
        lengthCounter += rotationUncertainty.length;
        int totalSize = (imageDetail.getData()).length;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] imageLength = new byte[4];
        imageLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(imageLength, 0, imageHeaderArray, lengthCounter, imageLength.length);
        lengthCounter += imageLength.length;
        return imageHeaderArray;
    }
}

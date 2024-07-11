// package in.gov.uidai.packetcompressor.utils.headeraddition;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FaceImageToIso {

    private static Logger logger = LoggerFactory.getLogger(FaceImageToIso.class);
    private static int imgWidth = 0;
    private static int imgHeight = 0;
    private static String imgCompressionAlgo = "01";

    public static byte[] getFacialHeaderWithimage(byte[] image, String imgCompAlgo) {
        imgCompressionAlgo = imgCompAlgo;
        return getFacialHeaderWithimage(image);
    }

    public static byte[] getFacialHeaderWithimage(byte[] image) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
            imgWidth = img.getWidth();
            imgHeight = img.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int totalHeaderSize = 46 + image.length;
        byte[] facialArray = new byte[totalHeaderSize];
        int lengthCounter = 0;
        byte[] recordHeader = getFacialRecordHeader(image.length);
        System.arraycopy(recordHeader, 0, facialArray, lengthCounter, recordHeader.length);
        lengthCounter += recordHeader.length;
        byte[] recordData = getFacialRecordData(image);
        System.arraycopy(recordData, 0, facialArray, lengthCounter, recordData.length);
        lengthCounter += recordData.length;
        return facialArray;
    }

    private static byte[] getFacialRecordHeader(int imageSize) {
        byte[] facialRecordHeaderArray = new byte[14];
        int lengthCounter = 0;
        String hexFormatIdentifier = "46414300";
        byte[] formatIdentifier = new byte[4];
        formatIdentifier = DataUtil.getByteArray(hexFormatIdentifier, 4);
        System.arraycopy(formatIdentifier, 0, facialRecordHeaderArray, lengthCounter, formatIdentifier.length);
        lengthCounter += formatIdentifier.length;
        String hexVersion = "30313000";
        byte[] version = new byte[4];
        version = DataUtil.getByteArray(hexVersion, 4);
        System.arraycopy(version, 0, facialRecordHeaderArray, lengthCounter, version.length);
        lengthCounter += version.length;
        int totalSize = 46 + imageSize;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] lengthOfRecord = new byte[4];
        lengthOfRecord = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(lengthOfRecord, 0, facialRecordHeaderArray, lengthCounter, lengthOfRecord.length);
        lengthCounter += lengthOfRecord.length;
        String hexNumberOfImages = "1";
        byte[] numberOfimages = new byte[2];
        numberOfimages = DataUtil.getByteArray(hexNumberOfImages, 2);
        System.arraycopy(numberOfimages, 0, facialRecordHeaderArray, lengthCounter, numberOfimages.length);
        lengthCounter += numberOfimages.length;
        return facialRecordHeaderArray;
    }

    private static byte[] getFacialRecordData(byte[] image) {
        int totalHeaderSize = 32 + image.length;
        byte[] facialRecordDataArray = new byte[totalHeaderSize];
        int lengthCounter = 0;
        int totalSize = 32 + image.length;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] recordDataLength = new byte[4];
        recordDataLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(recordDataLength, 0, facialRecordDataArray, lengthCounter, recordDataLength.length);
        lengthCounter += recordDataLength.length;
        String hexNumberOfFeaturePoints = "00";
        byte[] numberOfFeaturePoints = new byte[2];
        numberOfFeaturePoints = DataUtil.getByteArray(hexNumberOfFeaturePoints, 2);
        System.arraycopy(numberOfFeaturePoints, 0, facialRecordDataArray, lengthCounter, numberOfFeaturePoints.length);
        lengthCounter += numberOfFeaturePoints.length;
        String hexGender = "00";
        byte[] gender = new byte[1];
        gender = DataUtil.getByteArray(hexGender, 1);
        System.arraycopy(gender, 0, facialRecordDataArray, lengthCounter, gender.length);
        lengthCounter += gender.length;
        String hexEyeColour = "00";
        byte[] eyeColour = new byte[1];
        eyeColour = DataUtil.getByteArray(hexEyeColour, 1);
        System.arraycopy(eyeColour, 0, facialRecordDataArray, lengthCounter, eyeColour.length);
        lengthCounter += eyeColour.length;
        String hexHairColour = "00";
        byte[] hairColour = new byte[1];
        hairColour = DataUtil.getByteArray(hexHairColour, 1);
        System.arraycopy(hairColour, 0, facialRecordDataArray, lengthCounter, hairColour.length);
        lengthCounter += hairColour.length;
        String hexPropertyMask = "00";
        byte[] propertyMask = new byte[3];
        propertyMask = DataUtil.getByteArray(hexPropertyMask, 3);
        System.arraycopy(propertyMask, 0, facialRecordDataArray, lengthCounter, propertyMask.length);
        lengthCounter += propertyMask.length;
        byte[] expression = new byte[2];
        String hexExpression = "00";
        expression = DataUtil.getByteArray(hexExpression, 2);
        System.arraycopy(expression, 0, facialRecordDataArray, lengthCounter, expression.length);
        lengthCounter += expression.length;
        String hexPosAngle = "00";
        byte[] posAngle = new byte[3];
        posAngle = DataUtil.getByteArray(hexPosAngle, 3);
        System.arraycopy(posAngle, 0, facialRecordDataArray, lengthCounter, posAngle.length);
        lengthCounter += posAngle.length;
        String hexPosAngleUncertainty = "00";
        byte[] posAngleUncertainty = new byte[3];
        posAngleUncertainty = DataUtil.getByteArray(hexPosAngleUncertainty, 3);
        System.arraycopy(posAngleUncertainty, 0, facialRecordDataArray, lengthCounter, posAngleUncertainty.length);
        lengthCounter += posAngleUncertainty.length;
        String hexImageType = "01";
        byte[] imageType = new byte[1];
        imageType = DataUtil.getByteArray(hexImageType, 1);
        System.arraycopy(imageType, 0, facialRecordDataArray, lengthCounter, imageType.length);
        lengthCounter += imageType.length;
        String hexImageDatatype = imgCompressionAlgo;
        byte[] imageDatatype = new byte[1];
        imageDatatype = DataUtil.getByteArray(hexImageDatatype, 1);
        System.arraycopy(imageDatatype, 0, facialRecordDataArray, lengthCounter, imageDatatype.length);
        lengthCounter += imageDatatype.length;
        String hexWidth = Integer.toHexString(imgWidth);
        byte[] width = new byte[2];
        width = DataUtil.getByteArray(hexWidth, 2);
        System.arraycopy(width, 0, facialRecordDataArray, lengthCounter, width.length);
        lengthCounter += width.length;
        String hexHeight = Integer.toHexString(imgHeight);
        byte[] height = new byte[2];
        height = DataUtil.getByteArray(hexHeight, 2);
        System.arraycopy(height, 0, facialRecordDataArray, lengthCounter, height.length);
        lengthCounter += height.length;
        String hexImageColourSpace = "00";
        byte[] imageColourSpace = new byte[1];
        imageColourSpace = DataUtil.getByteArray(hexImageColourSpace, 1);
        System.arraycopy(imageColourSpace, 0, facialRecordDataArray, lengthCounter, imageColourSpace.length);
        lengthCounter += imageColourSpace.length;
        String hexSourceType = "06";
        byte[] sourceType = new byte[1];
        sourceType = DataUtil.getByteArray(hexSourceType, 1);
        System.arraycopy(sourceType, 0, facialRecordDataArray, lengthCounter, sourceType.length);
        lengthCounter += sourceType.length;
        String hexDeviceType = "00";
        byte[] deviceType = new byte[2];
        deviceType = DataUtil.getByteArray(hexDeviceType, 2);
        System.arraycopy(deviceType, 0, facialRecordDataArray, lengthCounter, deviceType.length);
        lengthCounter += deviceType.length;
        String hexQuality = "00";
        byte[] quality = new byte[2];
        quality = DataUtil.getByteArray(hexQuality, 2);
        System.arraycopy(quality, 0, facialRecordDataArray, lengthCounter, quality.length);
        lengthCounter += quality.length;
        System.arraycopy(image, 0, facialRecordDataArray, lengthCounter, image.length);
        lengthCounter += image.length;
        return facialRecordDataArray;
    }

    public static byte[] getFacialHeaderWithimage(ImageDetail imageDetail) {
        int totalHeaderSize = 46 + (imageDetail.getData()).length;
        byte[] facialArray = new byte[totalHeaderSize];
        int lengthCounter = 0;
        byte[] recordHeader = getFacialRecordHeader(imageDetail);
        System.arraycopy(recordHeader, 0, facialArray, lengthCounter, recordHeader.length);
        lengthCounter += recordHeader.length;
        byte[] recordData = getFacialRecordData(imageDetail);
        System.arraycopy(recordData, 0, facialArray, lengthCounter, recordData.length);
        lengthCounter += recordData.length;
        return facialArray;
    }

    private static byte[] getFacialRecordHeader(ImageDetail imageDetail) {
        byte[] facialRecordHeaderArray = new byte[14];
        int lengthCounter = 0;
        String hexFormatIdentifier = "46414300";
        byte[] formatIdentifier = new byte[4];
        formatIdentifier = DataUtil.getByteArray(hexFormatIdentifier, 4);
        System.arraycopy(formatIdentifier, 0, facialRecordHeaderArray, lengthCounter, formatIdentifier.length);
        lengthCounter += formatIdentifier.length;
        String hexVersion = "30313000";
        byte[] version = new byte[4];
        version = DataUtil.getByteArray(hexVersion, 4);
        System.arraycopy(version, 0, facialRecordHeaderArray, lengthCounter, version.length);
        lengthCounter += version.length;
        int totalSize = 46 + (imageDetail.getData()).length;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] lengthOfRecord = new byte[4];
        lengthOfRecord = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(lengthOfRecord, 0, facialRecordHeaderArray, lengthCounter, lengthOfRecord.length);
        lengthCounter += lengthOfRecord.length;
        String hexNumberOfImages = "1";
        byte[] numberOfimages = new byte[2];
        numberOfimages = DataUtil.getByteArray(hexNumberOfImages, 2);
        System.arraycopy(numberOfimages, 0, facialRecordHeaderArray, lengthCounter, numberOfimages.length);
        lengthCounter += numberOfimages.length;
        return facialRecordHeaderArray;
    }

    private static byte[] getFacialRecordData(ImageDetail imageDetail) {
        int totalHeaderSize = 32 + (imageDetail.getData()).length;
        byte[] facialRecordDataArray = new byte[totalHeaderSize];
        int lengthCounter = 0;
        int totalSize = 32 + (imageDetail.getData()).length;
        String lengthStr = Integer.toHexString(totalSize);
        byte[] recordDataLength = new byte[4];
        recordDataLength = DataUtil.getByteArray(lengthStr, 4);
        System.arraycopy(recordDataLength, 0, facialRecordDataArray, lengthCounter, recordDataLength.length);
        lengthCounter += recordDataLength.length;
        String hexNumberOfFeaturePoints = "00";
        byte[] numberOfFeaturePoints = new byte[2];
        numberOfFeaturePoints = DataUtil.getByteArray(hexNumberOfFeaturePoints, 2);
        System.arraycopy(numberOfFeaturePoints, 0, facialRecordDataArray, lengthCounter, numberOfFeaturePoints.length);
        lengthCounter += numberOfFeaturePoints.length;
        String hexGender = "00";
        byte[] gender = new byte[1];
        gender = DataUtil.getByteArray(hexGender, 1);
        System.arraycopy(gender, 0, facialRecordDataArray, lengthCounter, gender.length);
        lengthCounter += gender.length;
        String hexEyeColour = "00";
        byte[] eyeColour = new byte[1];
        eyeColour = DataUtil.getByteArray(hexEyeColour, 1);
        System.arraycopy(eyeColour, 0, facialRecordDataArray, lengthCounter, eyeColour.length);
        lengthCounter += eyeColour.length;
        String hexHairColour = "00";
        byte[] hairColour = new byte[1];
        hairColour = DataUtil.getByteArray(hexHairColour, 1);
        System.arraycopy(hairColour, 0, facialRecordDataArray, lengthCounter, hairColour.length);
        lengthCounter += hairColour.length;
        String hexPropertyMask = "00";
        byte[] propertyMask = new byte[3];
        propertyMask = DataUtil.getByteArray(hexPropertyMask, 3);
        System.arraycopy(propertyMask, 0, facialRecordDataArray, lengthCounter, propertyMask.length);
        lengthCounter += propertyMask.length;
        byte[] expression = new byte[2];
        String hexExpression = "00";
        expression = DataUtil.getByteArray(hexExpression, 2);
        System.arraycopy(expression, 0, facialRecordDataArray, lengthCounter, expression.length);
        lengthCounter += expression.length;
        String hexPosAngle = "00";
        byte[] posAngle = new byte[3];
        posAngle = DataUtil.getByteArray(hexPosAngle, 3);
        System.arraycopy(posAngle, 0, facialRecordDataArray, lengthCounter, posAngle.length);
        lengthCounter += posAngle.length;
        String hexPosAngleUncertainty = "00";
        byte[] posAngleUncertainty = new byte[3];
        posAngleUncertainty = DataUtil.getByteArray(hexPosAngleUncertainty, 3);
        System.arraycopy(posAngleUncertainty, 0, facialRecordDataArray, lengthCounter, posAngleUncertainty.length);
        lengthCounter += posAngleUncertainty.length;
        String hexImageType = "01";
        byte[] imageType = new byte[1];
        imageType = DataUtil.getByteArray(hexImageType, 1);
        System.arraycopy(imageType, 0, facialRecordDataArray, lengthCounter, imageType.length);
        lengthCounter += imageType.length;
        String hexImageDatatype = "01";
        byte[] imageDatatype = new byte[1];
        imageDatatype = DataUtil.getByteArray(hexImageDatatype, 1);
        System.arraycopy(imageDatatype, 0, facialRecordDataArray, lengthCounter, imageDatatype.length);
        lengthCounter += imageDatatype.length;
        String hexWidth = Integer.toHexString(imageDetail.getWidth());
        byte[] width = new byte[2];
        width = DataUtil.getByteArray(hexWidth, 2);
        System.arraycopy(width, 0, facialRecordDataArray, lengthCounter, width.length);
        lengthCounter += width.length;
        String hexHeight = Integer.toHexString(imageDetail.getHeight());
        byte[] height = new byte[2];
        height = DataUtil.getByteArray(hexHeight, 2);
        System.arraycopy(height, 0, facialRecordDataArray, lengthCounter, height.length);
        lengthCounter += height.length;
        String hexImageColourSpace = "00";
        byte[] imageColourSpace = new byte[1];
        imageColourSpace = DataUtil.getByteArray(hexImageColourSpace, 1);
        System.arraycopy(imageColourSpace, 0, facialRecordDataArray, lengthCounter, imageColourSpace.length);
        lengthCounter += imageColourSpace.length;
        String hexSourceType = "06";
        byte[] sourceType = new byte[1];
        sourceType = DataUtil.getByteArray(hexSourceType, 1);
        System.arraycopy(sourceType, 0, facialRecordDataArray, lengthCounter, sourceType.length);
        lengthCounter += sourceType.length;
        String hexDeviceType = "00";
        byte[] deviceType = new byte[2];
        deviceType = DataUtil.getByteArray(hexDeviceType, 2);
        System.arraycopy(deviceType, 0, facialRecordDataArray, lengthCounter, deviceType.length);
        lengthCounter += deviceType.length;
        String hexQuality = "00";
        byte[] quality = new byte[2];
        quality = DataUtil.getByteArray(hexQuality, 2);
        System.arraycopy(quality, 0, facialRecordDataArray, lengthCounter, quality.length);
        lengthCounter += quality.length;
        System.arraycopy(imageDetail.getData(), 0, facialRecordDataArray, lengthCounter, (imageDetail.getData()).length);
        lengthCounter += (imageDetail.getData()).length;
        return facialRecordDataArray;
    }
}

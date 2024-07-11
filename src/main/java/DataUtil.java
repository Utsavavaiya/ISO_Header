// package in.gov.uidai.packetcompressor.utils.headeraddition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.math.BigInteger;

public class DataUtil {
    //    private static Logger logger =  LoggerFactory.getLogger(DataUtil.class);
    private static Logger logger =  LoggerFactory.getLogger(DataUtil.class);
    public static byte[] getByteArray(String value, int byteArraylength) {
        byte[] requiredArray = new byte[byteArraylength];
        byte[] originalArray = (new BigInteger(value, 16)).toByteArray();
        int orgLength = originalArray.length;
        int destPos = byteArraylength - orgLength;
        if (orgLength < byteArraylength) {
            System.arraycopy(originalArray, 0, requiredArray, destPos,
                    orgLength);
        } else if (destPos == 0) {
            requiredArray = originalArray;
        }
        return requiredArray;
    }

    public static byte[] convertImageAndWrite(String dirPath, String fileName) throws IOException {
        File input = new File(String.valueOf(dirPath) + fileName);
        byte[] data = (byte[])null;
        int BUFFER = 2048;
        BufferedInputStream bufferedIpImage = null;
        try {
            if (input.exists()) {
                BufferedImage image = ImageIO.read(input);
                ImageSize.getIns().setHeight(image.getHeight());
                ImageSize.getIns().setWidth(image.getWidth());
                String imageName = "D:/Images/" +
                        fileName.substring(0, fileName.lastIndexOf(".")) +
                        ".jp2";
                File output = new File(imageName);
                ImageIO.write(image, "jpeg2000",
                        output);
                logger.info("Image has been converted successfully");
                FileInputStream objectImageFile = new FileInputStream(imageName);
                bufferedIpImage = new BufferedInputStream(objectImageFile,
                        2048);
                data = new byte[objectImageFile.available()];
                bufferedIpImage.read(data, 0, objectImageFile.available());
            }
        } catch (IOException e) {
            logger.error("Error"+e);
        }finally {
            bufferedIpImage.close();
        }
        return data;
    }

    public static byte[] convertImageWriteToCopyStructure(String dirPath, String fileName, String destination) {
        long startTime = System.currentTimeMillis();
        File input = new File(String.valueOf(dirPath) + fileName);
        byte[] data = (byte[])null;
        FileInputStream objectImageFile = null;
        BufferedInputStream bufferedIpImage = null;
        int BUFFER = 2048;
        try {
            if (input.exists()) {
                BufferedImage image = ImageIO.read(input);
                ImageSize.getIns().setHeight(image.getHeight());
                ImageSize.getIns().setWidth(image.getWidth());
                String imageName = String.valueOf(destination) + "/" +
                        fileName.substring(0, fileName.lastIndexOf(".")) +
                        ".jp2";
                File output = new File(imageName);
                ImageIO.write(image, "jpeg2000",
                        output);
                logger.info("Your image has been converted successfully");
                objectImageFile = new FileInputStream(imageName);
                bufferedIpImage = new BufferedInputStream(objectImageFile,
                        2048);
                data = new byte[objectImageFile.available()];
                bufferedIpImage.read(data, 0, objectImageFile.available());
                bufferedIpImage.close();
                objectImageFile.close();
            }
        } catch (IOException e) {
            logger.error("Error"+e);
        } finally {
            try {
                if (bufferedIpImage != null)
                    bufferedIpImage.close();
                if (objectImageFile != null)
                    objectImageFile.close();
            } catch (IOException e) {
                logger.error("Error"+e);
            }
        }
        long endTime = System.currentTimeMillis() - startTime;
        logger.info("image created " + endTime);
        return data;
    }

    public static byte[] getFingerOrPalm(String hexPalm) {
        byte[] palm = new byte[1];
        palm = getByteArray(hexPalm, 1);
        return palm;
    }

    public static ImageDetail convertImage(String dirPath, String fileName, String destination) {
        File input = new File(String.valueOf(dirPath) + fileName);
        ImageDetail imageDetail = null;
        try {
            if (input.exists()) {
                BufferedImage image = ImageIO.read(input);
                imageDetail = new ImageDetail();
                imageDetail.setWidth(image.getWidth());
                imageDetail.setHeight(image.getHeight());
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                if (fileName.endsWith(".jp2")) {
                    imageDetail.setData(((DataBufferByte)image.getRaster()
                            .getDataBuffer()).getData());
                } else {
                    ImageIO.write(image,
                            "jpeg2000", output);
                    imageDetail.setData(output.toByteArray());
                }
            }
        } catch (IOException e) {
            logger.error("Error"+e);
        }
        return imageDetail;
    }
}

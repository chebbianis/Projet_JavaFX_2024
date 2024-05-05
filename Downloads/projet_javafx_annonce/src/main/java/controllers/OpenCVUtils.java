package controllers;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class OpenCVUtils {

    public static Mat bufferedImageToMat(BufferedImage bufferedImage) {
        int type = CvType.CV_8UC3; // Définit le type de matrice OpenCV
        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), type);
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}

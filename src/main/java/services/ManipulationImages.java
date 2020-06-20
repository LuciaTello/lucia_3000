package services;

import pojo.CroppedType;
import pojo.DirectionEnum;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ManipulationImages {


    public BufferedImage cropLinePixel(int pixel, BufferedImage originalImage, DirectionEnum directionEnum, CroppedType croppedType, int contBalayage) throws IOException {

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        Integer targetWidth = null;
        Integer targetHeight = null;

        switch (directionEnum) {
            case X:
                targetWidth = pixel;
                targetHeight = height;
                break;
            case Y:
                targetWidth = width;
                targetHeight = pixel;
                break;
            default:
                break;
        }

        int xc = 0;
        int yc = 0;

        switch (croppedType) {
            case CENTER:
                // Coordinates of the image's middle
                xc = (width - targetWidth) / 2;
                yc = (height - targetHeight) / 2;
                break;
            case BALAYAGE:
                // Coordinates of balayage de l'image
                xc = contBalayage;
                yc = (height - targetHeight);
                break;
            default:
                break;
        }


        // Crop
        return originalImage.getSubimage(
                xc,
                yc,
                targetWidth, // width
                targetHeight // height
        );
    }

    public BufferedImage append(BufferedImage img1, BufferedImage img2, DirectionEnum directionEnum) {
        BufferedImage buf = null;
        if (img1 != null && img2 != null) {
            int w1 = img1.getWidth(null);
            int h1 = img1.getHeight(null);
            int w2 = img2.getWidth(null);
            int h2 = img2.getHeight(null);
            int hMax;
            int wMax;


            Graphics2D g2;

            switch (directionEnum) {
                case X:
                    hMax = (h1 >= h2) ? h1 : h2;
                    wMax = w1 + w2;

                    buf = new BufferedImage(wMax, hMax, BufferedImage.TYPE_INT_ARGB);
                    g2 = buf.createGraphics();
                    g2.drawImage(img1, 0, 0, null);
                    g2.drawImage(img2, w1, 0, null);
                    break;
                case Y:
                    wMax = (w1 >= w2) ? w1 : w2;
                    hMax = h1 + h2;

                    buf = new BufferedImage(wMax, hMax, BufferedImage.TYPE_INT_ARGB);
                    g2 = buf.createGraphics();
                    g2.drawImage(img1, 0, 0, null);
                    g2.drawImage(img2, 0, h1, null);
                    break;
                default:
                    break;
            }

        }
        return buf;
    }

    /*public BufferedImage append(BufferedImage img1, BufferedImage img2) {
        BufferedImage buf = null;
        if(img1 != null && img2 != null) {
            int w1 = img1.getWidth(null);
            int h1 = img1.getHeight(null);
            int w2 = img2.getWidth(null);
            int h2 = img2.getHeight(null);
            int hMax = 0;
            int wMax = 0;

            hMax = (h1 >= h2) ? h1 : h2;
            wMax = w1+w2;
            buf = new BufferedImage(wMax, hMax, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buf.createGraphics();
            g2.drawImage(img1, 0, 0, null);
            g2.drawImage(img2, w1, 0, null);
        }
        return buf;
    }*/



    /*    public BufferedImage cropLineCentre(double amount, BufferedImage originalImage) throws IOException {
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        int targetWidth = (int) (width * amount);
        int targetHeight = (int) (height * amount);
        // Coordinates of the image's middle
        int xc = (width - targetWidth) / 2;
        int yc = (height - targetHeight) / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc,
                yc,
                targetWidth, // width
                targetHeight // height
        );
        return croppedImage;
    }*/

}

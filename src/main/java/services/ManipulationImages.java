package services;

import pojo.CroppedType;
import pojo.DirectionEnum;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ManipulationImages {

    private Double PROPORTION = 0.5624;

    public BufferedImage cropOneLinePixel(BufferedImage originalImage, int contBalayage) throws IOException {

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        Integer targetHeight = 1;
        Integer targetWidth = width;

        int xc = 0;
        int yc = contBalayage;

        // Crop
        return originalImage.getSubimage(
                xc,
                yc,
                targetWidth, // width
                targetHeight // height
        );
    }

    public BufferedImage appendCroppedOneLinePixel() {
        BufferedImage resultat = null;

        return resultat;
    }

    public BufferedImage cropLinePixelVideoToImage(int pixel, BufferedImage originalImage, DirectionEnum directionEnum, CroppedType croppedType, int contBalayage) throws IOException {

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

    public BufferedImage appendFullHeight(BufferedImage initialImage) {
        BufferedImage resultat = null;
        Graphics2D g2;

        int widthOriginal = initialImage.getWidth();
        int height = (int) Math.round(widthOriginal * PROPORTION);

        int w = initialImage.getWidth();
        int h1 = initialImage.getHeight();

        int hMax;
        int wMax;

        for (int i = 0; i < height; i++) {

            if (0 == i) {
                resultat = initialImage;
                continue;
            }

            int h2 = resultat.getHeight();

            hMax = h1 + h2;

            resultat = new BufferedImage(w, hMax, BufferedImage.TYPE_INT_ARGB);
            g2 = resultat.createGraphics();
            g2.drawImage(initialImage, 0, 0, null);
            g2.drawImage(resultat, 0, h1, null);

        }

        return resultat;
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

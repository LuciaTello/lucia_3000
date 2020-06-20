package app;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import pojo.CroppedType;
import pojo.DirectionEnum;
import services.ManipulationImages;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    static ManipulationImages manipulationImages = new ManipulationImages();
    static Java2DFrameConverter converter = new Java2DFrameConverter();

    static int NOMBRE_PIXELS = 1;
    static int SPEED = 1;
    static DirectionEnum DIRECTION_VIDEO = DirectionEnum.Y;
    static CroppedType CROPPED_TYPE = CroppedType.CENTER;
    static String URL_VIDEO = "C:/Users/lucia/Documents/Alexis/videoBal3min.mp4";
    static String FORMAT_SORTIE = "png";
    static String URL_VIDEO_SORTIE = "C:/Users/lucia/Documents/Alexis/resultats/img" + System.currentTimeMillis() + "." + FORMAT_SORTIE;

    public static void main(String[] args) throws IOException, Exception {

        try {
            if (CroppedType.BALAYAGE.equals(CROPPED_TYPE)) {
                DIRECTION_VIDEO = DirectionEnum.X;
            }

            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(URL_VIDEO);
            fFmpegFrameGrabber.start();

            BufferedImage bufferedImageFinal = null;

            int contBalayage = 0;

            int contFrame = 0;


            for (int i = 0; i < fFmpegFrameGrabber.getLengthInVideoFrames(); i = i + SPEED) {

                BufferedImage imageFrame = converter.convert(fFmpegFrameGrabber.grabImage());

                if (imageFrame != null && (imageFrame.getWidth() > contBalayage)) {

                    BufferedImage imageCropped = manipulationImages.cropLinePixel(NOMBRE_PIXELS, imageFrame, DIRECTION_VIDEO, CROPPED_TYPE, contBalayage);

                    if (CroppedType.BALAYAGE.equals(CROPPED_TYPE)) {
                        contBalayage++;
                    }

                    if (0 == i) {
                        bufferedImageFinal = imageCropped;
                        continue;
                    }

                    bufferedImageFinal = manipulationImages.append(bufferedImageFinal, imageCropped, DIRECTION_VIDEO);
                } else {
                    break;
                }

            }

            ImageIO.write(bufferedImageFinal, FORMAT_SORTIE, new File(URL_VIDEO_SORTIE));

            fFmpegFrameGrabber.stop();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

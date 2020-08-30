package app;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import pojo.ActionEnum;
import pojo.CroppedType;
import pojo.DirectionEnum;
import services.ManipulationImages;
import services.ManipulationVideo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static ManipulationImages manipulationImages = new ManipulationImages();
    static ManipulationVideo manipulationVideo = new ManipulationVideo();
    static Java2DFrameConverter converter = new Java2DFrameConverter();


    static ActionEnum ACTION_TYPE = ActionEnum.IMAGE_TO_VIDEO;
    static String FORMAT_SORTIE_IMAGE = "png";

    //------IMAGE TO VIDEO
    static String NOM_IMAGE = "2test2";
    static String URL_IMAGE = "C:/Users/lucia/Documents/Alexis/image/" + NOM_IMAGE + ".jpg";


    static String DOSSIER_IMAGES_SORTIE = "C:/Users/lucia/Documents/Alexis/resultats/imageToVideo/" + NOM_IMAGE;
    static String FORMAT_SORTIE_VIDEO = "mp4";
    static String URL_VIDEO_SORTIE = DOSSIER_IMAGES_SORTIE + "/00" + NOM_IMAGE + "." + FORMAT_SORTIE_VIDEO;


    //------VIDEO TO IMAGE
    static int NOMBRE_PIXELS = 1;
    static int SPEED = 1;
    static DirectionEnum DIRECTION_VIDEO = DirectionEnum.Y;
    static CroppedType CROPPED_TYPE = CroppedType.CENTER;
    static String URL_VIDEO = "C:/Users/lucia/Documents/Alexis/videoBal3min.mp4";
    static String URL_IMAGE_SORTIE = "C:/Users/lucia/Documents/Alexis/resultats/img" + System.currentTimeMillis() + "." + FORMAT_SORTIE_IMAGE;

    public static void main(String[] args) throws IOException {

        switch (ACTION_TYPE) {
            case IMAGE_TO_VIDEO:
                getImageToVideo();
                break;
            case VIDEO_TO_IMAGE:
                getVideoToImage();
                break;
        }
    }


    private static void getVideoToImage() throws IOException {
        try {
            if (CroppedType.BALAYAGE.equals(CROPPED_TYPE)) {
                DIRECTION_VIDEO = DirectionEnum.X;
            }

            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(URL_VIDEO);
            fFmpegFrameGrabber.start();

            BufferedImage bufferedImageFinal = null;

            int contBalayage = 0;

            for (int i = 0; i < fFmpegFrameGrabber.getLengthInVideoFrames(); i = i + SPEED) {

                BufferedImage imageFrame = converter.convert(fFmpegFrameGrabber.grabImage());

                if (imageFrame != null && (imageFrame.getWidth() > contBalayage)) {

                    BufferedImage imageCropped = manipulationImages.cropLinePixelVideoToImage(NOMBRE_PIXELS, imageFrame, DIRECTION_VIDEO, CROPPED_TYPE, contBalayage);

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

            ImageIO.write(bufferedImageFinal, FORMAT_SORTIE_IMAGE, new File(URL_IMAGE_SORTIE));

            fFmpegFrameGrabber.stop();
        } catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
    }


    private static void getImageToVideo() throws IOException {

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(URL_IMAGE));
        } catch (IOException e) {
            System.out.println(e + e.getMessage());
        }

        BufferedImage bufferedImageFinal;

        new File(DOSSIER_IMAGES_SORTIE).mkdir();

        ArrayList<BufferedImage> bufferedImagesList = new ArrayList<BufferedImage>();

        for (int i = 1; i < bufferedImage.getHeight(); i++) {
            BufferedImage croppedImage = manipulationImages.cropOneLinePixel(bufferedImage, i);

            bufferedImageFinal = manipulationImages.appendFullHeight(croppedImage);

            bufferedImagesList.add(bufferedImageFinal);

            ImageIO.write(bufferedImageFinal, FORMAT_SORTIE_IMAGE, new File(DOSSIER_IMAGES_SORTIE + "/" + "img" + i + "." + FORMAT_SORTIE_IMAGE));

            System.out.println("*** " + i + " / " + bufferedImage.getHeight() + " ***");
        }

        manipulationVideo.getVideoFromListBufferedImages(URL_VIDEO_SORTIE, bufferedImagesList);
    }
}

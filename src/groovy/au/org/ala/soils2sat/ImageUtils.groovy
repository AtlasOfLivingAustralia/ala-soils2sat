package au.org.ala.soils2sat

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageUtils {

    static int IMAGE_BUF_INIT_SIZE = 2 * 1024 * 1024

    public static byte[] imageToBytes(BufferedImage image, String formatName = "JPG") {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(IMAGE_BUF_INIT_SIZE)
            ImageIO.write(image, formatName, baos)
            return baos.toByteArray()
        } finally {
        }
    }

}

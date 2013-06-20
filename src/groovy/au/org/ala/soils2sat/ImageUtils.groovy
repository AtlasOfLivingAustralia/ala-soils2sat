package au.org.ala.soils2sat

import org.imgscalr.Scalr

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

    public static BufferedImage bytesToImage(byte[] bytes) {
        ByteArrayInputStream bais = null
        try {
            bais = new ByteArrayInputStream(bytes)
            return ImageIO.read(new BufferedInputStream(bais))

        } finally {
            if (bais) {
                bais.close()
            }
        }
    }

    public static BufferedImage scaleWidth(BufferedImage src, int destWidth) {
        return Scalr.resize(src, Scalr.Method.SPEED, destWidth, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
    }

    public static BufferedImage scale(BufferedImage src, int destWidth, int destHeight) {
        return Scalr.resize(src, Scalr.Method.SPEED, destWidth, destHeight, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
    }

}

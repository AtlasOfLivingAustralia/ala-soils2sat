/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.soils2sat

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
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

    public static BufferedImage createImageFromPDFBytes(byte[] bytes, int pageNumber = 0, int resolution = 150) {
        InputStream is = new ByteArrayInputStream(bytes)
        PDDocument pdf = PDDocument.load(is)
        if (pdf) {
            def page = (PDPage) pdf.documentCatalog.allPages.first()
            if (page) {
                try {
                    def image = page.convertToImage(BufferedImage.TYPE_INT_RGB, resolution)
                    return image
                } catch (Exception ex) {
                    ex.printStackTrace()
                    throw ex
                }
            }
        }

        return null
    }

}

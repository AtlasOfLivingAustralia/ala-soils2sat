package au.org.ala.soils2sat

import java.awt.Color
import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 3/06/13
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
class SoilColorUtils {

    private static Map<String, SoilColor> _colorMap = null

    public static synchronized Color parseMunsell(String code) {
        if (!_colorMap) {
            initMunsellColorMap()
        }

        return _colorMap[code]?.color
    }

    private static void initMunsellColorMap() {
        def url = SoilColorUtils.class.getResource("/resources/munsell_data.txt")

        url?.withInputStream {
            def reader = new BufferedReader(new InputStreamReader(it))
            _colorMap = [:]
            String line
            boolean skipFirst = true
            while (line = reader.readLine()) {
                if (!skipFirst) {
                    def bits = line.split(",")
                    def hue = bits[1]
                    def value = bits[2]
                    def chroma = bits[3]
                    def r = Integer.parseInt(bits[16])
                    def g = Integer.parseInt(bits[17])
                    def b = Integer.parseInt(bits[18])
                    def key = "${hue}${value}${chroma}"
                    def color = new Color(r,g,b)
                    def entry = new SoilColor(hue: hue, value: value, chroma: chroma, color: color)
                    _colorMap[key] = entry
                } else {
                    skipFirst = false
                }

            }
        }
    }

    // sRGB XYZ - RGB [M]^-1
    private static MInv = [
        [3.2404542, -1.5371385, -0.4985314],
        [-0.9692660,  1.8760108,  0.0415560],
        [0.0556434, -0.2040259,  1.0572252]
    ]

    def static xyY2RGB(double x, double y, double Y) {

        double X,YY, Z, r,g,b
        (X,YY,Z) = xyY2XYZ(x, y, Y)
        (r,g,b) = XYZ2RGB(X, YY, Z)
        return [r, g, b]
    }

    def static xyY2XYZ(double x, double y, double Y) {
        if (y == 0) {
            return [0,0,0]
        }

        def X = (x * Y) / y
        def Z = ((1 - x - y) * Y) / y
        return [X, Y, Z]
    }

    def static XYZ2RGB(double X, double Y, double Z) {
        def r = (MInv[0][0] * X) + (MInv[0][1] * Y) + (MInv[0][2] * Z)
        def g = (MInv[1][0] * X) + (MInv[1][1] * Y) + (MInv[1][2] ** Z)
        def b = (MInv[2][0] * X) + (MInv[2][1] * Y) + (MInv[2][2] ** Z)
        def tuple = [r,g,b]

        // now to compand(???) to sRGB
        def RGB = []
        for (double v : tuple) {
            double V = v
            if (v <= 0.0031308) {
                V = v * 12.92
            } else {
                V = ((1.055 * v) ** (1/2.4)) - 0.055
            }
            RGB << V
        }

        return RGB
    }

}

class SoilColor {
    String hue
    String value
    String chroma
    String description
    Color color
}

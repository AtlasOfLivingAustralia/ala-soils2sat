package au.org.ala.soils2sat

import ala.soils2sat.DrawingUtils

import java.awt.BasicStroke
import java.awt.Color
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 29/05/13
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
class VisualisationUtils {

    public static Map<String, Color> getColorMapForIntersectProperty(String property) {
        switch (property) {
            case "substrate":
                return [
                    "Bare": new Color(218, 44, 67),
                    "Litter": new Color(107, 142, 35),
                    "Rock": Color.gray,
                    "Outcrop": Color.lightGray,
                    "Gravel": Color.yellow,
                    "CWD": Color.black,
                    "Crypto": Color.green
                ]
            case "growthForm":
                def big = [Color.decode("#006600"), Color.decode("#67CB33"), Color.decode("#CDFFCC"), Color.decode("#9999CD"), Color.decode("#66669A")]
                def medium = [Color.decode("#B5E2FF"), Color.decode("#3E99DF"), Color.decode("#0066CB"), Color.decode("#0F1E61"), Color.decode("#FECCFF")]
                def small = [Color.decode("#9A0000"), Color.decode("#FF794A"), Color.decode("#FFCC66"), Color.decode("#FFFF9A"), Color.decode("#CC9900")]
                def bigIndex = 0, mediumIndex = 0, smallIndex = 0
                return [
                    "Chenopod": medium[mediumIndex++],
                    "Epiphyte": small[smallIndex++],
                    "Fern": small[smallIndex++],
                    "Forb" :small[smallIndex++],
                    "Hummock grass" : medium[mediumIndex++],
                    "Sedge" : small[smallIndex++],
                    "Shrub" : medium[mediumIndex++],
                    "Shrub Mallee" : big[bigIndex++],
                    "Tree Mallee" : big[bigIndex++],
                    "Tree/Palm" : big[bigIndex++],
                    "Tussock grass" : small[smallIndex++],
                    "Vine": medium[mediumIndex++]
                ]

            default:
                return [:]
        }
    }

    public static Map<String, Double> getPointSizeMultiplierMapForIntersectProperty(String property) {
        def big = 3.0
        def medium = 2.0
        def small = 1.0

        switch (property) {
            case "growthForm":
                return [
                        "Chenopod": medium,
                        "Epiphyte": small,
                        "Fern": small,
                        "Forb" :small,
                        "Hummock grass" : medium,
                        "Sedge" : small,
                        "Shrub" : medium,
                        "Shrub Mallee" :big,
                        "Tree Mallee" : big,
                        "Tree/Palm" : big,
                        "Tussock grass" :small,
                        "Vine": medium
                ]
            default:
                return [:]
        }

    }

    public static getLitmusColors() {
        def litmusColors = [
            [pH:0, color:'#F61800'],
            [pH:1, color:'#F76502'],
            [pH:2, color:'#FCCC00'],
            [pH:3, color:'#FFFF02'],
            [pH:4, color:'#CCFF33'],
            [pH:5, color:'#56FF00'],
            [pH:6, color:'#5AB700'],
            [pH:7, color:'#1D6632'],
            [pH:8, color:'#2E9965'],
            [pH:9, color:'#36B7BE'],
            [pH:10, color:'#3398FF'],
            [pH:11, color:'#0066FF'],
            [pH:12, color:'#0000FF'],
            [pH:13, color:'#000099'],
            [pH:14, color:'#663266'],
        ]
        return litmusColors
    }

    public static getStructualSummaryColors() {

        def rootColors = ['#4E6228','#652524', '#4E81BD']
        def colors = []
        rootColors.each { rootColor ->
            colors << rootColor
            def root = Color.decode(rootColor)
            def g1 = fadeColor(root, 0.3)
            def g2 = fadeColor(root, 0.6)
            colors << "#" + Integer.toHexString(g1.getRGB()).substring(2);
            colors << "#" + Integer.toHexString(g2.getRGB()).substring(2);
        }

        return colors
    }

    public static Color fadeColor(Color c, double percent) {
        // def grad = 1 / percent;
        return new Color((int) Math.min(((255 - c.red) * percent) + c.red, 255), (int) Math.min(((255 - c.green) * percent) + c.green, 255), (int) Math.min(((255 - c.blue) * percent) + c.blue, 255))
    }

    public static TransectImageDescriptor drawPlotTransects(int gridSize, int gutterSize, int legendWidth) {


        BufferedImage image = new BufferedImage(gridSize + gutterSize * 3 + legendWidth, gridSize + gutterSize * 2, BufferedImage.TYPE_INT_RGB)
        def g = image.graphics as Graphics2D

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        Map<String, TransectDefinition> transects = [:]

        float[] dash1 = [ 10.0f ];
        BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 5.0f);

        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textLeadIn = 4;
        int textPadding = 4;
        int hgt = metrics.getHeight() + textPadding;

        def computeEWLabelRectangle = { int x, int y, String label ->
            int txtWidth = metrics.stringWidth(label) + textPadding;
            if ( x > gridSize) {
                return new Rectangle(x + textLeadIn, y - (int) (hgt / 2), txtWidth, hgt)
            } else {
                return new Rectangle(x - ( textLeadIn * 2 + txtWidth), y - (int) (hgt / 2), txtWidth, hgt)
            }
        }

        def computeNSLabelRectangle = { int x, int y, String label ->
            int adv = metrics.stringWidth(label) + textPadding;
            if (y > gridSize) {
                return new Rectangle(x - (int) (adv / 2), y + textLeadIn, adv, hgt)
            } else {
                return new Rectangle(x - (int) (adv / 2), y - (textLeadIn * 2 + hgt), adv, hgt)
            }
        }

        def drawRectangle = { Rectangle rect ->
            g.setStroke(new BasicStroke())
            g.setColor(Color.white)
            g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height)
            g.setColor(Color.black)
            g.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height)
        }

        try {
            def vTransectSpacing = gridSize / 5
            def hTransectSpacing = gridSize / 5
            def offsetX = (int) (vTransectSpacing / 2)
            def offsetY = (int) (hTransectSpacing / 2)

            def transectList = []
            for (int i = 1; i <= 5; ++i) {
                def EWy = (int) (( image.height - (offsetY + 1 + gutterSize)) - ((i-1) * vTransectSpacing))
                def NSx = (int) ((((i-1) * hTransectSpacing) + offsetX + gutterSize))
                transectList << new TransectDefinition( name: "W${i}-E${i}", x1: gutterSize, x2: gridSize + gutterSize, y1: EWy, y2: EWy, direction: 1, dx: gridSize / 100, dy: 0 )
                transectList << new TransectDefinition(name: "E${i}-W${i}", x1: gutterSize, x2: gridSize + gutterSize, y1: EWy, y2: EWy, direction: -1, dx: gridSize / 100, dy: 0 )
                transectList << new TransectDefinition(name: "N${i}-S${i}", x1: NSx, x2: NSx, y1: gutterSize, y2: gutterSize + gridSize , direction: 1, dx: 0, dy: gridSize / 100 )
                transectList << new TransectDefinition(name:"S${i}-N${i}", x1: NSx, x2: NSx, y1: gutterSize, y2: gutterSize + gridSize, direction: -1, dx: 0, dy: gridSize / 100 )
            }

            transects = transectList.collectEntries {
                [it.name, it]
            }

            // Clear the image buffer to white
            g.setColor(Color.white)
            g.fillRect(0,0,image.width,image.height);

            transects.values().each { transect ->

                if (transect.direction ==  1) {
                    g.setColor(Color.black)
                    g.setStroke(dashed);
                    g.drawLine(transect.x1, transect.y1, transect.x2, transect.y2)

                    def matcher = ( transect.name =~ /^([EWNS]\d).([EWNS]\d)/)

                    if (matcher.matches()) {

                        def startLabel = matcher.group(1)
                        def endLabel = matcher.group(2)

                        if (startLabel.startsWith("W")) {
                            def rect = computeEWLabelRectangle(transect.x1, transect.y1, startLabel)
                            drawRectangle(rect)
                            DrawingUtils.drawString(g, g.font, startLabel, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                            rect = computeEWLabelRectangle(transect.x2, transect.y2, endLabel)
                            drawRectangle(rect)
                            DrawingUtils.drawString(g, g.font, endLabel, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                        } else {
                            def rect = computeNSLabelRectangle(transect.x1, transect.y1, startLabel)
                            drawRectangle(rect)
                            DrawingUtils.drawString(g, g.font, startLabel, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                            rect = computeNSLabelRectangle(transect.x2, transect.y2, endLabel)
                            drawRectangle(rect)
                            DrawingUtils.drawString(g, g.font, endLabel, rect, DrawingUtils.TEXT_ALIGN_CENTER)
                        }
                    }
                }
            }
        } finally {
            if (g) {
                g.dispose()
            }
        }

        return new TransectImageDescriptor(gridSize: gridSize, gutterSize: gutterSize, legendWidth: legendWidth, transects: transects, image: image)
    }

}

public class TransectImageDescriptor {
    int gridSize
    int gutterSize
    int legendWidth
    Map<String, TransectDefinition> transects
    BufferedImage image

}

public class TransectDefinition {
    String name
    int x1
    int y1
    int x2
    int y2
    int direction
    float dx
    float dy
}

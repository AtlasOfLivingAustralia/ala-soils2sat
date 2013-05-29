package au.org.ala.soils2sat

import java.awt.Color

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 29/05/13
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
class VisualisationUtils {

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

}

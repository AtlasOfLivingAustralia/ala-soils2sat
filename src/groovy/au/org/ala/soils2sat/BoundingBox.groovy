package au.org.ala.soils2sat

class BoundingBox {

    double top
    public double left
    double bottom
    double right

    def contains(double x, double y) {
        if (x >= left && x <= right) {
            if (y >= bottom && y<= top) {
                return true
            }
        }
        return false
    }
}

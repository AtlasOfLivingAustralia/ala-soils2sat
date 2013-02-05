package au.org.ala.soils2sat

class BoundingBox {

    double top
    double left
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

    public String toString() {
        return "${top}\n${left}\n${bottom}\n${right}"
    }
}

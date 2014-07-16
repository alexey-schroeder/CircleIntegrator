package sample.point;

/**
 * Created with doubleelliJ IDEA.
 * User: schroedera85
 * Date: 27.08.13
 * Time: 09:24
 * To change this template use File | Settings | File Templates.
 */
public class DoublePoint extends Point implements Comparable<DoublePoint> {
    private double x, y, z;
    private double value;


    public DoublePoint() {
    }

    public DoublePoint(double x, double y, double z, double value) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = value;
    }

    public DoublePoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(DoublePoint o) {
        if (value > o.value) {
            return 1;
        }
        if (value < o.value) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DoublePoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", value=" + value +
                '}';
    }

    public DoublePoint(Point point) {
        x = point.getX().doubleValue();
        y = point.getY().doubleValue();
        z = point.getZ().doubleValue();
        value = point.getValue().doubleValue();
    }

    @Override
    public DoublePoint reflectByZeroPoint() {
        double newX = x == 0.0? x : -x;
        double newY = x == 0.0? y : -y;
        double newZ = x == 0.0? z : -z;
        return new DoublePoint(newX, newY, newZ);
    }

    public boolean isEquals(DoublePoint otherPoint, double maxDelta) {
        return Math.abs(x - otherPoint.getX()) < maxDelta &&
                Math.abs(y - otherPoint.getY()) < maxDelta &&
                Math.abs(z - otherPoint.getZ()) < maxDelta;
    }

    @Override
    public boolean equals(Object obj) {
        double maxDelta = 0.01;
        if (this == obj){
            return true;
        }

        if (obj == null){
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        DoublePoint otherPoint = (DoublePoint) obj;
       return isEquals(otherPoint, maxDelta);

    }

    @Override
    public int hashCode() {
        double tempResult = 31  +  x;
        tempResult = 31 * tempResult + y;
        tempResult = 31 * tempResult + z;
        return (int) tempResult;
    }
}

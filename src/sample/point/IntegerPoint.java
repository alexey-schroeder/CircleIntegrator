package sample.point;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 27.08.13
 * Time: 09:24
 * To change this template use File | Settings | File Templates.
 */
public class IntegerPoint extends Point implements Comparable<IntegerPoint> {
    private int x, y, z;
    private double value;

    public IntegerPoint() {
    }

    public IntegerPoint(int x, int y, int z, double value) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = value;
    }

    public IntegerPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IntegerPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(IntegerPoint o) {
        if (value > o.value) {
            return 1;
        }
        if (value < o.value) {
            return -1;
        }
        return 0;
    }

    public String toStringAsAllCoordinate() {
        return "" + x + y + z;
    }

    public String toStringAsXandYCoordinate() {
        return "" + x + y;
    }

    @Override
    public String toString() {
        return "IntegerPoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", value=" + value +
                '}';
    }

    @Override
    public IntegerPoint reflectByZeroPoint() {
        int newX = x == 0.0? x : -x;
        int newY = x == 0.0? y : -y;
        int newZ = x == 0.0? z : -z;
        return new IntegerPoint(newX, newY, newZ);
    }
}

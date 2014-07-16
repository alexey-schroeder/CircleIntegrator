package sample.point;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 27.08.13
 * Time: 09:24
 * To change this template use File | Settings | File Templates.
 */
public class ShortPoint extends Point implements Comparable<ShortPoint> {
    private short x, y, z;
    private short value;

    public ShortPoint() {
    }

    public ShortPoint(short x, short y, short z, short value) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = value;
    }

    public ShortPoint(short x, short y, short z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ShortPoint(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public Short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public Short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public Short getZ() {
        return z;
    }

    public void setZ(short z) {
        this.z = z;
    }

    public Short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public int compareTo(ShortPoint o) {
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
        return "ShortPoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", value=" + value +
                '}';
    }

    @Override
    public ShortPoint reflectByZeroPoint() {
        int newX = x == 0.0? x : -x;
        int newY = x == 0.0? y : -y;
        int newZ = x == 0.0? z : -z;
        return new ShortPoint((short)newX, (short)newY, (short)newZ);
    }
}

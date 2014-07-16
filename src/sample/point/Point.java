package sample.point;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 16.10.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class Point {

    public abstract <T extends Number> T getX();
    public abstract <T extends Number> T getY();
    public abstract <T extends Number> T getZ();
    public abstract <T extends Number> T getValue();
    public double distanceToPoint(Point otherPoint){
        double x = getX().doubleValue();
        double y = getY().doubleValue();
        double z = getZ().doubleValue();
        double dx = x - otherPoint.getX().doubleValue();
        double dy = y - otherPoint.getY().doubleValue();
        double dz = z - otherPoint.getZ().doubleValue();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public  double distanceToZeroPoint(){
        double x = getX().doubleValue();
        double y = getY().doubleValue();
        double z = getZ().doubleValue();
        return Math.sqrt(x * x + y * y + z * z);
    }
    public abstract Point reflectByZeroPoint();
    public boolean equals(Object obj) {
        Point otherPoint = (Point)obj;
        return getX().equals(otherPoint.getX()) && getY().equals(otherPoint.getY()) && getZ().equals(otherPoint.getZ());
    }
}

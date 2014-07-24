package sample.interpolation;

import sample.point.DoublePoint;

/**
 * Created by Alex on 20.07.2014.
 */
public class InterpolationBox {
    private DoublePoint theoreticalCenter;
    private DoublePoint pointWithMaxIntensity;
    private int boxSize = 4;


    public InterpolationBox (double x, double y){
        theoreticalCenter = new DoublePoint(x, y);
    }
    public DoublePoint getTheoreticalCenter() {
        return theoreticalCenter;
    }

    public void setTheoreticalCenter(DoublePoint theoreticalCenter) {
        this.theoreticalCenter = theoreticalCenter;
    }

    public DoublePoint getPointWithMaxIntensity() {
        return pointWithMaxIntensity;
    }

    public void setPointWithMaxIntensity(DoublePoint pointWithMaxIntensity) {
        this.pointWithMaxIntensity = pointWithMaxIntensity;
    }

    public int getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(int boxSize) {
        this.boxSize = boxSize;
    }
}

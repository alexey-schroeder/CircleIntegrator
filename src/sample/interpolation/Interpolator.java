package sample.interpolation;

import sample.Matrix;
import sample.point.DoublePoint;

import java.util.LinkedList;

/**
 * Created by Alex on 20.07.2014.
 */
public class Interpolator {
    private DoublePoint circleCenterFromUser;
    private double circleRadiusFromUser;
    private DoublePoint interpolatedCenter;
    private int interpolateBoxesNumber = 100;
    private Matrix matrix;
    private LinkedList<InterpolationBox> interpolationBoxes;

    public void interpolate() {
        interpolationBoxes = new LinkedList<>();
        int boxNumberInSector = (int) (90.0 / 360 * interpolateBoxesNumber);
        double angleStep = 90 / boxNumberInSector;
        double circleCenterX = circleCenterFromUser.getX();
        double circleCenterY = circleCenterFromUser.getY();
        for (double angle = 0; angle < 90; angle = angle + angleStep) {
            double x = circleRadiusFromUser * Math.cos(angle);
            double y = circleRadiusFromUser * Math.sin(angle);
            InterpolationBox interpolationBoxTopRight = new InterpolationBox(circleCenterX + x, circleCenterY - y);
            InterpolationBox interpolationBoxBottomRight = new InterpolationBox(circleCenterX + x, circleCenterY + y);
            InterpolationBox interpolationBoxTopLeft = new InterpolationBox(circleCenterX - x, circleCenterY - y);
            InterpolationBox interpolationBoxBottomLeft = new InterpolationBox(circleCenterX - x, circleCenterY + y);
            interpolationBoxes.add(interpolationBoxTopRight);
            interpolationBoxes.add(interpolationBoxBottomRight);
            interpolationBoxes.add(interpolationBoxTopLeft);
            interpolationBoxes.add(interpolationBoxBottomLeft);
        }
        findMaxIntensityPointInInterpolateBox(interpolationBoxes);
        interpolateCenter(interpolationBoxes);
    }

    private void interpolateCenter(LinkedList<InterpolationBox> interpolationBoxes) {
        double summeX = 0;
        double summeY = 0;
        for (InterpolationBox interpolationBox : interpolationBoxes) {
            DoublePoint pointWithMaxIntensity = interpolationBox.getPointWithMaxIntensity();
            summeX = summeX + pointWithMaxIntensity.getX();
            summeY = summeY + pointWithMaxIntensity.getY();
        }
        double x = summeX / interpolationBoxes.size();
        double y = summeY / interpolationBoxes.size();
        interpolatedCenter = new DoublePoint(x, y);
    }

    private void findMaxIntensityPointInInterpolateBox(LinkedList<InterpolationBox> interpolationBoxes) {
        for (InterpolationBox interpolationBox : interpolationBoxes) {
            findMaxIntensityPointInInterpolateBox(interpolationBox);
        }
    }

    private void findMaxIntensityPointInInterpolateBox(InterpolationBox interpolationBox) {
        DoublePoint boxCenter = interpolationBox.getTheoreticalCenter();
        int boxCenterX = boxCenter.getX().intValue();
        int boxCenterY = boxCenter.getY().intValue();
        int boxSize = interpolationBox.getBoxSize();

        short maxIntensityValue = Short.MIN_VALUE;
        int maxIntensityX = 0;
        int maxIntensityY = 0;

        int deviation = boxSize / 2;
        for (int x = -deviation; x <= deviation; x++) {
            for (int y = -deviation; y <= deviation; y++) {
                int tempX = boxCenterX + deviation;
                int tempY = boxCenterY + deviation;
                short value = matrix.get(tempX, tempY);
                if (value > maxIntensityValue) {
                    maxIntensityValue = value;
                    maxIntensityX = tempX;
                    maxIntensityY = tempY;
                }
            }
        }
        interpolationBox.setPointWithMaxIntensity(new DoublePoint(maxIntensityX, maxIntensityY));
    }

    public DoublePoint getCircleCenterFromUser() {
        return circleCenterFromUser;
    }

    public void setCircleCenterFromUser(DoublePoint circleCenterFromUser) {
        this.circleCenterFromUser = circleCenterFromUser;
    }

    public DoublePoint getInterpolatedCenter() {
        return interpolatedCenter;
    }

    public void setInterpolatedCenter(DoublePoint interpolatedCenter) {
        this.interpolatedCenter = interpolatedCenter;
    }

    public int getInterpolateBoxesNumber() {
        return interpolateBoxesNumber;
    }

    public void setInterpolateBoxesNumber(int interpolateBoxesNumber) {
        this.interpolateBoxesNumber = interpolateBoxesNumber;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public double getCircleRadiusFromUser() {
        return circleRadiusFromUser;
    }

    public void setCircleRadiusFromUser(double circleRadiusFromUser) {
        this.circleRadiusFromUser = circleRadiusFromUser;
    }

    public LinkedList<InterpolationBox> getInterpolationBoxes() {
        return interpolationBoxes;
    }
}

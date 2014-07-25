package sample;


import org.apache.commons.math3.util.FastMath;
import sample.point.DoublePoint;
import sample.point.IntegerPoint;
import sample.point.Point;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 27.08.13
 * Time: 08:27
 * To change this template use File | Settings | File Templates.
 */
public class Statistic {
    public static int[] calcHistogram(short[] data, double min, double max, int numBins) {
        final int[] result = new int[numBins];
        final double binSize = (max - min) / numBins;

        for (double d : data) {
            if (d >= min && d < max) {
                int bin = (int) ((d - min) / binSize);
                result[bin] += 1;
            }
        }

        return result;
    }

    public static int[] calcHistogram(short[] data, int numBins) {
        double min = data[0];
        double max = data[0];
        for (short d : data) {
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
        }
        return calcHistogram(data, min, max, numBins);
    }

    public static int[] calcHistogram(int[] data, int numBins) {
        int min = data[0];
        int max = data[0];
        for (int d : data) {
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
        }
        return calcHistogram(data, min, max, numBins);
    }

    public static int[] calcHistogram(int[] data, int min, int max, int numBins) {
        final int[] result = new int[numBins];
        final double binSize = (max - min) / numBins;

        for (int d : data) {
            if (d >= min && d < max) {
                int bin = (int) ((d - min) / binSize);
                result[bin] += 1;
            }
        }

        return result;
    }

    public static double calculateAngleBetweenVectors(Point a, Point b) {
        double lengthA = a.distanceToZeroPoint();
        double lengthB = b.distanceToZeroPoint();
        double dot = dotProduct(a, b);
        return FastMath.acos(dot / (lengthA * lengthB));
    }

    public static double dotProduct(Point a, Point b) {
        double dot = a.getX().doubleValue() * b.getX().doubleValue() + a.getY().doubleValue() * b.getY().doubleValue() + a.getZ().doubleValue() * b.getZ().doubleValue();
        return dot;
    }

    public static DoublePoint crossProduct(Point a, Point b) {
        double x = a.getY().doubleValue() * b.getZ().doubleValue() - a.getZ().doubleValue() * b.getY().doubleValue();
        double y = a.getZ().doubleValue() * b.getX().doubleValue() - a.getX().doubleValue() * b.getZ().doubleValue();
        double z = a.getX().doubleValue() * b.getY().doubleValue() - a.getY().doubleValue() * b.getX().doubleValue();
        return new DoublePoint(x, y, z);
    }

    public static short getMaxValue(Matrix matrix) {
        return getMaxValue(matrix.getData());
    }

    public static short getMaxValue(short[] matrixData) {
        short maxValue = matrixData[0];
        for (int x = 1; x < matrixData.length; x++) {
            short actuellValue = matrixData[x];
            if (maxValue < actuellValue) {
                maxValue = actuellValue;
            }
        }
        return maxValue;
    }

    public static int getMaxValue(int[] data) {
        int maxValue = data[0];
        for (int x = 1; x < data.length; x++) {
            int actuellValue = data[x];
            if (maxValue < actuellValue) {
                maxValue = actuellValue;
            }
        }
        return maxValue;
    }

    public static int getMinValue(int[] data) {
        int minValue = data[0];
        for (int x = 1; x < data.length; x++) {
            int actuellValue = data[x];
            if (minValue > actuellValue) {
                minValue = actuellValue;
            }
        }
        return minValue;
    }

    public static int getNextMinValue(int[] data, int minValue) {
        int nextMinValue = Statistic.getMaxValue(data);
        for (int x = 1; x < data.length; x++) {
            int actuellValue = data[x];
            if (minValue < actuellValue && nextMinValue > actuellValue) {
                nextMinValue = actuellValue;
            }
        }
        return nextMinValue;
    }

    public static short getNextMinValue(short[] data, short minValue) {
        short nextMinValue = Statistic.getMaxValue(data);
        for (int x = 1; x < data.length; x++) {
            short actuellValue = data[x];
            if (minValue < actuellValue && nextMinValue > actuellValue) {
                nextMinValue = actuellValue;
            }
        }
        return nextMinValue;
    }

    public static int minValuesCount(int[] data, int minValue) {
        int count = 0;
        for (int x = 1; x < data.length; x++) {
            if (data[x] <= minValue) {
                count++;
            }
        }
        return count;
    }

    public static int maxValuesCount(short[] data, short maxValue) {
        int count = 0;
        for (int x = 1; x < data.length; x++) {
            if (data[x] >= maxValue) {
                count++;
            }
        }
        return count;
    }

    public static int minValuesCount(short[] data, double minValue) {
        int count = 0;
        for (int x = 1; x < data.length; x++) {
            if (data[x] <= minValue) {
                count++;
            }
        }
        return count;
    }

    public static double getMaxValueWithExcludedValue(Matrix matrix, LinkedList<Double> excludedValues) {
        System.out.println(Arrays.toString(excludedValues.toArray()));
        double maxValue = Double.NEGATIVE_INFINITY;
        double actuellValue;
        for (int x = 0; x < matrix.numCols(); x++) {
            for (int y = 0; y < matrix.numRows(); y++) {
                actuellValue = matrix.get(x, y);
                if (maxValue < actuellValue && !excludedValues.contains(actuellValue)) {
                    maxValue = actuellValue;
                }
            }
        }
        return maxValue;
    }

    public static double getMinValueWithExcludedValue(Matrix matrix, LinkedList<Double> excludedValues) {
        System.out.println(Arrays.toString(excludedValues.toArray()));
        double minValue = Double.MAX_VALUE;
        double actuellValue;
        for (int x = 0; x < matrix.numCols(); x++) {
            for (int y = 0; y < matrix.numRows(); y++) {
                actuellValue = matrix.get(x, y);
                if (minValue > actuellValue && !excludedValues.contains(actuellValue)) {
                    minValue = actuellValue;
                }
            }
        }
        return minValue;
    }

    public static short getMinValue(Matrix matrix) {
        short minValue = matrix.get(0, 0);
        for (int x = 0; x < matrix.numCols(); x++) {
            for (int y = 0; y < matrix.numRows(); y++) {
                short actuellValue = matrix.get(x, y);
                if (minValue > actuellValue) {
                    minValue = actuellValue;
                }
            }
        }
        return minValue;
    }

    public static int getIndexOfMaxValue(double[] data) {
        double max = Double.NEGATIVE_INFINITY;
        int counter = 0;
        int result = 0;
        for (double value : data) {
            if (value > max) {
                max = value;
                result = counter;
            }
            counter++;
        }
        return result;
    }

    public static IntegerPoint getMaxPoint(Matrix matrix) {
        short[] data = matrix.getData();
        int location = getIndexOfMaxValue(data);

        double value = data[location];
        int x = location % matrix.numCols();
        int y = location / matrix.numCols();
        IntegerPoint result = new IntegerPoint(x, y, 0, value);
        return result;
    }

    public static int getIndexOfMaxValue(short[] data) {
        int max = Integer.MIN_VALUE;
        int counter = 0;
        int result = 0;
        for (int value : data) {
            if (value > max) {
                max = value;
                result = counter;
            }
            counter++;
        }
        return result;
    }

    public static double variance(Matrix matrix) {
        double mean = 0;
        int imagesize_x = matrix.numCols();
        int imagesize_y = matrix.numRows();
        for (int x = 0; x < imagesize_x; x++) {
            for (int y = 0; y < imagesize_y; y++) {
                mean += matrix.get(x, y);
            }
        }

        mean /= imagesize_x * imagesize_y;
        double variance = 0;
        for (int x = 0; x < imagesize_x; x++) {
            for (int y = 0; y < imagesize_y; y++) {
                variance += ((matrix.get(x, y) - mean) * (matrix.get(x, y) - mean));
            }
        }
        variance /= imagesize_x * imagesize_y;
        return variance;
    }


    public static int getIndexOfLimit(int[] histogram, double limit) {
        int summe = 0;
        for (int value : histogram) {
            summe = summe + value;
        }
        double limitSumme = summe * limit;

        summe = 0;
        int counter = 0;
        for (int value : histogram) {
            summe = summe + value;
            if (summe > limitSumme) {
                return counter;
            }
            counter++;
        }
        return histogram.length;
    }

    public static Matrix filterMatrix(Matrix matrix, short minValue, short maxValue) {
        int width = matrix.numCols();
        int height = matrix.numRows();
        Matrix result = new Matrix(height, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                short value = matrix.get(x, y);
                if (value > maxValue) {
                    value = maxValue;
                }

                if (value < minValue) {
                    value = minValue;
                }
                result.set(x, y, value);
            }
        }
        return result;
    }



    // liefert den nachsten point aus der Liste zu reference point
    public static IntegerPoint getNearestPoint(LinkedList<IntegerPoint> points, IntegerPoint referencePoint) {
        double minDistance = Double.MAX_VALUE;
        IntegerPoint result = null;
        for (IntegerPoint point : points) {
            double distance = FastMath.pow(referencePoint.getX() - point.getX(), 2) + FastMath.pow(referencePoint.getY() - point.getY(), 2);
            if (distance < minDistance) {
                minDistance = distance;
                result = point;
            }
        }
        return result;
    }

    public static DoublePoint normalizeVector(Point vector) {
        double lenght = vector.distanceToZeroPoint();
        return new DoublePoint(vector.getX().doubleValue() / lenght, vector.getY().doubleValue() / lenght, vector.getZ().doubleValue() / lenght);
    }



    /**
     * @param point
     * @param angle in grad
     * @return
     */
    public static DoublePoint rotatePoint(Point point, double angle) {
        double angleInRadians = FastMath.toRadians(angle);
        double oldX = point.getX().doubleValue();
        double oldY = point.getY().doubleValue();
        double newX = oldX * FastMath.cos(angleInRadians) + oldY * FastMath.sin(angleInRadians);
        double newY = -oldX * FastMath.sin(angleInRadians) + oldY * FastMath.cos(angleInRadians);
        return new DoublePoint(newX, newY);
    }


    public static double interpolate(double min, double max, double mappingFrom, double mappingTo, double value) {
        double tmp = (value - mappingFrom) / (mappingTo - mappingFrom) * max;

        if (min == 0) {
            return tmp;
        } else {
            return (mappingTo - value) / (mappingTo - mappingFrom) * min + tmp;
        }
    }



    public static Matrix createMatrix(short[][] data) {
        int width = data.length;
        int height = data[0].length;
        Matrix matrix = new Matrix(width, height);
        for (int x = 0; x < width; x++) {
            short[] datas = data[x];
            for (int y = 0; y < height; y++) {
                matrix.set(x, y, datas[y]);
            }
        }
        return matrix;
    }




    public static double getAvarageIntensity(Matrix matrix, IntegerPoint... points) {
        int numberOfNeighbors = 0;
        double totalSumme = 0;
        for (IntegerPoint point : points) {
            if (matrix.isInBounds(point.getX(), point.getY())) {
                totalSumme = totalSumme + matrix.get(point.getX(), point.getY());
                numberOfNeighbors++;
            }
        }
        return totalSumme / numberOfNeighbors;
    }


    public static String formatDoubleWithPrecision(double val, int precision) {
        int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};
        StringBuilder sb = new StringBuilder();
        if (val < 0) {
            sb.append('-');
            val = -val;
        }
        int exp = POW10[precision];
        long lval = (long) (val * exp + 0.5);
        sb.append(lval / exp).append('.');
        long fval = lval % exp;
        for (int p = precision - 1; p > 0 && fval < POW10[p]; p--) {
            sb.append('0');
        }
        sb.append(fval);
        return sb.toString();
    }
}

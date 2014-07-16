package sample;


/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 10.09.13
 * Time: 08:34
 * To change this template use File | Settings | File Templates.
 */
public class Matrix {
    private ShortMatrix dataContainer;

    public short[] getData() {
        return dataContainer.getData();
    }

    public void setData(short[] data) {
        dataContainer.setData(data);
    }

    public Matrix copy() {
        Matrix result = new Matrix();
        ShortMatrix newContainwer = dataContainer.copy();
        result.dataContainer = newContainwer;
        return result;
    }

    public short get(int x, int y) {
        return dataContainer.get(y, x);
    }

    public void set(int x, int y, short value) {
        dataContainer.set(y, x, value);
    }

    public Matrix() {
    }

    public Matrix minus(Matrix otherMatrix) {
        ShortMatrix resultOfMinus = dataContainer.minus(otherMatrix.dataContainer);
        Matrix result = new Matrix();
        result.dataContainer = resultOfMinus;
        return result;
    }

    public int numCols() {
        return dataContainer.numCols();
    }

    public int numRows() {
        return dataContainer.numRows();
    }

    public Matrix(int width, int height) {
        dataContainer = new ShortMatrix(height, width);
    }

    public boolean isInBounds(int x, int y) {
        return dataContainer.isInBounds(y, x);
    }

    public double getMaxElement() {
        final int size = dataContainer.getNumElements();

        double max = dataContainer.get(0);
        for (int i = 1; i < size; i++) {
            double val = dataContainer.get(i);
            if (val >= max) {
                max = val;
            }
        }
        return max;
    }
}

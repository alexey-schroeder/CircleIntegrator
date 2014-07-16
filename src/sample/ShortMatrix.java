package sample;


/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 07.10.13
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
public class ShortMatrix {
    private short[] data;
    /**
     * Number of rows in the matrix.
     */
    private int numRows;
    /**
     * Number of columns in the matrix.
     */
    private int numCols;

    public ShortMatrix() {
    }

    public ShortMatrix(int height, int width) {
        numCols = width;
        numRows = height;
        data = new short[numRows * numCols];
    }

    public short[] getData() {
        return data;
    }

    public void setData(short[] data) {
        this.data = data;
    }

    public int numRows() {
        return numRows;
    }

    public int numCols() {
        return numCols;
    }

    public ShortMatrix copy() {
        ShortMatrix result = new ShortMatrix();
        result.numRows = numRows;
        result.numCols = numCols;

        int N = numCols * numRows;
        result.data = new short[N];

        System.arraycopy(data, 0, result.data, 0, N);
        return result;
    }

    public short get(int row, int col) {
        return data[getIndex(row, col)];
    }

    public int getIndex(int row, int col) {
        return row * numCols + col;
    }

    public void set(int row, int col, short value) {
        data[getIndex(row, col)] = value;
    }

    public int getNumElements() {
        return numCols * numRows;
    }

    public short get(int i) {
        return data[i];
    }

    public ShortMatrix minus(ShortMatrix matrix) {
        ShortMatrix result = copy();
        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                int index = getIndex(y, x);
                result.data[index] = (short) (result.data[index] - matrix.data[index]);
            }
        }
        return result;
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < numRows && col < numCols;
    }
}

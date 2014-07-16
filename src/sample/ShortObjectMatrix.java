package sample;


import sample.point.ShortPoint;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 07.10.13
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
public class ShortObjectMatrix {
    private HashMap<String, Short> data;
    /**
     * Number of rows in the matrix.
     */
    private int numRows;
    /**
     * Number of columns in the matrix.
     */
    private int numCols;

    public ShortObjectMatrix() {
        data = new HashMap<>();
    }

    public ShortObjectMatrix getShallowCopy() {
        ShortObjectMatrix result = new ShortObjectMatrix(numCols, numRows);
        HashMap<String, Short> resultData = new HashMap<>(data);
        result.setData(resultData);
        return result;
    }

    public ShortObjectMatrix(int height, int width) {
        numCols = width;
        numRows = height;
        data = new HashMap<>();
    }

    public ShortObjectMatrix(int height, int width, int initSize) {
        numCols = width;
        numRows = height;
        data = new HashMap<>(initSize);
    }


    public int numRows() {
        return numRows;
    }

    public int numCols() {
        return numCols;
    }


    public Short get(int x, int y) {
        return data.get(getKeyByCoordinaten(x, y));
    }


    public  void set(int x, int y, Short value) {
        String key = getKeyByCoordinaten(x, y);
        if (value == null) {
            data.remove(key);
        } else {
            Short oldValue = data.get(key);
            if (oldValue == null) {
                data.put(key, value);
            } else {
                if (value > oldValue) {
                    data.put(key, value);
                }
            }
        }
    }

    private String getKeyByCoordinaten(int x, int y) {
        String koordinate = x + "_" + y;
        return koordinate;
    }

    public void setData(HashMap<String, Short> data) {
        this.data = data;
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && y < numRows && x < numCols;
    }

    public int getWidth() {
        return numCols;
    }

    public int getHeight() {
        return numRows;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public ShortPoint getAnyPoint() {
        if (!data.isEmpty()) {
            String key = data.keySet().iterator().next();
            ShortPoint result = getCoordinatenByKey(key);
            Short value = data.get(key);
            result.setValue(value);
            return result;
        } else {
            return null;
        }
    }

    private ShortPoint getCoordinatenByKey(String key) {
        String[] koordinaten = key.split("_");
        short x = Short.valueOf(koordinaten[0]);
        short y = Short.valueOf(koordinaten[1]);
        return new ShortPoint(x, y);
    }
}

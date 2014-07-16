package sample;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.DataBuffer;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

/**
 * Created by schroedera85 on 07.05.14.
 */
public class TiffImageReader {
    private File file;


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Matrix readImage(File file) {
        FileSeekableStream stream = null;
        try {
            stream = new FileSeekableStream(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        // Store the input stream in a ParameterBlock to be sent to
        // the operation registry, and eventually to the TIFF
        // decoder.
        ParameterBlock params = new ParameterBlock();
        params.add(stream);
        // Specify to TIFF decoder to decode images as they are and
        // not to convert unsigned short images to byte images.
        TIFFDecodeParam decodeParam = new TIFFDecodeParam();
        decodeParam.setDecodePaletteAsShorts(true);
        // Create an operator to decode the TIFF file.
        RenderedOp image1 = JAI.create("tiff", params);
        //Find out the first image’s data type.
        int dataType = image1.getSampleModel().getDataType();
        //RenderedOp image2 = null;
        if (dataType == DataBuffer.TYPE_BYTE) {
            // Display the byte image as it is.
            Matrix matrix = new Matrix(image1.getWidth(), image1.getHeight());
            short[] matrixData = matrix.getData();
            DataBuffer dataBuffer = image1.getData().getDataBuffer();
            int counter = 0;
            for (int i = 0; i < dataBuffer.getSize(); i = i + 3) {
                int data = dataBuffer.getElem(i);
                matrixData[counter] = (short) data;
                counter++;
            }
            return matrix;
        } else {
            int[] matrixData = new int[image1.getWidth() * image1.getHeight()];
            DataBuffer dataBuffer = image1.getData().getDataBuffer();
            boolean mustScaled = false;
            for (int i = 0; i < dataBuffer.getSize(); i++) {
                int data = dataBuffer.getElem(i);
                matrixData[i] = data;
                if (data > Short.MAX_VALUE) {
                    mustScaled = true;
                }
            }

            Matrix matrix = new Matrix(image1.getWidth(), image1.getHeight());
            if (mustScaled) {
                int minNumberOfMinValues = (int) (matrixData.length * 0.01); // 1 procent

                int minValue = Statistic.getMinValue(matrixData);

                int numberOfMinValues = Statistic.minValuesCount(matrixData, minValue);
                while (numberOfMinValues < minNumberOfMinValues) {
                    minValue = Statistic.getNextMinValue(matrixData, minValue);
                    numberOfMinValues = Statistic.minValuesCount(matrixData, minValue);
                }

                int maxValue = Statistic.getMaxValue(matrixData);
                int shiftValue = minValue;
                short[] shortMatrixData = matrix.getData();
                for (int i = 0; i < matrixData.length; i++) {
                    int newIntValue = matrixData[i] - shiftValue;
                    if (newIntValue < 0) {
                        newIntValue = 0;
                    }
                    short newValue = (short) Statistic.interpolate(0, Short.MAX_VALUE, 0, maxValue - shiftValue, newIntValue);
                    shortMatrixData[i] = newValue;
                }
            } else {
                short[] shortMatrixData = new short[matrixData.length];
                for (int i = 0; i < matrixData.length; i++) {
                    shortMatrixData[i] = (short) matrixData[i];
                }
                matrix.setData(shortMatrixData);
            }
            return matrix;
        }
    }

    public Matrix readImage() {
        return readImage(file);
    }
}

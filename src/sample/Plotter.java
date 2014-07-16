package sample;

//import info.monitorenter.gui.chart.Chart2D;
//import info.monitorenter.gui.chart.ITrace2D;
//import info.monitorenter.gui.chart.traces.Trace2DSimple;

//import info.monitorenter.gui.chart.Chart2D;
//import info.monitorenter.gui.chart.ITrace2D;
//import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import org.jzy3d.chart.Chart;
//import org.jzy3d.chart.ChartLauncher;
//import org.jzy3d.colors.ColorMapper;
//import org.jzy3d.colors.colormaps.ColorMapRainbow;
//import org.jzy3d.maths.Range;
//import org.jzy3d.plot3d.builder.Builder;
//import org.jzy3d.plot3d.builder.Mapper;
//import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
//import org.jzy3d.plot3d.primitives.Shape;
//import org.jzy3d.plot3d.primitives.axes.layout.renderers.FixedDecimalTickRenderer;
//import org.jzy3d.plot3d.rendering.canvas.Quality;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;

//import javax.swing.*;
//import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 27.08.13
 * Time: 08:46
 * To change this template use File | Settings | File Templates.
 */
public class Plotter {
    static int cores = Runtime.getRuntime().availableProcessors();
    private static boolean isLibraryLoaded = false;


//    public static void draw3DPlot(final Matrix mat, int xStart, int xEnd, int yStart, int yEnd, final double minValue, final double maxValue) throws IOException {
//        // Define a function to plot
//        Mapper mapper = new Mapper() {
//            public double f(double x, double y) {
//                double value = mat.get((int)x, (int)y);
//                if(value > maxValue){
//                    return  maxValue;
//                }
//
//                if(value < minValue) {
//                    return   minValue;
//                }
//                return value;
//            }
//        };
//
//// Define range and precision for the function to plot
//        Range rangeX = new Range(xStart, xEnd);
//        Range rangeY = new Range(yStart, yEnd);
//        int stepsX = (xEnd- xStart) / 20;
//        int stepsY = (yEnd - yStart) / 20;
//
//// Create a surface drawing that function
//        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(rangeX, stepsX, rangeY, stepsY), mapper);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, .5f)));
//        surface.setFaceDisplayed(true);
//        surface.setWireframeDisplayed(false);
//        surface.setWireframeColor(org.jzy3d.colors.Color.BLACK);
//
//// Create a chart and add the surface
//        Chart chart = new Chart(Quality.Advanced);
//        FixedDecimalTickRenderer fdtr = new FixedDecimalTickRenderer(0);
//        chart.getAxeLayout().setYTickRenderer(fdtr);
//        chart.getAxeLayout().setZTickRenderer(fdtr);
//        chart.getAxeLayout().setXTickRenderer(fdtr);
//        chart.getScene().getGraph().add(surface);
//        ChartLauncher.openChart(chart);
//    }

//    public static void draw2dPlot(HashMap<Matrix, Color> mat, int y, int xStart, int xEnd, double minValue, double maxValue) {
//
//        // Create a chart:
//        Chart2D chart = new Chart2D();
//        for (Matrix matrix : mat.keySet()) {
//            // Create an ITrace:
//            ITrace2D trace = new Trace2DSimple();
//            trace.setColor(mat.get(matrix));
//            // Add the trace to the chart. This has to be done before adding points (deadlock prevention):
//            chart.addTrace(trace);
//            // Add all points, as it is static:
//            for (int x = xStart; x < xEnd; x++) {
//                double value = matrix.get(x, y);
//                if (value > maxValue) {
//                    value = maxValue;
//                }
//
//                if (value < minValue) {
//                    value = minValue;
//                }
//
//                trace.addPoint(x, value);
//            }
//        }
//        // Make it visible:
//        // Create a frame.
//        JFrame frame = new JFrame("MinimalStaticChart");
//        // add the chart to the frame:
//        frame.getContentPane().add(chart);
//        frame.setSize(1600, 1000);
//        // Enable the termination button [cross on the upper right edge]:
//        frame.addWindowListener(
//                new WindowAdapter() {
//                    public void windowClosing(WindowEvent e) {
//                        System.exit(0);
//                    }
//                }
//        );
//        frame.setVisible(true);
//    }

//    public static void  draw2dPlot(HashMap<HashMap<Double, Double>, Color> data){
//
//        // Create a chart:
//        Chart2D chart = new Chart2D();
//        for(HashMap<Double, Double> localData: data.keySet()) {
//            // Create an ITrace:
//            ITrace2D trace = new Trace2DSimple();
//            trace.setColor(data.get(localData));
//            // Add the trace to the chart. This has to be done before adding points (deadlock prevention):
//            chart.addTrace(trace);
//            // Add all points, as it is static:
//
//            Set<Double> keys = localData.keySet();
//            LinkedList<Double> keysAsList = new LinkedList<>(keys);
//            Collections.sort(keysAsList);
//            for(Double key: keysAsList){
//                Double value = localData.get(key);
//                trace.addPoint(key, value);
//                System.out.println(key + "->" + value);
//            }
//        }
//        // Make it visible:
//        // Create a frame.
//        JFrame frame = new JFrame("MinimalStaticChart");
//        // add the chart to the frame:
//        frame.getContentPane().add(chart);
//        frame.setSize(1600, 1000);
//        // Enable the termination button [cross on the upper right edge]:
//        frame.addWindowListener(
//                new WindowAdapter() {
//                    public void windowClosing(WindowEvent e) {
//                        System.exit(0);
//                    }
//                }
//        );
//        frame.setVisible(true);
//    }


    public static BufferedImage getBufferedImage(Matrix mat, double minValue, boolean withColor) {
        int width = mat.numCols();
        int height = mat.numRows();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if (withColor) {
            double maxValue = mat.getMaxElement();

            double valueStep = (maxValue - minValue) / 16777215;
            System.out.println("minValue:" + minValue + ", maxValue:" + maxValue + ", step: " + valueStep);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    double value = mat.get(x, y);
                    if (value > minValue) {
                        int color = (int) ((value - minValue) / valueStep);
                        img.setRGB(x, y, color);
                    } else
                        img.setRGB(x, y, 0);
                }
            }
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    double value = mat.get(x, y);
                    if (value > 255) {
//                    Color color =  new Color(255, 255, 255);
//                    img.setRGB(x, y, color.getRGB());
                        img.setRGB(x, y, -1);
                    } else if (value < minValue) {
//                    Color color =  new Color(0, 0, 0);
//                    img.setRGB(x, y, color.getRGB());
                        img.setRGB(x, y, -16777216);
                    } else {
                        Color color = new Color((int) value, (int) value, (int) value);
//                    System.out.println(value);
//                    Color color =  new Color(0, 255, 0);
                        img.setRGB(x, y, color.getRGB());
                    }
                }
            }
        }
        return img;
    }

    public static BufferedImage getColoredBufferedImage(Matrix mat) {
        int width = mat.numCols();
        int height = mat.numRows();

        LinkedList<Double> excludedValues = new LinkedList<>();

        double minValue = Statistic.getMinValue(mat);

        double maxValue = Statistic.getMaxValue(mat);
        excludedValues.clear();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = mat.get(x, y);
                int[] rgb;
                if (value < minValue) {
                    rgb = new int[3];
                } else {
                    rgb = integerToColoredRGB(value, minValue, maxValue, 450.0, 680.0);
                }
                Color color = new Color(rgb[0], rgb[1], rgb[2]);
                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }

    public static BufferedImage getInterpolateGrauScaleBufferedImage(Matrix mat) {
        int width = mat.numCols();
        int height = mat.numRows();
        double maxValue = Statistic.getMaxValue(mat);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = mat.get(x, y);
                if (value < 0) {
                    value = 0;
                }
                double interpolatedValue = Statistic.interpolate(0, 255, 0, maxValue, value);
                Color color = new Color((int) interpolatedValue, (int) interpolatedValue, (int) interpolatedValue);
                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }

    public static javafx.scene.image.WritableImage getDummyGrayscaleFXImage(Matrix mat, double minValue) {
        int width = mat.numCols();
        int height = mat.numRows();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = mat.get(x, y);
                if (value > 255) {
                    javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(255, 255, 255);
                    pixelWriter.setColor(x, y, color);
                } else if (value < minValue) {
                    javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(0, 0, 0);
                    pixelWriter.setColor(x, y, color);
                } else {
                    javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb((int) value, (int) value, (int) value);
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
        return writableImage;
    }

    public static javafx.scene.image.WritableImage getGrayscaleFXImage(Matrix matrix) {
        return getGrayscaleFXImage(matrix, false);
    }


    public static javafx.scene.image.WritableImage getGrayscaleNegativeFXImage(Matrix mat) {
        return getGrayscaleFXImage(mat, true);
    }

    /**
     * hier ist die schnelle version mit bit- verschiebung:
     * http://tech-algorithm.com/articles/nearest-neighbor-image-scaling/
     */
    public static Matrix resizeMatrix(Matrix matrix, double scaleFactor) {
        if (scaleFactor >= 1) {
            return matrix.copy();
        } else {
            int newWidth = (int) (matrix.numCols() * scaleFactor);
            int newHeight = (int) (matrix.numRows() * scaleFactor);
            Matrix result = new Matrix(newWidth, newHeight);
            int px, py;
            for (int x = 0; x < newWidth; x++) {
                for (int y = 0; y < newHeight; y++) {
                    px = (int) Math.floor(x / scaleFactor);
                    py = (int) Math.floor(y / scaleFactor);
                    result.set(x, y, matrix.get(px, py));
                }
            }
            return result;
        }
    }

    public static BufferedImage fxImageToBufferedImage(javafx.scene.image.WritableImage fxImage) {
        return SwingFXUtils.fromFXImage(fxImage, null);
    }

    public static WritableImage bufferedImageToFXImage(BufferedImage image) {
        return SwingFXUtils.toFXImage(image, null);
    }

    public static WritableImage copy(WritableImage writableImage) {
        PixelReader pixelReader = writableImage.getPixelReader();

        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();

        //Copy from source to destination pixel by pixel
        WritableImage resultImage
                = new WritableImage(width, height);
        PixelWriter pixelWriter = resultImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }
        return resultImage;
    }




    public static javafx.scene.image.WritableImage getFXColoredImage(Matrix mat) {
        double minValue = Statistic.getMinValue(mat);
        if (minValue < 0) {
            minValue = 0;
        }
        double maxValue = Statistic.getMaxValue(mat);
        double factor = 1.0;
        double minMaxValue = 1000; // maximale intensity muss mindestens 1000 sein, sonst werden alle intensities skaliert
        if (maxValue < minMaxValue) {
            factor = minMaxValue / maxValue;
            maxValue = minMaxValue;
        }
        int width = mat.numCols();
        int height = mat.numRows();

        int shiftInBin = 10;

        int numBins = (int) ((maxValue - minValue) / shiftInBin);
        int[] histogramm = Statistic.calcHistogram(mat.getData(), minValue, maxValue, numBins);
        int partAlimitIndex = Statistic.getIndexOfLimit(histogramm, 0.8);
        int backGroundValue = (partAlimitIndex + 1) * shiftInBin;
        int partA = (int) (backGroundValue * 0.5);

        int partBlimitIndex = Statistic.getIndexOfLimit(histogramm, 0.997);
        int partB = (partBlimitIndex + 1) * shiftInBin;
        double colorAEnd = 455; // part A hat die farben mit visibleLengt ab 450 bis 455
        double colorBEnd = 615; // part B hat die farben ab visibleLenght ab 456  bis 615, part C hat die farben ab 616 bis 680
        WritableImage writableImage = new WritableImage(width, height);

        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        int heightStep = height / cores;
        for (int threadNumber = 0; threadNumber < cores - 1; threadNumber++) {
            int xStart = 0;
            int xEnd = width;
            int yStart = threadNumber * heightStep;
            int yEnd = (threadNumber + 1) * heightStep;

            executorService.execute(getThreadForProduceOfColoredImage(mat, writableImage, xStart, xEnd, yStart, yEnd,
                    partA, partB, colorAEnd, colorBEnd, minValue, maxValue, factor));
        }
        int xStart = 0;
        int xEnd = width;
        int yStart = (cores - 1) * heightStep;
        int yEnd = height;
        executorService.execute(getThreadForProduceOfColoredImage(mat, writableImage, xStart, xEnd, yStart, yEnd,
                partA, partB, colorAEnd, colorBEnd, minValue, maxValue, factor));
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return writableImage;
    }

    private static Thread getThreadForProduceOfColoredImage(Matrix mat, WritableImage writableImage, int xStart, int xEnd, int yStart, int yEnd,
                                                            double partA, double partB, double colorAEnd, double colorBEnd, double minValue, double maxValue, double factor) {
        return new Thread() {
            @Override
            public void run() {
                PixelWriter pixelWriter = writableImage.getPixelWriter();
                for (int x = xStart; x < xEnd; x++) {
                    for (int y = yStart; y < yEnd; y++) {
                        double value = mat.get(x, y) * factor;
                        int[] rgb;
                        if (value <= partA) {
                            rgb = Plotter.integerToColoredRGB(value, minValue, partA, 450.0, colorAEnd);
                        } else if (value > partA && value <= partB) {
                            rgb = Plotter.integerToColoredRGB(value, partA + 1, partB, colorAEnd + 1, colorBEnd);
                        } else {
                            rgb = Plotter.integerToColoredRGB(value, partB + 1, maxValue, colorBEnd, 680.0);
                        }

                        javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(rgb[0], rgb[1], rgb[2]);
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }

            ;
        };
    }

    private static Thread getThreadForProduceOfGrayImage(Matrix mat, WritableImage writableImage, int xStart, int xEnd, int yStart, int yEnd,
                                                         double partA, double partB, double colorAEnd, double colorBEnd, double minValue, double maxValue, double factor, boolean negativ) {
        return new Thread() {
            @Override
            public void run() {
                PixelWriter pixelWriter = writableImage.getPixelWriter();
                for (int x = xStart; x < xEnd; x++) {
                    for (int y = yStart; y < yEnd; y++) {
                        double value = mat.get(x, y) * factor;
                        int colorValue;
                        if (value <= partA) {
                            colorValue = Plotter.integerToGrayRGB(value, minValue, partA, 450.0, colorAEnd);
                        } else if (value > partA && value <= partB) {
                            colorValue = Plotter.integerToGrayRGB(value, partA + 1, partB, colorAEnd + 1, colorBEnd);
                        } else {
                            colorValue = Plotter.integerToGrayRGB(value, partB + 1, maxValue, colorBEnd, 680.0);
                        }

                        javafx.scene.paint.Color color;
                        if (negativ) {
                            colorValue = 255 - colorValue;
                        }
                        color = javafx.scene.paint.Color.rgb(colorValue, colorValue, colorValue);
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }

            ;
        };
    }


    public static javafx.scene.image.WritableImage getGrayscaleFXImage(Matrix mat, boolean negativ) {
        double minValue = Statistic.getMinValue(mat);
        if (minValue < 0) {
            minValue = 0;
        }
        double maxValue = Statistic.getMaxValue(mat);
        double factor = 1.0;
        double minMaxValue = 1000; // maximale intensity muss mindestens 1000 sein, sonst werden alle intensities skaliert
        if (maxValue < minMaxValue) {
            factor = minMaxValue / maxValue;
            maxValue = minMaxValue;
        }
        int width = mat.numCols();
        int height = mat.numRows();

        int shiftInBin = 10;

        int numBins = (int) ((maxValue - minValue) / shiftInBin);
        int[] histogramm = Statistic.calcHistogram(mat.getData(), minValue, maxValue, numBins);
        int partAlimitIndex = Statistic.getIndexOfLimit(histogramm, 0.8);
        int backGroundValue = (partAlimitIndex + 1) * shiftInBin;
        int partA = (int) (backGroundValue * 0.5);

        int partBlimitIndex = Statistic.getIndexOfLimit(histogramm, 0.997);
        int partB = (partBlimitIndex + 1) * shiftInBin;
        double colorAEnd = 455; // part A hat die farben mit visibleLengt ab 450 bis 455
        double colorBEnd = 615; // part B hat die farben ab visibleLenght ab 456  bis 615, part C hat die farben ab 616 bis 680
        WritableImage writableImage = new WritableImage(width, height);

        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        int heightStep = height / cores;
        for (int threadNumber = 0; threadNumber < cores - 1; threadNumber++) {
            int xStart = 0;
            int xEnd = width;
            int yStart = threadNumber * heightStep;
            int yEnd = (threadNumber + 1) * heightStep;

            executorService.execute(getThreadForProduceOfGrayImage(mat, writableImage, xStart, xEnd, yStart, yEnd,
                    partA, partB, colorAEnd, colorBEnd, minValue, maxValue, factor, negativ));
        }
        int xStart = 0;
        int xEnd = width;
        int yStart = (cores - 1) * heightStep;
        int yEnd = height;
        executorService.execute(getThreadForProduceOfGrayImage(mat, writableImage, xStart, xEnd, yStart, yEnd,
                partA, partB, colorAEnd, colorBEnd, minValue, maxValue, factor, negativ));
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return writableImage;
    }


    public static BufferedImage getBufferedImage(boolean[][] mat, boolean toShow) {
        int width = mat.length;
        int height = mat[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                boolean value = mat[x][y];
                Color color;
                if (value == toShow) {
                    color = new Color(255, 255, 255);
                } else {
                    color = new Color(0, 0, 0);
                }
                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }

    public static Matrix getMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Matrix result = new Matrix(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = image.getRGB(x, y);

                int r = (argb) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb >> 16) & 0xFF;
                int a = (argb >> 24) & 0xFF;
                double gamma = 2.2;
                double Y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
//                double Y = (r + g + b) / 3;
                result.set(x, y, (short) Y);
            }
        }
        return result;
    }

    public static double[] filterPixels(int width, int height, double[] inPixels) {
        int index = 0;
        double[] outPixels = new double[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double pixel = 0xff000000;
                for (int dy = -1; dy <= 1; dy++) {
                    int iy = y + dy;
                    int ioffset;
                    if (0 <= iy && iy < height) {
                        ioffset = iy * width;
                        for (int dx = -1; dx <= 1; dx++) {
                            int ix = x + dx;
                            if (0 <= ix && ix < width) {
                                pixel = Math.max(pixel, inPixels[ioffset + ix]);
                            }
                        }
                    }
                }
                outPixels[index++] = pixel;
            }
        }
        return outPixels;
    }

    /**
     * source : http://stackoverflow.com/questions/2374959/algorithm-to-convert-any-positive-integer-to-an-rgb-value
     *
     * @param value
     * @param minValues
     * @param maxValues
     * @param minVisibleWaveLength
     * @param maxVisibleWaveLength
     * @return
     */
    public static int[] integerToColoredRGB(double value, double minValues, double maxValues, double minVisibleWaveLength, double maxVisibleWaveLength) {
        int[] result = new int[3];
        double waveLength = (value - minValues) / (maxValues - minValues) * (maxVisibleWaveLength - minVisibleWaveLength) + minVisibleWaveLength;

        double blue;
        double factor;
        double green;
        double red;

        if (waveLength >= 380 && waveLength <= 440) {
            red = -(waveLength - 440) / (440 - 380);
            green = 0.0;
            blue = 1.0;
        } else if (waveLength >= 440 && waveLength <= 490) {
            red = 0.0;
            green = (waveLength - 440) / (490 - 440);
            blue = 1.0;
        } else if (waveLength >= 490 && waveLength <= 510) {
            red = 0.0;
            green = 1.0;
            blue = -(waveLength - 510) / (510 - 490);
        } else if (waveLength >= 510 && waveLength <= 580) {
            red = (waveLength - 510) / (580 - 510);
            green = 1.0;
            blue = 0.0;
        } else if (waveLength >= 580 && waveLength <= 645) {
            red = 1.0;
            green = -(waveLength - 645) / (645 - 580);
            blue = 0.0;
        } else if (waveLength >= 645 && waveLength <= 780) {
            red = 1.0;
            green = 0.0;
            blue = 0.0;
        } else {
            red = 0.0;
            green = 0.0;
            blue = 0.0;
        }
        // Let the intensityPoint fall off near the vision limits

        if (waveLength >= 380 && waveLength <= 419) {
            factor = 0.3 + 0.7 * (waveLength - 380) / (420 - 380);
        } else if (waveLength >= 420 && waveLength <= 700) {
            factor = 1.0;
        } else if (waveLength >= 701 && waveLength <= 780) {
            factor = 0.3 + 0.7 * (780 - waveLength) / (780 - 700);
        } else {
            factor = 0.0;
        }
        int r = adjust(red, factor);
        int g = adjust(green, factor);
        int b = adjust(blue, factor);
        result[0] = r;
        result[1] = g;
        result[2] = b;
        return result;
    }

    public static int integerToGrayRGB(double value, double minValues, double maxValues, double minVisibleWaveLength, double maxVisibleWaveLength) {
        int[] result = new int[3];
        double waveLength = (value - minValues) / (maxValues - minValues) * (maxVisibleWaveLength - minVisibleWaveLength) + minVisibleWaveLength;

        double color;
        double factor;

        if (waveLength >= 380 && waveLength <= 440) {
            color = Statistic.interpolate(0, 20, 380, 440, waveLength);
        } else if (waveLength >= 440 && waveLength <= 490) {
            color = Statistic.interpolate(20, 60, 440, 490, waveLength);
        } else if (waveLength >= 490 && waveLength <= 510) {
            color = Statistic.interpolate(60, 120, 490, 510, waveLength);
        } else if (waveLength >= 510 && waveLength <= 580) {
            color = Statistic.interpolate(120, 160, 510, 580, waveLength);
        } else if (waveLength >= 580 && waveLength <= 645) {
            color = Statistic.interpolate(160, 220, 580, 645, waveLength);
        } else if (waveLength >= 645 && waveLength <= 780) {
            color = Statistic.interpolate(220, 255, 645, 780, waveLength);
        } else {
            color = 0;
        }
        return (int) color;
    }


    private static int adjust(double color, double factor) {
        double gamma = 0.80;
        double intensityMax = 255;
        if (color == 0.0) {
            return 0;
        } else {   // Don't want 0^x = 1 for x <> 0
            return (int) FastMath.round(intensityMax * FastMath.pow(color * factor, gamma));
        }
    }

    public static ImageView getIconByPath(String path) {
        File file = new File(path);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        javafx.scene.image.Image image = new javafx.scene.image.Image(inputStream);
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    private static void loadLibrarys() {
        if (!isLibraryLoaded) {
            System.setProperty("java.library.path", System.getProperty("java.library.path") + System.getProperty("path.separator") + "lib/opencv" + System.getProperty("path.separator") + "gui/lib/opencv");
            try {
                Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
                fieldSysPath.setAccessible(true);
                fieldSysPath.set(null, null);
                System.loadLibrary("opencv_java246");
                isLibraryLoaded = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.centerFindTool.CenterFindTool;
import sample.interpolation.InterpolationBox;
import sample.interpolation.Interpolator;
import sample.point.DoublePoint;
import sample.point.IntegerPoint;
import sample.point.SynchronizedList;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {
    public CanvasWithZoom canvasWithZoom;
    public Label xCenterCoordinateLabel;
    public Label yCenterCoordinateLabel;
    public Label xInterpolatedCenterCoordinateLabel;
    public Label yInterpolatedCenterCoordinateLabel;
    public LineChart chart;
    private DoublePoint centerOfCircle;
    private DoublePoint interpolatedCenter;
    private Matrix matrix;
    private CenterFindTool centerFindTool;
    private double[] mapRadiusToIntegratedValue;

    public void initialize() {
        Registry.setCanvasWithZoom(canvasWithZoom);
        canvasWithZoom.setMinCanvasHeight(512);
        canvasWithZoom.setMinCanvasWidth(512);
        chart.setCreateSymbols(false);
    }

    public void onFileChooserButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("tif", "*.tif"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            TiffImageReader tiffImageReader = new TiffImageReader();
            matrix = tiffImageReader.readImage(file);
            WritableImage writableImage = Plotter.getFXColoredImage(matrix);
            canvasWithZoom.setAndShowImage(writableImage);
            centerFindTool = new CenterFindTool();
            centerFindTool.addOnCenterChangeListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object o2) {
                    centerOfCircle = (DoublePoint) o2;

                    xCenterCoordinateLabel.setText(String.format("%.1f", centerOfCircle.getX()));
                    yCenterCoordinateLabel.setText(String.format("%.1f", centerOfCircle.getY()));
                    interpolateCenter();
                }
            });
            centerFindTool.addOnChangeEndListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object o2) {
                    integrateIntensities();
                }
            });
            canvasWithZoom.addTool(centerFindTool);
        }
    }

    private void integrateIntensities() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int width = matrix.numCols();
                int height = matrix.numRows();

                int maxRadius = getMaxRadius();
                SynchronizedList<IntegerPoint>[] mapRadiusToPoints = new SynchronizedList[maxRadius];
                for (int i = 0; i < mapRadiusToPoints.length; i++) {
                    int size = (int) (10 * i) + 1;
                    mapRadiusToPoints[i] = new SynchronizedList<>(size);
                }
                int cores = Runtime.getRuntime().availableProcessors();
                int halfWidth = width / 2;
                ExecutorService taskExecutor = Executors.newFixedThreadPool(cores);
                for (int x = 0; x < halfWidth; x++) {
                    taskExecutor.execute(createThreadForRadiusCalculation(x, height, mapRadiusToPoints));
                    taskExecutor.execute(createThreadForRadiusCalculation(x + halfWidth, height, mapRadiusToPoints));
                }
                taskExecutor.shutdown();
                try {
                    taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                mapRadiusToIntegratedValue = new double[maxRadius];
                taskExecutor = Executors.newFixedThreadPool(cores);
                for (int radius = 0; radius < maxRadius; radius++) {
                    taskExecutor.execute(createThreadForRadiusIntegration(radius, mapRadiusToPoints));
                }
                taskExecutor.shutdown();
                try {
                    taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        showChart();
                    }
                };
                Platform.runLater(runnable);
            }
        };
        thread.start();
    }

    private int getMaxRadius() {
        int width = matrix.numCols();
        int height = matrix.numRows();
        IntegerPoint top_left = new IntegerPoint(0, 0);
        IntegerPoint top_right = new IntegerPoint(width, 0);
        IntegerPoint bottom_left = new IntegerPoint(0, height);
        IntegerPoint bottom_right = new IntegerPoint(width, height);
        double distance1 = interpolatedCenter.distanceToPoint(top_left);
        double distance2 = interpolatedCenter.distanceToPoint(top_right);
        double distance3 = interpolatedCenter.distanceToPoint(bottom_left);
        double distance4 = interpolatedCenter.distanceToPoint(bottom_right);

        double max1 = Math.max(distance1, distance2);
        double max2 = Math.max(distance3, distance4);
        return (int) Math.max(max1, max2) + 2;
    }

    private Runnable createThreadForRadiusIntegration(int radius, SynchronizedList<IntegerPoint>[] mapRadiusToPoints) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                integrateRadiusPoints(radius, mapRadiusToPoints);
            }
        };
        return runnable;
    }

    private void integrateRadiusPoints(int radius, SynchronizedList<IntegerPoint>[] mapRadiusToPoints) {
        SynchronizedList<IntegerPoint> pointsInRadius = mapRadiusToPoints[radius];

        if (!pointsInRadius.isEmpty()) {
            double integratedValue = 0;
            for (int i = 0; i < pointsInRadius.size(); i++) {
                IntegerPoint point = pointsInRadius.get(i);
                integratedValue = integratedValue + point.getValue();
            }
            double averageIntegratedValue = integratedValue / pointsInRadius.size();
            mapRadiusToIntegratedValue[radius] = averageIntegratedValue;
        }
    }

    private Runnable createThreadForRadiusCalculation(int x, int height, SynchronizedList<IntegerPoint>[] mapRadiusToPoints) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                calculateCirclePoints(x, height, mapRadiusToPoints);
            }
        };
        return runnable;
    }

    private void calculateCirclePoints(int x, int height, SynchronizedList<IntegerPoint>[] mapRadiusToPoints) {
        for (int y = 0; y < height; y++) {
            short value = matrix.get(x, y);
            IntegerPoint point = new IntegerPoint(x, y);
            point.setValue(value);
            int radius = (int) Math.round(interpolatedCenter.distanceToPoint(point));
            addRadiusPoint(radius, point, mapRadiusToPoints);
        }
    }

    private void addRadiusPoint(int radius, IntegerPoint point, SynchronizedList<IntegerPoint>[] mapRadiusToPoints) {
//        if(radius >= mapRadiusToPoints.length){
//            System.out.println(radius + ", " + mapRadiusToPoints.length);
//        }
        SynchronizedList<IntegerPoint> points = mapRadiusToPoints[radius];
        points.add(point);
    }

    private void showChart() {
        XYChart.Series integratedValuesChartData = new XYChart.Series();
        integratedValuesChartData.setName("Integrated Values");

        for (int radius = 1; radius < mapRadiusToIntegratedValue.length; radius++) {
            double integratedValue = mapRadiusToIntegratedValue[radius];
            integratedValuesChartData.getData().add(new XYChart.Data(radius, integratedValue));
        }
        chart.getData().clear();
        chart.getData().add(integratedValuesChartData);
    }

    private void interpolateCenter() {
        Interpolator interpolator = new Interpolator();
        interpolator.setCircleCenterFromUser(centerOfCircle);
        interpolator.setMatrix(matrix);
        interpolator.setCircleRadiusFromUser(centerFindTool.getCircleRadius());
        interpolator.interpolate();
        interpolatedCenter = interpolator.getInterpolatedCenter();
        xInterpolatedCenterCoordinateLabel.setText(String.format("%.1f", interpolatedCenter.getX()));
        yInterpolatedCenterCoordinateLabel.setText(String.format("%.1f", interpolatedCenter.getY()));
        LinkedList<InterpolationBox> boxes = interpolator.getInterpolationBoxes();
        canvasWithZoom.showInterpolateBoxes(boxes);
    }
}

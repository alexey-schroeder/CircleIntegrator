package sample;

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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

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
    private HashMap<Integer, Double> mapRadiusToIntegratedValue;

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
        int width = matrix.numCols();
        int height = matrix.numRows();
        HashMap<Integer, LinkedList<IntegerPoint>> mapRadiusToPoints = new HashMap<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                short value = matrix.get(x, y);
                IntegerPoint point = new IntegerPoint(x, y);
                point.setValue(value);
                int distance = (int) Math.round(interpolatedCenter.distanceToPoint(point));
                LinkedList<IntegerPoint> points = mapRadiusToPoints.get(distance);
                if (points == null) {
                    points = new LinkedList<>();
                    mapRadiusToPoints.put(distance, points);
                }
                points.add(point);
            }
        }

        mapRadiusToIntegratedValue = new HashMap<>();
        for (Integer radius : mapRadiusToPoints.keySet()) {
            LinkedList<IntegerPoint> pointsInRadius = mapRadiusToPoints.get(radius);
            double integratedValue = 0;
            for (IntegerPoint point : pointsInRadius) {
                integratedValue = integratedValue + point.getValue();
            }
            double averageIntegratedValue = integratedValue / pointsInRadius.size();
            mapRadiusToIntegratedValue.put(radius, averageIntegratedValue);
        }
        showChart();
    }

    private void showChart() {
        XYChart.Series integratedValuesChartData = new XYChart.Series();
        integratedValuesChartData.setName("Integrated Values");
        Set<Integer> radiusesSet = mapRadiusToIntegratedValue.keySet();
        LinkedList<Integer>  radiusesList = new LinkedList<>(radiusesSet);
        Collections.sort(radiusesList);
        for(Integer radius :radiusesList){
           double integratedValue = mapRadiusToIntegratedValue.get(radius);
            integratedValuesChartData.getData().add(new XYChart.Data(radius, integratedValue));
        }
        chart.getData().clear();
        chart.getData().add(integratedValuesChartData);
    }

//    public void onCenterIterpolateButtonClick(ActionEvent actionEvent) {
//        Interpolator interpolator = new Interpolator();
//        interpolator.setCircleCenterFromUser(centerOfCircle);
//        interpolator.setMatrix(matrix);
//        interpolator.setCircleRadiusFromUser(centerFindTool.getCircleRadius());
//        interpolator.interpolate();
//        DoublePoint interpolatedCenter = interpolator.getInterpolatedCenter();
//        xInterpolatedCenterCoordinateLabel.setText(String.format("%.1f", interpolatedCenter.getX()));
//        yInterpolatedCenterCoordinateLabel.setText(String.format("%.1f", interpolatedCenter.getY()));
//        LinkedList<InterpolationBox> boxes =  interpolator.getInterpolationBoxes();
//        canvasWithZoom.showInterpolateBoxes(boxes);
//    }

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

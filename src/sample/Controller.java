package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.centerFindTool.CenterFindTool;
import sample.interpolation.Interpolator;
import sample.point.DoublePoint;

import java.io.File;

public class Controller {
    public CanvasWithZoom canvasWithZoom;
    public Label xCenterCoordinateLabel;
    public Label yCenterCoordinateLabel;
    public Label xInterpolatedCenterCoordinateLabel;
    public Label yInterpolatedCenterCoordinateLabel;
    private DoublePoint centerOfCircle;
    private Matrix matrix;
    private CenterFindTool centerFindTool;

    public void initialize() {
        Registry.setCanvasWithZoom(canvasWithZoom);
        canvasWithZoom.setMinCanvasHeight(512);
        canvasWithZoom.setMinCanvasWidth(512);
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

                    xCenterCoordinateLabel.setText(String.format("%.0f", centerOfCircle.getX()));
                    yCenterCoordinateLabel.setText(String.format("%.0f", centerOfCircle.getY()));
                }
            });
            canvasWithZoom.addTool(centerFindTool);
        }
    }

    public void onCenterIterpolateButtonClick(ActionEvent actionEvent) {
        Interpolator interpolator = new Interpolator();
        interpolator.setCircleCenterFromUser(centerOfCircle);
        interpolator.setMatrix(matrix);
        interpolator.setCircleRadiusFromUser(centerFindTool.getCircleRadius());
        interpolator.interpolate();
        DoublePoint interpolatedCenter = interpolator.getInterpolatedCenter();
        xInterpolatedCenterCoordinateLabel.setText(String.format("%.0f", interpolatedCenter.getX()));
        yInterpolatedCenterCoordinateLabel.setText(String.format("%.0f", interpolatedCenter.getY()));
    }
}

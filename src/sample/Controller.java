package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.centerFindTool.CenterFindTool;
import sample.point.DoublePoint;

import java.io.File;

public class Controller {
    public CanvasWithZoom canvasWithZoom;
    public Label xCenterCoordinateLabel;
    public Label yCenterCoordinateLabel;

    public void initialize(){
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
            Matrix matrix = tiffImageReader.readImage(file);
            WritableImage writableImage = Plotter.getFXColoredImage(matrix);
            canvasWithZoom.setAndShowImage(writableImage);
            CenterFindTool centerFindTool = new CenterFindTool();
            centerFindTool.addOnCenterChangeListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object o2) {
                    DoublePoint newCenter = (DoublePoint) o2;
                    xCenterCoordinateLabel.setText(String.format("%.0f", newCenter.getX()));
                    yCenterCoordinateLabel.setText(String.format("%.0f", newCenter.getY()));
                }
            });
            canvasWithZoom.addTool(centerFindTool);

        }
    }
}

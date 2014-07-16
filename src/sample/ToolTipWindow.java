package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 10.10.13
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class ToolTipWindow extends Pane {
    private VBox splashLayout;

    public ToolTipWindow() {
        splashLayout = new VBox();
        splashLayout.setSpacing(3);
        splashLayout.setPadding(new Insets(5, 5, 5, 5));
        getStyleClass().add("toolTipWindow");
        getChildren().add(splashLayout);
    }

    public void setContent(String[] content) {
        splashLayout.getChildren().clear();
        for (String string : content) {
            Label label = new Label(string);
            splashLayout.getChildren().add(label);
        }
    }
}

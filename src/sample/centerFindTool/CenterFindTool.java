package sample.centerFindTool;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import sample.CanvasWithZoom;
import sample.Registry;
import sample.point.DoublePoint;

import java.util.LinkedList;

/**
 * Created by Alex on 08.07.2014.
 */
public class CenterFindTool extends Group {
    private DoublePoint center;
    private Rectangle leftRectangle;
    private DoublePoint realyLeftPoint;
    private Rectangle rightRectangle;
    private DoublePoint realyRightPoint;
    private Rectangle topRectangle;
    private DoublePoint realyTopPoint;
    //    private Rectangle bottomRectangle;
    private Circle circle;
    private double width = 20;
    private double height = 20;
    private double lineWidth = 2;
    private CanvasWithZoom parentPane;
    private LinkedList<ChangeListener<DoublePoint>> onCenterChangeListeners;


    public CenterFindTool() {
        onCenterChangeListeners = new LinkedList<>();
        CanvasWithZoom canvasWithZoom = Registry.getCanvasWithZoom();
        double imageHeight = canvasWithZoom.getImageHeight();
        double imageWidth = canvasWithZoom.getImageWidth();
        DoublePoint leftPoint = canvasWithZoom.getZoomPoint(new DoublePoint(imageWidth / 4 - width / 2, imageHeight / 2 - height / 2));
        leftRectangle = new Rectangle(0, 0, width, height);
        leftRectangle.setTranslateX(leftPoint.getX());
        leftRectangle.setTranslateY(leftPoint.getY());
        leftRectangle.setStrokeWidth(lineWidth);
        leftRectangle.setFill(Color.TRANSPARENT);
        leftRectangle.setStroke(Color.WHITE);
        leftRectangle.setCursor(Cursor.HAND);
        initEdgeListeners(leftRectangle);

        DoublePoint rightPoint = canvasWithZoom.getZoomPoint(new DoublePoint(imageWidth * 3 / 4 - width / 2, imageHeight / 2 - height / 2));
        rightRectangle = new Rectangle(0, 0, width, height);
        rightRectangle.setTranslateX(rightPoint.getX());
        rightRectangle.setTranslateY(rightPoint.getY());
        rightRectangle.setStrokeWidth(lineWidth);
        rightRectangle.setFill(Color.TRANSPARENT);
        rightRectangle.setStroke(Color.WHITE);
        rightRectangle.setCursor(Cursor.HAND);
        initEdgeListeners(rightRectangle);

        DoublePoint topPoint = canvasWithZoom.getZoomPoint(new DoublePoint(imageWidth / 2 - width / 2, imageHeight / 4 - height / 2));
        topRectangle = new Rectangle(0, 0, width, height);
        topRectangle.setTranslateX(topPoint.getX());
        topRectangle.setTranslateY(topPoint.getY());
        topRectangle.setStrokeWidth(lineWidth);
        topRectangle.setFill(Color.TRANSPARENT);
        topRectangle.setStroke(Color.WHITE);
        topRectangle.setCursor(Cursor.HAND);
        initEdgeListeners(topRectangle);

//        IntegerPoint bottomPoint = canvasWithZoom.getZoomPoint(new DoublePoint(imageWidth / 2, imageHeight * 3 / 4));
//        bottomRectangle = new Rectangle(0, 0, width, height);
//        bottomRectangle.setTranslateX(bottomPoint.getX());
//        bottomRectangle.setTranslateY(bottomPoint.getY());

        circle = new Circle();
        circle.setStrokeWidth(lineWidth);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        refreshCircle();

        getChildren().addAll(leftRectangle, rightRectangle, topRectangle, circle);
        leftRectangle.toFront();
        rightRectangle.toFront();
        topRectangle.toFront();
    }

    public void setZoom(double zoom) {
        DoublePoint leftZoomPoint = parentPane.getZoomPoint(realyLeftPoint);
        leftRectangle.setTranslateX(leftZoomPoint.getX() - width / 2);
        leftRectangle.setTranslateY(leftZoomPoint.getY() - height / 2);

        DoublePoint rightZoomPoint = parentPane.getZoomPoint(realyRightPoint);
        rightRectangle.setTranslateX(rightZoomPoint.getX() - width / 2);
        rightRectangle.setTranslateY(rightZoomPoint.getY() - height / 2);

        DoublePoint topZoomPoint = parentPane.getZoomPoint(realyTopPoint);
        topRectangle.setTranslateX(topZoomPoint.getX() - width / 2);
        topRectangle.setTranslateY(topZoomPoint.getY() - height / 2);
//        bottomRectangle.setTranslateX(bottomRectangle.getTranslateX() * zoom);
//        bottomRectangle.setTranslateY(bottomRectangle.getTranslateY() * zoom);

        refreshCircle();
    }

    private void refreshCircle() {
        DoublePoint center = getCircleCenter(getRectangleCenter(leftRectangle), getRectangleCenter(topRectangle), getRectangleCenter(rightRectangle));
        double radius = getRectangleCenter(leftRectangle).distanceToPoint(center);
        circle.setRadius(radius);
        circle.setCenterX(center.getX());
        circle.setCenterY(center.getY());
        onCenterChange();
    }

    private void onCenterChange() {
        if (parentPane != null) {
            DoublePoint center = parentPane.getRealyPoint(new DoublePoint(circle.getCenterX(), circle.getCenterY()));
            for (ChangeListener changeListener : onCenterChangeListeners) {
                changeListener.changed(null, null, center);
            }
        }
    }

    private DoublePoint getRectangleCenter(Rectangle rectangle) {
        double x = rectangle.getTranslateX() + width / 2;
        double y = rectangle.getTranslateY() + height / 2;
        return new DoublePoint(x, y);
    }

    private DoublePoint getRectangleTranslate(Rectangle rectangle) {
        double x = rectangle.getTranslateX();
        double y = rectangle.getTranslateY();
        return new DoublePoint(x, y);
    }

    private DoublePoint getCircleCenter(DoublePoint point1, DoublePoint point2, DoublePoint point3) {
        double mA = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
        double mB = (point3.getY() - point2.getY()) / (point3.getX() - point2.getX());

        double xTop = mA * mB * (point1.getY() - point3.getY()) + mB * (point1.getX() + point2.getX()) - mA * (point2.getX() + point3.getX());
        double xBottom = 2 * (mB - mA);
        double x = xTop / xBottom;

        double y = -1 / mA * (x - (point1.getX() + point2.getX()) / 2) + (point1.getY() + point2.getY()) / 2;
        return new DoublePoint(x, y);
    }

    private void initEdgeListeners(Rectangle edge) {
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty();
        final ObjectProperty<Point2D> lastTranslate = new SimpleObjectProperty();
        edge.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                lastMouseCoordinates.set(new Point2D(event.getScreenX(), event.getScreenY()));
                lastTranslate.set(new Point2D(edge.getTranslateX(), edge.getTranslateY()));
            }
        });

        edge.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                double deltaX = event.getScreenX() - lastMouseCoordinates.get().getX();
                double deltaY = event.getScreenY() - lastMouseCoordinates.get().getY();
                double newEdgCoordinateX = lastTranslate.get().getX() + deltaX;
                double newEdgCoordinateY = lastTranslate.get().getY() + deltaY;
                if (parentPane.isInBound(newEdgCoordinateX, newEdgCoordinateY)
                        && parentPane.isInBound(newEdgCoordinateX + edge.getWidth(), newEdgCoordinateY + edge.getHeight())) {
                    edge.setTranslateX(newEdgCoordinateX);
                    edge.setTranslateY(newEdgCoordinateY);
                    refreshCircle();
                    refreshRealyPoints();
                }
            }
        });

//        edge.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
//            @Override
//            public void handle(MouseDragEvent mouseDragEvent) {
//                sendChangeEvent();
//                refreshRealyPoints();
//            }
//        });
//        edge.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent event) {
//                sendChangeEvent();
//                refreshRealyPoints();
//            }
//        });
    }

    private void sendChangeEvent() {

    }

    private void refreshRealyPoints() {
        realyLeftPoint = parentPane.getRealyPoint(getRectangleCenter(leftRectangle));
        realyRightPoint = parentPane.getRealyPoint(getRectangleCenter(rightRectangle));
        realyTopPoint = parentPane.getRealyPoint(getRectangleCenter(topRectangle));
    }

    public CanvasWithZoom getParentPane() {
        return parentPane;
    }

    public void setParentPane(CanvasWithZoom parentPane) {
        this.parentPane = parentPane;
        refreshRealyPoints();
        onCenterChange();
    }

    public void addOnCenterChangeListener(ChangeListener changeListener) {
        onCenterChangeListeners.add(changeListener);
    }

    public double getCircleRadius(){
        DoublePoint center = parentPane.getRealyPoint(new DoublePoint(circle.getCenterX(), circle.getCenterY()));
        return realyLeftPoint.distanceToPoint(center);
    }
}

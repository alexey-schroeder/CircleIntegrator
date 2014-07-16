package sample;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sample.centerFindTool.CenterFindTool;
import sample.point.DoublePoint;
import sample.point.Point;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: schroedera85
 * Date: 09.09.13
 * Time: 08:38
 * To change this template use File | Settings | File Templates.
 */
public class CanvasWithZoom extends Pane {
    private Canvas canvasForImage;
    private WritableImage image;
    private double minCanvasHeight = 512;
    private double minCanvasWidth = 512;
    private double zoomStep = 0.1;
    private ScrollPane scroller;
    private HashSet<EventHandler<MouseEvent>> onClickProcessListeners;
    private HashSet<EventHandler<MouseEvent>> onClickMeasureToolsListeners;
    private HashSet<EventHandler<MouseEvent>> onMouseMovedMeasureToolsListeners;
    private HashSet<EventHandler<MouseEvent>> onLineDrawListeners;
    private HashSet<EventHandler<MouseEvent>> onRechtangleDrawListeners;
    private HashSet<EventHandler<MouseEvent>> onImageChangeListeners;
    private CenterFindTool centerFindTool;
    private ToolTipWindow toolTipWindow;
    private Group contentGroup;
    private LinkedList<EventHandler<ScrollEvent>> scrollListeners;


    public CanvasWithZoom() {
        this(null);
    }

    public CanvasWithZoom(Image image) {
        scrollListeners = new LinkedList<>();
        onClickMeasureToolsListeners = new HashSet<>();
        onMouseMovedMeasureToolsListeners = new HashSet<>();
        onClickProcessListeners = new HashSet<>();
        onLineDrawListeners = new HashSet<>();
        onRechtangleDrawListeners = new HashSet<>();
        onImageChangeListeners = new HashSet<>();

        canvasForImage = new Canvas();
        canvasForImage.setWidth(minCanvasWidth);
        canvasForImage.setHeight(minCanvasHeight);

        if (image != null) {
            canvasForImage.getGraphicsContext2D().drawImage(image, 0, 0, canvasForImage.getWidth(), canvasForImage.getHeight());
        }
        contentGroup = new Group(canvasForImage);
        Parent zoomPane = createZoomPane(contentGroup);
        getChildren().add(zoomPane);
        setLayoutX(0);
        setLayoutY(0);
    }

    public boolean isInBound(double x, double y) {
        return x >= 0 && canvasForImage.getWidth() >= x && y >= 0 && canvasForImage.getHeight() >= y;
    }

    public void setAndShowImage(WritableImage image) {
        this.image = image;
        if (image != null) {
            canvasForImage.getGraphicsContext2D().drawImage(image, 0, 0, canvasForImage.getWidth(), canvasForImage.getHeight());
        } else {
            canvasForImage.getGraphicsContext2D().clearRect(0, 0, canvasForImage.getWidth(), canvasForImage.getHeight());
        }
        Event event = new Event(EventType.ROOT);
        for (EventHandler eventHandler : onImageChangeListeners) {
            eventHandler.handle(event);
        }
    }

    public void setImage(WritableImage image) {
        this.image = image;
    }

    public void showImage() {
        showImage(image);
    }

    public void showImage(WritableImage image) {
        if (image != null) {
            canvasForImage.getGraphicsContext2D().drawImage(image, 0, 0, canvasForImage.getWidth(), canvasForImage.getHeight());
        } else {
            canvasForImage.getGraphicsContext2D().clearRect(0, 0, canvasForImage.getWidth(), canvasForImage.getHeight());
        }
    }

    //todo ist sehr änlicht der methode getContentWidth/Height. diese methode muss wahrscheinnlich weg
    public double getMinCanvasHeight() {
        return minCanvasHeight;
    }

    public void setMinCanvasHeight(double minHeight) {
        this.minCanvasHeight = minHeight;
        canvasForImage.setHeight(minHeight);
        scroller.setPrefViewportHeight(minHeight);
    }

    public double getMinCanvasWidth() {
        return minCanvasWidth;
    }

    public void setMinCanvasWidth(double minWidth) {
        this.minCanvasWidth = minWidth;
        canvasForImage.setWidth(minWidth);
        scroller.setPrefViewportWidth(minWidth);
    }

    public double getZoomStep() {
        return zoomStep;
    }

    public void setZoomStep(double zoomStep) {
        this.zoomStep = zoomStep;
    }

    public void showToolTip(String[] tooltipContent, double x, double y) {
        if (toolTipWindow != null) {
            contentGroup.getChildren().remove(toolTipWindow);
            toolTipWindow = null;
        }
        toolTipWindow = new ToolTipWindow();
        toolTipWindow.setContent(tooltipContent);

        contentGroup.getChildren().add(toolTipWindow);
        toolTipWindow.setTranslateX(x);
        toolTipWindow.setTranslateY(y);
        toolTipWindow.toFront();
    }

    private Parent createZoomPane(final Group group) {
        final double SCALE_DELTA = 1 + zoomStep;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);

        scroller = new ScrollPane();
        final Pane scrollContent = new Pane();
        scrollContent.getChildren().add(zoomPane);

        scroller.setContent(scrollContent);
        scroller.setPrefViewportWidth(minCanvasWidth);
        scroller.setPrefViewportHeight(minCanvasHeight);

        zoomPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hideTooltipWindow();
                for (EventHandler eventHandler : onMouseMovedMeasureToolsListeners) {
                    eventHandler.handle(event);
                }
            }
        });

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                event.consume();
                for (EventHandler eventHandler : scrollListeners) {
                    eventHandler.handle(event);
                }
                if (image != null) {
                    if (event.getDeltaY() > 0) {
                        canvasForImage.setHeight(canvasForImage.getHeight() * (1 + zoomStep));
                        canvasForImage.setWidth(canvasForImage.getWidth() * (1 + zoomStep));
                        centerFindTool.setZoom(1 + zoomStep);
                    } else {
                        double newHeight = canvasForImage.getHeight() * (1 - zoomStep);
                        double newWidth = canvasForImage.getWidth() * (1 - zoomStep);
                        if (newHeight < minCanvasHeight) {
                            newHeight = minCanvasHeight;
                        }
                        if (newWidth < minCanvasWidth) {
                            newWidth = minCanvasWidth;
                        }

                        double zoom = newHeight / canvasForImage.getHeight();
                        canvasForImage.setHeight(newHeight);
                        canvasForImage.setWidth(newWidth);
                        centerFindTool.setZoom(zoom);
                    }
                    canvasForImage.getGraphicsContext2D().drawImage(image, 0, 0, canvasForImage.getWidth(), canvasForImage.getHeight());

                    double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
                            : 1 / SCALE_DELTA;
//
//                // amount of scrolling in each direction in scrollContent coordinate
//                // units
                    Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);
//

                    // move viewport so that old center remains in the center after the
                    // scaling
                    repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);
                }
            }
        });

        // Panning via drag....
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
            }
        });

        scrollContent.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    double deltaX = Math.abs(event.getX() - lastMouseCoordinates.get().getX());
                    double deltaY = Math.abs(event.getY() - lastMouseCoordinates.get().getY());
                    double maxDelta = Math.max(deltaX, deltaY);
                    if (maxDelta <= 1) {
                        for (EventHandler eventHandler : onClickMeasureToolsListeners) {
                            eventHandler.handle(event);
                        }
                        for (EventHandler eventHandler : onClickProcessListeners) {
                            eventHandler.handle(event);
                        }
                        event.consume();
                    }
                }
            }
        });

        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (image != null) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        double deltaX = event.getX() - lastMouseCoordinates.get().getX();
                        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
                        double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
                        double desiredH = scroller.getHvalue() - deltaH;
                        scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

                        double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
                        double deltaV = deltaY * (scroller.getHmax() - scroller.getHmin()) / extraHeight;
                        double desiredV = scroller.getVvalue() - deltaV;
                        scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        if (event.isControlDown()) {
                            for (EventHandler eventHandler : onRechtangleDrawListeners) {
                                eventHandler.handle(event);
                            }
                            event.consume();
                        } else {
                            for (EventHandler eventHandler : onLineDrawListeners) {
                                eventHandler.handle(event);
                            }
                            event.consume();
                        }
                    }
                }
            }
        });

        return scroller;
    }

    private Point2D figureScrollOffset(Pane scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    public void addOnClickMeasureToolsListener(EventHandler<MouseEvent> listener) {
        onClickMeasureToolsListeners.add(listener);
    }

    public void clearOnClickMeasureToolsListeners() {
        onClickMeasureToolsListeners.clear();
    }

    public void addOnMouseMovedMeasureToolsListener(EventHandler<MouseEvent> listener) {
        onMouseMovedMeasureToolsListeners.add(listener);
    }

    public void clearOnMouseMovedMeasureToolsListeners() {
        onMouseMovedMeasureToolsListeners.clear();
    }

    public void addOnClickProcessListener(EventHandler<MouseEvent> listener) {
        onClickProcessListeners.add(listener);
    }

    public void addOnLineDrawListener(EventHandler<MouseEvent> listener) {
        onLineDrawListeners.add(listener);
    }

    public void addOnRechtangleDrawListener(EventHandler<MouseEvent> listener) {
        onRechtangleDrawListeners.add(listener);
    }

    public void clearOnClickProcessListeners() {
        onClickProcessListeners.clear();
    }

    public void clearOnLineDrawListeners() {
        onLineDrawListeners.clear();
    }

    public void clearOnRechtangleDrawListeners() {
        onRechtangleDrawListeners.clear();
    }

    public void clearAllListeners() {
        clearOnClickProcessListeners();
        clearOnLineDrawListeners();
        clearOnRechtangleDrawListeners();
        clearScrollListeners();
    }

    public double getContentWidth() {
        return canvasForImage.getWidth();
    }

    public double getContentHeight() {
        return canvasForImage.getHeight();
    }

    private void hideTooltipWindow() {
        if (toolTipWindow != null) {
            contentGroup.getChildren().remove(toolTipWindow);
        }
        toolTipWindow = null;
    }

    public void addScrollListener(EventHandler<ScrollEvent> listener) {
        scrollListeners.add(listener);
    }

    public void removeScrollListener(EventHandler<ScrollEvent> listener) {
        scrollListeners.remove(listener);
    }

    public void clearScrollListeners() {
        scrollListeners.clear();
    }

    public WritableImage getImage() {
        return image;
    }

    public void addOnImageChangeListener(EventHandler listener) {
        onImageChangeListeners.add(listener);
    }

    public void removeOnImageChangeListener(EventHandler listener) {
        onImageChangeListeners.remove(listener);
    }

    public void clearOnImageChangeListeners() {
        onImageChangeListeners.clear();
    }

    public DoublePoint getRealyPoint(Point point) {
        if (image != null) {
            double realyWidth = image.getWidth();
            double contentWidth = getContentWidth();
            double scaleFactor = realyWidth / contentWidth;
            double realyEventX = point.getX().doubleValue() * scaleFactor;
            double realyEventY = point.getY().doubleValue() * scaleFactor;
            return new DoublePoint(realyEventX, realyEventY);
        }
        return null;
    }

    public DoublePoint getZoomPoint(Point point) {
        if (image != null) {
            double realyWidth = image.getWidth();
            double contentWidth = getContentWidth();
            double scaleFactor = realyWidth / contentWidth;
            double realyEventX = point.getX().doubleValue() / scaleFactor;
            double realyEventY = point.getY().doubleValue() / scaleFactor;
            return new DoublePoint(realyEventX, realyEventY);
        }
        return null;
    }

    public WritableImage createScreenshot() {
        return contentGroup.snapshot(new SnapshotParameters(), null);
    }

    public double getImageWidth() {
        if (image != null) {
            return image.getWidth();
        } else {
            return -1;
        }
    }

    public double getImageHeight() {
        if (image != null) {
            return image.getHeight();
        } else {
            return -1;
        }
    }

    public void addTool(CenterFindTool centerFindTool) {
        this.centerFindTool = centerFindTool;
        centerFindTool.setParentPane(this);
        contentGroup.getChildren().add(centerFindTool);
        centerFindTool.toFront();
    }
}

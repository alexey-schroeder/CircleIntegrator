package sample;

/**
 * Created by Alex on 08.07.2014.
 */
public class Registry {

    private static CanvasWithZoom canvasWithZoom;

    public static CanvasWithZoom getCanvasWithZoom() {
        return canvasWithZoom;
    }

    public static void setCanvasWithZoom(CanvasWithZoom canvasWithZoom) {
        Registry.canvasWithZoom = canvasWithZoom;
    }
}

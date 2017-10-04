package stateconfiguration.stateXML.dragSystem;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author s_stanis
 */
public interface Dragable {

    void setX(double x);

    void setY(double y);

    double getX();

    double getY();

    double getHeight();

    double getWidth();

    void dragged();

    void unDragged();

    void render(GraphicsContext gc);

}

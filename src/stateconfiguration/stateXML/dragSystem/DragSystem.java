package stateconfiguration.stateXML.dragSystem;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author s_stanis
 */
public class DragSystem {

    ObservableList<Dragable> items;
    double initialX = 0, initialY = 0;
    Dragable dragged = null;
    GraphicsContext gc;
    Consumer<Object> reRender;
    boolean enable;

    public DragSystem(GraphicsContext gc, Consumer<Object> reRender) {
        items = FXCollections.observableArrayList();
        this.reRender = reRender;
        this.gc = gc;
    }

    public void setItems(ObservableList<Dragable> items) {
        this.items = items;
    }

    public void addItem(Dragable item) {
        items.add(item);
    }

    public void removeItem(Dragable item) {
        items.remove(item);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void pressEvent(double x, double y) {
        if (dragged != null) {
            return;
        }
        for (Dragable item : items) {
            if (Math.abs(item.getX() - x) <= item.getWidth() / 2 && Math.abs(item.getY() - y) <= item.getHeight()/ 2) {
           
                initialX = (int) x - item.getX();
                initialY = (int) y - item.getY();
                dragged = item;

                item.dragged();
                reRender.accept(null);
                break;
            }
        }
    }

    public void releaseEvent(double x, double y) {
        if (dragged != null) {
            dragged.unDragged();
            dragged.render(gc);
            dragged = null;

            reRender.accept(null);
        }
    }

    public void dragEvent(double x, double y) {
        if (dragged != null) {
            dragged.setX(x - initialX);
            dragged.setY(y - initialY);
            reRender.accept(null);
        }

    }

    public boolean isDragging() {
        return dragged != null;
    }

    public Dragable getDragged() {
        return dragged;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML.Structures.linkSystem;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author s_stanis
 * @param <F>
 */
public class LinkSystem<F extends Linkable> {

    ArrayList<F> items;
    F first, second;
    GraphicsContext gc;
    Consumer<Object> reRender;
    BiConsumer<F, F> newConnection;
    double lastX, lastY;

    public LinkSystem(GraphicsContext gc, Consumer<Object> reRender, BiConsumer<F, F> newConnection) {
        items = new ArrayList<>();
        this.reRender = reRender;
        this.newConnection = newConnection;
        this.gc = gc;
    }

    public void addItem(F item) {
        items.add(item);
    }

    public void removeItem(F item) {
        items.remove(item);
    }

    public void pressEvent(double x, double y) {
        if (first != null) {
            return;
        }
        for (F item : items) {
            if (Math.abs(item.getX() - x) <= item.getWidth() / 2 && Math.abs(item.getY() - y) <= item.getHeight() / 2) {
                first = item;
                first.selected();
                lastX = first.getX();
                lastY = first.getY();
                reRender.accept(null);
                renderCurrentConnection();
                second = null;
                break;
            }

        }
    }

    public void releaseEvent(double x, double y) {
        if (first != null) {
            if (second != null) {
                second.unselect();
                newConnection.accept(first, second);
            }

            first.unselect();
            first = null;
            reRender.accept(null);

        }

    }

    public void dragEvent(double x, double y) {
        if (first != null) {
            lastX = x;
            lastY = y;

            for (F item : items) {
                if (item.equals(first)) {
                    continue;
                }
                if (Math.abs(item.getX() - x) <= item.getWidth() / 2 && Math.abs(item.getY() - y) <= item.getHeight() / 2) {
                    second = item;
                    second.selected();
                    lastX = second.getX();
                    lastY = second.getY();
                    break;
                }
                if (second != null) {
                    second.unselect();
                    second = null;
                }
            }

            reRender.accept(null);
        }
    }

    public void renderCurrentConnection() {
        if (first != null) {
            gc.setStroke(javafx.scene.paint.Color.BLACK);
            gc.strokeLine(first.getX(), first.getY(), lastX, lastY);
        }

    }

}

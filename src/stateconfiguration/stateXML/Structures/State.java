/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML.Structures;

import java.util.LinkedList;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import stateconfiguration.stateXML.Structures.linkSystem.Linkable;
import stateconfiguration.stateXML.dragSystem.Dragable;

/**
 *
 * @author s_stanis
 */
public class State implements Dragable, Linkable {

    private final SimpleStringProperty name;
    private final SimpleDoubleProperty xPos;
    private final SimpleDoubleProperty yPos;
    private final ObservableMap<String, SimpleObjectProperty<String>> values;
    private final ObservableList<Link> links;
    private final SimpleBooleanProperty focused;
    private final SimpleObjectProperty<Color> color;
    private final long id;

    public State(String name, double x, double y, long id) {
        this.name = new SimpleStringProperty(name);
        this.xPos = new SimpleDoubleProperty(x);
        this.yPos = new SimpleDoubleProperty(y);
        this.focused = new SimpleBooleanProperty(false);
        this.id = id;
        color = new SimpleObjectProperty<>();
        color.bind(Bindings.when(focused).then(Color.CYAN).otherwise(Color.LIGHTCYAN));

        values = FXCollections.observableHashMap();
        links = FXCollections.observableList(new LinkedList<Link>());
    }

    public ObservableList<Link> getLinks() {
        return links;
    }

    public SimpleStringProperty getNameProperty() {
        return name;
    }

    public ObservableMap<String, SimpleObjectProperty<String>> getValues() {
        return values;
    }

    public SimpleDoubleProperty getXPosProperty() {
        return xPos;
    }

    public SimpleDoubleProperty getYPosProperty() {
        return yPos;
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return ((State) obj).id == this.id;
    }

    public SimpleObjectProperty<Color> getColorProperty() {
        return color;
    }

    public SimpleBooleanProperty getFocusedProperty() {
        return focused;
    }

    @Override
    public void setX(double x) {
        xPos.set(x);
    }

    @Override
    public void setY(double y) {
        yPos.set(y);
    }

    @Override
    public double getX() {
        return xPos.get();
    }

    @Override
    public double getY() {
        return yPos.get();
    }

    @Override
    public double getHeight() {
        return 50;
    }

    @Override
    public double getWidth() {
        return 50;
    }

    @Override
    public void dragged() {
        focused.set(true);
    }

    @Override
    public void unDragged() {
        focused.set(false);
    }

    @Override
    public void render(GraphicsContext gcWorkingPane) {
        double x = this.getXPosProperty().doubleValue();
        double y = this.getYPosProperty().doubleValue();

        gcWorkingPane.setFill(this.getColorProperty().get());
        gcWorkingPane.fillOval(x - getWidth() / 2, y - getHeight() / 2, getHeight(), getHeight());
        gcWorkingPane.setStroke(Color.BLACK);
        gcWorkingPane.strokeOval(x - getWidth() / 2, y - getHeight() / 2, getHeight(), getHeight());

        gcWorkingPane.setFill(Color.BLACK);
        gcWorkingPane.fillText(this.toString(), 5 + x - getWidth() / 2, y);
    }

    @Override
    public void selected() {
        focused.set(true);
    }

    @Override
    public void unselect() {
        focused.set(false);
    }

}

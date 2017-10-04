/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.config;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author s_stanis
 */
public class Field {

    public enum ValueType {

        Real,
        Text,
        Integer
    }

    private SimpleStringProperty name;
    private ValueType type;

    public Field(String name, ValueType type) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
    }

    public SimpleStringProperty getNameProperty() {
        return name;
    }

    public ValueType getType() {
        return type;
    }

}

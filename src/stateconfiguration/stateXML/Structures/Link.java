/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML.Structures;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 *
 * @author s_stanis
 */
public class Link {

    private final State from;
    private final State to;
    private final ObservableMap<String, String> values;

    public Link(State from, State to) {
        this.from = from;
        this.to = to;
        this.values = FXCollections.observableHashMap();
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    public ObservableMap<String, String> getValues() {
        return values;
    }

}

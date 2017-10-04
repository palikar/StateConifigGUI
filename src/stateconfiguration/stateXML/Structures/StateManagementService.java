/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML.Structures;

import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import stateconfiguration.config.ConfigurationService;
import stateconfiguration.config.Field;

/**
 *
 * @author s_stanis
 */
public class StateManagementService {

    private final ObservableList<State> states;
    private final SimpleObjectProperty<State> focusedState;
    private final SimpleObjectProperty<State> selectedState;
    private int id = 0;
    private ConfigurationService config;

    public StateManagementService(ConfigurationService config) {
        focusedState = new SimpleObjectProperty<>(null);
        selectedState = new SimpleObjectProperty<>(null);
        states = FXCollections.observableArrayList();
        this.config = config;
    }

    public void removeState(State state) {
        for (State state_con : states) {
            state_con.getLinks().removeAll(state_con.getLinks().stream().filter(link -> link.getTo().equals(state)).collect(Collectors.toList()));
        }
        states.remove(state);
    }

    public void addState(State state) {
        for (Field field : config.getStatesFields()) {
            state.getValues().put(field.getNameProperty().get(), new SimpleObjectProperty<>(""));
        }
        states.add(state);
    }

    public ObservableList<State> getStates() {
        return states;
    }

    public int getUnusedNumber() {
        return id++;
    }

    public SimpleObjectProperty<State> getFocusedState() {
        return focusedState;
    }

    public SimpleObjectProperty<State> getSelectedState() {
        return selectedState;
    }

}

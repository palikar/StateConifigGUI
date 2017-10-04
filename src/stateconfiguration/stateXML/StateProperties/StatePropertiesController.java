/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML.StateProperties;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import stateconfiguration.config.ConfigurationService;
import stateconfiguration.config.Field;
import stateconfiguration.stateXML.Structures.State;
import stateconfiguration.stateXML.Structures.StateManagementService;

/**
 * FXML Controller class
 *
 * @author s_stanis
 */
public class StatePropertiesController implements Initializable {

    StateManagementService stateManagement = stateconfiguration.StateConfiguration.mainModule.getStateManagmentService();
    SimpleObjectProperty<State> selectedState = stateManagement.getSelectedState();
    ConfigurationService config = stateconfiguration.StateConfiguration.mainModule.getConfiguration();

    @FXML
    TextField stateNameField;
    @FXML
    VBox stateFields;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        selectedState.addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> {
            if (newValue != null) {
                if (oldValue != null) {
                    stateNameField.textProperty().unbindBidirectional(oldValue.getNameProperty());
                    stateNameField.setText("");
                }
                stateNameField.textProperty().bindBidirectional(newValue.getNameProperty());

            } else {
                stateNameField.textProperty().unbindBidirectional(oldValue.getNameProperty());
                stateNameField.setText("");

            }
        });

        stateNameField.disableProperty().bind(Bindings.when(Bindings.isNull(selectedState)).then(true).otherwise(false));
        buildFields();
        config.getStatesFields().addListener(new ListChangeListener<Field>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends Field> c) {
                stateFields.getChildren().remove(2, stateFields.getChildren().size());
                buildFields();
            }
        });
    }

    private void buildFields() {

        for (Field statesField : config.getStatesFields()) {
            HBox fieldBox = new HBox(10);
            fieldBox.setPadding(new Insets(0, 0, 0, 20));
            Label name = new Label("");
            name.setFont(new Font("Arial", 16));
            name.textProperty().bind(statesField.getNameProperty());
            final TextField field = new TextField();
            field.setFont(new Font("Arial", 16));
            switch (statesField.getType()) {
                case Integer:
                    field.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d*")) {
                                field.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        }
                    });
                    break;
                case Real:
                    break;
                case Text:
                    break;
                default:
                    break;
            }
            selectedState.addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> {
                if (newValue != null) {
                    if (oldValue != null) {
                        field.textProperty().unbindBidirectional(oldValue.getValues().get(statesField.getNameProperty().get()));
                        field.setText("");
                    }
                    field.textProperty().bindBidirectional(newValue.getValues().get(statesField.getNameProperty().get()));

                } else {
                    field.textProperty().unbindBidirectional(oldValue.getValues().get(statesField.getNameProperty().get()));
                    field.setText("");

                }
            });

            field.disableProperty().bind(Bindings.when(Bindings.isNull(selectedState)).then(true).otherwise(false));
            fieldBox.getChildren().addAll(name, field);
            stateFields.getChildren().add(fieldBox);

        }
    }

}

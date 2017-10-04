/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.config;

import java.io.File;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author s_stanis
 */
public class ConfigurationService {

    ObservableList<Field> statesFields;
    ObservableList<Field> linksFields;

    public ConfigurationService() {
        statesFields = FXCollections.observableList(new LinkedList<Field>());
        linksFields = FXCollections.observableList(new LinkedList<Field>());
        
        
        statesFields.add(new Field("Threshold1", Field.ValueType.Real));
        statesFields.add(new Field("Threshold2", Field.ValueType.Real));
        statesFields.add(new Field("Threshold3", Field.ValueType.Real));
        statesFields.add(new Field("Threshold4", Field.ValueType.Real));
        statesFields.add(new Field("Threshold5", Field.ValueType.Text));
        statesFields.add(new Field("Threshold6", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold7", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold8", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold9", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold0", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold11", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold12", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold13", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold14", Field.ValueType.Integer));
        statesFields.add(new Field("Threshold15", Field.ValueType.Integer));
        
        
        
    }

    public ObservableList<Field> getStatesFields() {
        return statesFields;
    }

    public boolean saveToFile(File file) {

        return true;
    }

    public boolean loadFromFile(File file) {

        return true;
    }

}

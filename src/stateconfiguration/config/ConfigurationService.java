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

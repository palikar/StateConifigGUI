/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration;

import com.vanillasource.jaywire.standalone.StandaloneModule;
import stateconfiguration.config.ConfigurationService;
import stateconfiguration.stateXML.Structures.StateManagementService;

/**
 *
 * @author s_stanis
 */
public class MainModule extends StandaloneModule {

    public ConfigurationService getConfiguration() {
        return singleton(() -> new ConfigurationService());
    }

    public StateManagementService getStateManagmentService() {
        return singleton(() -> new StateManagementService(getConfiguration()));
    }

    public GlobalSettingsService getGlobalSettingsService() {
        return singleton(() -> new GlobalSettingsService());
    }

}

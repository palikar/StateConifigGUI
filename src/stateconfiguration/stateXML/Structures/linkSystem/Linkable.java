/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML.Structures.linkSystem;

/**
 *
 * @author s_stanis
 */
public interface Linkable {

    double getX();

    double getY();


    double getHeight();

    double getWidth();

    public void selected();

    public void unselect();

}

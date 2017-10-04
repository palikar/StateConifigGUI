/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateconfiguration.stateXML;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jfxtras.scene.menu.CirclePopupMenu;
import stateconfiguration.stateXML.Structures.Link;
import stateconfiguration.stateXML.Structures.StateManagementService;
import stateconfiguration.stateXML.Structures.State;
import stateconfiguration.stateXML.Structures.linkSystem.LinkSystem;
import stateconfiguration.stateXML.dragSystem.DragSystem;

/**
 * FXML Controller class
 *
 * @author s_stanis
 */
public class StateConfigController implements Initializable {

    private final static ImageView addIcon = new ImageView("stateconfiguration/resources/add.png");
    private final static ImageView removeIcon = new ImageView("stateconfiguration/resources/remove.png");
    private final static ImageView linkIcon = new ImageView("stateconfiguration/resources/link.png");

    @FXML
    Canvas statePane;
    private GraphicsContext gcWorkingPane;
    private double canvasWidth;
    private double canvasHeight;
    @FXML
    private TableView<State> statesListView;
    @FXML
    private TableColumn<State, String> nameColumn;
    private MenuItem add;
    private MenuItem remove;
    private MenuItem connect;
    private CirclePopupMenu pop;

    private final int horizontalLinesCnt = 5;
    private final int verticalLinesCnt = 20;
    double stateCirlceWidth = 50;
    double stateCirlceHeight = 50;

    DragSystem dragging;
    LinkSystem<State> linking;

    private final StateManagementService stateManagementService = stateconfiguration.StateConfiguration.mainModule.getStateManagmentService();
    private SimpleObjectProperty<State> focusedState = stateManagementService.getFocusedState();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvasHeight = statePane.getHeight();
        canvasWidth = statePane.getWidth();
        gcWorkingPane = statePane.getGraphicsContext2D();
        stateManagementService.getStates().addListener((Change<? extends State> c) -> refreshWokringPane());
        statesListView.setItems(stateManagementService.getStates());
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());
        nameColumn.setCellFactory(TextFieldTableCell.<State>forTableColumn());
        statesListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> focusedStateChange(newValue));
        focusedState.addListener((ObservableValue<? extends State> observable, State oldValue, State newValue) -> {
            if (newValue != null) {
                statesListView.getSelectionModel().select(newValue);
                focusedStateChange(newValue);
            }
        });
        generatePopupMenu();
        dragging = new DragSystem(gcWorkingPane, object -> refreshWokringPane());
        stateManagementService.getStates().addListener((Change<? extends State> c) -> {
            c.next();
            if (c.wasRemoved()) {
                c.getRemoved().forEach(obj -> {
                    linking.removeItem(obj);
                    dragging.removeItem(obj);
                });
            } else if (c.wasAdded()) {
                c.getAddedSubList().forEach(obj -> {
                    dragging.addItem(obj);
                    linking.addItem(obj);
                });

            }
        });
        linking = new LinkSystem<>(gcWorkingPane, obj -> refreshWokringPane(), (obj1, obj2) -> obj1.getLinks().add(new Link(obj1, obj2)));
        drawGrid(gcWorkingPane);
    }

    private double lastClickedX = 0;
    private double lastClickedY = 0;

    @FXML
    private void dragged(MouseEvent event) {
        dragging.dragEvent(event.getX(), event.getY());
        linking.dragEvent(event.getX(), event.getY());
        linking.renderCurrentConnection();
    }

    @FXML
    private void mouseMoved(MouseEvent event) {
        linking.dragEvent(event.getX(), event.getY());
        linking.renderCurrentConnection();
    }

    @FXML
    private void workingPaneReleased(MouseEvent event) {
        dragging.releaseEvent(event.getX(), event.getY());
        linking.releaseEvent(event.getX(), event.getY());
    }

    @FXML
    private void workingPanePressed(MouseEvent event) {
        dragging.pressEvent(event.getX(), event.getY());
        //linking.pressEvent(event.getX(), event.getY());

    }

    @FXML
    private void workingPaneClicked(MouseEvent event) {
        lastClickedX = event.getX();
        lastClickedY = event.getY();

        if (focusedState.get() != null) {
            focusedState.get().getFocusedProperty().set(false);
            focusedState.set(null);
            refreshWokringPane();
        }

        if (!pop.isShown() && event.getButton() == MouseButton.SECONDARY) {
            pop.show(event);
            for (State state : stateManagementService.getStates()) {
                double stateX = state.getXPosProperty().get();
                double stateY = state.getYPosProperty().get();
                if (Math.abs(stateX - lastClickedX) <= stateCirlceWidth / 2 && Math.abs(stateY - lastClickedY) <= stateCirlceHeight / 2) {
                    focusedState.set(state);
                    focusedState.get().getFocusedProperty().set(true);
                    refreshWokringPane();
                    break;
                }
            }
        } else {

            pop.hide();
        }

    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gcWorkingPane.strokeRect(-gcWorkingPane.getTransform().getTx(), -gcWorkingPane.getTransform().getTy(), canvasWidth, canvasHeight);
        gc.setStroke(Color.LIGHTGRAY.desaturate());

        double horizontalLinesOffSet = -gcWorkingPane.getTransform().getTy();

        double verticalLinesOffSet = -gcWorkingPane.getTransform().getTx();
        //vertical lines
        for (int i = 0; i < horizontalLinesCnt; i++) {
            gc.strokeLine(
                    -gcWorkingPane.getTransform().getTx(),
                    horizontalLinesOffSet + i * (canvasHeight / horizontalLinesCnt),
                    canvasWidth + -gcWorkingPane.getTransform().getTx(),
                    horizontalLinesOffSet + i * (canvasHeight / horizontalLinesCnt));
        }
        //horizontal lines
        for (int i = 0; i < verticalLinesCnt; i++) {
            gc.strokeLine(
                    verticalLinesOffSet + i * (canvasWidth / verticalLinesCnt),
                    -gcWorkingPane.getTransform().getTy(),
                    verticalLinesOffSet + i * (canvasWidth / verticalLinesCnt),
                    canvasHeight + gcWorkingPane.getTransform().getTy());
        }
    }

    private void drawStates() {

        for (State state : stateManagementService.getStates()) {
            state.render(gcWorkingPane);
        }
    }

    private void drawLinks() {
        gcWorkingPane.setStroke(Color.BLACK);
        double d, x2, y2, L1, x3, y3, x4, y4, thetha, startX, startY, endX, endY;
        for (State state : stateManagementService.getStates()) {
            for (State state_con : state.getLinks().stream().map(link -> link.getTo()).collect(Collectors.toList())) {
                startX = state.getX();
                startY = state.getY();
                endX = state_con.getX();
                endY = state_con.getY();
                d = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
                thetha = Math.asin((endY - startY) / (d));
                x2 = Math.signum(endX - startX) * (d - stateCirlceWidth / 2) * Math.cos(thetha) + startX;
                y2 = (d - stateCirlceWidth / 2) * Math.sin(thetha) + startY;
                L1 = Math.sqrt((startX - x2) * (startX - x2) + (startY - y2) * (startY - y2));
                x3 = x2 + ((15) / (L1)) * ((startX - x2) * Math.cos(Math.PI / 4) + (startY - y2) * Math.sin(Math.PI / 4));
                y3 = y2 + ((15) / (L1)) * ((startY - y2) * Math.cos(Math.PI / 4) - (startX - x2) * Math.sin(Math.PI / 4));
                x4 = x2 + ((15) / (L1)) * ((startX - x2) * Math.cos(Math.PI / 4) - (startY - y2) * Math.sin(Math.PI / 4));
                y4 = y2 + ((15) / (L1)) * ((startY - y2) * Math.cos(Math.PI / 4) + (startX - x2) * Math.sin(Math.PI / 4));
                gcWorkingPane.strokeLine(startX, startY, endX, endY);
                gcWorkingPane.strokeLine(x2, y2, x3, y3);
                gcWorkingPane.strokeLine(x2, y2, x4, y4);

            }
        }

    }

    private void refreshWokringPane() {
        gcWorkingPane.clearRect(0 - gcWorkingPane.getTransform().getTx(), 0 - gcWorkingPane.getTransform().getTy(), canvasWidth, canvasHeight);
        drawGrid(gcWorkingPane);
        drawLinks();
        drawStates();
    }

    private void generatePopupMenu() {

        addIcon.setFitWidth(32);
        addIcon.setFitHeight(32);
        removeIcon.setFitWidth(32);
        removeIcon.setFitHeight(32);
        linkIcon.setFitHeight(32);
        linkIcon.setFitWidth(32);
        

        add = new MenuItem("Add state", addIcon);
        add.setOnAction(e -> addState(e));

        remove = new MenuItem("Remove", removeIcon);
        remove.setOnAction(e -> removeState(e));

        connect = new MenuItem("Link", linkIcon);
        connect.setOnAction(e -> startLink(e));

        pop = new CirclePopupMenu(statePane, MouseButton.NONE);
        pop.setAnimationDuration(Duration.millis(100));
        pop.getItems().addAll(remove, add, connect);
    }

    private void addState(ActionEvent e) {
        if (focusedState.get() != null) {
            focusedState.get().getFocusedProperty().set(false);
            focusedState.set(null);
        }
        State newState = new State("State", lastClickedX, lastClickedY, stateManagementService.getUnusedNumber());
        newState.getFocusedProperty().addListener((obj, oldVal, newVal) -> {
            if (newVal) {
                focusedState.set(newState);
            }
        });
        newState.getNameProperty().addListener((s, u, b) -> refreshWokringPane());
        stateManagementService.addState(newState);

    }

    private void removeState(ActionEvent e) {
        stateManagementService.removeState(focusedState.get());

    }

    private void startLink(ActionEvent e) {
        if (focusedState.get() != null) {
            linking.pressEvent(focusedState.get().getX(), focusedState.get().getY());
        }

    }

    private void focusedStateChange(State newValue) {
        if (newValue == null) {
            return;
        }
        stateManagementService.getSelectedState().set(newValue);

    }

    @FXML
    private void clearStates() {
        focusedState.set(null);
        stateManagementService.getSelectedState().set(null);
        stateManagementService.getStates().clear();
    }

    @FXML
    private void deleteState() {
        int i = statesListView.getSelectionModel().getSelectedIndex();
        if (i != -1) {
            stateManagementService.removeState(stateManagementService.getStates().get(i));
        }
    }

}

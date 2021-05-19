package pl.klaudia.mp3player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuPaneController {

    @FXML
    private MenuItem fileMenuItem;

    @FXML
    private MenuItem dirMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem aboutMenuItem;


    public MenuItem getFileMenuItem() {
        return fileMenuItem;
    }

    public MenuItem getDirMenuItem() {
        return dirMenuItem;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }
}

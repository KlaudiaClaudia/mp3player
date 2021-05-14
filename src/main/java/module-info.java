module mp3player {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports pl.klaudia.mp3player.main to javafx.graphics;
    opens pl.klaudia.mp3player.controller to javafx.fxml;
}
module mp3player {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires jid3lib;
    requires javafx.media;

    exports pl.klaudia.mp3player.main to javafx.graphics;
    opens pl.klaudia.mp3player.controller to javafx.fxml;
    opens pl.klaudia.mp3player.mp3 to javafx.base;
}
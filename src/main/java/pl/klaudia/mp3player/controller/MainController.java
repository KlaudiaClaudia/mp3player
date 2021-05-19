package pl.klaudia.mp3player.controller;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import pl.klaudia.mp3player.mp3.Mp3Parser;
import pl.klaudia.mp3player.mp3.Mp3Song;
import pl.klaudia.mp3player.player.Mp3Player;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private ContentPaneController contentPaneController;
    @FXML
    private ControlPaneController controlPaneController;
    @FXML
    private MenuPaneController menuPaneController;

    private Mp3Player player;


    public void initialize() {
        createPlayer();
        configureTableClick();
        configureButtons();
        configureMenu();
    }


    //configuration of control buttons
    private void createPlayer() {
        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
        player = new Mp3Player(items);
    }
    //configuration of click events for songs
    private void configureTableClick() {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        contentTable.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->{
            if (event.getClickCount()==1){
                // returns the index of the row someone clicked on
                int selectedIndex = contentTable.getSelectionModel().getSelectedIndex();
                playSelectedSong(selectedIndex);
            }
        });
    }
    private void playSelectedSong(int selectedIndex){
        player.loadSong(selectedIndex);
        configureProgressBar();
        configureVolume();
        controlPaneController.getPlayButton().setSelected(true);
    }

    private void configureProgressBar(){
        Slider progressSlider = controlPaneController.getProgressSlider();
        //set max value of ProgressSlider a value equal to the number of seconds
        player.getMediaPlayer().setOnReady(() -> progressSlider.setMax(player.getLoadedSongLength()));
        // time change in player will automatically update slider
        player.getMediaPlayer().currentTimeProperty().addListener((arg,oldVal,newVal)->{
            progressSlider.setValue(newVal.toSeconds());
        });
        // moving the slider will scroll the song to the indicated place
        progressSlider.valueProperty().addListener((observable,odlValue,newValue)-> {
            if (progressSlider.isValueChanging()){
                player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));
            }
        });
    }
    // as moving volumeSlider, the volume of the song being played will change
    private void configureVolume(){
        Slider volumeSlider = controlPaneController.getVolumeSlider();
        volumeSlider.valueProperty().unbind();
        volumeSlider.setMax(1.0);
        volumeSlider.valueProperty().bindBidirectional(player.getMediaPlayer().volumeProperty());
    }
    // configuration of play, next and previous buttons
    private void configureButtons(){
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        ToggleButton playButton = controlPaneController.getPlayButton();
        Button prevButton = controlPaneController.getPreviousButton();
        Button nextButton = controlPaneController.getNextButton();

        playButton.setOnAction(event -> {
            if (playButton.isSelected()) {
                player.play();
            } else {
                player.stop();
            }
        });
        nextButton.setOnAction(event -> {
            contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1);
            playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
        });

        prevButton.setOnAction(event -> {
            contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() - 1);
            playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
        });
    }
    // loading and adding a test track to the playlist, method copied from ContentPaneController

    private void configureMenu(){
        MenuItem openFile = menuPaneController.getFileMenuItem();
        MenuItem openDir = menuPaneController.getDirMenuItem();

        openFile.setOnAction(event ->{
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3","*mp3"));
            File file = fc.showOpenDialog(new Stage());
            try{
                contentPaneController.getContentTable().getItems().add(Mp3Parser.createMp3Song(file));
            }catch (Exception e){
                e.printStackTrace();
            }

        });
        openDir.setOnAction(event ->{
            DirectoryChooser dc = new DirectoryChooser();
            File dir = dc.showDialog(new Stage());
            try{
                contentPaneController.getContentTable().getItems().addAll(Mp3Parser.createMp3List(dir));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

}

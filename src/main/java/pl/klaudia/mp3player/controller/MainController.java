package pl.klaudia.mp3player.controller;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
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
        addTestMp3();
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
            if (event.getClickCount()==2){
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
    private void addTestMp3(){
        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
        Mp3Song mp3SongFromPath = createMp3SongFromPath("LittleMIx_SOng.mp3");
        items.add(mp3SongFromPath);
        items.add(mp3SongFromPath);
        items.add(mp3SongFromPath);
        items.add(mp3SongFromPath);
    }

    private Mp3Song createMp3SongFromPath(String filePath) {
        File file = new File(filePath);
        try {
            MP3File mp3File = new MP3File(file);
            String absolutePath = file.getAbsolutePath();
            String title = mp3File.getID3v2Tag().getSongTitle();
            String author = mp3File.getID3v2Tag().getLeadArtist();
            String album = mp3File.getID3v2Tag().getAlbumTitle();
            return new Mp3Song(title, author, album, absolutePath);
        } catch (IOException | TagException e) {
            e.printStackTrace();
            return null;
        }
    }

}

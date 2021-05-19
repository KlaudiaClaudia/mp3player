package pl.klaudia.mp3player.player;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pl.klaudia.mp3player.mp3.Mp3Song;

import java.io.File;

public class Mp3Player {

    private ObservableList <Mp3Song> songList;

    private Media media;
    private MediaPlayer mediaPlayer;

    public Mp3Player(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    public void play(){
        if (mediaPlayer !=null && (mediaPlayer.getStatus() ==MediaPlayer.Status.READY || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)){
            mediaPlayer.play();
        }
    }
    public void stop(){
        if (mediaPlayer!= null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
        }
    }
    public double getLoadedSongLength(){
        if (media!=null){
            return media.getDuration().toSeconds();
        }else {
            return 0;
        }
    }
    public void setVolume(double volume){
        if (mediaPlayer!=null){
            mediaPlayer.setVolume(volume);
        }
    }
    public void loadSong(int index){
       if (mediaPlayer!=null && mediaPlayer.getStatus()== MediaPlayer.Status.PLAYING){
           mediaPlayer.stop();
       }
       Mp3Song mp3s = songList.get(index);
       media = new Media(new File(mp3s.getFilePath()).toURI().toString());
       mediaPlayer = new MediaPlayer(media);
       mediaPlayer.statusProperty().addListener((observable,oldStatus,newStatus) -> {
           if (newStatus == MediaPlayer.Status.PAUSED){
               mediaPlayer.setAutoPlay(true);
           }
        });
    }
}

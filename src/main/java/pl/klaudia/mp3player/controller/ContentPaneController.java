package pl.klaudia.mp3player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.klaudia.mp3player.mp3.Mp3Song;

public class ContentPaneController {

    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String ALBUM_COLUMN = "Album";


    @FXML
    private TableView<Mp3Song> contentTable;

    public TableView<Mp3Song> getContentTable() {
        return contentTable;
    }

    public void initialize(){
        cofigureTableColumns();
    }

    private void cofigureTableColumns() {
        TableColumn<Mp3Song, String> titleColumn = new TableColumn<Mp3Song,String>(TITLE_COLUMN);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Mp3Song, String> authorColumn = new TableColumn<Mp3Song,String>(AUTHOR_COLUMN);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Mp3Song, String> albumColumn = new TableColumn<Mp3Song,String>(ALBUM_COLUMN);
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        contentTable.getColumns().add(titleColumn);
        contentTable.getColumns().add(authorColumn);
        contentTable.getColumns().add(albumColumn);
    }

}

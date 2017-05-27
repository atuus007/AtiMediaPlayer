package hu.abstergo.ati.mediaplayerjava;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
//import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.util.*;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.LoggerFactory;

public class FXMLController implements Initializable {

    @FXML
    private Button btnStop;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnSlow;
    @FXML
    private Button btnRestart;
    @FXML
    private Button btnFast;
    @FXML
    private Slider volumeSlide;
    @FXML
    private Label lbSound;
    @FXML
    private MenuItem mnOpen;
    @FXML
    private Label lbStatus;
    @FXML
    private MediaView mvPlayer;
    @FXML
    private Button btnPause;
    @FXML
    private Slider slTimeSlider;
    @FXML
    private Label timerLabel;
    @FXML
    private MenuBar menuBar;
    @FXML
    private StackPane mediaHolder;
    @FXML
    private Button btnOpenPlaylist;
    @FXML
    private ListView<PlayItem> lvPlayList;

    
   
    private DoubleProperty width, height;
    private MediaPlayer mp;
    private Media me;
    public String path;

    private MediaModell mmModel;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FXMLController.class);
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mmModel = new MediaModell(mvPlayer, slTimeSlider, volumeSlide, timerLabel, mediaHolder, lvPlayList);
        
    }

    @FXML
    private void onStopPlay(ActionEvent event) {
        mmModel.mmStopMediaPlay();
    }

    @FXML
    private void onPlay(ActionEvent event) {
        mmModel.mmPlay();
    }

    @FXML
    private void onPausePlay(ActionEvent event) {
        mmModel.mmPausePlay();
    }

    @FXML
    private void onSlowerPlay(ActionEvent event) {
        mmModel.mmSlowerPlay();
    }

    @FXML
    private void onRestartPlay(ActionEvent event) {
        mmModel.mmRestart();
    }

    @FXML
    private void onFasterPlay(ActionEvent event) {
        mmModel.mmFasterPlay();
    }


    @FXML
    private void openMenuAction(ActionEvent event) {
        
        logger.info("openMenuAction");
        mmModel.openFileChooser();
        
    }

    @FXML
    private void onPlaylistOpen(ActionEvent event) {
        mmModel.mmOpenPlaylist(btnOpenPlaylist, lvPlayList);
    }

    @FXML
    private void handleDropFile(DragEvent event) {

        List<File> files = event.getDragboard().getFiles();
        mmModel.dragAndDrop(files);
    }

    @FXML
    private void handleDragOverFile(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
}

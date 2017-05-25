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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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

    private long timeStamp;
    
    private DoubleProperty width, height;
    private MediaPlayer mp;
    private Media me;
    public String path;
    private FileChooser fc;

    
    private final List<PlayItem> listOfPlayItems = new ArrayList<>();
    private ObservableList<PlayItem> obPlayList;
    private MediaModell mmModel;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FXMLController.class);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mmModel = new MediaModell(mvPlayer, slTimeSlider, volumeSlide, timerLabel, mediaHolder);
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

    private void initialieListeners() {
//        mp.setOnReady(new Runnable() {
//            ObservableMap<String, Object> mediaMetadata = me.getMetadata();
//
//            @Override
//            public void run() {
//                duration = mp.getMedia().getDuration();
//                update();
//                System.out.println("fasdfasdfasdf");
//                listOfPlayItems.add(new PlayItem(getMediaTitle(path), TimeFromatConverter.formatTime(duration), getMediaExtension(path), path));
//
//                obPlayList = FXCollections.observableArrayList(listOfPlayItems);
//                lvPlayList.setItems(obPlayList);
//            }
//        });
//
//        mp.currentTimeProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                update();
//            }
//        });
//        slTimeSlider.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                if (slTimeSlider.isValueChanging()) {
//                    mp.seek(duration.multiply(slTimeSlider.getValue() / 100));
//                }
//            }
//        });
//        volumeSlide.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                if (volumeSlide.isValueChanging()) {
//                    mvPlayer.getMediaPlayer().setVolume(volumeSlide.getValue() / 100);
//                    double volume = mvPlayer.getMediaPlayer().getVolume() * 100;
//                    lbSound.setText(Integer.toString((int) volume));
//                }
//            }
//        });
//
//        mediaHolder.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            System.out.println(mediaHolder.getWidth());
//            if (playlistOpen) {
//                mvPlayer.fitWidthProperty().bind(mediaHolder.widthProperty());
//            }
//        });

    }


    @FXML
    private void openMenuAction(ActionEvent event) {
        
        logger.info("openMenuAction");
        mmModel.openFileChooser();

    }

//    public void update() {
//        if (slTimeSlider != null && timerLabel != null) {
//            Platform.runLater(() -> {
//                Duration jelenlegiIdo = mp.getCurrentTime();
//                double durationToDouble = duration.toMillis();
//                timerLabel.setText(TimeFromatConverter.formatTime(jelenlegiIdo, duration));
//                slTimeSlider.setDisable(duration.isUnknown());
//                if (!slTimeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slTimeSlider.isValueChanging()) {
//                    slTimeSlider.setValue(jelenlegiIdo.divide(durationToDouble).toMillis() * 100.0);
//                }
//            });
//        }
//        mediaHolder.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            System.out.println(mediaHolder.getWidth());
//            if (playlistOpen) {
//                //mediaHolder.setPrefWidth(mediaHolder.getWidth()-lvPlayList.getWidth());
//            }
//        });
//    }

//    public void startMediaPlay(final String path) {
//        if (mvPlayer.getMediaPlayer() != null) {
//            mvPlayer.getMediaPlayer().dispose();
//        }
//        me = new Media(new File(path).toURI().toString());
//        mp = new MediaPlayer(me);
//        mp.setAutoPlay(true);
//        mvPlayer.setMediaPlayer(mp);
//        initialieListeners();
//        
//        mvPlayer.setPreserveRatio(true);
//        mvPlayer.autosize();
//        volumeSlide.setValue(mp.getVolume() * 100);
//        boundMediaView();
//
//    }
    @FXML
    private void onPlaylistOpen(ActionEvent event) {
        mmModel.mmOpenPlaylist(btnOpenPlaylist, lvPlayList);
    }

    @FXML
    private void handleDropFile(DragEvent event) {

//        List<File> files = event.getDragboard().getFiles();
//        String filePath = files.get(0).toString();
//        ExtensionChecker ch = new ExtensionChecker();
//        if (ch.isGoodExtension(filePath)) {
//            startMediaPlay(filePath);
//        }
    }

    @FXML
    private void handleDragOverFile(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
}

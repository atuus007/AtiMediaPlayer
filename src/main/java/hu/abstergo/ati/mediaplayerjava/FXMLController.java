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
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableMap;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
    private Duration duration;
    private DoubleProperty width, height;
    private MediaPlayer mp;
    private Media me;
    public String path;
    private FileChooser fc;
    private PlayItemBuilderImpl builder;
    private boolean playlistOpen = false;
    @FXML
    private ImageView imgCover;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbSound.setText("100.0");
        fc = new FileChooser();

        fc.setInitialDirectory(new File("E:\\Image\\Soundtrack\\Assasins Creed 2\\Disc 1"));
        fc.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Music files", "*.mp3"),
                        new FileChooser.ExtensionFilter("Video Files", "*.mp4")
                );
        builder = new PlayItemBuilderImpl();
    }

    @FXML
    private void onStopPlay(ActionEvent event) {
        mp.seek(mp.getTotalDuration());
        mp.stop();

    }

    @FXML
    private void onPlay(ActionEvent event) {
        Status status = mp.getStatus();
        if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
            // don't do anything in these states
            return;
        }
        if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
            mvPlayer.getMediaPlayer().setRate(1);
            mvPlayer.getMediaPlayer().play();
        }
    }

    @FXML
    private void onPausePlay(ActionEvent event) {
        mvPlayer.getMediaPlayer().pause();
    }

    @FXML
    private void onSlowerPlay(ActionEvent event) {
        mvPlayer.getMediaPlayer().setRate(.5);
    }

    @FXML
    private void onRestartPlay(ActionEvent event) {
        mvPlayer.getMediaPlayer().seek(mp.getStartTime());
        mvPlayer.getMediaPlayer().play();
    }

    @FXML
    private void onFasterPlay(ActionEvent event) {
        mvPlayer.getMediaPlayer().setRate(2);
    }

    @FXML
    private void openMenuAction(ActionEvent event) {
        File selectedFile = fc.showOpenDialog(null);
        if (mvPlayer.getMediaPlayer() != null) {
            mvPlayer.getMediaPlayer().dispose();
        }
        if (selectedFile != null) {
            // mp.currentTimeProperty()
            path = selectedFile.getAbsolutePath();
            me = new Media(new File(path).toURI().toString());
            mp = new MediaPlayer(me);
            mvPlayer.setMediaPlayer(mp);
            mp.setAutoPlay(true);
            mvPlayer.setPreserveRatio(true);
            mvPlayer.autosize();
            volumeSlide.setValue(mp.getVolume() * 100);
            mvPlayer.fitWidthProperty().bind(mediaHolder.widthProperty());
            mvPlayer.fitHeightProperty().bind(mediaHolder.heightProperty());
            mediaHolder.getScene().widthProperty();
            mp.setOnReady(new Runnable() {

                ObservableMap<String, Object> mediaMetadata = me.getMetadata();

                @Override
                public void run() {
                    duration = mp.getMedia().getDuration();
                    update();
                    if (mediaMetadata.isEmpty()) {
                        System.out.println("media nem tartalmaz metaadatot");
                        getMediaTitle(path);
                    } else {
                        for (String key : mediaMetadata.keySet()) {
                            System.out.println(key + " = " + mediaMetadata.get(key));

                        }
                    }

                }
            });
            mp.currentTimeProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    update();
                }
            });
            slTimeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (slTimeSlider.isValueChanging()) {
                        mp.seek(duration.multiply(slTimeSlider.getValue() / 100));
                    }
                }
            });
            volumeSlide.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (volumeSlide.isValueChanging()) {
                        mvPlayer.getMediaPlayer().setVolume(volumeSlide.getValue() / 100);
                        double volume = mvPlayer.getMediaPlayer().getVolume() * 100;
                        lbSound.setText(Integer.toString((int) volume));
                    }
                }
            });
        }
    }

    public void update() {
        if (slTimeSlider != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration jelenlegiIdo = mp.getCurrentTime();
                    double durationToDouble = duration.toMillis();
                    timerLabel.setText(TimeFromatConverter.formatTime(jelenlegiIdo, duration));
                    slTimeSlider.setDisable(duration.isUnknown());
                    if (!slTimeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slTimeSlider.isValueChanging()) {
                        slTimeSlider.setValue(jelenlegiIdo.divide(durationToDouble).toMillis() * 100.0);
                    }
                }
            });
        }
    }
    public String getMediaTitle(String in){
         String temp="";
         int pos;
         System.out.println(in);
         if(in.lastIndexOf("\\")!=-1){
             pos=in.lastIndexOf("\\");
             System.out.println(in.lastIndexOf("\\"));
         }else{
             pos=in.lastIndexOf("/");
             System.out.println(in.lastIndexOf("/"));
         }
         
        for(int i=pos+1; i<in.length(); i++){
            System.out.print(in.charAt(i));
            temp+=in.charAt(i);
            
        }
       return temp;
        
       
    }
    @FXML
    private void onPlaylistOpen(ActionEvent event) {
        if (playlistOpen) {
            btnOpenPlaylist.setText("Close Playlist");
            playlistOpen = false;
            lvPlayList.setPrefWidth(100);

        } else {
            btnOpenPlaylist.setText("Open Playlist");
            playlistOpen = true;
            lvPlayList.setPrefWidth(0);
        }
    }

}

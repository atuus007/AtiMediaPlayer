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
    private MenuItem mnOpen1;
    @FXML
    private Label lbStatus;
    @FXML
    private MediaView mvPlayer;
    
    @FXML
    private Button btnPause;
    
    @FXML
    private Slider slTimeSlider;
    private long timeStamp;
    private Duration duration;
    private DoubleProperty width, height;
    private MediaPlayer mp;
    private Media me;
    public String path;
    private FileChooser fc;
    @FXML
    private Label timerLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbSound.setText("100.0");
        fc = new FileChooser();
        
        fc.setInitialDirectory(new File("E:\\Image\\Soundtrack\\Assasins Creed 2\\Disc 1"));
        fc.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Music files", "*.mp3"),
                        new FileChooser.ExtensionFilter("Video Files", "*.mp4")
                );
        
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
        
        mvPlayer.getMediaPlayer().setRate(1);
        mvPlayer.getMediaPlayer().play();
        
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
            volumeSlide.setValue(mp.getVolume() * 100);
            mp.setOnReady(new Runnable() {
                @Override
                public void run() {
                    duration = mp.getMedia().getDuration();
                    update();
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

                        // lbStatus.setText("fasdfas " + duration.multiply(slTimeSlider.getValue()));
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
            
        } else {
            //lbStatus.setText("Nem ervenyes file");
        }
    }
    
    public void update() {
        if (slTimeSlider != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration jelenlegiIdo = mp.getCurrentTime();
                    timerLabel.setText(formatTime(jelenlegiIdo, duration));
                    System.out.println(jelenlegiIdo);
                    
                    slTimeSlider.setDisable(duration.isUnknown());
                    if (!slTimeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slTimeSlider.isValueChanging()) {
                        slTimeSlider.setValue(jelenlegiIdo.divide(duration).toMillis() * 100.0);
                    }
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;
        
        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else if (elapsedHours > 0) {
            return String.format("%d:%02d:%02d", elapsedHours,
                    elapsedMinutes, elapsedSeconds);
        } else {
            return String.format("%02d:%02d", elapsedMinutes,
                    elapsedSeconds);
        }
    }
    
}

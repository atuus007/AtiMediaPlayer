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

    private boolean playlistOpen = false;
    private final List<PlayItem> listOfPlayItems = new ArrayList<>();
    private ObservableList<PlayItem> obPlayList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbSound.setText("100.0");
        fc = new FileChooser();
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

            path = selectedFile.getAbsolutePath();
            startMediaPlay(path);

            mp.setOnReady(new Runnable() {
                ObservableMap<String, Object> mediaMetadata = me.getMetadata();

                @Override
                public void run() {
                    duration = mp.getMedia().getDuration();
                    update();
                    /**/
 /* if (mediaMetadata.isEmpty()) {
                        
                        for (PlayItem i : listOfPlayItems) {
                            writePlayItem(i);
                        }
                    } else {

                        for (String key : mediaMetadata.keySet()) {
                            if (mediaMetadata.get(key).equals("")) {
                                System.out.println("null");
                            } else {
                                System.out.println(key + " = " + mediaMetadata.get(key));
                            }
                        }

                        /*listOfPlayItems.add(new PlayItem(
                                mediaMetadata.get("title").toString(),
                                (int) mediaMetadata.get("disc count"),
                                (int) mediaMetadata.get("track count"),
                                mediaMetadata.get("artist").toString(),
                                (Image) mediaMetadata.get("image"),
                                (int) mediaMetadata.get("year"),
                                mediaMetadata.get("album").toString(),
                                mediaMetadata.get("composer").toString(),
                                mediaMetadata.get("genre").toString(),
                                TimeFromatConverter.formatTime(duration),
                                getMediaExtension(path),
                                path
                        ));
                    }
                     */
                    //ConvertMetadata meta=new ConvertMetadata(mediaMetadata);
                    //System.out.println(meta.getValami());
                    System.out.println("fasdfasdfasdf");
                    listOfPlayItems.add(new PlayItem(getMediaTitle(path), TimeFromatConverter.formatTime(duration), getMediaExtension(path), path));

                    obPlayList = FXCollections.observableArrayList(listOfPlayItems);
                    lvPlayList.setItems(obPlayList);
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
            
            mediaHolder.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(mediaHolder.getWidth());
                if (playlistOpen) {
                    mvPlayer.fitWidthProperty().bind(mediaHolder.widthProperty());
                } 
            }
        });

        }
    }

    public void update() {
        if (slTimeSlider != null && timerLabel != null) {
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
        mediaHolder.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(mediaHolder.getWidth());
                if (playlistOpen) {
                    //mediaHolder.setPrefWidth(mediaHolder.getWidth()-lvPlayList.getWidth());
                } 
            }
        });
    }

    public void startMediaPlay(final String path) {

        me = new Media(new File(path).toURI().toString());

        mp = new MediaPlayer(me);
        mvPlayer.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mvPlayer.setPreserveRatio(true);
        mvPlayer.autosize();
        volumeSlide.setValue(mp.getVolume() * 100);
        mvPlayer.fitWidthProperty().bind(mediaHolder.widthProperty());
        mvPlayer.fitHeightProperty().bind(mediaHolder.heightProperty());

    }

    public void writePlayItem(PlayItem item) {
        System.out.println((item.toString()));
    }

    public String getMediaExtension(String in) {
        String temp;
        int pos = 0;
        if (in.lastIndexOf(".") != -1) {
            pos = in.lastIndexOf(".");
        }
        temp = in.substring(pos + 1, in.length());
        return temp;
    }

    public String getMediaTitle(String in) {
        String temp;
        int pos;
        if (in.lastIndexOf("\\") != -1) {
            pos = in.lastIndexOf("\\");
        } else {
            pos = in.lastIndexOf("/");
        }
        temp = in.substring(pos + 1, in.length() - 3);
        return temp;
    }

    @FXML
    private void onPlaylistOpen(ActionEvent event) {
        if (playlistOpen) {
            btnOpenPlaylist.setText("Close Playlist");
            playlistOpen = false;
            lvPlayList.setPrefWidth(150);

        } else {
            btnOpenPlaylist.setText("Open Playlist");
            playlistOpen = true;
            lvPlayList.setPrefWidth(0);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava;

import java.io.File;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer.Status;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fodor Edit
 */
public class MediaModell implements IExtensionFinder{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MediaModell.class);
    private FileChooser fc;
    private Media me;
    private MediaPlayer mp;
    private MediaView mmView;
    private Slider mmStatus;
    private Slider mmVolume;
    private Label mmTime;
    private StackPane mHolder;
    private Duration duration;
    private boolean openList = false;
    

    public MediaModell(final MediaView mv, final Slider status, final Slider volume, final Label time, final StackPane mediaHolder) {
        //logger.info(mv.getId());
        //mv.setId("fasdfasdfasdfasdfasdfasdfasfasfdasdf");
        //logger.info("mmView "+mv.getId());
        this.mmView = mv;
        this.mmStatus = status;
        this.mmVolume = volume;
        this.mmTime = time;
        this.mHolder=mediaHolder;
        initExtensionFilter();

        //logger.info("mmView "+mmView.getId());
    }

    public void initExtensionFilter() {
        fc = new FileChooser();
        fc.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Music files", "*.mp3"),
                        new FileChooser.ExtensionFilter("Video Files", "*.mp4")
                );
    }

    public void openFileChooser() {
        String path;
        File selectedFile = fc.showOpenDialog(null);
        logger.info("openFileChooser");
        if (selectedFile != null) {

            path = selectedFile.getAbsolutePath();
            startMediaPlay(path);

        }
    }

    public void mmPausePlay() {
        mmView.getMediaPlayer().pause();
    }

    public void mmSlowerPlay() {
        mmView.getMediaPlayer().setRate(.5);
    }

    public void mmRestart() {
        mmView.getMediaPlayer().seek(mp.getStartTime());
        mmView.getMediaPlayer().play();
    }

    public void mmPlay() {
        Status status = mp.getStatus();
        if (Status.HALTED == status || Status.UNKNOWN == status) {
            return;
        }
        if (status == Status.PAUSED || Status.READY == status || status == Status.STOPPED) {
            mmView.getMediaPlayer().setRate(1);
            mmView.getMediaPlayer().play();

        }
    }

    public void mmFasterPlay() {
        mmView.getMediaPlayer().setRate(2);
    }

    public void mmOpenPlaylist(Button btnOpenPlaylist, ListView<PlayItem> lvPlayList) {
        if (openList) {
            btnOpenPlaylist.setText("Close Playlist");
            openList = false;
            lvPlayList.setPrefWidth(150);

        } else {
            btnOpenPlaylist.setText("Open Playlist");
            openList = true;
            lvPlayList.setPrefWidth(0);
        }
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

    public void startMediaPlay(final String path) {
        if (mmView.getMediaPlayer() != null) {
            mmView.getMediaPlayer().dispose();
        }
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mp.setAutoPlay(true);
        mmView.setMediaPlayer(mp);
        mmView.setPreserveRatio(true);
        mmView.autosize();
        initListeners();

//        volumeSlide.setValue(mp.getVolume() * 100);
//        boundMediaView();
    }

    public void mmStopMediaPlay() {
        mp.seek(mp.getTotalDuration());
        mp.stop();
    }

    public void update() {
        if (mmStatus != null && mmTime != null) {
            Platform.runLater(() -> {
                Duration jelenlegiIdo = mp.getCurrentTime();
                double durationToDouble = duration.toMillis();
                mmTime.setText(TimeFromatConverter.formatTime(jelenlegiIdo, duration));
                mmStatus.setDisable(duration.isUnknown());
                if (!mmStatus.isDisabled() && duration.greaterThan(Duration.ZERO) && !mmStatus.isValueChanging()) {
                    mmStatus.setValue(jelenlegiIdo.divide(durationToDouble).toMillis() * 100.0);
                }
            });
        }
//        mediaHolder.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            System.out.println(mediaHolder.getWidth());
//            if (playlistOpen) {
//                //mediaHolder.setPrefWidth(mediaHolder.getWidth()-lvPlayList.getWidth());
//            }
//        });
    }

    public void initListeners() {
        mp.setOnReady(new Runnable() {
            ObservableMap<String, Object> mediaMetadata = me.getMetadata();

            @Override
            public void run() {
                duration = mp.getMedia().getDuration();
                update();

//                listOfPlayItems.add(new PlayItem(getMediaTitle(path), TimeFromatConverter.formatTime(duration), getMediaExtension(path), path));
//                obPlayList = FXCollections.observableArrayList(listOfPlayItems);
//                lvPlayList.setItems(obPlayList);
            }
        });

        mp.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                update();
            }
        });
        mmStatus.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (mmStatus.isValueChanging()) {
                    mp.seek(duration.multiply(mmStatus.getValue() / 100));
                }
            }
        });
        mmVolume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (mmVolume.isValueChanging()) {
                    mmView.getMediaPlayer().setVolume(mmVolume.getValue() / 100);
                    double volume = mmView.getMediaPlayer().getVolume() * 100;
//                    lbSound.setText(Integer.toString((int) volume));
                }
            }
        });
//
//        mediaHolder.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            System.out.println(mediaHolder.getWidth());
//            if (playlistOpen) {
//                mvPlayer.fitWidthProperty().bind(mediaHolder.widthProperty());
//            }
//        });

    }

    private void boundMediaView() {
//        mvPlayer.fitWidthProperty().bind(mediaHolder.widthProperty());
//        mvPlayer.fitHeightProperty().bind(mediaHolder.heightProperty());
    }
    @Override
    public String getMediaExtension(String in) {
        String temporal;
        int pos = 0;
        if (in.lastIndexOf(".") != -1) {
            pos = in.lastIndexOf(".");
        }
        temporal = in.substring(pos + 1, in.length());
        return temporal;
    }

}

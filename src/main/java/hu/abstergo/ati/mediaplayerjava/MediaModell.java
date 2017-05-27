/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class MediaModell implements IExtensionFinder {

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
    private String path;
    private long timeStamp;
    private ObservableList<PlayItem> obPlayList;
    private ListView<PlayItem> lvPlayList;
    private final List<PlayItem> listOfPlayItems = new ArrayList<>();

    public MediaModell(final MediaView mv, final Slider status, final Slider volume, final Label time, final StackPane mediaHolder, final ListView<PlayItem> lvPlayList) {
        //logger.info(mv.getId());
        //mv.setId("fasdfasdfasdfasdfasdfasdfasfasfdasdf");
        //logger.info("mmView "+mv.getId());
        this.mmView = mv;
        this.mmStatus = status;
        this.mmVolume = volume;
        this.mmTime = time;
        this.mHolder = mediaHolder;
        this.lvPlayList = lvPlayList;
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
    public void dragAndDrop(List<File> files){
        String filePath = files.get(0).toString();
        logger.info(""+filePath);
        ExtensionChecker ch = new ExtensionChecker();
        if (ch.isGoodExtension(filePath)) {
            logger.info(""+filePath);
            startMediaPlay(filePath);
        }
    }
    public void mmOpenPlaylist(final Button btnOpenPlaylist, final ListView<PlayItem> lvPlayList) {
        if (openList) {
            btnOpenPlaylist.setText("Close Playlist");
            openList = false;
            lvPlayList.setPrefWidth(150);
            mmView.fitWidthProperty().bind(mHolder.widthProperty().subtract(lvPlayList.getPrefWidth()));

        } else {
            btnOpenPlaylist.setText("Open Playlist");
            openList = true;

            lvPlayList.setPrefWidth(0);
            mmView.fitWidthProperty().bind(mHolder.widthProperty());

        }
    }

    private void boundMediaView() {

        mmView.fitWidthProperty().bind(mHolder.widthProperty());
        mmView.fitHeightProperty().bind(mHolder.heightProperty());

    }

    public String getMediaTitle(String in) {
        String temp;
        int pos;
        
        logger.info(in);
        if (in.lastIndexOf("\\") != -1) {
       
            pos = in.lastIndexOf("\\");
            logger.info(""+pos);
        } else {
            pos = in.lastIndexOf("/");
        }
        temp = in.substring(pos + 1, in.length() - 3);
        return temp;
    }

    public void addPlayElement(final String path) {
        logger.info(""+path);
        listOfPlayItems.add(new PlayItem(getMediaTitle(path), TimeFromatConverter.formatTime(duration), getMediaExtension(path), path));
        obPlayList = FXCollections.observableArrayList(listOfPlayItems);
        lvPlayList.setItems(obPlayList);
        listOfPlayItems.forEach((p) -> {
            logger.info(p.getTitle());
        });
    }

    public void startMediaPlay(final String path) {
        logger.info(path);
        if (mmView.getMediaPlayer() != null) {
            mmView.getMediaPlayer().dispose();
        }
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        initListeners();
        //addPlayElement(path);
        mp.setAutoPlay(true);
        mmView.setMediaPlayer(mp);
        mmView.setPreserveRatio(true);
        mmView.autosize();
       
        

        mmVolume.setValue(mp.getVolume() * 100);
        boundMediaView();
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
    }

    public void initListeners() {
        logger.info("initListeners");
        mp.setOnReady(new Runnable() {
//            ObservableMap<String, Object> mediaMetadata = me.getMetadata();

            @Override
            public void run() {
                duration = mp.getMedia().getDuration();
                update();

                listOfPlayItems.add(new PlayItem(getMediaTitle(path), TimeFromatConverter.formatTime(duration), getMediaExtension(path), path));
                obPlayList = FXCollections.observableArrayList(listOfPlayItems);
                lvPlayList.setItems(obPlayList);
                listOfPlayItems.forEach((p) -> {
                    logger.info(p.getTitle());
                });
            }
        });

        mp.currentTimeProperty().addListener((Observable observable) -> {
            update();
        });
        mmStatus.valueProperty().addListener((Observable observable) -> {
            if (mmStatus.isValueChanging()) {
                mp.seek(duration.multiply(mmStatus.getValue() / 100));
            }
        });
        mmVolume.valueProperty().addListener((Observable observable) -> {
            if (mmVolume.isValueChanging()) {
                mmView.getMediaPlayer().setVolume(mmVolume.getValue() / 100);
                double volume = mmView.getMediaPlayer().getVolume() * 100;
//                    lbSound.setText(Integer.toString((int) volume));
            }
        });
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

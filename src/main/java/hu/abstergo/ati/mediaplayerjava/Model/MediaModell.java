/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
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
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Ati
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
    private Label mmVol;
    private StackPane mHolder;
    private Duration duration;

    private boolean openList = false;
    private String path;
    private long timeStamp;
    private ObservableList<PlayItem> obPlayList;
    private ListView<PlayItem> lvPlayList;
    private List<PlayItem> listOfPlayItems = new ArrayList<>();

    public MediaModell(final MediaView mv, final Slider status, final Slider volume, final Label time, final Label lbVol, final StackPane mediaHolder, final ListView<PlayItem> lvPlayList) {
        //logger.info(mv.getId());
        //mv.setId("fasdfasdfasdfasdfasdfasdfasfasfdasdf");
        //logger.info("mmView "+mv.getId());
        this.mmView = mv;
        this.mmStatus = status;
        this.mmVolume = volume;
        this.mmTime = time;
        this.mHolder = mediaHolder;
        this.lvPlayList = lvPlayList;
        this.mmVol = lbVol;
        lvPlayList.setPrefWidth(150);
        initExtensionFilter();
        initPlayList();

    }

    public void initExtensionFilter() {
        fc = new FileChooser();
        fc.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("MP3 Music files", "*.mp3"),
                        new FileChooser.ExtensionFilter("MP4 Video Files", "*.mp4"),
                        new FileChooser.ExtensionFilter("Flash Video Files", "*.flv"),
                        new FileChooser.ExtensionFilter("Waveform Audio Format", "*.wav")
                );
    }

    public void initPlayList() {
        listOfPlayItems = SaveList.loadItems();
        obPlayList = FXCollections.observableArrayList(listOfPlayItems);
        lvPlayList.setItems(obPlayList);
    }

    public void openFileChooser() {
        String localPath;
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            localPath = selectedFile.getAbsolutePath();
            logger.info("openFileChooser: " + localPath);
            startMediaPlay(localPath);
        }
    }

    public void startMediaPlay(final String inPath) {
        logger.info("startMediaPlay: " + inPath);
        path = inPath;
        if (mmView.getMediaPlayer() != null) {
            mmView.getMediaPlayer().dispose();
        }
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        initListeners();
        boundMediaView();
        mp.setAutoPlay(true);
        mmView.setMediaPlayer(mp);

        mmView.setPreserveRatio(true);
        mmView.autosize();

        mmVolume.setValue(mp.getVolume() * 100);

    }

    public void dragAndDrop(List<File> files) {
        String filePath = files.get(0).getAbsolutePath();
        logger.info("" + filePath);
        ExtensionChecker ch = new ExtensionChecker();
        if (ch.isGoodExtension(filePath)) {
            logger.info("" + filePath);
            startMediaPlay(filePath);
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

    public void mPlayistControl(final Button btnOpenPlaylist, final ListView<PlayItem> lvPlayList) {
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
        mmView.fitHeightProperty().bind(mHolder.heightProperty());
//        if (openList) {
        mmView.fitWidthProperty().bind(mHolder.widthProperty().subtract(lvPlayList.getPrefWidth()));
//        } else {
//            mmView.fitWidthProperty().bind(mHolder.widthProperty());
//        }

    }

    public String getMediaTitle(String in) {
        String temp;
        int pos;

        logger.info(in);
        if (in.lastIndexOf("\\") != -1) {

            pos = in.lastIndexOf("\\");
            logger.info("" + pos);
        } else {
            pos = in.lastIndexOf("/");
        }
        temp = in.substring(pos + 1, in.length() - 4);
        return temp;
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
                logger.info("public void run() {");
                duration = mp.getMedia().getDuration();
                update();
                PlayItem pl = new PlayItem(getMediaTitle(path), TimeFromatConverter.formatTime(duration), getMediaExtension(path), path);

                listOfPlayItems.add(pl);
                obPlayList = FXCollections.observableArrayList(listOfPlayItems);
                lvPlayList.setItems(obPlayList);

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
                mmVol.setText(Integer.toString((int) volume));
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

    public void startPressEnter() {
        PlayItem item = lvPlayList.getSelectionModel().getSelectedItem();
        startMediaPlay(item.getUriPath());
        //
    }

    public void executeSave() {
        SaveList.saveList(listOfPlayItems);
    }

}

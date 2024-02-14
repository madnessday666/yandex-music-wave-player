package ymwp.gui.player;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ymwp.config.ClientConfiguration;
import ymwp.facade.FeedbackFacade;
import ymwp.gui.manager.WindowManager;
import ymwp.httpclient.station.StationHttpClient;
import ymwp.httpclient.track.TrackHttpClient;
import ymwp.model.artist.Artist;
import ymwp.model.sequence.SequenceItem;
import ymwp.model.station.StationFeedback;
import ymwp.model.station.StationFeedbackType;
import ymwp.model.station.StationTracksResult;
import ymwp.model.track.Track;
import ymwp.model.track.TrackDownloadInfo;
import ymwp.model.track.TrackShort;
import ymwp.util.DurationAdjuster;
import ymwp.util.FadeTransitionFiller;
import ymwp.util.FileLoader;
import ymwp.util.StyleHelper;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static ymwp.httpclient.UrlConstants.*;

public class WavePlayerWindow {

    private final int CENTRAL_MODULES_WIDTH = 310;
    private final int CENTRAL_MODULES_MIN_WIDTH = 0;
    private final int MODULES_HEIGHT = 30;
    private final int PROGRESS_BAR_HEIGHT = 12;
    private final int SIDE_MODULES_WIDTH = 40;
    private final int WINDOW_WIDTH = 410;

    private final HBox allModules;
    private final StackPane centralModulesGroup;
    private final ContextMenu contextMenu;
    private final Label currentDurationLabel;
    private final Button likeButton;
    private final HBox mainContainer;
    private final HBox modulesCentral;
    private final HBox modulesLeft;
    private final HBox modulesRight;
    private final Button nextButton;
    private final Button playButton;
    private final ProgressBar progressBar;
    private final Rectangle rectangle;
    private final Label totalDurationLabel;
    private final Label trackTitleLabel;
    private final Button volumeButton;

    private String batchId;
    private boolean isLiked;
    private boolean isMuted;
    private boolean isPaused;
    private Set<String> likedTracks;
    private Media media;
    private MediaPlayer mediaPlayer;
    private final Map<String, LocalDateTime> playedTracks;
    private final Queue<SequenceItem> stationOnYourWaveTrackSequence;
    private final StationHttpClient stationHttpClient;
    private final TrackHttpClient trackHttpClient;
    private Track track;
    private String trackArtists;
    private String trackDownloadLink;
    private String trackId;
    private String trackTitle;
    private int trackTotalLength;
    private long userId;
    private double volume;

    public WavePlayerWindow() {
        this.allModules = new HBox();
        this.centralModulesGroup = new StackPane();
        this.contextMenu = new ContextMenu();
        this.currentDurationLabel = new Label();
        this.likeButton = new Button();
        this.mainContainer = new HBox();
        this.modulesCentral = new HBox();
        this.modulesLeft = new HBox();
        this.modulesRight = new HBox();
        this.nextButton = new Button();
        this.playButton = new Button();
        this.progressBar = new ProgressBar();
        this.rectangle = new Rectangle();
        this.totalDurationLabel = new Label();
        this.trackTitleLabel = new Label();
        this.volumeButton = new Button();

        this.isLiked = false;
        this.isMuted = false;
        this.isPaused = true;
        this.likedTracks = new HashSet<>();
        this.playedTracks = new HashMap<>();
        this.stationHttpClient = new StationHttpClient();
        this.stationOnYourWaveTrackSequence = new ConcurrentLinkedQueue<>();
        this.trackHttpClient = new TrackHttpClient();
        this.volume = ClientConfiguration.config.getVolume();

        this.init();
    }

    private void checkIfTrackIsLiked() {
        if (likedTracks.contains(trackId)) {
            isLiked = true;
            StyleHelper.changeNodeStyle(likeButton, StyleHelper.HEART_FULL);
        } else {
            isLiked = false;
            StyleHelper.changeNodeStyle(likeButton, StyleHelper.HEART);
        }
    }

    private Track checkIfTrackRecentlyPlayed() {
        track = Objects.requireNonNull(stationOnYourWaveTrackSequence.poll()).getTrack();
        String unsureTrackId = track.getId();
        LocalDateTime lastPlayed = playedTracks.get(unsureTrackId);
        if (playedTracks.containsKey(unsureTrackId)) {
            if (LocalDateTime.now().isAfter(lastPlayed.plusMinutes(45))) {
                playedTracks.remove(unsureTrackId);
            } else {
                while (playedTracks.containsKey(unsureTrackId) && stationOnYourWaveTrackSequence.size() > 1) {
                    track = stationOnYourWaveTrackSequence.poll().getTrack();
                }
            }
        }
        if (stationOnYourWaveTrackSequence.size() <= 3) {
            CompletableFuture.runAsync(this::fetchStationTrackSequence);
        }
        trackId = track.getId();
        trackTitle = track.getTitle();
        trackArtists = track.getArtists().stream().map(Artist::getName).collect(Collectors.joining(", "));
        trackTotalLength = (int) track.getDurationMs() / 1000;
        playedTracks.put(trackId, LocalDateTime.now());
        return track;
    }

    private void init() {
        stationHttpClient.sendFeedback(null, STATION_ON_YOUR_WAVE, new StationFeedback(StationFeedbackType.radioStarted));
        this.fetchUserLikedTracks();
        this.fetchStationTrackSequence();
    }

    private void fetchStationTrackSequence() {
        StationTracksResult stationTracksResult = stationHttpClient.getStationOnYourWaveTracksResult(
                track == null ? "" : String.format("%s:%s", trackId, track.getAlbums().get(0).getId())
        );
        batchId = stationTracksResult.getBatchId();
        this.stationOnYourWaveTrackSequence.addAll(stationTracksResult.getSequence());
    }

    private void fetchUserLikedTracks() {
        userId = ClientConfiguration.config.getUser().getId();
        likedTracks = trackHttpClient
                .getUserLikedTracks(userId)
                .stream()
                .map(TrackShort::getId)
                .collect(Collectors.toSet());
    }

    private Label getCurrentDurationLabel() {
        currentDurationLabel.setText("0:00");
        currentDurationLabel.setVisible(false);
        currentDurationLabel.setStyle("""
                -fx-padding: 0 0 0 5;
                -fx-min-width: %d;
                -fx-max-width:  %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-text-fill: #000;
                -fx-font-size: 11;
                -fx-alignment: center-left;
                            """
                .formatted(
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        return currentDurationLabel;
    }

    private Button getLikeButton() {
        likeButton.getStyleClass().add(StyleHelper.HEART);
        likeButton.setOnMouseClicked(mouseEvent -> {
            if (!mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                if (mediaPlayer != null) {
                    if (likedTracks.remove(trackId)) {
                        trackHttpClient.addOrRemoveTrackFromLiked(userId, trackId, REMOVE_TRACK_FROM_LIKED);
                        StyleHelper.changeNodeStyle(likeButton, StyleHelper.HEART);
                    } else {
                        trackHttpClient.addOrRemoveTrackFromLiked(userId, trackId, ADD_TRACK_TO_LIKED);
                        likedTracks.add(trackId);
                        StyleHelper.changeNodeStyle(likeButton, StyleHelper.HEART_FULL);
                    }
                    isLiked = !isLiked;
                }
            }
        });
        return likeButton;
    }

    public HBox getMainContainer() {
        mainContainer.getChildren().add(this.getAllModulesWithBg());
        this.setContextMenu();
        mainContainer.setOnContextMenuRequested(contextMenuEvent -> {
            String target = contextMenuEvent.getTarget().getClass().getName();
            if (!target.contains("Label")) {
                contextMenu.show(
                        (Node) contextMenuEvent.getTarget(),
                        contextMenuEvent.getScreenX(),
                        contextMenuEvent.getScreenY()
                );
            }
        });
        return mainContainer;
    }

    private StackPane getAllModulesWithBg() {
        allModules.getChildren().addAll(
                this.getModulesLeft(),
                this.getModulesCentral(),
                this.getModulesRight()
        );
        allModules.setStyle("""
                -fx-min-width:  %d;
                -fx-max-width: %d;
                -fx-pref-width: %d;
                -fx-max-height:  %d;
                -fx-min-height:  %d;
                -fx-pref-height: %d;
                -fx-alignment: center;
                            """
                .formatted(
                        WINDOW_WIDTH,
                        WINDOW_WIDTH,
                        WINDOW_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        return new StackPane(this.getPlayerBg(), allModules);
    }

    private HBox getModulesCentral() {
        modulesCentral.getChildren().add(this.getModulesCentralGroup());
        modulesCentral.setStyle("""
                -fx-min-width: %d;
                -fx-max-width: %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-alignment: center;
                            """
                .formatted(
                        CENTRAL_MODULES_MIN_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        return modulesCentral;
    }

    private HBox getModulesCentralGroup() {
        centralModulesGroup.getChildren().addAll(
                this.getProgressBar(),
                this.getCurrentDurationLabel(),
                this.getTotalDurationLabel(),
                this.getTrackTitleLabel()
        );
        centralModulesGroup.setStyle("""
                -fx-min-width: %d;
                -fx-max-width:  %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-font-size: 11;
                            """
                .formatted(
                        CENTRAL_MODULES_MIN_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        return new HBox(centralModulesGroup);
    }

    private HBox getModulesLeft() {
        modulesLeft.getChildren().addAll(this.getPlayButton(), this.getNextButton());
        modulesLeft.setStyle("""
                -fx-padding: 0 10 0 0;
                -fx-min-width: %d;
                -fx-max-width: %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-alignment: center;
                            """
                .formatted(
                        SIDE_MODULES_WIDTH,
                        SIDE_MODULES_WIDTH,
                        SIDE_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        return modulesLeft;
    }

    private Button getNextButton() {
        nextButton.getStyleClass().add(StyleHelper.NEXT);
        nextButton.setOnMouseClicked(mouseEvent -> {
            if (!mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                if (mediaPlayer != null) {
                    int totalSecondsPlayed = (int) mediaPlayer.getCurrentTime().toSeconds();
                    FeedbackFacade.sendStationFeedbackAndTrackStatus(
                            batchId,
                            STATION_ON_YOUR_WAVE,
                            StationFeedbackType.skip,
                            track,
                            totalSecondsPlayed,
                            trackTotalLength,
                            totalSecondsPlayed
                    );
                }
                this.play();
            }
        });
        return nextButton;
    }

    private Button getPlayButton() {
        playButton.getStyleClass().add(StyleHelper.PLAY);
        playButton.setOnMouseClicked(mouseEvent -> {
            if (!mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                if (mediaPlayer != null) {
                    if (isPaused) {
                        mediaPlayer.play();
                    } else {
                        mediaPlayer.pause();
                    }
                } else {
                    this.play();
                }
            }
        });
        return playButton;
    }

    private ProgressBar getProgressBar() {
        progressBar.setVisible(false);
        progressBar.setProgress(0);
        progressBar.setStyle("""
                -fx-border-radius: 0;
                -fx-accent : %s;
                -fx-min-width: %d;
                -fx-max-width:  %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-background-insets: 0;
                -fx-background-radius: 0;
                -fx-background-color: %s;
                -fx-opacity: %f;
                            """
                .formatted(
                        ClientConfiguration.config.getBar().getColor(),
                        CENTRAL_MODULES_MIN_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        PROGRESS_BAR_HEIGHT,
                        PROGRESS_BAR_HEIGHT,
                        PROGRESS_BAR_HEIGHT,
                        ClientConfiguration.config.getBar().getBgColor(),
                        ClientConfiguration.config.getBar().getOpacity()
                ));
        return progressBar;
    }

    private HBox getModulesRight() {
        modulesRight.getChildren().addAll(this.getLikeButton(), this.getVolumeButton());
        modulesRight.setStyle("""
                -fx-padding: 0 0 0 10;
                -fx-min-width: %d;
                -fx-max-width: %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-alignment: center;
                            """
                .formatted(
                        SIDE_MODULES_WIDTH,
                        SIDE_MODULES_WIDTH,
                        SIDE_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        return modulesRight;
    }

    private Rectangle getPlayerBg() {
        rectangle.setWidth(WINDOW_WIDTH);
        rectangle.setHeight(MODULES_HEIGHT);
        rectangle.setStyle("""
                -fx-fill: %s;
                -fx-opacity: %f;
                -fx-arc-width: %d;
                -fx-arc-height: %d;
                """
                .formatted(
                        ClientConfiguration.config.getBackground().getColor(),
                        ClientConfiguration.config.getBackground().getOpacity(),
                        ClientConfiguration.config.getBackground().getRadius(),
                        ClientConfiguration.config.getBackground().getRadius()
                ));
        return rectangle;
    }

    private Label getTotalDurationLabel() {
        totalDurationLabel.setText("0:00");
        totalDurationLabel.setVisible(false);
        totalDurationLabel.setStyle("""
                -fx-padding: 0 5 0 0;
                -fx-min-width: %d;
                -fx-max-width:  %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-text-fill: #000;
                -fx-alignment: center-right;
                            """
                .formatted(
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        totalDurationLabel.setOnMouseClicked(mouseEvent -> {
            if (!mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                if (mediaPlayer != null) {
                    double progress = mouseEvent.getX() / (progressBar.getWidth() / 100);
                    double newDuration = media.getDuration().toSeconds() / 100 * progress;
                    mediaPlayer.seek(Duration.seconds(newDuration));
                }
            }
        });
        return totalDurationLabel;
    }

    private Label getTrackTitleLabel() {
        trackTitleLabel.setText("On Your Wave");
        trackTitleLabel.setStyle("""
                -fx-font-size: 14;
                -fx-min-width: %d;
                -fx-max-width:  %d;
                -fx-pref-width: %d;
                -fx-max-height: %d;
                -fx-min-height: %d;
                -fx-pref-height: %d;
                -fx-text-fill: #f4f4f4;
                -fx-alignment: center;
                            """
                .formatted(
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        CENTRAL_MODULES_WIDTH,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT,
                        MODULES_HEIGHT
                ));
        trackTitleLabel.setOnMouseEntered(mouseEntered -> {
            trackTitleLabel.setVisible(false);
            totalDurationLabel.setVisible(true);
            currentDurationLabel.setVisible(true);
            progressBar.setVisible(true);
            totalDurationLabel.setOnMouseExited(mouseExited -> {
                totalDurationLabel.setVisible(false);
                currentDurationLabel.setVisible(false);
                progressBar.setVisible(false);
                FadeTransitionFiller.fillWithFade(0, 1, Duration.millis(500), trackTitleLabel);
                trackTitleLabel.setVisible(true);
            });
        });

        return trackTitleLabel;
    }

    private Button getVolumeButton() {
        volumeButton.getStyleClass().add(StyleHelper.VOLUME_HIGH);
        volumeButton.setOnScroll(mouseEvent -> {
            if (mediaPlayer != null) {
                double deltaY = mouseEvent.getDeltaY();
                volume = new BigDecimal(volume).setScale(2, RoundingMode.HALF_UP).doubleValue();
                if (deltaY > 0 && volume < 1.0) {
                    if (isMuted) {
                        isMuted = false;
                        mediaPlayer.setMute(false);
                    }
                    volume += 0.02;
                } else if (deltaY < 0 && volume > 0.0) {
                    volume -= 0.02;
                }
                if (volume > 0.5) {
                    StyleHelper.changeNodeStyle(volumeButton, StyleHelper.VOLUME_HIGH);
                } else if (volume <= 0.5 && volume > 0.0) {
                    StyleHelper.changeNodeStyle(volumeButton, StyleHelper.VOLUME_MEDIUM);
                } else if (volume == 0.0) {
                    isMuted = true;
                    mediaPlayer.setMute(true);
                    StyleHelper.changeNodeStyle(volumeButton, StyleHelper.VOLUME_MUTED);
                }
                volume = new BigDecimal(volume).setScale(2, RoundingMode.HALF_UP).doubleValue();
                mediaPlayer.setVolume(volume);
            }
        });
        volumeButton.setOnMouseClicked(mouseEvent -> {
            if (!mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                if (mediaPlayer != null) {
                    volume = new BigDecimal(volume).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    String styleToAdd = volume > 0.5 ?
                            StyleHelper.VOLUME_HIGH : volume == 0.0 ?
                            StyleHelper.VOLUME_MUTED : StyleHelper.VOLUME_MEDIUM;
                    if (isMuted) {
                        StyleHelper.changeNodeStyle(volumeButton, styleToAdd);
                    } else {
                        StyleHelper.changeNodeStyle(volumeButton, StyleHelper.VOLUME_MUTED);
                    }
                    isMuted = !isMuted;
                    mediaPlayer.setMute(isMuted);
                }
            }
        });
        return volumeButton;
    }

    private void play() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        this.setupMedia();
        this.setupPlayerSettings();
        FeedbackFacade.sendStationFeedbackAndTrackStatus(
                batchId,
                STATION_ON_YOUR_WAVE,
                StationFeedbackType.trackStarted,
                track,
                0,
                trackTotalLength,
                0
        );
        CompletableFuture.runAsync(FileLoader::cleanUp);
        mediaPlayer.play();
    }

    private void setupMedia() {
        track = checkIfTrackRecentlyPlayed();
        TrackDownloadInfo trackDownloadInfo = trackHttpClient.getTrackDownloadInfo(trackId);
        trackDownloadLink = trackHttpClient.getTrackDownloadLink(trackDownloadInfo);
        File file = FileLoader.loadFileFromUrl(trackDownloadLink, trackId);
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    private void setContextMenu() {
        MenuItem trackInfo = new MenuItem("Copy title");
        MenuItem downloadLink = new MenuItem("Copy link");
        MenuItem logout = new MenuItem("Logout");
        MenuItem exit = new MenuItem("Exit");
        contextMenu.setStyle("-fx-font-size: 13;");
        contextMenu.getItems().addAll(
                trackInfo,
                downloadLink,
                logout,
                exit
        );
        contextMenu.setOnAction(actionEvent -> {
            String target = ((MenuItem) (actionEvent.getTarget())).getText();
            switch (target) {
                case ("Copy title"):
                    ClipboardContent tInfo = new ClipboardContent();
                    tInfo.putString(this.trackTitleLabel.getText());
                    Clipboard.getSystemClipboard().setContent(tInfo);
                    break;
                case ("Copy link"):
                    ClipboardContent tLink = new ClipboardContent();
                    tLink.putString(this.trackDownloadLink);
                    Clipboard.getSystemClipboard().setContent(tLink);
                    break;
                case ("Logout"):
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    new ClientConfiguration().resetUserData();
                    trackTitleLabel.getParent().getScene().getWindow().hide();
                    WindowManager.showAuthenticationWindow();
                    break;
                case ("Exit"):
                    System.exit(0);
                    break;
            }
        });
    }

    private void setupPlayerSettings() {
        mediaPlayer.setOnPlaying(() -> {
            mediaPlayer.setVolume(volume);
            isPaused = false;
            StyleHelper.changeNodeStyle(playButton, StyleHelper.PAUSE);
            trackTitleLabel.setText(String.format("%s – %s", trackArtists, trackTitle));
        });
        mediaPlayer.setOnPaused(() -> {
            isPaused = true;
            StyleHelper.changeNodeStyle(playButton, StyleHelper.PLAY);
        });
        mediaPlayer.setOnStopped(() -> {
            mediaPlayer.setMute(isMuted);
            mediaPlayer.setVolume(volume);
            StyleHelper.changeNodeStyle(playButton, StyleHelper.PLAY);
        });
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.setVolume(volume);
            this.checkIfTrackIsLiked();
            Duration totalDuration = media.getDuration();
            totalDurationLabel.setText(DurationAdjuster.adjust(totalDuration));
        });
        mediaPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
            double progress = mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
            progressBar.setProgress(progress);
            currentDurationLabel.setText(DurationAdjuster.adjust(newValue));
        }));
        mediaPlayer.setOnEndOfMedia(() -> {
            FeedbackFacade.sendStationFeedbackAndTrackStatus(
                    batchId,
                    STATION_ON_YOUR_WAVE,
                    StationFeedbackType.trackFinished,
                    track,
                    trackTotalLength,
                    trackTotalLength,
                    trackTotalLength
            );
            this.play();
        });
    }

}



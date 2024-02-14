package ymwp.facade;

import ymwp.httpclient.station.StationHttpClient;
import ymwp.httpclient.track.TrackHttpClient;
import ymwp.model.station.StationFeedback;
import ymwp.model.station.StationFeedbackType;
import ymwp.model.track.Track;

public class FeedbackFacade {

    private static final StationHttpClient stationHttpClient;
    private static final TrackHttpClient trackHttpClient;

    static {
        stationHttpClient = new StationHttpClient();
        trackHttpClient = new TrackHttpClient();
    }

    public static void sendStationFeedbackAndTrackStatus(String batchId,
                                                         String stationId,
                                                         StationFeedbackType stationFeedbackType,
                                                         Track track,
                                                         int totalPlayedSeconds,
                                                         int trackLengthSeconds,
                                                         int endPositionSeconds) {
        StationFeedback feedback;
        String trackId = String.format("%s:%s", track.getId(), track.getAlbums().get(0).getId());
        if (stationFeedbackType == StationFeedbackType.trackStarted) {
            feedback = new StationFeedback(StationFeedbackType.trackStarted, trackId);
        } else if (stationFeedbackType == StationFeedbackType.trackFinished) {
            feedback = new StationFeedback(StationFeedbackType.trackFinished, trackId, totalPlayedSeconds);
        } else {
            feedback = new StationFeedback(StationFeedbackType.skip, trackId, totalPlayedSeconds);
        }
        stationHttpClient.sendFeedback(batchId, stationId, feedback);
        trackHttpClient.sendCurrentTrackStatus(track, totalPlayedSeconds, trackLengthSeconds, endPositionSeconds);
    }

}

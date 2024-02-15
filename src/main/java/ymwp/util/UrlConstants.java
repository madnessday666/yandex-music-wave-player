package ymwp.util;

public class UrlConstants {

    private UrlConstants() {
    }

    public static final String BASE_URL = "https://api.music.yandex.net:443";

    public static final String GET_TRACK_DOWNLOAD_INFO = "/tracks/%s/download-info";
    public static final String GET_USER_LIKED_TRACKS = "/users/%d/likes/tracks";
    public static final String ADD_TRACK_TO_LIKED = "/users/%d/likes/tracks/add-multiple";
    public static final String REMOVE_TRACK_FROM_LIKED = "/users/%d/likes/tracks/remove";
    public static final String SEND_TRACK_STATUS = "/play-audio";

    public static final String GET_STATION_TRACK_SEQUENCE = "/rotor/station/%s/tracks";
    public static final String GET_ACCOUNT_STATUS = "/rotor/account/status";
    public static final String SEND_FEEDBACK = "/rotor/station/%s/feedback";
    public static final String STATION_ON_YOUR_WAVE = "user:onyourwave";

}

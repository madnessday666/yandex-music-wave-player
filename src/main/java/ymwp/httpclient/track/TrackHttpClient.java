package ymwp.httpclient.track;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ymwp.config.ClientConfiguration;
import ymwp.model.track.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ymwp.util.UrlConstants.*;

public class TrackHttpClient {

    private final HttpClient client;
    private final Gson gson;

    public TrackHttpClient() {
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    public void addOrRemoveTrackFromLiked(long userId, String trackId, String operation) {
        CompletableFuture.runAsync(() -> {
            try {
                String requestBody = "track-ids=" + URLEncoder.encode(trackId, StandardCharsets.UTF_8);
                URI uri = new URI(BASE_URL + String.format(operation, userId));
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Accept", "application/json")
                        .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .uri(uri)
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new RuntimeException("An error occurred while executing the request");
            }
        });
    }

    public TrackDownloadInfo getTrackDownloadInfo(String trackId) {
        try {
            URI uri = new URI(BASE_URL + String.format(GET_TRACK_DOWNLOAD_INFO, trackId));
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray result = gson.fromJson(response.body(), JsonObject.class).getAsJsonArray("result");
            return findBestQualityTrackDownloadInfoLink(result);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("An error occurred while executing the request");
        }
    }

    public String getTrackDownloadLink(TrackDownloadInfo downloadInfo) {
        try {
            URI uri = new URI(downloadInfo.getDownloadInfoUrl() + "&format=json");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            TrackDownloadLinkDetails result = gson.fromJson(response.body(), TrackDownloadLinkDetails.class);
            return String.format(
                    "https://%s/get-mp3/%s/%s%s",
                    result.getHost(),
                    result.getS(),
                    result.getTs(),
                    result.getPath()
            );
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("An error occurred while executing the request");
        }
    }

    public void sendCurrentTrackStatus(Track track,
                                       int totalPlayedSeconds,
                                       int trackLengthSeconds,
                                       int endPositionSeconds) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("from", "desktop_win-home-playlist_of_the_day-playlist-default");
                params.put("end-position-seconds", (endPositionSeconds > 0 ? String.valueOf(endPositionSeconds) : ""));
                params.put("total-played-seconds", String.valueOf(totalPlayedSeconds));
                params.put("uid", "");
                params.put("client-now", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSSZ")));
                params.put("play-id", generatePlayId());
                params.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSSZ")));
                params.put("playlist-id", "");
                params.put("album-id", String.valueOf(track.getAlbums().get(0).getId()));
                params.put("track-length-seconds", String.valueOf(trackLengthSeconds));
                params.put("track-id", track.getId());
                String requestBody = params
                        .keySet()
                        .stream()
                        .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&"));
                URI uri = new URI(BASE_URL + SEND_TRACK_STATUS);
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Accept", "application/json")
                        .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .uri(uri)
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new RuntimeException("An error occurred while executing the request");
            }
        });
    }

    public List<TrackShort> getUserLikedTracks(long userId) {
        try {
            URI uri = new URI(BASE_URL + String.format(GET_USER_LIKED_TRACKS, userId));
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray jsonArray = gson
                    .fromJson(response.body(), JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonObject("library")
                    .getAsJsonArray("tracks");
            Type listType = new TypeToken<List<TrackShort>>() {
            }.getType();
            return gson.fromJson(jsonArray, listType);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("An error occurred while executing the request");
        }
    }

    private TrackDownloadInfo findBestQualityTrackDownloadInfoLink(JsonArray result) {
        int highestBitrate = 0;
        int highestBitrateIndex = 0;
        for (int i = 0; i < result.size(); i++) {
            TrackDownloadInfo downloadInfo = gson.fromJson(result.get(i), TrackDownloadInfo.class);
            if (downloadInfo.getCodec() == TrackCodec.mp3 && !downloadInfo.isPreview()) {
                if (downloadInfo.getBitrateInKbps() > highestBitrate && downloadInfo.getBitrateInKbps() < 320) {
                    highestBitrate = downloadInfo.getBitrateInKbps();
                    highestBitrateIndex = i;
                }
            }
        }
        return gson.fromJson(result.get(highestBitrateIndex), TrackDownloadInfo.class);
    }

    private static String generatePlayId() {
        return String.format(
                "%s-%s-%s",
                new Random().nextInt(1000),
                new Random().nextInt(1000),
                new Random().nextInt(1000)
        );
    }

}

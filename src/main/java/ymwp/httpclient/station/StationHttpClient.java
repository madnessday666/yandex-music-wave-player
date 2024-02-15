package ymwp.httpclient.station;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ymwp.config.ClientConfiguration;
import ymwp.model.station.StationFeedback;
import ymwp.model.station.StationTracksResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static ymwp.util.UrlConstants.*;

public class StationHttpClient {

    private final HttpClient client;
    private final Gson gson;

    public StationHttpClient() {
        client = HttpClient.newHttpClient();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public StationTracksResult getStationOnYourWaveTracksResult(String lastPlayedTrackId) {
        try {
            URI uri = new URI(BASE_URL +
                    String.format(GET_STATION_TRACK_SEQUENCE, STATION_ON_YOUR_WAVE) +
                    "?settings2=true" +
                    (lastPlayedTrackId.isEmpty() ? "" : "&queue=" + lastPlayedTrackId)
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(
                    gson
                            .fromJson(response.body(), JsonObject.class)
                            .getAsJsonObject("result"), StationTracksResult.class
            );
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("An error occurred while executing the request");
        }
    }

    public void sendFeedback(String batchId, String stationId, StationFeedback stationFeedback) {
        CompletableFuture.runAsync(() -> {
            try {
                URI uri = new URI(
                        BASE_URL +
                                String.format(SEND_FEEDBACK, stationId) +
                                (batchId == null ? "" : String.format("?batch-id=%s", batchId))
                );
                String requestBody = gson.toJson(stationFeedback);
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Accept", "application/json")
                        .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                        .uri(uri)
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new RuntimeException("An error occurred while executing the request");
            }
        });
    }

    public void getAccountId() {
        try {
            URI uri = new URI(BASE_URL + GET_ACCOUNT_STATUS);
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .header("Authorization", "OAuth " + ClientConfiguration.config.getUser().getToken())
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            long userId = gson
                    .fromJson(response.body(), JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonObject("account")
                    .get("uid")
                    .getAsLong();
            ClientConfiguration.updateUserId(userId);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("An error occurred while executing the request");
        }
    }

}

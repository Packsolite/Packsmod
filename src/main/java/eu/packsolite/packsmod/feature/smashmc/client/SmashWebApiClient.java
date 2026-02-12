package eu.packsolite.packsmod.feature.smashmc.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.packsolite.packsmod.Packsmod;
import eu.packsolite.packsmod.feature.smashmc.client.model.SmashIdentity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class SmashWebApiClient {

	private static final String BASE_URL = "https://api.smashmc.eu";

	private final Gson gson = new GsonBuilder().create();
	private final HttpClient client = HttpClient.newHttpClient();

	public CompletableFuture<SmashIdentity[]> getSmashProfiles(String... nameOrUuid) {
		if (nameOrUuid.length == 0) throw new IllegalArgumentException("nameOrUuid must not be empty");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(BASE_URL + "/identity/minecraft/" + String.join(",", nameOrUuid)))
				.build();
		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(json -> {
					if (json.isBlank()) {
						return new SmashIdentity[0];
					}
					try {
						return gson.fromJson(json, SmashIdentity[].class);
					} catch (RuntimeException ex) {
						Packsmod.LOGGER.error("Failed deserializing smash api response: {}", json);
						throw ex;
					}
				});
	}
}

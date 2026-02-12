package eu.packsolite.packsmod.feature.smashmc;


import eu.packsolite.packsmod.Packsmod;
import eu.packsolite.packsmod.feature.smashmc.client.SmashWebApiClient;
import eu.packsolite.packsmod.feature.smashmc.client.model.SmashIdentity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SmashApi {

	private final SmashWebApiClient client = new SmashWebApiClient();
	private final Map<UUID, Optional<SmashIdentity>> knownIdentities = new ConcurrentHashMap<>();
	private final Set<UUID> requestedIdenties = Collections.synchronizedSet(new HashSet<>());
	private final Set<UUID> pendingIdentities = new HashSet<>();

	private long lastRequest = System.currentTimeMillis();

	public void update() {
		if (pendingIdentities.isEmpty()) return;

		// rate limit
		if (System.currentTimeMillis() - lastRequest < 3_000) return;
		lastRequest = System.currentTimeMillis();

		String[] toRequest = pendingIdentities.stream().map(UUID::toString).toArray(String[]::new);
		var pendingCopy = new ArrayList<>(pendingIdentities);
		requestedIdenties.addAll(pendingCopy);
		pendingIdentities.clear();
		client.getSmashProfiles(toRequest).thenAccept(ids -> {
			pendingCopy.forEach(requestedIdenties::remove);
			outer:
			for (var requested : pendingCopy) {
				// find
				for (var id : ids) {
					if (id.uuid().equals(requested)) {
						// found
						knownIdentities.put(requested, Optional.of(id));
						continue outer;
					}
				}
				// not found
				knownIdentities.put(requested, Optional.empty());
			}
		}).exceptionally(ex -> {
			Packsmod.LOGGER.error("Failed to look up smashmc identities", ex);
			return null;
		});
	}

	public IdType isCurrentlyKnownId(UUID uuid) {
		if (knownIdentities.containsKey(uuid)) {
			return knownIdentities.get(uuid).isPresent() ? IdType.KNOWN : IdType.UNKNOWN;
		}

		if (requestedIdenties.contains(uuid)) {
			return IdType.PENDING;
		}

		pendingIdentities.add(uuid);
		return IdType.PENDING;
	}

	public enum IdType {
		PENDING, KNOWN, UNKNOWN;
	}
}

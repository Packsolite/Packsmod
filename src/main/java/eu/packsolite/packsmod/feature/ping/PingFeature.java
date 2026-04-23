package eu.packsolite.packsmod.feature.ping;

import eu.packsolite.packsmod.util.ChatUtil;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;

public enum PingFeature {
	INSTANCE;

	@Getter
	@Nullable
	private Instant lastPingTimestamp;

	public void awaitResponse() {
		this.lastPingTimestamp = Instant.now();
	}

	public boolean isAwaitingResponse() {
		return lastPingTimestamp != null;
	}

	public void processResponse() {
		if (isAwaitingResponse()) {
			long ping = yieldResponse();
			ChatUtil.display("Approximate ping: " + ping + "ms");
		}
	}

	private long yieldResponse() {
		if (lastPingTimestamp == null) {
			return -1;
		}
		long ping = Duration.between(lastPingTimestamp, Instant.now()).toMillis();
		lastPingTimestamp = null;
		return ping;
	}
}

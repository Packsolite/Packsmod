package eu.packsolite.packsmod.feature.smashmc;

import eu.packsolite.packsmod.Packsmod;
import eu.packsolite.packsmod.util.GlowUtil;
import lombok.Getter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.chat.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum SmashMcFeature {
	INSTANCE;

	private static final Pattern REPORT_JOIN_PATTERN = Pattern.compile(
			"^(Der|Die) Spieler (?<players>[A-Za-z0-9_, ]+) (wurde|wurden) (?<amount>\\d+) mal für (?<reason>.+?) reportet\\.$"
	);
	private static final Pattern REPORT_DENIED_PATTERN = Pattern.compile(
			"^Du hast den Report (?<players>[A-Za-z0-9_,]+)(?:/.+)? abgelehnt!$"
	);
	private static final Pattern REPORT_ACCEPTED_PATTERN = Pattern.compile(
			"^Du hast den Report angenommen und (?<players>[A-Za-z0-9_, ]+) (wurde|wurden) bestraft!$"
	);

	private final SmashApi api = new SmashApi();

	public void init() {
		PayloadTypeRegistry.playC2S().register(StatusCustomPayload.TYPE, StatusCustomPayload.CODEC);
		ClientTickEvents.END_CLIENT_TICK.register(client -> api.update());
	}

	/**
	 * Handles a message that came directly from the server via network packets.
	 */
	public void handleSystemChat(Component content) {
		String plain = content.getString()
				.replace("\n", "")
				.replaceAll("§[a-f0-9]", "")
				.trim();
		if (isReportSystemMessage(plain)) {
			handleReportSystemMessage(plain);
		}
	}

	/**
	 * Handles a SmashMC report system message.
	 */
	private void handleReportSystemMessage(String message) {
		message = message.substring("● Reports ➥".length()).trim();
		Packsmod.LOGGER.info("Handling report system message: {}", message);

		Matcher reportJoinedMatcher = REPORT_JOIN_PATTERN.matcher(message);
		if (reportJoinedMatcher.matches()) {
			handleReportJoinedMessage(reportJoinedMatcher);
			return; // stop after first match
		}
		Matcher reportDeniedMatcher = REPORT_DENIED_PATTERN.matcher(message);
		if (reportDeniedMatcher.matches()) {
			handleReportCompletedMessage(reportDeniedMatcher);
			return;
		}

		Matcher reportAcceptedMatcher = REPORT_ACCEPTED_PATTERN.matcher(message);
		if (reportAcceptedMatcher.matches()) {
			handleReportCompletedMessage(reportAcceptedMatcher);
		}
	}

	private void handleReportJoinedMessage(Matcher matcher) {
		int amount = Integer.parseInt(matcher.group("amount"));
		String reason = matcher.group("reason");

		String playersRaw = matcher.group("players");
		String[] players = playersRaw.split("\\s*,\\s*");

		for (String player : players) {
			Packsmod.LOGGER.info(
					"Detected report join {} ({} reports) for {}",
					player, amount, reason
			);
			GlowUtil.glowPlayer(player);
		}
	}

	private void handleReportCompletedMessage(Matcher matcher) {
		String playersRaw = matcher.group("players");
		String[] players = playersRaw.split("\\s*,\\s*");

		for (String player : players) {
			Packsmod.LOGGER.info("Removing glow from player {}", player);
			GlowUtil.removeGlow(player);
		}
	}

	private boolean isReportSystemMessage(String message) {
		return message.trim().startsWith("● Reports ➥");
	}
}

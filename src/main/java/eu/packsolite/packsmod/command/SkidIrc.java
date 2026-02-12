package eu.packsolite.packsmod.command;

import de.liquiddev.ircclient.api.IrcClient;
import de.liquiddev.ircclient.api.SimpleIrcApi;
import de.liquiddev.ircclient.client.ClientType;
import de.liquiddev.ircclient.client.IrcClientFactory;
import eu.packsolite.packsmod.Packsmod;
import eu.packsolite.packsmod.config.ConfigProvider;
import lombok.Getter;
import lombok.Synchronized;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

import static net.minecraft.network.chat.Component.literal;

public class SkidIrc extends SimpleIrcApi {

	@Getter
	private static SkidIrc instance = new SkidIrc();
	@Getter
	private IrcClient client;
	private Minecraft mc = Minecraft.getInstance();
	@Getter
	private String lastWhisperTarget = "";
	@Getter
	private CommandHandler commandHandler;

	public void onInitialize() {
		instance = this;
		this.commandHandler = new CommandHandler();
		ConfigProvider.getConfigHolder().registerSaveListener((holder, config) -> {
			if (config.irc.enabled) {
				enable();
			} else {
				disable();
			}
			return InteractionResult.SUCCESS;
		});
		Packsmod.LOGGER.info("SkidIrc initialized!");
	}

	@Synchronized
	public void enable() {
		if (client != null) {
			return;
		}
		String ign = mc.getGameProfile().name();
		String[] versionArgs = mc.getLaunchedVersion().split("-");
		String version = versionArgs[versionArgs.length - 1];
		IrcClientFactory factory = IrcClientFactory.getDefault();
		this.client = factory.createIrcClient("daisy.smashmc.eu", ClientType.FABRIC, "EzVLMm8hXj3fSzFm", ign, version);
		this.client.getApiManager().registerApi(this);
		Packsmod.LOGGER.info("Skid irc enabled!");
	}

	@Synchronized
	public void disable() {
		if (this.client == null) {
			return;
		}
		this.client.disconnect();
		this.client.getApiManager().unregisterApi(this);
		this.client = null;
	}

	public void print(String message) {
		this.addChat("§a➡ §7" + message);
	}

	public void sendMinecraftChatMessage(String chatText) {
		if (mc.player == null) return;
		if (chatText.startsWith("/")) {
			mc.player.connection.sendCommand(chatText.substring(1));
		} else {
			mc.player.connection.sendChat(chatText);
		}
	}

	@Override
	public void addChat(String message) {
		if (mc.player != null) {
			mc.execute(() -> mc.player.displayClientMessage(literal(message), false));
		}
	}

	@Override
	public void onWhisperMessage(String player, String message, boolean isFromMe) {
		if (!isFromMe) {
			lastWhisperTarget = player;
		}
		super.onWhisperMessage(player, message, isFromMe);
	}
}

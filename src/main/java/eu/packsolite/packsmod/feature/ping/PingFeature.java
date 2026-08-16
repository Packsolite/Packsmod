package eu.packsolite.packsmod.feature.ping;

import eu.packsolite.packsmod.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;

public enum PingFeature {
	INSTANCE;

	private volatile long outstandingId = -1;

	public void awaitResponse() {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection == null) {
			return;
		}
		outstandingId = System.currentTimeMillis();
		connection.send(new ServerboundPingRequestPacket(outstandingId));
	}

	public void onPong(ClientboundPongResponsePacket packet) {
		if (packet.time() != outstandingId) {
			return;
		}
		outstandingId = -1;
		long ping = System.currentTimeMillis() - packet.time();
		ChatUtil.display("Ping: " + ping + "ms");
	}
}

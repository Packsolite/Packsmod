package eu.packsolite.packsmod.feature.smashmc;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record StatusCustomPayload(int command, String text)
		implements CustomPacketPayload {

	public static final Identifier ID_LOCATION = Identifier.fromNamespaceAndPath("smashmc", "status");

	public static final CustomPacketPayload.Type<@NotNull StatusCustomPayload> TYPE =
			new CustomPacketPayload.Type<>(ID_LOCATION);

	public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull StatusCustomPayload> CODEC =
			new StreamCodec<>() {

				@Override
				public StatusCustomPayload decode(RegistryFriendlyByteBuf buf) {
					throw new UnsupportedOperationException();
				}

				@Override
				public void encode(RegistryFriendlyByteBuf buf, StatusCustomPayload payload) {
					buf.writeInt(payload.command());

					if (payload.command() == 0) {
						buf.writeUtf(payload.text());
					}
				}
			};

	@Override
	public CustomPacketPayload.@NotNull Type<? extends @NotNull CustomPacketPayload> type() {
		return TYPE;
	}
}

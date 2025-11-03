package net.hasnat4763.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hasnat4763.SixSeven;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SixSevenNetworking {

    public record PlayCurseMusicPayload(int curseLevel) implements CustomPayload {
        public static final CustomPayload.Id<PlayCurseMusicPayload> ID =
                new CustomPayload.Id<>(Identifier.of("six_seven", "play_curse_music"));

        public static final PacketCodec<RegistryByteBuf, PlayCurseMusicPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeInt(value.curseLevel),
                        buf -> new PlayCurseMusicPayload(buf.readInt())
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void RegisterNetworking() {
        PayloadTypeRegistry.playS2C().register(PlayCurseMusicPayload.ID, PlayCurseMusicPayload.CODEC);
    }
    public static void SendCurseMusicPacket(ServerPlayerEntity player) {
        SendCurseMusicPacket(player, 0);
    }

    public static void SendCurseMusicPacket(ServerPlayerEntity player, int curseLevel) {
        ServerPlayNetworking.send(player, new PlayCurseMusicPayload(curseLevel));
        SixSeven.LOGGER.info("[Networking] Sent curse music packet to player {} (level {})",
                player.getName().getString(), curseLevel);
    }
}
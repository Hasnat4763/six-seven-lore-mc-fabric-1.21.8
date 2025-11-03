package net.hasnat4763.SixSevenCurse;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hasnat4763.SixSeven;
import net.hasnat4763.networking.SixSevenNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CurseEventHandlers {

    private static final Map<UUID, Long> lastMusicPacketTime = new HashMap<>();
    private static final long MUSIC_PACKET_COOLDOWN = 2000;

    public static void RegisterCurseEventHandlers() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            server.execute(() -> checkAndStartMusic(player));
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            Objects.requireNonNull(newPlayer.getServer()).execute(() -> checkAndStartMusic(newPlayer));
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> lastMusicPacketTime.remove(handler.getPlayer().getUuid()));
    }

    private static void checkAndStartMusic(ServerPlayerEntity player) {
        SixSevenCurseDataKeeper data = SixSevenCurseDataKeeper.get(player.getServer());
        SixSevenCurseDataKeeper.PlayerCurseInfo info = data.getCurseInfo(player.getUuid());
        if (info != null && info.isCursed) {
            UUID playerUuid = player.getUuid();
            long currentTime = System.currentTimeMillis();
            Long lastSent = lastMusicPacketTime.get(playerUuid);
            if (lastSent == null || (currentTime - lastSent) > MUSIC_PACKET_COOLDOWN) {
                SixSeven.LOGGER.info("[Curse] Player {} is cursed, sending music packet (level {})",
                        player.getName().getString(), info.curseLevel);
                SixSevenNetworking.SendCurseMusicPacket(player, info.curseLevel);
                lastMusicPacketTime.put(playerUuid, currentTime);
            } else {
                SixSeven.LOGGER.info("[Curse] Skipping music packet for {} - sent too recently",
                        player.getName().getString());
            }
        }
    }
}
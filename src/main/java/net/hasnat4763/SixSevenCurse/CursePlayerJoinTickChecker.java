package net.hasnat4763.SixSevenCurse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hasnat4763.ModStatusEffect.ModEffects;
import net.hasnat4763.SixSeven;
import net.hasnat4763.networking.SixSevenNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CursePlayerJoinTickChecker {
    private static final int EFFECT_COOLDOWN_TICKS = 20 * 60;

    public static void RegisterCursePlayerJoin() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();

            SixSevenCurseDataKeeper data = SixSevenCurseDataKeeper.get(server);
            SixSevenCurseDataKeeper.PlayerCurseInfo info = data.getCurseInfo(player.getUuid());

            if (info != null && info.isCursed) {
                SixSeven.LOGGER.info("[SixSevenCurse] Player {} rejoined while cursed, reapplying effects",
                        player.getName().getString());

                player.addStatusEffect(new StatusEffectInstance(
                        ModEffects.SIX_SEVEN_CURSE_EFFECT,
                        20 * 60 * 60 * 24,
                        0,
                        true,
                        true,
                        true
                ));

                player.sendMessage(Text.literal("§4§oThe dark presence returns..."), false);

                SixSevenNetworking.SendCurseMusicPacket(player);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(CursePlayerJoinTickChecker::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        SixSevenCurseDataKeeper data = SixSevenCurseDataKeeper.get(server);
        long currentTime = server.getOverworld().getTime();

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            SixSevenCurseDataKeeper.PlayerCurseInfo info = data.getCurseInfo(player.getUuid());

            if (info != null && info.isCursed) {
                data.updateCurseLevel(player.getUuid(), currentTime);

                if (data.shouldTriggerEffect(player.getUuid(), currentTime, EFFECT_COOLDOWN_TICKS)) {
                    SixSeven.LOGGER.info("[SixSevenCurse] Triggering timed effect for {} at level {}",
                            player.getName().getString(), info.curseLevel);

                    BookCurseManager.triggerTimedEffect(player, info.curseLevel);
                    data.recordEffect(player.getUuid(), currentTime);
                }
            }
        }

        data.save(server);
    }
}
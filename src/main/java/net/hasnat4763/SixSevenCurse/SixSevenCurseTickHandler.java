package net.hasnat4763.SixSevenCurse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hasnat4763.SixSeven;
import net.minecraft.server.network.ServerPlayerEntity;

public class SixSevenCurseTickHandler {

    private static int tickCounter = 0;
    private static int saveCounter = 0;
    private static boolean registered = false;

    public static void RegisterServerCurseTick() {
        if (registered) {
            SixSeven.LOGGER.warn("[SixSevenCurse] Tick handler already registered; skipping");
            return;
        }
        registered = true;
        SixSeven.LOGGER.info("[SixSevenCurse] Registering server tick handlers");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            saveCounter++;

            if (saveCounter >= 200) { // ~10s
                SixSevenCurseDataKeeper.get(server).save(server);
                SixSeven.LOGGER.info("[SixSevenCurse] Saved curse data");
                saveCounter = 0;
            }

            // Beat log once per second so we know this runs
            if (server.getTicks() % 20 == 0) {
                SixSeven.LOGGER.info("[SixSevenCurse] Tick beat (players={} ticks={})",
                        server.getPlayerManager().getPlayerList().size(), server.getTicks());
            }

            // Check cooldowns every 20 ticks (~1s)
            if (tickCounter % 20 != 0) return;

            SixSevenCurseDataKeeper curseData = SixSevenCurseDataKeeper.get(server);
            long now = server.getOverworld().getTime(); // or server.getTicks() if your keeper uses server ticks

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                var info = curseData.getCurseInfo(player.getUuid());
                if (info == null || !info.isCursed) {
                    SixSeven.LOGGER.debug("[SixSevenCurse] {} not cursed", player.getGameProfile().getName());
                    continue;
                }

                // Keep level up-to-date
                curseData.updateCurseLevel(player.getUuid(), now);

                int cooldown = switch (info.curseLevel) {
                    case 0 -> 1200; // 1 min
                    case 1 -> 900;  // 45s
                    case 2 -> 600;  // 30s
                    case 3 -> 400;  // 20s
                    case 4 -> 300;  // 15s
                    case 5 -> 200;  // 10s
                    default -> 1200;
                };

                boolean should = curseData.shouldTriggerEffect(player.getUuid(), now, cooldown);
                SixSeven.LOGGER.info("[SixSevenCurse] player={}, level={}, now={}, last={}, cd={}, should={}",
                        player.getGameProfile().getName(), info.curseLevel, now, info.LastEffectTime, cooldown, should);

                if (should) {
                    BookCurseManager.triggerTimedEffect(player, info.curseLevel);
                    curseData.recordEffect(player.getUuid(), now);
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isStopping()) {
                SixSevenCurseDataKeeper.get(server).save(server);
                SixSeven.LOGGER.info("[SixSevenCurse] Saved curse data on shutdown");
            }
        });
    }
}
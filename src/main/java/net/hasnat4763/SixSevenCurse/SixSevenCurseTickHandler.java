package net.hasnat4763.SixSevenCurse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class SixSevenCurseTickHandler {

    private static int tickCounter = 0;
    private static int saveCounter = 0;

    public static void RegisterServerCurseTick() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            saveCounter++;

            // Save data every 10 seconds (200 ticks)
            if (saveCounter >= 200) {
                SixSevenCurseDataKeeper.get(server).save(server);
                saveCounter = 0;
            }

            // Check every 5 seconds (100 ticks)
            if (tickCounter % 100 != 0) return;

            SixSevenCurseDataKeeper curseData = SixSevenCurseDataKeeper.get(server);
            long currentTime = server.getOverworld().getTime();

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                SixSevenCurseDataKeeper.PlayerCurseInfo info = curseData.getCurseInfo(player.getUuid());

                if (info == null || !info.is_Cursed) continue;

                // Update curse level based on playtime
                curseData.updateCurseLevel(player.getUuid(), currentTime);

                // Trigger effects based on curse level
                int cooldown = getCooldownForLevel(info.curseLevel);

                if (curseData.shouldTriggerEffect(player.getUuid(), currentTime, cooldown)) {
                    BookCurseManager.triggerTimedEffect(player, info.curseLevel);
                    curseData.recordEffect(player.getUuid(), currentTime);
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isStopping()) {
                SixSevenCurseDataKeeper.get(server).save(server);
            }
        });
    }

    private static int getCooldownForLevel(int level) {
        return switch (level) {
            case 0 -> 6000;  // 5 minutes
            case 1 -> 3600;  // 3 minutes
            case 2 -> 2400;  // 2 minutes
            case 3 -> 1200;  // 1 minute
            case 4 -> 600;   // 30 seconds
            case 5 -> 300;   // 15 seconds
            default -> 6000;
        };
    }
}
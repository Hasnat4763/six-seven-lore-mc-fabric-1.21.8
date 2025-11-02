package net.hasnat4763.SixSevenCurse;

import net.hasnat4763.SixSeven;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SixSevenCurseDataKeeper {
    private static final String DATA_NAME = "six_seven_curse";
    private static SixSevenCurseDataKeeper instance;

    private final Map<UUID, PlayerCurseInfo> cursedPlayers = new HashMap<>();
    private boolean isDirty = false;

    public static class PlayerCurseInfo {
        public boolean is_Cursed = false;
        public long curseStartTime = 0L;
        public int curseLevel = 0;
        public long LastEffectTime = 0L;
        public int TotalEffectsApplied = 0;

        public void writeToNbt(NbtCompound nbt) {
            nbt.putBoolean("is_cursed", is_Cursed);
            nbt.putLong("curse_start_time", curseStartTime);
            nbt.putInt("curse_level", curseLevel);
            nbt.putLong("last_effect_time", LastEffectTime);
            nbt.putInt("total_effects_applied", TotalEffectsApplied);
        }

        public static PlayerCurseInfo fromNbt(NbtCompound nbt) {
            PlayerCurseInfo info = new PlayerCurseInfo();
            info.is_Cursed = nbt.getBoolean("is_cursed");
            info.curseStartTime = nbt.getLong("curse_start_time");
            info.curseLevel = nbt.getInt("curse_level");
            info.LastEffectTime = nbt.getLong("last_effect_time");
            info.TotalEffectsApplied = nbt.getInt("total_effects_applied");
            return info;
        }
    }

    private SixSevenCurseDataKeeper() {}

    public static SixSevenCurseDataKeeper get(MinecraftServer server) {
        if (instance == null) {
            instance = new SixSevenCurseDataKeeper();
            instance.load(server);
        }
        return instance;
    }

    private void load(MinecraftServer server) {
        try {
            Path worldDir = server.getSavePath(WorldSavePath.ROOT);
            File dataDir = worldDir.resolve("data").toFile();
            File dataFile = new File(dataDir, DATA_NAME + ".dat");

            if (dataFile.exists()) {
                NbtCompound nbt = NbtIo.readCompressed(dataFile.toPath(), NbtSizeTracker.ofUnlimitedBytes());
                readFromNbt(nbt);
            }
        } catch (IOException e) {
            SixSeven.LOGGER.info(String.valueOf(e));
        }
    }

    public void save(MinecraftServer server) {
        if (!isDirty) return;

        try {
            Path worldDir = server.getSavePath(WorldSavePath.ROOT);
            File dataDir = worldDir.resolve("data").toFile();
            if (!dataDir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dataDir.mkdirs();
            }

            File dataFile = new File(dataDir, DATA_NAME + ".dat");
            NbtCompound nbt = new NbtCompound();
            writeToNbt(nbt);

            NbtIo.writeCompressed(nbt, dataFile.toPath());
            isDirty = false;
        } catch (IOException e) {
            SixSeven.LOGGER.info(String.valueOf(e));
        }
    }

    // nbt is the root compound of the file
    private void readFromNbt(NbtCompound playersNbt) {
        cursedPlayers.clear();

        if (playersNbt.contains("cursedPlayers")) {
            for (String uuidString : playersNbt.getKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    PlayerCurseInfo info = PlayerCurseInfo.fromNbt(playersNbt);
                    cursedPlayers.put(uuid, info);
                } catch (IllegalArgumentException e) {
                    SixSeven.LOGGER.info(String.valueOf(e));
                }
            }
        }
    }

    private void writeToNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();

        for (Map.Entry<UUID, PlayerCurseInfo> entry : cursedPlayers.entrySet()) {
            NbtCompound playerNbt = new NbtCompound();
            entry.getValue().writeToNbt(playerNbt);
            playersNbt.put(entry.getKey().toString(), playerNbt);
        }

        nbt.put("cursedPlayers", playersNbt);
    }

    private void markDirty() {
        isDirty = true;
    }

    // API

    public void cursePlayer(UUID playerUuid, long currentTime) {
        PlayerCurseInfo info = cursedPlayers.computeIfAbsent(playerUuid, k -> new PlayerCurseInfo());
        if (!info.is_Cursed) {
            info.is_Cursed = true;
            info.curseStartTime = currentTime;
            info.curseLevel = 0;
            markDirty();
        }
    }

    public PlayerCurseInfo getCurseInfo(UUID playerUuid) {
        return cursedPlayers.get(playerUuid);
    }

    public void updateCurseLevel(UUID playerUuid, long currentTime) {
        PlayerCurseInfo info = cursedPlayers.get(playerUuid);
        if (info == null || !info.is_Cursed) return;

        long ticksSinceCurse = currentTime - info.curseStartTime;
        long minutesSinceCurse = ticksSinceCurse / 1200;

        if (minutesSinceCurse >= 120) info.curseLevel = 5;
        else if (minutesSinceCurse >= 60) info.curseLevel = 4;
        else if (minutesSinceCurse >= 30) info.curseLevel = 3;
        else if (minutesSinceCurse >= 15) info.curseLevel = 2;
        else if (minutesSinceCurse >= 5) info.curseLevel = 1;
        else info.curseLevel = 0;

        markDirty();
    }

    public boolean shouldTriggerEffect(UUID playerUuid, long currentTime, int cooldownTicks) {
        PlayerCurseInfo info = cursedPlayers.get(playerUuid);
        if (info == null || !info.is_Cursed) return false;

        return (currentTime - info.LastEffectTime) >= cooldownTicks;
    }

    public void recordEffect(UUID playerUuid, long currentTime) {
        PlayerCurseInfo info = cursedPlayers.get(playerUuid);
        if (info != null) {
            info.LastEffectTime = currentTime;
            info.TotalEffectsApplied++;
            markDirty();
        }
    }

    public void removeCurse(UUID playerUuid) {
        PlayerCurseInfo info = cursedPlayers.get(playerUuid);
        if (info != null) {
            info.is_Cursed = false;
            markDirty();
        }
    }
}
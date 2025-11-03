package net.hasnat4763;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;

@Environment(EnvType.CLIENT)
public class SixSevenCurseMusicHandler {
    public static void playCustomMusic(SoundEvent music) {
    MinecraftClient client = MinecraftClient.getInstance();

    if (client == null || client.world == null) {
        return;
    }
    client.getMusicTracker().stop();
    client.getSoundManager().play(PositionedSoundInstance.music(music, 2.0f));
    SixSeven.LOGGER.info("Playing custom music");
}


    public static void stopCustomMusic() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) {
            return;
        }
        client.getSoundManager().stopAll();
    }
}

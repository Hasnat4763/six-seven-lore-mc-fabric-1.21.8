package net.hasnat4763;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hasnat4763.ScreenHandler.ModScreenHandler;
import net.hasnat4763.networking.SixSevenNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.sound.SoundEvent;

public class SixSevenClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(
                ModScreenHandler.SIX_SEVEN_STORY_BOOK_HANDLER,
                SixSevenStoryBookScreen::new
        );
        ClientPlayNetworking.registerGlobalReceiver(
                SixSevenNetworking.PlayCurseMusicPayload.ID,
                (payload, context) -> context.client().execute(() -> {
                    int curseLevel = payload.curseLevel();
                    SixSeven.LOGGER.info("[Client] Received curse music packet (level {})", curseLevel);
                    SoundEvent music = getMusicForCurseLevel(curseLevel);
                    SixSevenCurseMusicHandler.playCustomMusic(music);
                    SixSeven.LOGGER.info("[Client] Playing music for curse level {}", curseLevel);
                })
        );
    }
    private static SoundEvent getMusicForCurseLevel(int level) {
        return switch (level) {
            case 0, 1 -> ModSounds.THICK_OF_IT;
            default -> ModSounds.RICKROLL;
        };
    }
}
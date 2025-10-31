package net.hasnat4763;

import net.fabricmc.api.ClientModInitializer;
import net.hasnat4763.ScreenHandler.ModScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class SixSevenClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        HandledScreens.register(
                ModScreenHandler.SIX_SEVEN_STORY_BOOK_HANDLER,
                SixSevenStoryBookScreen::new
        );
	}
}
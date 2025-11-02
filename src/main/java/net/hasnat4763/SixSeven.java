package net.hasnat4763;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.hasnat4763.ModSounds.RegisterModSounds;
import static net.hasnat4763.ModStatusEffect.ModEffects.RegisterModEffects;
import static net.hasnat4763.SixSevenCurse.SixSevenCurseTickHandler.RegisterServerCurseTick;
import static net.hasnat4763.items.ModItems.RegisterModItems;
import static net.hasnat4763.ScreenHandler.ModScreenHandler.RegisterModScreenHandler;

public class SixSeven implements ModInitializer {
    public static final String MOD_ID = "six_seven";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SixSeven mod initializing!");
        RegisterModItems();
        RegisterModScreenHandler();
        RegisterModSounds();
        RegisterServerCurseTick();
        RegisterModEffects();
    }
}
